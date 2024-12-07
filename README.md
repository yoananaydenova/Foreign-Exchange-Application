# Foreign-Exchange-Application

This is simple foreign exchange application that is eveloped as RESTful APIs with Spring Boot and running without need of extra configuration.
For quick setup and presentational convenience is used in memory DB - H2. and for build and dependency management tool is used Maven.
The application has central error logging mechanism, unit tests and API Documentation.
In the case of an error, a specific code to the error and a meaningful message is provided to client. 

The application use https://app.exchangerate-api.com as a service provider to retrieve exchange rates with limitations of service provider free services (e.g. no
real time rates).
   

Functional requirements:
1. Exchange Rate API
 - input : currency pair to retrieve the exchange rate
 - output : exchange rate
2. Conversion API:
 - input: source amount, source currency, target currency
 - output: amount in target currency, and transaction id.
3. Conversion List API
 - input: transaction id or transaction date (at least one of the inputs shall be provided for each call)
 - output: list of conversions filtered by the inputs and paging (required).
 

Useful links about documentation API:

 - Access the API documentation at:
   http://localhost:8080/api-docs

 - Access the API documentation with Swagger UI at:
   http://localhost:8080/swagger-ui.html
	
 - Access the custom H2 Console URL at:
   http://localhost:8080/h2
