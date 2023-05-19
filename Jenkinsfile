pipeline {
    agent any
    tools {
        maven 'maven-3.9.1'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh "mvn clean install"
                sh "mvn package"
            }
        }
        stage('Run') {
            steps {
                echo 'Running...'
                echo "--------------------PWD-------------------------"
                sh "pwd"
                sh "ls"
                dir('./target/') {
                    sh "java -jar ApiAutomation-0.0.1-SNAPSHOT.jar"
                }
            }
        }
    }
}
