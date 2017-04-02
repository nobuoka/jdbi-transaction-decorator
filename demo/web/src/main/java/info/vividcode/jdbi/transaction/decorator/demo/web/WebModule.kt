package info.vividcode.jdbi.transaction.decorator.demo.web

interface WebModule {

    interface Dependencies :
            MessageResource.Dependencies

    interface Providers :
            JaxRsResourcesProvider

    class Connection(d: Lazy<Dependencies>): Providers {
        override val jaxRsResources: Collection<Any> by lazy {
            listOf(
                    MessageResource(d.value)
            )
        }
    }

    interface JaxRsResourcesProvider {
        val jaxRsResources: Collection<Any>
    }

}
