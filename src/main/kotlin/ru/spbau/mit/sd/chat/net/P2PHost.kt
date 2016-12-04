package ru.spbau.mit.sd.chat.net

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket

class P2PHost(override val host: String, override val port: Int) : DataConnection {
    private val connection = ServerSocket(port)
    private val socket = connection.accept()!!

    override val input = DataInputStream(socket.inputStream)
    override val output = DataOutputStream(socket.outputStream)

    override fun close() {
        socket.close()
    }
}