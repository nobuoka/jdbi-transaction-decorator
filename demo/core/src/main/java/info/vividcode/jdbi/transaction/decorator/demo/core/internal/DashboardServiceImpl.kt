package info.vividcode.jdbi.transaction.decorator.demo.core.internal

import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import info.vividcode.jdbi.transaction.decorator.demo.core.DashboardService

class DashboardServiceImpl(d: Dependencies) : DashboardService {
    interface Dependencies :
            Database.Provider

    private val database = d.database

    override fun loadDashboard() = DashboardService.Dashboard(database.readMessage(), database.readUpdateCount())

}
