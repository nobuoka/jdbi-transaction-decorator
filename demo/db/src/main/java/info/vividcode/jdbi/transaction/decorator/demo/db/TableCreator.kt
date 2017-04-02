package info.vividcode.jdbi.transaction.decorator.demo.db

class TableCreator(private val d: Dependencies) {
    interface Dependencies : DbiProvider
    interface Provider {
        val tableCreator: TableCreator
    }

    fun createTables() {
        d.dbi.useHandle { handle ->
            handle.execute(
                    "CREATE TABLE messages ( id INTEGER AUTO_INCREMENT PRIMARY KEY, value TEXT );" +
                    "CREATE TABLE message_counts ( id INTEGER AUTO_INCREMENT PRIMARY KEY, value INTEGER );"
            )
        }
    }

}
