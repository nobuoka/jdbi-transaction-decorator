package info.vividcode.jdbi.transaction.decorator.demo.db

import org.skife.jdbi.v2.DBI

interface DbiProvider {
    val dbi: DBI
}
