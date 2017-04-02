package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import org.skife.jdbi.v2.sqlobject.CreateSqlObject
import org.skife.jdbi.v2.sqlobject.mixins.Transactional

interface SqlObjectProvider : Transactional<SqlObjectProvider> {

    @get:CreateSqlObject
    val messageDao: MessageDao

    @get:CreateSqlObject
    val messageUpdateCountDao: MessageUpdateCountDao

}
