package ru.spbau.mit.sd.chat.net

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class P2PClient(override val host: String, override val port: Int) : DataConnection {
    val socket = Socket(host, port)

    override val input = DataInputStream(socket.inputStream)
    override val output = DataOutputStream(socket.outputStream)

    override fun close() {
        socket.close()
    }
}

