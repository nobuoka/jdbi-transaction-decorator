package info.vividcode.jdbi.transaction.decorator.demo.web

import info.vividcode.jdbi.transaction.decorator.demo.core.DashboardService
import info.vividcode.jdbi.transaction.decorator.demo.core.MessageUpdateService
import java.io.File
import javax.ws.rs.*

@Path("/")
class MessageResource(private val d: Dependencies) {
    interface Dependencies :
            MessageUpdateService.Provider,
            DashboardService.Provider

    @GET
    @Path("/")
    @Produces("text/html; charset=utf-8")
    fun dashboardHtml(): File = File(MessageResource::class.java.getResource("/web/view/home.html").file)

    @GET
    @Path("/dashboard")
    @Produces("application/json")
    fun dashboard(): DashboardService.Dashboard = d.dashboardService.loadDashboard()

    @POST
    @Path("/updateMessage")
    @Produces("application/json")
    @Consumes("text/plain")
    fun updateMessage(message: String): Unit = d.messageUpdateService.updateMessage(message)

}
