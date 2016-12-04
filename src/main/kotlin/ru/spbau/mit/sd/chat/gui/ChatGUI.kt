package ru.spbau.mit.sd.chat.gui

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.TextInputDialog
import ru.spbau.mit.sd.chat.Chat
import ru.spbau.mit.sd.chat.ChatMessage
import ru.spbau.mit.sd.chat.gui.utils.ConfigScope
import ru.spbau.mit.sd.chat.net.build
import tornadofx.*

class ChatConfigWindow : View() {
    val configScope = ConfigScope()
    val model = configScope.model

    override val root = form {
        fieldset("Chat config") {
            field("Nickname: ") { textfield(model.nickname).required() }
            field("Ip:") { textfield(model.ip).required() }
            field("Port:") { textfield(model.port).required() }
            checkbox("Server", model.isServer)
        }
        button("Start") { setOnAction { startServer() } }
    }

    private fun startServer() {
        if (model.commit()) {
            replaceWith(find(ChatMainWindow::class, configScope))
        }
    }

}

class ChatMainWindow : View() {
    val configModel = (scope as ConfigScope).model
    val chatMessages = FXCollections.observableArrayList<String>()!!
    val currentMessage = SimpleStringProperty()
    val chat = Chat(configModel.nickname.value, build(configModel.ip.value, configModel.port.value as Int, configModel.isServer.value), { printMessage(it) })

    override val root = vbox {
        listview(chatMessages)
        hbox {
            textfield(currentMessage)
            button("Send") { setOnAction { sendMessage() } }
            button("Nickname") {
                setOnAction {
                    val dialog = TextInputDialog("Set nickname")
                    dialog.contentText = "New nickname:"
                    val result = dialog.showAndWait()
                    if (result.isPresent) {
                        chat.nickname = result.get()
                    }
                }

            }
        }
    }

    private fun sendMessage() {
        printMessage(chat.sendMessage(currentMessage.value))
    }

    private fun printMessage(message: ChatMessage) {
        Platform.runLater({ chatMessages.add("${message.name}: ${message.text}") })
    }
}

class ChatGUIApp : App(ChatConfigWindow::class)

