package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate

interface MessageUpdateCountDao {

    @SqlQuery("SELECT value FROM message_counts WHERE id = 1")
    fun findMessageCount(): Int?

    @SqlUpdate("UPDATE message_counts SET value = :count WHERE id = 1")
    fun updateMessageCount(@Bind("count") count: Int): Unit

    @SqlUpdate("INSERT INTO message_counts VALUES (1, :count)")
    fun createMessageCount(@Bind("count") count: Int): Unit

}
