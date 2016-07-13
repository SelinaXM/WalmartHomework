Walmart Coding Assignment

Ticket Service

X. Ma, 07/12/2016

===================================
1. Assumptions
===================================

1). A seat can be held only if it is currently available.

2). A seat can be reserved only if it is currently held.

3). A hold placed on held seats expires after a threshold value. 
     The threshold value is set to be 30 seconds for testing purpose.
     
4). A held seat must be reserved before 30 seconds otherwise the hold is released. 

5). No seat is held if the number of requested seats is greater than the available seats.

===================================
2. Build Project By Gradle
===================================

Run "gradle build" command inside of this project folder

===================================
3. Test Project By Gradle
===================================

Run "gradle test" command inside of this project folder

===================================
4. Testing
===================================

The project has been tested in the following environment:

Windows 8.1 pro

Java 1.8.0_45

Gradle 2.14
