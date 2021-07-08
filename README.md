# checkoutApp

This is a simple checkout app that can be a part of a store, calculating the total amount a user should be charged based on the items they scanned.


The app is written using the next libraries:

* Java 11
* Spring boot 2.5
* JooQ (not implemented)
* Liquibase (connection to MySQL) 
* Lombok
* Swagger
* JUnit
* Mockito

The app provides Rest APIs:

* POST /checkout - calculates the total price of a basked based on provided items

The project is designed in the way that it’s easy to split later in development lifecycle and consist of 3 modules (directories atm): 

* Checkout - module to manage checkout (including payment later)
* Sku - module to manage promotions
* Promotions - module to manage and calculate the promotions.

