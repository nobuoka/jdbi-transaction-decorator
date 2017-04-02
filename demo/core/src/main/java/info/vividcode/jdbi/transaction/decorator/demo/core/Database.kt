package info.vividcode.jdbi.transaction.decorator.demo.core

interface Database {
    interface Provider {
        val database: Database
    }

    fun incrementUpdateCount(): Unit

    fun readUpdateCount(): Int

    fun updateMessage(message: String): Unit

    fun readMessage(): String

}
