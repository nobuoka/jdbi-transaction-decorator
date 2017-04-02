package info.vividcode.jdbi.transaction.decorator.demo.db.internal

import io.dropwizard.jdbi.*
import io.dropwizard.jdbi.args.*
import org.skife.jdbi.v2.DBI
import java.util.*
import javax.sql.DataSource

/**
 * See [DBIFactory.build] method.
 */
fun createTestDbi(dataSource: DataSource, dbTimeZone: TimeZone?, driverClassName: String) = DBI(dataSource).also { dbi ->
    dbi.registerArgumentFactory(GuavaOptionalArgumentFactory(driverClassName))
    dbi.registerArgumentFactory(OptionalArgumentFactory(driverClassName))
    dbi.registerArgumentFactory(OptionalDoubleArgumentFactory())
    dbi.registerArgumentFactory(OptionalIntArgumentFactory())
    dbi.registerArgumentFactory(OptionalLongArgumentFactory())
    dbi.registerColumnMapper(OptionalDoubleMapper())
    dbi.registerColumnMapper(OptionalIntMapper())
    dbi.registerColumnMapper(OptionalLongMapper())
    dbi.registerContainerFactory(ImmutableListContainerFactory())
    dbi.registerContainerFactory(ImmutableSetContainerFactory())
    dbi.registerContainerFactory(GuavaOptionalContainerFactory())
    dbi.registerContainerFactory(OptionalContainerFactory())

    val timeZone = dbTimeZone?.let { Optional.of(it) } ?: Optional.empty()
    dbi.registerArgumentFactory(JodaDateTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(LocalDateArgumentFactory())
    dbi.registerArgumentFactory(LocalDateTimeArgumentFactory())
    dbi.registerArgumentFactory(InstantArgumentFactory(timeZone))
    dbi.registerArgumentFactory(OffsetDateTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(ZonedDateTimeArgumentFactory(timeZone))

    // Should be registered after GuavaOptionalArgumentFactory to be processed first
    dbi.registerArgumentFactory(GuavaOptionalJodaTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(GuavaOptionalLocalDateArgumentFactory())
    dbi.registerArgumentFactory(GuavaOptionalLocalDateTimeArgumentFactory())
    dbi.registerArgumentFactory(GuavaOptionalInstantArgumentFactory(timeZone))
    dbi.registerArgumentFactory(GuavaOptionalOffsetTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(GuavaOptionalZonedTimeArgumentFactory(timeZone))

    // Should be registered after OptionalArgumentFactory to be processed first
    dbi.registerArgumentFactory(OptionalJodaTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(OptionalLocalDateArgumentFactory())
    dbi.registerArgumentFactory(OptionalLocalDateTimeArgumentFactory())
    dbi.registerArgumentFactory(OptionalInstantArgumentFactory(timeZone))
    dbi.registerArgumentFactory(OptionalOffsetDateTimeArgumentFactory(timeZone))
    dbi.registerArgumentFactory(OptionalZonedDateTimeArgumentFactory(timeZone))

    dbi.registerColumnMapper(JodaDateTimeMapper(timeZone))
    dbi.registerColumnMapper(InstantMapper(timeZone))
    dbi.registerColumnMapper(LocalDateMapper())
    dbi.registerColumnMapper(LocalDateTimeMapper())
    dbi.registerColumnMapper(OffsetDateTimeMapper())
    dbi.registerColumnMapper(ZonedDateTimeMapper())
}
