Demo of jdbi-transaction-decorator
==================================

This is demo project of [`jdbi-transaction-decorator` library](https://github.com/nobuoka/jdbi-transaction-decorator).

It's a web application with layered architecture, which uses [Dropwizard](https://github.com/dropwizard/dropwizard) as WAF.

## Application structure

[![Application structure](https://docs.google.com/drawings/d/1BnyZIUmn2EItQ0R5ND2OFM3Mbs3QUua772o8YhCn4W0/pub?w=738&amp;h=486)](https://docs.google.com/drawings/d/1BnyZIUmn2EItQ0R5ND2OFM3Mbs3QUua772o8YhCn4W0/edit?usp=sharing)

This application use [Pseudo “Thin Cake” Pattern (Japanese)](https://nobuoka.github.io/presentations/2017/0120-kyobashi-dex-4.html) for Dependency Injection.

## Way to run application

```
../gradlew run
```
