package sut.ist912.zelen.dialogs

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.OperationClient
import sut.ist912.zelen.rest.dto.ZelenException
import sut.ist912.zelen.utils.*

@Component
class TransferDialog(
        private val operationClient: OperationClient
) {
    fun transfer() {
        val dialog: Dialog<Pair<String, String>> = Dialog()
        dialog.title = "Перевед денег"
        dialog.headerText = "Введите сумму и ID получателя"

        val loginButtonType = ButtonType("Продолжить", ButtonBar.ButtonData.OK_DONE)
        dialog.dialogPane.buttonTypes.addAll(loginButtonType, ButtonType.CANCEL)

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 15.0, 10.0, 10.0)

        val receiverField = TextField()
        val amountField = TextField()

        grid.add(Label("ID получателя:"), 0, 0)
        grid.add(receiverField, 1, 0)
        grid.add(Label("Сумма:"), 0, 1)
        grid.add(amountField, 1, 1)

        amountField.textFormatter = textFormatter()
        receiverField.textFormatter = textFormatterLong()

        val depositButton: Node = dialog.dialogPane.lookupButton(loginButtonType)

        depositButton.disableProperty().bind(
                amountField.textProperty().isEqualTo("0.0")
                        .or(receiverField.textProperty().isEqualTo("0"))
        )

        dialog.getDialogPane().setContent(grid)
        dialog.setResultConverter { dialogButton ->
            if (dialogButton === loginButtonType) {
                return@setResultConverter receiverField.text to amountField.text
            }
            null
        }

        val result = dialog.showAndWait()

        result.ifPresent {
            val receiverId = it.first.toLong()
            val amount = it.second.toDouble()
            createTransferReceipt(receiverId, amount)
        }
    }

    private fun createTransferReceipt(receiverId : Long, amount : Double) {
        try {
            val (operation, receiver) = operationClient.createTransfer(receiverId, amount)
            val confirmAlert = Alert(Alert.AlertType.CONFIRMATION)
            confirmAlert.title = "Подтвержедение операции"
            confirmAlert.headerText = "Подтверждение перевода денег"


            val receiptGrid = GridPane()
            receiptGrid.hgap = 10.0
            receiptGrid.vgap = 10.0
            receiptGrid.padding = Insets(20.0, 15.0, 10.0, 10.0)

            var column = 0
            receiptGrid.add(Label("Информация о переводе"), 0, column)
            receiptGrid.add(Label("Сумма перевода:"), 0, ++column)
            receiptGrid.add(Label(operation.outcome.format()), 1, column)
            receiptGrid.add(Label("Комиссия:"), 0, ++column)
            receiptGrid.add(Label(operation.fee.format()), 1, column)
            receiptGrid.add(Label("Итого:"), 0, ++column)
            receiptGrid.add(Label(operation.income.format()), 1, column)

            receiptGrid.add(Label("Информация о получателе"), 0, ++column)
            receiptGrid.add(Label("ID:"), 0, ++column)
            receiptGrid.add(Label(receiver!!.userId.toString()), 1, column)
            receiptGrid.add(Label("Имя:"), 0, ++column)
            receiptGrid.add(Label(receiver.firstName), 1, column)
            receiptGrid.add(Label("Фамилия:"), 0, ++column)
            receiptGrid.add(Label(receiver.lastName), 1, column)

            confirmAlert.dialogPane.content = receiptGrid

            val confirmButton = ButtonType("Продолжить")
            val cancelButton = ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE)
            confirmAlert.buttonTypes.setAll(confirmButton, cancelButton)

            val confirmResult =  confirmAlert.showAndWait();

            if (confirmResult.get() == confirmButton){
                proceedOperationAndShowResult(operation.id)
            } else {
                operationClient.confirmTransfer(operation.id, false)
            }
        } catch (exc: ZelenException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Ошибка операции"
            alert.headerText = "Что-то пошло не так"
            alert.contentText = exc.message
            alert.showAndWait()
        }
    }

    private fun proceedOperationAndShowResult(transferId: Long){
        try {
            val (operation, receiver) = operationClient.confirmTransfer(transferId)
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null

            val receiptGrid = GridPane()
            receiptGrid.hgap = 10.0
            receiptGrid.vgap = 10.0
            receiptGrid.padding = Insets(20.0, 15.0, 10.0, 10.0)

            var column = 0
            receiptGrid.add(Label("Информация о переводе"), 0, column)
            receiptGrid.add(Label("ID операции:"), 0, ++column)
            receiptGrid.add(Label(operation.id.toString()), 1, column)
            receiptGrid.add(Label("Сумма перевода:"), 0, ++column)
            receiptGrid.add(Label(operation.outcome.format()), 1, column)
            receiptGrid.add(Label("Комиссия:"), 0, ++column)
            receiptGrid.add(Label(operation.fee.format()), 1, column)
            receiptGrid.add(Label("Итого:"), 0, ++column)
            receiptGrid.add(Label(operation.income.format()), 1, column)
            receiptGrid.add(Label("Время перевода:"), 0, ++column)
            receiptGrid.add(Label(operation.updated.toZString()), 1, column)

            receiptGrid.add(Label("Информация о получателе"), 0, ++column)
            receiptGrid.add(Label("ID:"), 0, ++column)
            receiptGrid.add(Label(receiver!!.userId.toString()), 1, column)
            receiptGrid.add(Label("Имя:"), 0, ++column)
            receiptGrid.add(Label(receiver.firstName), 1, column)
            receiptGrid.add(Label("Фамилия:"), 0, ++column)
            receiptGrid.add(Label(receiver.lastName), 1, column)

            alert.dialogPane.content = receiptGrid
            alert.showAndWait()
        } catch (exc: ZelenException){
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Ошибка операции"
            alert.headerText = "Что-то пошло не так"
            alert.contentText = exc.message
            alert.showAndWait()
        }

    }
}