package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate

interface MessageDao {

    @SqlQuery("SELECT value FROM messages WHERE id = 1")
    fun findMessage(): String?

    @SqlUpdate("UPDATE messages SET value = :message WHERE id = 1")
    fun updateMessage(@Bind("message") message: String): Unit

    @SqlUpdate("INSERT INTO messages VALUES (1, :message)")
    fun createMessage(@Bind("message") message: String): Unit

}
