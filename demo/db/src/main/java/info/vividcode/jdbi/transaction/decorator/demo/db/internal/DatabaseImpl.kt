package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import info.vividcode.jdbi.transaction.decorator.demo.core.Database

class DatabaseImpl(d: SqlObjectProvider) : Database {

    private val messageDao = d.messageDao
    private val messageCountDao = d.messageUpdateCountDao

    override fun incrementUpdateCount() {
        val messageCount = messageCountDao.findMessageCount()
        if (messageCount != null) {
            messageCountDao.updateMessageCount(messageCount + 1)
        } else {
            messageCountDao.createMessageCount(1)
        }
    }

    override fun readUpdateCount(): Int = messageCountDao.findMessageCount() ?: 0

    override fun updateMessage(message: String) {
        if (messageDao.findMessage() != null) {
            messageDao.updateMessage(message)
        } else {
            messageDao.createMessage(message)
        }
    }

    override fun readMessage(): String = messageDao.findMessage() ?: ""

}
