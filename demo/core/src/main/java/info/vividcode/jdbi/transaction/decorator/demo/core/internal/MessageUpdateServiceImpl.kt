package info.vividcode.jdbi.transaction.decorator.demo.core.internal

import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import info.vividcode.jdbi.transaction.decorator.demo.core.MessageUpdateService

class MessageUpdateServiceImpl(d: Dependencies) : MessageUpdateService {
    interface Dependencies :
            Database.Provider

    private val database = d.database

    @MyTransaction
    override fun updateMessage(message: String): Unit {
        database.incrementUpdateCount()
        database.updateMessage(message)
    }

}
