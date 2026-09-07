pipeline {

    agent any

    environment {
        DOCKER_IMAGE = "shreyasdocs/spring-boot-ci-demo:latest"
        EC2_HOST = "13.127.87.141"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/shrysshety97/spring-boot-ci-demo.git'
            }
        }

        stage('Build') {
            steps {
                sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t $DOCKER_IMAGE .
                '''
            }
        }

        stage('Docker Push') {
            steps {
                sh '''
                    docker push $DOCKER_IMAGE
                '''
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no \
                            ec2-user@$EC2_HOST "
                                docker rm -f spring-demo || true
                                docker pull $DOCKER_IMAGE
                                docker run -d \
                                    --name spring-demo \
                                    -p 8081:8081 \
                                    $DOCKER_IMAGE
                            "
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }

        failure {
            echo 'Deployment failed!'
        }
    }
}
