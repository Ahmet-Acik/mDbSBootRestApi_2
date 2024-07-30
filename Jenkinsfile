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
                script {
                    def javaHome = tool name: 'JDK 17', type: 'jdk'
                    env.JAVA_HOME = javaHome
                    env.PATH = "${javaHome}/bin:${env.PATH}"
                }
            }
        }

        stage('Start MongoDB') {
            steps {
                script {
                    docker.image('mongo:latest').withRun('-p 27017:27017') { c ->
                        sh 'for i in `seq 1 60`; do nc -z localhost 27017 && echo "MongoDB is up" && exit 0; echo "Waiting for MongoDB..."; sleep 5; done; echo "MongoDB did not start in time" && exit 1'
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
                    sh 'for i in `seq 1 60`; do nc -z localhost 8080 && echo "Spring Boot is up" && exit 0; echo "Waiting for Spring Boot..."; sleep 5; done; echo "Spring Boot did not start in time" && exit 1'
                }
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
                sh 'mvn test -Dcucumber.options="--plugin pretty" -e -X'
            }
        }
    }
}