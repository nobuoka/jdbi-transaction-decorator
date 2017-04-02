package info.vividcode.jdbi.transaction.decorator.demo.app

import info.vividcode.jdbi.transaction.decorator.demo.core.ApplicationModule
import info.vividcode.jdbi.transaction.decorator.demo.db.DbModule
import info.vividcode.jdbi.transaction.decorator.demo.web.WebModule
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.setup.Environment
import java.util.concurrent.atomic.AtomicReference

interface DemoComponent {

    class ObjectGraph(self: Lazy<ObjectGraph>, override val environment: Environment, override val dataSourceFactory: DataSourceFactory) :
            ApplicationModule.Providers by ApplicationModule.Connection(self), ApplicationModule.Dependencies,
            DbModule.Providers by DbModule.Connection(self), DbModule.Dependencies,
            WebModule.Providers by WebModule.Connection(self), WebModule.Dependencies

    companion object {
        fun create(environment: Environment, dataSourceFactory: DataSourceFactory): ObjectGraph = AtomicReference<ObjectGraph>().also {
            it.set(ObjectGraph(lazy { it.get() }, environment, dataSourceFactory))
        }.get()
    }

}
