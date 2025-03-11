pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS = credentials('docker-credentials')  // Jenkins Credentials ID 사용
        SPRING_RABBITMQ_USERNAME = credentials('spring-rabbitmq-username')
        SPRING_RABBITMQ_PASSWORD = credentials('spring-rabbitmq-password')
        SPRING_RABBITMQ_HOST = credentials('spring-rabbitmq-host')
        SPRING_RABBITMQ_PORT = credentials('spring-rabbitmq-port')

        DATABASE_URL = credentials('database-url')
        DATABASE_USERNAME = credentials('database-username')
        DATABASE_PASSWORD = credentials('database-password')

        OPENAI_API_KEY = credentials('openai-api-key')
        OPENAI_API_URL = credentials('openai-api-url')
        OPENAI_MODEL = credentials('openai-model')

        KAKAO_CLIENT_ID = credentials('kakao-client-id')
        KAKAO_AUTHORIZATION_URI = credentials('kakao-authorization-uri')
        KAKAO_USER_INFO_URI = credentials('kakao-user-info-uri')
        KAKAO_REDIRECT_URI = credentials('kakao-redirect-uri')
        KAKAO_API_KEY = credentials('kakao-api-key')

        SPRING_DATA_REDIS_HOST = credentials('spring-data-redis-host')
        SPRING_DATA_REDIS_PORT = credentials('spring-data-redis-port')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/fourroro/nolleogasil_backend.git', credentialsId: 'nolleogasil-jenkins-token']]
                ])
            }
        }
        stage('Build Spring Boot') {
            steps {
                script {
                    // Spring Boot Docker 이미지 빌드
                    sh '''
                    docker build -t $DOCKER_CREDENTIALS_USR/nolleogasil_backend -f Dockerfile.spring . \
                       --build-arg SPRING_RABBITMQ_USERNAME=${SPRING_RABBITMQ_USERNAME} \
                       --build-arg SPRING_RABBITMQ_PASSWORD=${SPRING_RABBITMQ_PASSWORD} \
                       --build-arg SPRING_RABBITMQ_HOST=${SPRING_RABBITMQ_HOST} \
                       --build-arg SPRING_RABBITMQ_PORT=${SPRING_RABBITMQ_PORT} \
                       --build-arg SPRING_DATASOURCE_URL=${DATABASE_URL} \
                       --build-arg SPRING_DATASOURCE_USERNAME=${DATABASE_USERNAME} \
                       --build-arg SPRING_DATASOURCE_PASSWORD=${DATABASE_PASSWORD} \
                       --build-arg OPENAI_API_KEY=${OPENAI_API_KEY} \
                       --build-arg OPENAI_API_URL=${OPENAI_API_URL} \
                       --build-arg OPENAI_MODEL=${OPENAI_MODEL} \
                       --build-arg SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID} \
                       --build-arg SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_AUTHORIZATION_URI=${KAKAO_AUTHORIZATION_URI} \
                       --build-arg SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_USER_INFO_URI=${KAKAO_USER_INFO_URI} \
                       --build-arg SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI} \
                       --build-arg KAKAO_API_KEY=${KAKAO_API_KEY} \
                       --build-arg SPRING_DATA_REDIS_HOST=${SPRING_DATA_REDIS_HOST} \
                       --build-arg SPRING_DATA_REDIS_PORT=${SPRING_DATA_REDIS_PORT} \
                       --build-arg SPRING_SESSION_REDIS_NAMESPACE=nolleogasil:session \
                    '''
                }
            }
        }
        stage('Push Docker Images') {
            steps {
                script {
                    // Docker Hub 로그인
                    sh '''
                    echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin
                    '''

                    // Spring Boot 이미지 푸시
                    sh 'docker push $DOCKER_CREDENTIALS_USR/nolleogasil_backend'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                   sh '''
                   docker pull $DOCKER_CREDENTIALS_USR/nolleogasil_backend

                   # spring-container가 이미 있으면, 중지하고 삭제
                   if [ $(docker ps -q -f name=spring-container) ]; then
                       echo "Stopping existing container..."
                       docker stop spring-container || true
                   fi

                   if [ $(docker ps -aq -f name=spring-container) ]; then
                       echo "Removing existing container..."
                       docker rm spring-container || true
                   fi

                    # Docker에서 dangling 이미지 ID 목록 조회
                    dangling_images=$(docker images -f dangling=true -q)

                    # 결과가 비어 있지 않다면, 이미지 삭제
                    if [ -n "$dangling_images" ]; then
                        docker rmi -f $dangling_images
                    else
                        echo "No dangling images to remove."
                    fi

                    docker run -d -p 8080:8080 --name spring-container \
                        -e SPRING_RABBITMQ_USERNAME=${SPRING_RABBITMQ_USERNAME} \
                        -e SPRING_RABBITMQ_PASSWORD=${SPRING_RABBITMQ_PASSWORD} \
                        -e SPRING_RABBITMQ_HOST=${SPRING_RABBITMQ_HOST} \
                        -e SPRING_RABBITMQ_PORT=${SPRING_RABBITMQ_PORT} \
                        -e SPRING_DATASOURCE_URL=${DATABASE_URL} \
                        -e SPRING_DATASOURCE_USERNAME=${DATABASE_USERNAME} \
                        -e SPRING_DATASOURCE_PASSWORD=${DATABASE_PASSWORD} \
                        -e OPENAI_API_KEY=${OPENAI_API_KEY} \
                        -e OPENAI_API_URL=${OPENAI_API_URL} \
                        -e OPENAI_MODEL=${OPENAI_MODEL} \
                        -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID} \
                        -e SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_AUTHORIZATION_URI=${KAKAO_AUTHORIZATION_URI} \
                        -e SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KAKAO_USER_INFO_URI=${KAKAO_USER_INFO_URI} \
                        -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI} \
                        -e KAKAO_API_KEY=${KAKAO_API_KEY} \
                        -e SPRING_DATA_REDIS_HOST=${SPRING_DATA_REDIS_HOST} \
                        -e SPRING_DATA_REDIS_PORT=${SPRING_DATA_REDIS_PORT} \
                        -e SPRING_SESSION_REDIS_NAMESPACE=nolleogasil:session \
                        $DOCKER_CREDENTIALS_USR/nolleogasil_backend
                    '''
                }
            }
        }
    }
    post {
            failure {
                script {
                    currentBuild.result = 'FAILURE'
                    echo "Nolleogasil_backend build failed with status: ${currentBuild.result}"
                }
            }
            always {
                echo 'Nolleogasil_backend build and deployment completed.'
            }
        }
}
