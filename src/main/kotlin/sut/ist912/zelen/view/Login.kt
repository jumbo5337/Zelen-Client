package sut.ist912.zelen.view

import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.ButtonType
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import sut.ist912.zelen.rest.LoginClient
import sut.ist912.zelen.rest.dto.ZelenException
import tornadofx.*


class Login : View("Вкатиться в Зелень") {
    override val root: BorderPane by fxml("/Login.fxml")
    private val maiTabPane: TabPane by fxid("loginMainTab")
    private val loginButton: Button by fxid("loginButton")
    private val resetButton: Button by fxid("restPassword")
    private val loginField: TextField by fxid("usenameField1")
    private val passwordField: PasswordField by fxid("passwordField")
    private val loginClient: LoginClient by di()
    private val registerButton: Button by fxid("registerButton")
    private val usernameRegField: TextField by fxid("usenameField")
    private val firstNameRegField: TextField by fxid("fnField")
    private val lastNameRegField: TextField by fxid("lnField")
    private val emailRegField: TextField by fxid("email")
    private val secret: TextField by fxid("secretField")
    private val p1RegField: PasswordField by fxid("p1Field")
    private val p2RegField: PasswordField by fxid("p2Field")

    init {
        loginButton.setOnAction { tryLogin() }
        registerButton.setOnAction { tryRegister() }
        resetButton.setOnAction { tryResetPassword() }
    }

    private fun tryRegister() {
        if (usernameRegField.text.isNullOrBlank()
                || firstNameRegField.text.isNullOrBlank()
                || lastNameRegField.text.isNullOrBlank()
                || emailRegField.text.isNullOrBlank()
                || secret.text.isNullOrBlank()
                || p1RegField.text.isNullOrBlank()
                || p2RegField.text.isNullOrBlank()) {
            val alert = Alert(AlertType.INFORMATION)
            alert.title = "Info"
            alert.headerText = null
            alert.contentText = "Все поля обязательны к заполнению"
            alert.showAndWait()
        } else {
            try {
                loginClient.register(
                        username = usernameRegField.text,
                        password1 = p1RegField.text,
                        password2 = p2RegField.text,
                        secret = secret.text,
                        email = emailRegField.text,
                        lastName = lastNameRegField.text,
                        firstName = firstNameRegField.text
                )
                val alert = Alert(AlertType.INFORMATION)
                alert.title = "Info"
                alert.headerText = null
                alert.contentText = "Вы успешно зарегистрировались"
                alert.showAndWait()
                maiTabPane.selectionModel.selectFirst()
            } catch (exc: ZelenException) {
                val alert = Alert(AlertType.ERROR)
                alert.title = "Ошибка Регистрации"
                alert.headerText = "Что-то пошло не так"
                alert.contentText = exc.message
                alert.showAndWait()
            }
        }

    }

    private fun tryLogin() {
        if (loginField.text.isNullOrBlank() ||
                passwordField.text.isNullOrBlank()) {
            return
        } else {
            try {
                loginClient.login(loginField.text, passwordField.text)
                replaceWith(MainView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT), sizeToScene = true)
            } catch (exc: ZelenException) {
                val alert = Alert(AlertType.ERROR)
                alert.title = "Ошибка Авторизации"
                alert.headerText = "Что-то пошло не так"
                alert.contentText = exc.message
                alert.showAndWait()
            }
        }
    }

    private fun tryResetPassword() {
        val dialog: Dialog<Boolean> = Dialog()
        dialog.title = "Сброс пароля"
        dialog.headerText = "Введите данные для сброса пароля"

        val loginButtonType = ButtonType("Reset", ButtonData.OK_DONE)
        dialog.dialogPane.buttonTypes.addAll(loginButtonType, ButtonType.CANCEL)

        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 15.0, 10.0, 10.0)

        val usernameField = TextField()
        val secretField = TextField()
        val passwordField1 = PasswordField()
        val passwordField2 = PasswordField()


        grid.add(Label("Логин:"), 0, 0)
        grid.add(usernameField, 1, 0)
        grid.add(Label("Кодовое слово:"), 0, 1)
        grid.add(secretField, 1, 1)

        grid.add(Label("Новый пароль:"), 0, 2)
        grid.add(passwordField1, 1, 2)

        grid.add(Label("Новый пароль:"), 0, 3)
        grid.add(passwordField2, 1, 3)


        val loginButton: Node = dialog.dialogPane.lookupButton(loginButtonType)
        loginButton.disableProperty().bind(
                usernameField.textProperty().isEmpty
                        .and(secretField.textProperty().isEmpty)
                        .and(passwordField1.textProperty().isEmpty)
                        .and(passwordField2.textProperty().isEmpty))

        dialog.dialogPane.content = grid

        dialog.setResultConverter { dialogButton ->
            if (dialogButton === loginButtonType) {
                return@setResultConverter true
            }
            null
        }

        val result = dialog.showAndWait()

        result.ifPresent {
            try {
                val msg = loginClient.resetPassword(
                        usernameField.text,
                        passwordField1.text,
                        passwordField2.text,
                        secretField.text
                )
                val alert = Alert(AlertType.INFORMATION)
                alert.title = "Info"
                alert.headerText = null
                alert.contentText = msg
                alert.showAndWait()
                maiTabPane.selectionModel.selectFirst()
            } catch (exc: ZelenException) {
                val alert = Alert(AlertType.ERROR)
                alert.title = "Ошибка Авторизации"
                alert.headerText = "Что-то пошло не так"
                alert.contentText = exc.message
                alert.showAndWait()
            }
        }


    }

}
