package info.vividcode.jdbi.transaction.decorator.demo.core

interface DashboardService {
    interface Provider {
        val dashboardService: DashboardService
    }

    fun loadDashboard(): Dashboard

    data class Dashboard(
            val message: String,
            val updateCount: Int
    )

}
