/*
 * Copyright 2017 Nobuoka Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.vividcode.jdbi.transaction.decorator.jdbi;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.*;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JdbiTransactionDecoratorTest {

    private TestDao testDao;

    private JdbiTransactionDecorator<TestDao> transactionDecorator;

    @BeforeEach
    public void init() {
        DataSource ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "username", "password");
        DBI dbi = new DBI(ds);
        testDao = dbi.onDemand(TestDao.class);
        testDao.createTable();

        transactionDecorator = new JdbiTransactionDecorator<>(testDao);
    }

    @AfterEach
    public void tearDown() {
        testDao.dropTable();
    }

    @Nested
    @DisplayName("annotated method of decorated object")
    public class AnnotatedMethodOfDecoratedObject {
        private TestApplicationService decoratedObject;

        @BeforeEach
        public void init() {
            decoratedObject = transactionDecorator.decorate(
                    TestApplicationService.class, MyTransaction.class, new TestApplicationServiceImpl(testDao));
        }

        @Test
        @DisplayName("should commit changes when error doesn't occur")
        public void decorate_annotatedMethod_commit() throws Throwable {
            decoratedObject.doSomething(null);

            // Assert
            assertThat(testDao.selectAll(), is(new HashSet<>(Arrays.asList(100, 200))));
        }

        @Nested
        @DisplayName("should rollback changes")
        public class RollbackOnErrorThrownTest {
            @Test
            @DisplayName("when `Throwable` is thrown")
            public void whenThrowableIsThrown() throws Throwable {
                Optional<Throwable> thrownOptional = tryCatch(() -> decoratedObject.doSomething(() -> new Throwable("Test throwable")));

                // Assert
                Throwable thrown = thrownOptional.orElseThrow(() -> new AssertionError("Exception must be thrown."));
                assertThat(thrown.getClass(), equalTo(Throwable.class));
                assertThat(thrown.getMessage(), is("Test throwable"));
                assertThat(testDao.selectAll(), is(Collections.emptySet()));
            }

            @Test
            @DisplayName("when `Exception` is thrown")
            public void whenExceptionIsThrown() throws Throwable {
                Optional<Throwable> thrownOptional = tryCatch(() -> decoratedObject.doSomething(() -> new Exception("Test exception")));

                // Assert
                Throwable thrown = thrownOptional.orElseThrow(() -> new AssertionError("Exception must be thrown."));
                assertThat(thrown.getClass(), equalTo(Exception.class));
                assertThat(thrown.getMessage(), is("Test exception"));
                assertThat(testDao.selectAll(), is(Collections.emptySet()));
            }

            @Test
            @DisplayName("when `RuntimeException` is thrown")
            public void whenRuntimeExceptionIsThrown() throws Throwable {
                Optional<Throwable> thrownOptional = tryCatch(() -> decoratedObject.doSomething(() -> new RuntimeException("Test runtime exception")));

                // Assert
                Throwable thrown = thrownOptional.orElseThrow(() -> new AssertionError("Exception must be thrown."));
                assertThat(thrown.getClass(), equalTo(RuntimeException.class));
                assertThat(thrown.getMessage(), is("Test runtime exception"));
                assertThat(testDao.selectAll(), is(Collections.emptySet()));
            }

            @Test
            @DisplayName("when `Error` is thrown")
            public void whenErrorIsThrown() throws Throwable {
                Optional<Throwable> thrownOptional = tryCatch(() -> decoratedObject.doSomething(() -> new Error("Test error")));

                // Assert
                Throwable thrown = thrownOptional.orElseThrow(() -> new AssertionError("Exception must be thrown."));
                assertThat(thrown.getClass(), equalTo(Error.class));
                assertThat(thrown.getMessage(), is("Test error"));
                assertThat(testDao.selectAll(), is(Collections.emptySet()));
            }
        }
    }

    public interface TestDao extends Transactional<TestDao> {
        @SqlUpdate("CREATE TABLE test (value INTEGER NOT NULL)")
        void createTable();

        @SqlUpdate("DROP TABLE test")
        void dropTable();

        @SqlUpdate("INSERT INTO test (value) VALUES (:value)")
        void insert(@Bind("value") int value);

        @SqlQuery("SELECT value FROM test")
        Set<Integer> selectAll();
    }

    public interface TestApplicationService {
        void doSomething(@Nullable Supplier<Throwable> thrower) throws Throwable;
    }

    private static class TestApplicationServiceImpl implements TestApplicationService {
        private final TestDao testDao;

        private TestApplicationServiceImpl(TestDao testDao) {
            this.testDao = testDao;
        }

        @MyTransaction
        @Override
        public void doSomething(@Nullable Supplier<Throwable> thrower) throws Throwable {
            testDao.insert(100);
            if (thrower != null) throw thrower.get();
            testDao.insert(200);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface MyTransaction {
    }

    private static Optional<Throwable> tryCatch(ThrowableProcess process) {
        try {
            process.exec();
        } catch (Throwable e) {
            return Optional.of(e);
        }
        return Optional.empty();
    }

    @FunctionalInterface
    private interface ThrowableProcess {
        void exec() throws Throwable;
    }

}
