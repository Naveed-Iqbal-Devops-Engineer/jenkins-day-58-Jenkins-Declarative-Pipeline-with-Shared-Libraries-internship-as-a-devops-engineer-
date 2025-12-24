pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        AWS_ECR_REPO_FRONTEND = 'three-tier-app-frontend'
        AWS_ECR_REPO_BACKEND = 'three-tier-app-backend'
        ECR_URI = '534589602727.dkr.ecr.us-east-1.amazonaws.com'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Clone the Repo') {
            steps {
                git branch: 'main', url: 'https://github.com/Naveed-Iqbal-Devops-Engineer/day54.git'
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'aws-ecr',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(credentialsId: 'aws-ecr-session', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
                        export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
                        export AWS_SESSION_TOKEN=$AWS_SESSION_TOKEN
                        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_URI}
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    env.FRONTEND_TAG = "${ECR_URI}/${AWS_ECR_REPO_FRONTEND}:${IMAGE_TAG}"
                    env.BACKEND_TAG = "${ECR_URI}/${AWS_ECR_REPO_BACKEND}:${IMAGE_TAG}"

                    sh "docker build -t ${env.BACKEND_TAG} ./backend"
                    sh "docker build -t ${env.FRONTEND_TAG} ./frontend"
                }
            }
        }

        stage('Push Images to ECR') {
            steps {
                sh """
                    docker push ${env.FRONTEND_TAG}
                    docker push ${env.BACKEND_TAG}
                """
            }
        }

        stage('Clean up after push') {
            steps {
                sh """
                    docker rmi -f ${env.FRONTEND_TAG} || true
                    docker rmi -f ${env.BACKEND_TAG} || true
                """
            }
        }

        stage('Update docker-compose.yml') {
            steps {
                sh """
                    sed -i 's|${ECR_URI}/.*three-tier-app-backend:.*|${env.BACKEND_TAG}|' docker-compose.yml
                    sed -i 's|${ECR_URI}/.*three-tier-app-frontend:.*|${env.FRONTEND_TAG}|' docker-compose.yml
                """
            }
        }

        stage('Run with Docker Compose') {
            steps {
                sh """
                    docker-compose down
                    docker-compose pull
                    docker-compose up -d
                """
            }
        }
    }

    post {
        success {
            echo "✅ Successfully deployed app using AWS ECR images."
        }
        failure {
            echo "❌ Deployment failed. Please check logs."
        }
    }
}
