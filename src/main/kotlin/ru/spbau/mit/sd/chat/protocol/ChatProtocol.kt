package ru.spbau.mit.sd.chat.protocol

import ru.spbau.mit.sd.chat.ChatMessage
import ru.spbau.mit.sd.chat.net.DataConnection

fun ChatMessage.send(connection: DataConnection) {
    connection.output.writeUTF(name)
    connection.output.writeUTF(text)

    connection.output.flush()
}

fun receiveChatMessage(connection: DataConnection) = ChatMessage(connection.input.readUTF(), connection.input.readUTF())

