package info.vividcode.jdbi.transaction.decorator.demo.core

interface MessageUpdateService {
    interface Provider {
        val messageUpdateService: MessageUpdateService
    }

    fun updateMessage(message: String): Unit

}
