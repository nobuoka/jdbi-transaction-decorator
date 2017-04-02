jdbi-transaction-decorator
==============================

[![CircleCI](https://circleci.com/gh/nobuoka/jdbi-transaction-decorator.svg?style=svg)](https://circleci.com/gh/nobuoka/jdbi-transaction-decorator)

This library helps you to make transactional-annotated methods of your application's application layer independent to jDBI.

## Library modules

This library has two modules.

* `info.vividcode.jdbi:transaction-decorator-api` : provides interface of `TransactionDecorator`.
  * Typically used by application layer.
* `info.vividcode.jdbi:transaction-decorator-jdbi` : provides implementation of `TransactionDecorator`.
  * Typically used by data access layer.
  * Depends on jDBI.

## Usage

Assume that you are developing web application using layered architecture.

### In your application layer

Use `info.vividcode.jdbi:transaction-decorator-api` module.

To use this library, you must separate an application service into an interface and an implementation.

```java
// Interface
public interface TestService {
  void doSomething();
}

// Implementation
class TestServiceImpl implements TestService {
  @MyTransactional
  public void doSomething() {
    // Do something
  }
}

// Annotation to represent that the method should be executed in transaction
@Retention(RetentionPolicy.RUNTIME)
  private @interface MyTransaction {
}
```

And then, you can provide an implementation of an application service through the `TransactionDecorator#decorate` method.

```java
TestService provideTestService(TransactionDecorator transactionDecorator) { // In this case, this object is provided by data access layer.
  return transactionDecorator.decorate(TestService.class, MyTransaction.class, new TestServiceImpl());
}
```

The annotated methods of the decorated object are executed in transaction.

### In your data access layer (using jDBI)

Use `info.vividcode.jdbi:transaction-decorator-jdbi` module.

To provide `TransactionDecorator` object, make a root SQL object have the `Transactional` interface.

```java
interface SqlObjectProvider extends Transactional<RootSqlObject> {
  @CreateSqlObject
  TestDao createTestDao();
}
```

To create `TransactionDecorator` object, call `JdbiTransactionDecorator` constrctor with your root SQL object.

```java
DBI dbi = new DBI(dataSource);
SqlObjectProvider sqlObjectProvider = dbi.onDemand(SqlObjectProvider.class);
TransactionDecorator transactionDecorator = new JdbiTransactionDecorator(sqlObjectProvider);
```

## Demo

This project contains the demo as sub project.
See [README of demo project](./demo/README.markdown).

## Why not using the `Transaction` annotation of jDBI directory?

To enable `Transaction` annotation of jDBI, application service must depends on jDBI and
must be instantiated by jDBI.

```java
public abstrace class TestService {
  @CreateSqlObject
  TestDao createTestDao();

  @Transaction
  public void doSomething() {
    // Do something
  }
}

// To instantiate `TestService` class:
TestService testService = dbi.onDemand(TestSerivce.class);
```

This way is not good if you want to let application layer avoid depending on detail of data access layer.

## License

```
Copyright 2017 Nobuoka Yu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
