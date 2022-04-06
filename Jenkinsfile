pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo '1 - Checkout project'
                git branch: 'master', url:'https://github.com/ArJau/isikaDeploiement.git'
            }
        }
        stage('Compile') {
            steps {
                echo '2 - Compile project'
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                echo '3 - Test project'
                sh 'mvn test'
            }
            
            post{
                success {//publication des tests
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package') {
            steps {
                echo '4 - Package project'
                sh 'mvn package -DskipTests'
            }
            
            post{
                always {//archive de tous les jar
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint:true
                }
            }
        }
    }
}
