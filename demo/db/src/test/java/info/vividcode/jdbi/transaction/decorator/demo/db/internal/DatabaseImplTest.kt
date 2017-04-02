package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import info.vividcode.jdbi.transaction.decorator.demo.core.Database
import info.vividcode.jdbi.transaction.decorator.demo.db.TableCreator
import org.h2.Driver
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.h2.jdbcx.JdbcConnectionPool
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.skife.jdbi.v2.DBI

internal class DatabaseImplTest {

    lateinit var dataSource: JdbcConnectionPool
    lateinit var dbi: DBI
    lateinit var database: Database

    @BeforeEach
    fun setUp() {
        dataSource = JdbcConnectionPool.create("jdbc:h2:mem:test", "", "")
        dbi = createTestDbi(dataSource, null, Driver::class.java.name)
        TableCreator(object : TableCreator.Dependencies {
            override val dbi = this@DatabaseImplTest.dbi
        }).createTables()
        database = DatabaseImpl(dbi.onDemand(SqlObjectProvider::class.java))
    }

    @AfterEach
    fun tearDown() {
        dataSource.dispose()
    }

    @Test
    fun incrementUpdateCount() {
        // Act
        database.incrementUpdateCount()
        // Assert
        assertThat(database.readUpdateCount(), equalTo(1))
    }

    @Test
    fun incrementUpdateCount_multiTimes() {
        // Act
        database.incrementUpdateCount()
        database.incrementUpdateCount()
        database.incrementUpdateCount()
        // Assert
        assertThat(database.readUpdateCount(), equalTo(3))
    }

    @Test
    fun readUpdateCount_noIncrement() {
        // Act
        val updateCount = database.readUpdateCount()
        // Assert
        assertThat(updateCount, equalTo(0))
    }

    @Test
    fun readUpdateCount_withIncrement() {
        // Arrange
        database.incrementUpdateCount()
        database.incrementUpdateCount()
        // Act
        val updateCount = database.readUpdateCount()
        // Assert
        assertThat(updateCount, equalTo(2))
    }

    @Test
    fun updateMessage() {
        // Act
        database.updateMessage("Test message!")
        // Assert
        assertThat(database.readMessage(), equalTo("Test message!"))
    }

    @Test
    fun updateMessage_multiTimes() {
        // Act
        database.updateMessage("Test message 1!")
        database.updateMessage("Test message 2!")
        database.updateMessage("Test message 3!")
        // Assert
        assertThat(database.readMessage(), equalTo("Test message 3!"))
    }

    @Test
    fun readMessage_noUpdate() {
        // Act
        val message = database.readMessage()
        // Assert
        assertThat(message, equalTo(""))
    }

    @Test
    fun readMessage_withUpdate() {
        // Arrange
        database.updateMessage("Test message!")
        // Act
        val message = database.readMessage()
        // Assert
        assertThat(message, equalTo("Test message!"))
    }

}
