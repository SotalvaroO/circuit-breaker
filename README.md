# Custom Circuit breaker

## Description

<p>The Circuit Breaker pattern is a resilience pattern used in software development to prevent repeated failures in
distributed systems, especially in microservices. In this specific implementation, the circuit breaker was defined in
two ways: The first one is a class that wraps any business logic through a Supplier, so that, the circuit breaker class
needs to be injected to be used. The second implementation is more convoluted since it uses Spring AOP to define an
aspect that will act during the business logic through a custom annotation.</p>

## Objective

<p>The purpose of this repository is educational. It provides a circuit breaker implemented from scratch in Java (JDK 17)
using different modules of Spring Framework such as Spring Boot, Spring Web and Spring AOP. Moreover, JUnit and JMeter
was used for testing purpose.</p>

## Step by step
<p>
Leaving aside the creation and structuring of the project, here are some steps that were needed in order to achieve this circuit breaker
</p>

#### Circuit Breaker V1
1. Define some variables related with the different states of the circuit breaker such as the different states of the circuit breaker (Closed, Open and Half-Open) and the failure threshold, half-open threshold, etc.
2. Create an execute method that receives a supplier with the purpose of wrapping the business logic, this method will check if the state is open and if the last failure has passed the reset timeout, if that's the case, it will change the state to half open, otherwise, it will throw an exception that indicates that the circuit breaker is activated, then it will block all the requests.

#### Circuit Breaker V2
The second version of the circuit breaker is similar to the first one, the difference is that the execute method is no longer needed, as it makes every business rule depends on the circuit breaker, the best alternative would be to implement a custom annotation along with an aspect with AOP using Spring AOP, this annotation is responsible for intercepting all the requests and check whether the reset timeout is up or not.

## Tech stack
- Java (JDK 17)
- JMeter
- JUnit 5.4.11
- Spring Boot 3.4.3
- Spring Web 3.4.3

## Results

The results obtained were as expected, when the circuit breaker is open, it will not allow any request to execute any business logic until the reset time out expires. this is the main purpose of the circuit breaker pattern. Another important thing is that the variables were parametrized through the annotation.