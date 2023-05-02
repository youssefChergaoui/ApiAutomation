pipeline {
    agent any
    tools {
        maven 'Maven-3.9.1'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                sh "mvn clean install"
                sh "pwd"
                sh "mvn package"
            }
        }
        stage('Run') {
            steps {
                echo 'Running...'
                dir('./target/') {
                    sh "pwd"
                    sh "java -jar ApiAutomation-0.0.1-SNAPSHOT.jar"
                }
            }
        }
    }
}
