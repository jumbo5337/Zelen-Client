package sut.ist912.zelen.view

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import sut.ist912.zelen.rest.UserClient
import sut.ist912.zelen.utils.format
import tornadofx.*
import java.net.URL
import java.util.*

class MainView : View("Zelen"){
    override val root: BorderPane by fxml("/MainView.fxml")

    private val userClient : UserClient by di()

    private val balanceField : TextField by fxid("balanceField")
    private val idField : TextField by fxid("idField")
    private val usernameField : TextField by fxid("loginInfo")
    private val firstNameField : TextField by fxid("fnInfo")
    private val lastNameField : TextField by fxid("lnInfo")
    private val emailField : TextField by fxid("email")

    private val refreshButton : Button by fxid("refreshButton")
    private val depositButton : Button by fxid("depositButton")
    private val withdrawalButton : Button by fxid("withdrawalButton")
    private val transferButton : Button by fxid("transferButton")

    private val passwordButton : Button by fxid("changePassButton")
    private val secretButton : Button by fxid("changeSecretButton")



    init {
        loadInfo()
        balanceField.editableProperty().set(false)
        idField.editableProperty().set(false)
        usernameField.editableProperty().set(false)
        firstNameField.editableProperty().set(false)
        lastNameField.editableProperty().set(false)
        emailField.editableProperty().set(false)

        refreshButton.setOnAction { updateBalance() }

    }


    fun loadInfo(){
        val userInfo = userClient.loadUserProfile()
        balanceField.text = userInfo.balance.toString()
        idField.text = userInfo.userId.toString()
        usernameField.text = userInfo.username
        firstNameField.text = userInfo.firstName
        lastNameField.text = userInfo.lastName
        emailField.text = userInfo.email
    }

    fun updateBalance(){
        val updateBalance = userClient.updateBalance()
        balanceField.text = updateBalance.balance.format()
    }
}
