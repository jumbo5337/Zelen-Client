package sut.ist912.zelen.dialogs

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import sut.ist912.zelen.rest.UserClient
import java.util.function.UnaryOperator
import java.util.regex.Pattern


@Component
class PasswordDialog {
    @Autowired
    private lateinit var userClient: UserClient


    fun changePassword() {
        val dialog: Dialog<Triple<String, String, String>> = Dialog()
        dialog.title = "Смена пароля"
        dialog.headerText = "Введите данные для смены пароля"

        val loginButtonType = ButtonType("Сменить пароль", ButtonBar.ButtonData.OK_DONE)
        dialog.dialogPane.buttonTypes.addAll(loginButtonType, ButtonType.CANCEL)

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 15.0, 10.0, 10.0)

        val oldPasswordField = PasswordField()
        val passwordField1 = PasswordField()
        val passwordField2 = PasswordField()


        grid.add(Label("Старый пароль:"), 0, 0)
        grid.add(oldPasswordField, 1, 0)
        grid.add(Label("Новый пароль:"), 0, 1)
        grid.add(passwordField1, 1, 1)

        grid.add(Label("Новый пароль:"), 0, 2)
        grid.add(passwordField2, 1, 2)


        val loginButton: Node = dialog.dialogPane.lookupButton(loginButtonType)
        loginButton.disableProperty().bind(
                oldPasswordField.textProperty().isEmpty
                        .and(passwordField1.textProperty().isEmpty)
                        .and(passwordField2.textProperty().isEmpty))

        dialog.getDialogPane().setContent(grid)

        dialog.setResultConverter { dialogButton ->
            if (dialogButton === loginButtonType) {
                return@setResultConverter Triple(oldPasswordField.text, passwordField1.text, passwordField2.text)
            }
            null
        }

        val result = dialog.showAndWait()
        result.ifPresent {
            val msg = userClient.changePassword(
                    it.first,
                    it.second,
                    it.third
            )
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null
            alert.contentText = msg.message
            alert.showAndWait()
        }
    }

    fun changeSecret() {
        val dialog: Dialog<String> = Dialog()
        dialog.title = "Смена секретного слова"
        dialog.headerText = "Введите новое секретное слово"

        val loginButtonType = ButtonType("Сменить", ButtonBar.ButtonData.OK_DONE)
        dialog.dialogPane.buttonTypes.addAll(loginButtonType, ButtonType.CANCEL)

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 15.0, 10.0, 10.0)

        val secretField = TextField()

        grid.add(Label("Новое слово:"), 0, 0)
        grid.add(secretField, 1, 0)

        val loginButton: Node = dialog.dialogPane.lookupButton(loginButtonType)

        loginButton.disableProperty().bind(
                secretField.textProperty().isEmpty)

        dialog.getDialogPane().setContent(grid)

        dialog.setResultConverter { dialogButton ->
            if (dialogButton === loginButtonType) {
                return@setResultConverter secretField.text
            }
            null
        }

        val result = dialog.showAndWait()
        result.ifPresent {
            val msg = userClient.resetSecret(it)
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null
            alert.contentText = msg.message
            alert.showAndWait()
        }
    }
}





