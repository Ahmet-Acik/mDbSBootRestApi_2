pipeline {
    agent any

    environment {
        MONGODB_AUTH_DB = 'admin' // MongoDB authentication database
        MONGODB_DB = 'school' // MongoDB database name
        MONGODB_PORT = '27017' // MongoDB port
        MONGODB_HOST = 'localhost' // MongoDB host
    }

    stages {
        stage('Checkout code') {
            steps {
                checkout scm // Checkout the source code from the SCM
            }
        }

        stage('Set up JDK 17') {
            steps {
                tool name: 'JDK 17', type: 'jdk' // Set up JDK 17
                env.JAVA_HOME = "${tool 'JDK 17'}" // Set JAVA_HOME environment variable
                env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}" // Update PATH environment variable
            }
        }

        stage('Wait for MongoDB to start') {
            steps {
                script {
                    def mongoStarted = false
                    for (int i = 0; i < 60; i++) {
                        if (sh(script: 'nc -z localhost 27017', returnStatus: true) == 0) {
                            mongoStarted = true
                            echo 'MongoDB is up' // MongoDB is up and running
                            break
                        }
                        echo 'Waiting for MongoDB...' // Waiting for MongoDB to start
                        sleep 5
                    }
                    if (!mongoStarted) {
                        error 'MongoDB did not start in time' // Error if MongoDB did not start in time
                    }
                }
            }
        }

        stage('Check MongoDB logs') {
            steps {
                sh 'docker logs $(docker ps -q --filter ancestor=mongo:latest)' // Check the logs of the MongoDB container
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install -e -X' // Build the project using Maven
            }
        }

        stage('Start Spring Boot application') {
            steps {
                sh 'mvn spring-boot:run &' // Start the Spring Boot application
                script {
                    def springBootStarted = false
                    for (int i = 0; i < 60; i++) {
                        if (sh(script: 'nc -z localhost 8080', returnStatus: true) == 0) {
                            springBootStarted = true
                            echo 'Spring Boot is up' // Spring Boot is up and running
                            break
                        }
                        echo 'Waiting for Spring Boot...' // Waiting for Spring Boot to start
                        sleep 5
                    }
                    if (!springBootStarted) {
                        error 'Spring Boot did not start in time' // Error if Spring Boot did not start in time
                    }
                }
            }
        }

        stage('Run smoke tests') {
            steps {
                sh 'mvn test -P smoke-tests -e -X' // Run smoke tests using Maven
            }
        }

        stage('Run unit tests') {
            steps {
                sh 'mvn test -e -X' // Run unit tests using Maven
            }
        }

        stage('Run integration tests') {
            steps {
                sh 'mvn failsafe:integration-test -e -X' // Run integration tests using Maven
            }
        }

        stage('Run Cucumber tests') {
            steps {
                sh 'mvn test -P cucumber-tests -Dcucumber.options="--plugin pretty" -e -X' // Run Cucumber tests using Maven
            }
        }
    }
}