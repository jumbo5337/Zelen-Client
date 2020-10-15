package sut.ist912.zelen.dialogs

import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.OperationClient
import sut.ist912.zelen.utils.format
import sut.ist912.zelen.utils.isValidAmount
import sut.ist912.zelen.utils.textFormatter
import sut.ist912.zelen.utils.toZString
import tornadofx.*
import java.util.*
import java.util.function.UnaryOperator
import java.util.regex.Pattern

@Component
class DepositDialog(
        private val operationClient: OperationClient
) {

    fun deposit() {
        val dialog: Dialog<String> = Dialog()
        dialog.title = "Внесение денег"
        dialog.headerText = "Введите сумму"

        val loginButtonType = ButtonType("Внести", ButtonBar.ButtonData.OK_DONE)
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
            val operation = operationClient.deposit(it.toDouble())
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null

            val receiptGrid = GridPane()
            receiptGrid.hgap = 10.0
            receiptGrid.vgap = 10.0
            receiptGrid.padding = Insets(20.0, 15.0, 10.0, 10.0)

            receiptGrid.add(Label("ID операции:"), 0, 0)
            receiptGrid.add(Label(operation.id.toString()), 1, 0)
            receiptGrid.add(Label("Сумма зачисления:"), 0, 1)
            receiptGrid.add(Label(operation.income.format()), 1, 1)
            receiptGrid.add(Label("Время зачисления:"), 0, 2)
            receiptGrid.add(Label(operation.updated.toZString()), 1, 2)

            alert.dialogPane.content = receiptGrid
            alert.showAndWait()
        }
    }






}