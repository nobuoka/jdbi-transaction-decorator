package info.vividcode.jdbi.transaction.decorator.demo.app

import io.dropwizard.Application
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.setup.Environment
import org.h2.Driver

class DemoApplication : Application<DemoConfiguration>() {

    override fun run(configuration: DemoConfiguration, environment: Environment) {
        val component = DemoComponent.create(environment, createDataSourceFactory())

        component.tableCreator.createTables()

        component.jaxRsResources.forEach {
            environment.jersey()?.register(it)
        }
    }

    private fun createDataSourceFactory() = DataSourceFactory().apply {
        driverClass = Driver::class.java.name
        url = "jdbc:h2:mem:demo"
    }

}
