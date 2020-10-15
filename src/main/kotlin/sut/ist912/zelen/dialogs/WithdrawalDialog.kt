package sut.ist912.zelen.dialogs

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.layout.GridPane
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.OperationClient
import sut.ist912.zelen.rest.dto.ZelenException
import sut.ist912.zelen.utils.calcFee
import sut.ist912.zelen.utils.format
import sut.ist912.zelen.utils.textFormatter
import sut.ist912.zelen.utils.toZString


@Component
class WithdrawalDialog(
        private val operationClient: OperationClient
) {
    fun withdrawal() {
        val dialog: Dialog<String> = Dialog()
        dialog.title = "Вывести"
        dialog.headerText = "Введите сумму"

        val loginButtonType = ButtonType("Вывести", ButtonBar.ButtonData.OK_DONE)
        dialog.dialogPane.buttonTypes.addAll(loginButtonType, ButtonType.CANCEL)

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 15.0, 10.0, 10.0)

        val amountField = TextField()

        grid.add(Label("Сумма:"), 0, 0)
        grid.add(amountField, 1, 0)

        amountField.textFormatter = textFormatter()

        val depositButton: Node = dialog.dialogPane.lookupButton(loginButtonType)

        depositButton.disableProperty().bind(
                amountField.textProperty().isEqualTo("0.0")
        )

        dialog.getDialogPane().setContent(grid)
        dialog.setResultConverter { dialogButton ->
            if (dialogButton === loginButtonType) {
                return@setResultConverter amountField.text
            }
            null
        }

        val result = dialog.showAndWait()

        result.ifPresent {
            val amount = it.toDouble()
            val confirmAlert = Alert(AlertType.CONFIRMATION)
            confirmAlert.title = "Подтвержедение операции"
            confirmAlert.headerText = "Подтверждение вывода денег"
            confirmAlert.contentText = "С вашего счёта будет снято ${amount.calcFee().format()}"

            val confirmButton = ButtonType("Продолжить")
            val cancelButton = ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE)
            confirmAlert.buttonTypes.setAll(confirmButton, cancelButton)

            val confirmResult =  confirmAlert.showAndWait();

            if (confirmResult.get() == confirmButton){
                proceedOperationAndShowResult(amount)
            }

        }
    }

    private fun proceedOperationAndShowResult(amount: Double){
        try {
            val operation = operationClient.withdrawal(amount)
            val alert = Alert(AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null

            val receiptGrid = GridPane()
            receiptGrid.hgap = 10.0
            receiptGrid.vgap = 10.0
            receiptGrid.padding = Insets(20.0, 15.0, 10.0, 10.0)

            receiptGrid.add(Label("ID операции:"), 0, 0)
            receiptGrid.add(Label(operation.id.toString()), 1, 0)
            receiptGrid.add(Label("Сумма вывода:"), 0, 1)
            receiptGrid.add(Label(operation.outcome.format()), 1, 1)
            receiptGrid.add(Label("Комиссия:"), 0, 2)
            receiptGrid.add(Label(operation.fee.format()), 1, 2)
            receiptGrid.add(Label("Итого:"), 0, 3)
            receiptGrid.add(Label(operation.income.format()), 1, 3)
            receiptGrid.add(Label("Время вывода:"), 0, 4)
            receiptGrid.add(Label(operation.updated.toZString()), 1, 4)

            alert.dialogPane.content = receiptGrid
            alert.showAndWait()
        } catch (exc: ZelenException){
            val alert = Alert(AlertType.ERROR)
            alert.title = "Ошибка операции"
            alert.headerText = "Что-то пошло не так"
            alert.contentText = exc.message
            alert.showAndWait()
        }

    }
}