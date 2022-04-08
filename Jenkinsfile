pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS=credentials('docker-hub-cred')
    }
    
    stages {
        stage('1-Checkout from git') {
            steps {
                echo '1 - Checkout project'
                git branch: 'master', url:'https://github.com/ArJau/isikaDeploiement.git'
            }
        }
        stage('2-Compile') {
            steps {
                echo '2 - Compile project'
                sh 'mvn clean compile'
            }
        }
        stage('3-Test') {
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
        stage('4-Package') {
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
        
        stage('5-Nettoyage des containers') {
            steps {
                echo 'Clean docker image and container'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker stop demo-isika || true'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker rm demo-isika || true'
                sh 'docker rmi demo-isika || true'
            }
            
        } 
        stage('6-Docker build') {
            steps {
                echo 'Docker Build'
                sh 'docker build -t demo-isika .'
            }
            
        }
        
        stage('7-Tag image') {
            steps {
                echo '9 - Tag image'
                sh 'docker tag demo-isika jaujau31/demo-isika'     
            }
            
        }
        stage('8-Login dockerhub') {
            steps {
                echo '10 - Login dockerhub'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'     
            }
            
        }
        stage('9-Push image dockerhub') {
            steps {
                echo '11 - Push image dockerhub'
                sh 'docker push jaujau31/demo-isika'   
            }
        }
        stage('10-Run Container to local') {
            steps {
                echo 'Docker run'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker run -d --name demo-isika -p8080:8080 jaujau31/demo-isika'   
            }
            
        }
        
        stage ('11-Deploy To Prod AWS'){
              input{
                message "Do you want to proceed for production deployment?"
              }
            steps {
                sh 'echo "Deploy into Prod"'
                sh 'ssh -v -o StrictHostKeyChecking=no ubuntu@35.180.117.10 sudo docker stop demo-isika || true'
                sh 'ssh -v -o StrictHostKeyChecking=no ubuntu@35.180.117.10 sudo docker rm demo-isika || true'
                sh 'ssh -v -o StrictHostKeyChecking=no ubuntu@35.180.117.10 sudo docker run -d --name demo-isika -p8080:8080 jaujau31/demo-isika'   
            

            }
        }

    }
    post {
        always {
            sh 'docker logout'
        }
    }
}
