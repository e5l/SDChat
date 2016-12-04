package ru.spbau.mit.sd.chat

import ru.spbau.mit.sd.chat.net.DataConnection
import ru.spbau.mit.sd.chat.protocol.receiveChatMessage
import ru.spbau.mit.sd.chat.protocol.send

class Chat(var nickname: String, private val connection: DataConnection, val onMessage: (message: ChatMessage) -> Unit) {
    private val messageReceiver = Thread(Receiver())

    init {
        messageReceiver.start()
    }

    fun sendMessage(text: String): ChatMessage {
        val message = ChatMessage(nickname, text)
        message.send(connection)
        return message
    }

    fun disconnect() {
        messageReceiver.interrupt()
        messageReceiver.join()
        connection.close()
    }

    inner class Receiver() : Runnable {
        override fun run() {
            while (!Thread.interrupted()) {
                processMessage()
            }
        }

        private fun processMessage() {
            val message = receiveChatMessage(connection)
            this@Chat.onMessage(message)
    }
}
}



