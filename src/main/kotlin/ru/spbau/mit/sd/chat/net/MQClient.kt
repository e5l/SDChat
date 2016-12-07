package ru.spbau.mit.sd.chat.net

import com.rabbitmq.client.*
import java.io.*
import java.nio.ByteBuffer
import java.util.*


class MQClient(override val host: String, override val port: Int, isServer: Boolean) : DataConnection {
    val QUEUE_NAME = "CHAT"

    val factory = ConnectionFactory()
    val connection: Connection
    val channel: Channel
    val received = LinkedList<Int>()

    init {
        factory.host = "$host:$port"
        connection = factory.newConnection()
        channel = connection.createChannel()

        if (isServer) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        }

        val consumer = object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                received.push(ByteBuffer.allocate(4).put(body).int)
            }
        }
        channel.basicConsume(QUEUE_NAME, true, consumer)
    }

    private fun send(message: ByteArray) {
        channel.basicPublish("", QUEUE_NAME, null, message)
    }

    override val input: DataInputStream = DataInputStream(object : InputStream() {
        override fun read(): Int {
            while (received.isEmpty()) {}
            return received.pop()
        }
    })

    override val output: DataOutputStream = DataOutputStream(object : OutputStream() {
        override fun write(b: Int) {
            send(ByteBuffer.allocate(4).putInt(b).array())
        }
    })

    override fun close() {
        channel.close()
        connection.close()
    }
}
