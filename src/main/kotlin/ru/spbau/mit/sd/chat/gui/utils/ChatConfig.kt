package ru.spbau.mit.sd.chat.gui.utils

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.Scope
import tornadofx.ViewModel

class ChatConfig {
    val ip = SimpleStringProperty()
    val port = SimpleIntegerProperty()
    val isServer = SimpleBooleanProperty()
    val nickname = SimpleStringProperty()

    init {
        ip.value = "127.0.0.1"
        port.value = 81
        nickname.value = "Default"
    }
}

class ChatConfigModel(var chatConfig: ChatConfig) : ViewModel() {
    val ip = bind { chatConfig.ip }
    val port = bind { chatConfig.port }
    val isServer = bind { chatConfig.isServer }
    val nickname = bind { chatConfig.nickname }
}

class ConfigScope : Scope() {
    val model = ChatConfigModel(ChatConfig())
}
