package info.vividcode.jdbi.transaction.decorator.demo.core.internal

import com.nhaarman.mockito_kotlin.*
import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import org.junit.jupiter.api.Test

internal class MessageUpdateServiceImplTest {

    @Test
    fun updateMessage() {
        val mockedDatabase = mock<Database> {}
        val messageUpdateService = MessageUpdateServiceImpl(object : MessageUpdateServiceImpl.Dependencies {
            override val database = mockedDatabase
        })

        // Act
        messageUpdateService.updateMessage("Test message!")

        // Assert
        verify(mockedDatabase, times(1)).updateMessage("Test message!")
        verify(mockedDatabase, times(1)).incrementUpdateCount()
        verifyNoMoreInteractions(mockedDatabase)
    }

}
