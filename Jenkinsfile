pipeline {
    agent any

    stages {
        stage('Checkout from git') {
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
        stage('Transfer des jar sur jenkins') {
            steps {
                echo '5 - Transfer jar'
                script {
                    sshPublisher(publishers: [
                        sshPublisherDesc(configName: 'docker-host', transfers:[
                            sshTransfer(
                              sourceFiles:"target/*.jar",
                              removePrefixe:"target",
                              remoteDirectory:"//home//vagrant",
                              execCommand: "ls /"
                            ),sshTransfer(
                              sourceFiles:"Dockerfile",
                              removePrefixe:"",
                              remoteDirectory:"//home//vagrant"
                            )
                        ])
                    ])                
                }
            }
        }  
        stage('Nettoyage des containers') {
            steps {
                echo 'Clean docker image and container'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker stop demo-isika || true'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker rm demo-isika || true'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker rmi demo-isika || true'
            }
            
        } 
        stage('Docker build') {
            steps {
                echo 'Docker Build'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker build -t demo-isika .'
            }
            
        } 
        stage('Docker run') {
            steps {
                echo 'Deploy'
                sh 'ssh -v -o StrictHostKeyChecking=no vagrant@192.168.33.11 sudo docker run -d --name demo-isika -p8080:8080 demo-isika'   
            }
            
        }
    }
}
