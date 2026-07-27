pipeline {
    agent any
    environment {
        ECR_REPO = "deep/flight-backend"
        DOCKER_USER = "deepbijwe"
        CLUSTER_NAME = "cbz-cluster"
        REGION = "ap-south-1"

    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/deepbijwe/flight-reservation-backend.git'
            }
        }

        stage('Verify Java') {
            steps {
                sh '''
                whoami
                echo "JAVA_HOME=$JAVA_HOME"
                which java
                java -version
                which javac
                javac -version
                mvn -version
                '''
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t ${ECR_REPO}:${BUILD_NUMBER} .
                ''' 
            }
        }
        // stage('Docker login'){
        //     steps{
        //        withCredentials([
        //                 usernamePassword(
        //                     credentialsId: 'docker-hub-creds',
        //                     usernameVariable: 'DOCKER_USERNAME',
        //                     passwordVariable: 'DOCKER_PASSWORD'
        //                 )
        //             ]) 
        //             {
        //                 sh 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
        //             }
        //         }
        //     }

        // stage('Docker Push') {
        //     steps {
        //         withCredentials([
        //             usernamePassword(
        //                 credentialsId: 'docker-hub-creds',
        //                 usernameVariable: 'DOCKER_USERNAME',
        //                 passwordVariable: 'DOCKER_PASSWORD'
        //             )
        //         ]) {
        //             sh '''
        //                 docker tag ${DOCKER_REPO}:${BUILD_NUMBER} \
        //                 ${DOCKER_USER}/${DOCKER_REPO}:${BUILD_NUMBER}

        //                 docker push ${DOCKER_USER}/${DOCKER_REPO}:${BUILD_NUMBER}
        //             '''
        //         }
        //     }
        // }
        // stage('Image-Name-change'){
        //         steps {
          
        //             sh '''
        //              sed -i "s|mayurwagh/node-app:latest|${DOCKER_USER}/${DOCKER_REPO}:${BUILD_NUMBER}|g" k8s/deployment.yaml
        //             '''
        //             sh 'cat k8s/deployment.yaml'
        //         }
        //     }
        // stage('Deploy to cluster'){
        //         steps{
        //             withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws_creds', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
        //                  sh '''
        //                     aws eks update-kubeconfig --name ${CLUSTER_NAME} --region ${REGION}
                            
        //                     kubectl get nodes
        //                     kubectl apply -f k8s/deployment.yaml
        //                     kubectl apply -f k8s/service.yaml
        //                     kubectl get pods 
        //                     kubectl get deployment
        //                     kubectl get svc 
        //                  '''
        //             }
        //         }
        //    }
    }
}