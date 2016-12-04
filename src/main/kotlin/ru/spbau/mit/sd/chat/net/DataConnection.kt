package ru.spbau.mit.sd.chat.net

import java.io.DataInputStream
import java.io.DataOutputStream

interface DataConnection {
    val host: String
    val port: Int

    val input: DataInputStream
    val output: DataOutputStream

    fun close()
}

fun build(ip: String, port: Int, isServer: Boolean): DataConnection = when(isServer) {
    true -> P2PHost(ip, port)
    false -> P2PClient(ip, port)
}