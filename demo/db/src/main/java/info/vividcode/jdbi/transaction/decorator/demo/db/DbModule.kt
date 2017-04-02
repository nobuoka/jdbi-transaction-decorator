package info.vividcode.jdbi.transaction.decorator.demo.db

import info.vividcode.jdbi.transaction.decorator.api.TransactionDecorator
import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import info.vividcode.jdbi.transaction.decorator.demo.core.TransactionDecoratorProvider
import info.vividcode.jdbi.transaction.decorator.demo.db.internal.DatabaseImpl
import info.vividcode.jdbi.transaction.decorator.demo.db.internal.SqlObjectProvider
import info.vividcode.jdbi.transaction.decorator.jdbi.JdbiTransactionDecorator
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI

interface DbModule {

    interface Dependencies :
            DbiProvider,
            EnvironmentProvider,
            DataSourceFactoryProvider,
            TableCreator.Dependencies

    interface Providers :
            DbiProvider,
            TransactionDecoratorProvider,
            Database.Provider,
            TableCreator.Provider

    class Connection(d: Lazy<Dependencies>): Providers {
        override val dbi: DBI by lazy {
            DBIFactory().build(d.value.environment, d.value.dataSourceFactory, "app-database")
        }
        override val transactionDecorator: TransactionDecorator by lazy { JdbiTransactionDecorator(sqlObjectProvider) }
        override val database: Database by lazy { DatabaseImpl(sqlObjectProvider) }
        override val tableCreator: TableCreator by lazy { TableCreator(d.value) }
        private val sqlObjectProvider: SqlObjectProvider by lazy { d.value.dbi.onDemand(SqlObjectProvider::class.java) }
    }

    interface EnvironmentProvider {
        val environment: Environment
    }

    interface DataSourceFactoryProvider {
        val dataSourceFactory: DataSourceFactory
    }

}
