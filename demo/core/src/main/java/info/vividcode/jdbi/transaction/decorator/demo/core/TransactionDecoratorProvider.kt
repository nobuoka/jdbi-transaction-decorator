package info.vividcode.jdbi.transaction.decorator.demo.core

import info.vividcode.jdbi.transaction.decorator.api.TransactionDecorator

interface TransactionDecoratorProvider {
    val transactionDecorator: TransactionDecorator
}
