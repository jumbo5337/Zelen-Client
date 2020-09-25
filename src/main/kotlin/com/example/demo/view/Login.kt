package com.example.demo.view

import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import tornadofx.*

class Login : View() {
    override val root : BorderPane by fxml("/Login.fxml")

    private val loginButton : Button by fxid("")

}
