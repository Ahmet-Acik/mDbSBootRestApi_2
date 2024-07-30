pipeline {
    agent any

    environment {
        MONGODB_AUTH_DB = 'admin'
        MONGODB_DB = 'school'
        MONGODB_PORT = '27017'
        MONGODB_HOST = 'localhost'
    }

    stages {
        stage('Checkout code') {
            steps {
                checkout scm
            }
        }

        stage('Set up JDK 17') {
            steps {
                tool name: 'JDK 17', type: 'jdk'
                env.JAVA_HOME = "${tool 'JDK 17'}"
                env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
            }
        }

        stage('Wait for MongoDB to start') {
            steps {
                script {
                    def mongoStarted = false
                    for (int i = 0; i < 60; i++) {
                        if (sh(script: 'nc -z localhost 27017', returnStatus: true) == 0) {
                            mongoStarted = true
                            echo 'MongoDB is up'
                            break
                        }
                        echo 'Waiting for MongoDB...'
                        sleep 5
                    }
                    if (!mongoStarted) {
                        error 'MongoDB did not start in time'
                    }
                }
            }
        }

        stage('Check MongoDB logs') {
            steps {
                sh 'docker logs $(docker ps -q --filter ancestor=mongo:latest)'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install -e -X'
            }
        }

        stage('Start Spring Boot application') {
            steps {
                sh 'mvn spring-boot:run &'
                script {
                    def springBootStarted = false
                    for (int i = 0; i < 60; i++) {
                        if (sh(script: 'nc -z localhost 8080', returnStatus: true) == 0) {
                            springBootStarted = true
                            echo 'Spring Boot is up'
                            break
                        }
                        echo 'Waiting for Spring Boot...'
                        sleep 5
                    }
                    if (!springBootStarted) {
                        error 'Spring Boot did not start in time'
                    }
                }
            }
        }

        stage('Run smoke tests') {
            steps {
                sh 'mvn test -P smoke-tests -e -X'
            }
        }

        stage('Run unit tests') {
            steps {
                sh 'mvn test -e -X'
            }
        }

        stage('Run integration tests') {
            steps {
                sh 'mvn failsafe:integration-test -e -X'
            }
        }

        stage('Run Cucumber tests') {
            steps {
                sh 'mvn test -P cucumber-tests -Dcucumber.options="--plugin pretty" -e -X'
            }
        }
    }
}