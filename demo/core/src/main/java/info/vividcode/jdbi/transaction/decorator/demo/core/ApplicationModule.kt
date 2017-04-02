package info.vividcode.jdbi.transaction.decorator.demo.core

import info.vividcode.jdbi.transaction.decorator.demo.core.internal.DashboardServiceImpl
import info.vividcode.jdbi.transaction.decorator.demo.core.internal.MessageUpdateServiceImpl
import info.vividcode.jdbi.transaction.decorator.demo.core.internal.MyTransaction

interface ApplicationModule {

    interface Dependencies:
            TransactionDecoratorProvider,
            MessageUpdateServiceImpl.Dependencies,
            DashboardServiceImpl.Dependencies

    interface Providers :
            MessageUpdateService.Provider,
            DashboardService.Provider

    class Connection(d: Lazy<Dependencies>): Providers {
        override val dashboardService: DashboardService by decorateTransaction(d, DashboardService::class.java, ::DashboardServiceImpl)
        override val messageUpdateService: MessageUpdateService by decorateTransaction(d, MessageUpdateService::class.java, ::MessageUpdateServiceImpl)
    }

    companion object {
        private inline fun <T, S : T> decorateTransaction(d: Lazy<Dependencies>, target: Class<T>, crossinline constructor: (Dependencies) -> S): Lazy<T> =
                lazy { d.value.transactionDecorator.decorate(target, MyTransaction::class.java, constructor(d.value)) }
    }

}
