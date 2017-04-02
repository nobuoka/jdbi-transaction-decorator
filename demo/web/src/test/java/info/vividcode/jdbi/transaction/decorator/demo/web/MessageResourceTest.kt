package info.vividcode.jdbi.transaction.decorator.demo.web

import com.nhaarman.mockito_kotlin.*
import info.vividcode.jdbi.transaction.decorator.demo.core.DashboardService
import info.vividcode.jdbi.transaction.decorator.demo.core.MessageUpdateService
import io.dropwizard.testing.junit.ResourceTestRule
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType

class MessageResourceTest {

    private object messageResourceDependencies : MessageResource.Dependencies {
        override lateinit var dashboardService: DashboardService
        override lateinit var messageUpdateService: MessageUpdateService
    }

    @Rule @JvmField
    val resourceTestRule: ResourceTestRule = ResourceTestRule.builder()
            .addResource(MessageResource(messageResourceDependencies))
            .build()

    @Test
    fun dashboardHtml() {
        val response = resourceTestRule.client().target("/").request().get()

        val responseContent = response.readEntity(String::class.java)
        assertThat(response.status, equalTo(200))
        assertThat(response.mediaType, equalTo(MediaType.valueOf("text/html; charset=utf-8")))
        assertThat(responseContent, startsWith("<!DOCTYPE html>\n<html>"))
    }

    @Test
    fun dashboard() {
        messageResourceDependencies.dashboardService = mock<DashboardService> {
            on { loadDashboard() } doReturn DashboardService.Dashboard("Test message!", 50)
        }

        val response = resourceTestRule.client().target("/dashboard").request().get()

        val responseContent: Map<Any, Any> = response.readEntity(object : GenericType<Map<Any, Any>>() {})
        assertThat(response.status, equalTo(200))
        assertThat(response.mediaType, equalTo(MediaType.valueOf("application/json")))
        assertThat(responseContent, equalTo(mapOf<Any, Any>(Pair("message", "Test message!"), Pair("updateCount", 50))))
    }

    @Test
    fun updateMessage() {
        messageResourceDependencies.messageUpdateService = mock<MessageUpdateService> {}

        val response = resourceTestRule.client().target("/updateMessage").request().post(
                Entity.entity("Test message!", MediaType.valueOf("text/plain; charset=utf-8")))

        assertThat(response.status, equalTo(204))
        assertThat(response.length, equalTo(0))
        assertThat(response.mediaType, nullValue())
        verify(messageResourceDependencies.messageUpdateService, times(1)).updateMessage("Test message!")
        verifyNoMoreInteractions(messageResourceDependencies.messageUpdateService)
    }

}
