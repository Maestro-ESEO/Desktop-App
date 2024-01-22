# Project Management Desktop App

The Project Management Desktop App is a JavaFX application designed for project administrators. This app provides a user-friendly interface to manage projects, tasks, and collaborators efficiently. The app is built using Java 21 and utilizes JavaFX for the graphical user interface.

## Introduction

The Project Management Desktop App serves as a centralized tool for project administrators to manage and oversee various aspects of their projects. With an intuitive user interface, administrators can efficiently handle project details, tasks, and collaborators.

## Features

- User-friendly interface for project management.
- Efficient handling of projects, tasks, and collaborators.
- Desktop application for administrators.

### Prerequisites

Before you start, ensure that you have the following installed on your system:
- Java 21
- MySQL database

## Configuration

The Project Management Desktop App offers configuration options to customize settings. Please refer to the [Configuration](#configuration) section in the main README for details.

## Database Connection

To connect to a local database, follow those steps.

1. To create the database structure, refer to the README of the web app where every step is explained.
2. Go to java/com/maestro/desktop/utils/DatabaseConnection.java and update those variables with the data from your local database:
   private static final String URL;
   private static final String USER;
   private static final String PASS;
   Update those variables with the url to the database (with this format: "jdbc:mysql://HostName:Port/nameOfTheDatabase"), the user and the password.

## Dependencies

The Project Management Desktop App utilizes the following dependencies managed by Gradle:

```gradle
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation 'mysql:mysql-connector-java:8.0.33'
    implementation 'org.springframework.security:spring-security-crypto:6.1.2'
    implementation 'org.controlsfx:controlsfx:11.2.0'
    implementation 'com.mysql:mysql-connector-j:8.2.0'
}
