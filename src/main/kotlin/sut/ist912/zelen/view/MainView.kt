package sut.ist912.zelen.view

import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import sut.ist912.zelen.rest.UserClient
import sut.ist912.zelen.utils.format
import sut.ist912.zelen.view.model.DepositModel
import sut.ist912.zelen.view.model.OpeationModel
import sut.ist912.zelen.view.model.WithdrawalModel
import tornadofx.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainView : View("Zelen"){
    override val root: BorderPane by fxml("/MainView.fxml")

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")

    private val userClient : UserClient by di()

    private val balanceField : TextField by fxid("balanceField")
    private val idField : TextField by fxid("idField")
    private val usernameField : TextField by fxid("loginInfo")
    private val firstNameField : TextField by fxid("fnInfo")
    private val lastNameField : TextField by fxid("lnInfo")
    private val emailField : TextField by fxid("email")

    private val depositTable : TableView<DepositModel> by fxid("depositTable")
    private val depositId : TableColumn<DepositModel, String> by fxid("depositId")
    private val depositTs : TableColumn<DepositModel, String> by fxid("depositTs")
    private val depositSum : TableColumn<DepositModel, String> by fxid("depositSum")

    private val withdrawalTable : TableView<WithdrawalModel> by fxid("withdrawalTable")
    private val withdrawalId : TableColumn<WithdrawalModel, String> by fxid("withdrawalId")
    private val withdrawalTs : TableColumn<WithdrawalModel, String> by fxid("withdrawalTs")
    private val withdrawalSum : TableColumn<WithdrawalModel, String> by fxid("withdrawalSum")
    private val withdrawalFee : TableColumn<WithdrawalModel, String> by fxid("withdrawalFee")
    private val withdrawalTotal : TableColumn<WithdrawalModel, String> by fxid("withdrawalTotal")

    private val opTable : TableView<OpeationModel> by fxid("opTable")
    private val opId : TableColumn<OpeationModel, String> by fxid("opId")
    private val opTs : TableColumn<OpeationModel, String> by fxid("opTs")
    private val opSum : TableColumn<OpeationModel, String> by fxid("opSum")
    private val opFee : TableColumn<OpeationModel, String> by fxid("opFee")
    private val opTotal : TableColumn<OpeationModel, String> by fxid("opTotal")
    private val opType : TableColumn<OpeationModel, String> by fxid("opType")
    private val opMember : TableColumn<OpeationModel, String> by fxid("opMember")

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

        initTableView()
        loadDeposits()
        loadWithdrawals()
        loadOperations()

        refreshButton.setOnAction { updateBalance() }

    }


    fun loadInfo(){
        val userInfo = userClient.loadUserProfile()
        balanceField.text = userInfo.balance.format()
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

    fun loadDeposits(){
        val deposits = userClient.loadOperations(1)
                .map {
                    val op = it.operation
                    DepositModel(
                            dateFormat.format(op.updated),
                            op.id.toString(),
                            op.income.format()
                    )
                }.asObservable()
        depositTable.items = deposits
    }

    fun loadWithdrawals(){
        val deposits = userClient.loadOperations(3)
                .map {
                    val op = it.operation
                    WithdrawalModel(
                            dateFormat.format(op.updated),
                            op.id.toString(),
                            op.outcome.format(),
                            op.fee.format(),
                            op.income.format()
                    )
                }.asObservable()
        withdrawalTable.items = deposits
    }

    fun loadOperations(){
        val currentUserId = idField.text
        val deposits = userClient.loadOperations(2)
                .filter {
                    it.operation.opState == "COMPLETED"
                }
                .map {
                    val op = it.operation
                    if(op.senderId.toString() == currentUserId) { // Исходящий перевод
                        OpeationModel(
                                dateFormat.format(op.updated),
                                op.id.toString(),
                                op.outcome.format(),
                                op.fee.format(),
                                op.income.format(),
                                "ИСХОДЯЩИЙ",
                                op.receiverId.toString()
                        )
                    } else {
                        OpeationModel(
                                dateFormat.format(op.updated),
                                op.id.toString(),
                                "-",
                                "-",
                                op.outcome.format(),
                                "ВХОДЯЩИЙ",
                                op.senderId.toString()
                        )
                    }
                }.asObservable()
        opTable.items = deposits
    }

    private fun initTableView(){
        depositId.cellValueFactory = PropertyValueFactory("DepositId")
        depositTs.cellValueFactory = PropertyValueFactory("DepositTs")
        depositSum.cellValueFactory = PropertyValueFactory("DepositSum")

        withdrawalTs.cellValueFactory = PropertyValueFactory("WithdrawalTs")
        withdrawalId.cellValueFactory = PropertyValueFactory("WithdrawalId")
        withdrawalSum.cellValueFactory = PropertyValueFactory("WithdrawalSum")
        withdrawalFee.cellValueFactory = PropertyValueFactory("WithdrawalFee")
        withdrawalTotal.cellValueFactory = PropertyValueFactory("WithdrawalTotal")

        opTs.cellValueFactory = PropertyValueFactory("OpTs")
        opId.cellValueFactory = PropertyValueFactory("OpId")
        opSum.cellValueFactory = PropertyValueFactory("OpSum")
        opFee.cellValueFactory = PropertyValueFactory("OpFee")
        opTotal.cellValueFactory = PropertyValueFactory("OpTotal")
        opType.cellValueFactory = PropertyValueFactory("OpType")
        opMember.cellValueFactory = PropertyValueFactory("OpMember")
    }

}
