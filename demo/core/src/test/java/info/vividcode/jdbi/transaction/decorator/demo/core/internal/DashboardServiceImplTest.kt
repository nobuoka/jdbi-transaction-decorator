package info.vividcode.jdbi.transaction.decorator.demo.core.internal

import com.nhaarman.mockito_kotlin.*
import info.vividcode.jdbi.transaction.decorator.demo.core.DashboardService
import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

internal class DashboardServiceImplTest {

    @Test
    fun loadDashboard() {
        val mockedDatabase = mock<Database> {
            on { readMessage() } doReturn "Test message!"
            on { readUpdateCount() } doReturn 3
        }
        val dashboardService = DashboardServiceImpl(object : DashboardServiceImpl.Dependencies {
            override val database = mockedDatabase
        })

        // Act
        val dashboard = dashboardService.loadDashboard()

        // Assert
        assertThat(dashboard, equalTo(DashboardService.Dashboard("Test message!", 3)))
        verify(mockedDatabase, times(1)).readMessage()
        verify(mockedDatabase, times(1)).readUpdateCount()
        verifyNoMoreInteractions(mockedDatabase)
    }

}
