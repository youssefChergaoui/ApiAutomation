pipeline {
    agent any
    tools {
        maven 'Maven-3.9.1'
    }
    stages {
        stage('Build') {
            steps {
//                 echo 'Building...'
                bat "mvn clean install"
                bat "pwd"
                bat "mvn package"
            }
        }
        stage('Run') {
            steps {
//                 echo 'Running...'
                dir('./target/') {
                    bat "pwd"
                    bat "java -jar ApiAutomation-0.0.1-SNAPSHOT.jar"
                }
            }
        }
    }
}
