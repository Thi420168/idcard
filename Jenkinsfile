pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Git Pull') {
            steps {
                git 'https://github.com/Thi420168/idcard.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'ansible-playbook deploy.yml'
            }
        }
    }

    post {
        failure {
            mail to: 'srengty@gmail.com',
                 subject: 'Jenkins Build Failed',
                 body: 'Build failed. Please check Jenkins.'
        }
    }
}