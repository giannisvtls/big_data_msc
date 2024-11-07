
# Big Data exercises!

A repository created to serve as a collection of exercises written in Java for the Big Data Analysis class for the MsC Web and Data Science AUTH

## Exercise 2
### Prerequisites

 - Java >= 8, Created using Java17

### Description

#### Overview

This program simulates a healthcare scenario where two concurrent threads manage the flow of ICU cases in a hospital environment. It consists of two main components: a **Disease** thread that generates new ICU cases and a **Hospital** thread that recovers cases and frees up ICU beds. The threads interact with shared resources (ICU bed occupancy) using a locking mechanism to avoid data inconsistencies.

#### Functionality

The **Disease** thread generates new cases at random intervals, constrained by the maximum number of cases (`MAX_NEW_CASES`). If ICU capacity (`MAX_ICU_CAPACITY`) is exceeded, the overflow cases are rejected. The **Hospital** thread recovers patients at random intervals, freeing up beds as specified by `MAX_RECOVERIES`. The total recovered and rejected cases are logged for final reporting.

#### Synchronization and Configuration

A `ReentrantLock` and `Condition` are used to coordinate access to shared variables, ensuring thread-safe updates on ICU bed occupancy. Both the `Disease` and `Hospital` threads are configured to sleep between iterations, emulating real-time case management intervals. Configurable parameters such as ICU capacity, iteration count, and thread sleep intervals can be set via environment variables or default values.

### How to run:
1. Clone the project
2. Optionally set the environment variables for the following values

| ENV VAR | DEFAULT VALUE |
|--|--|
| DISEASE_THREAD_SLEEP_TIME_MS | 3000 |
|HOSPITAL_THREAD_SLEEP_TIME_MS|5000|
|ITERATIONS|20|
|MAX_ICU_CAPACITY|20|
|MAX_NEW_CASES|10|
|MAX_RECOVERIES|5|

3. Run `Main.java`
