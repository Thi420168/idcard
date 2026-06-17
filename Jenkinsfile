pipeline {
agent any

```
triggers {
    pollSCM('H/5 * * * *')
}

stages {

    stage('Git Pull') {
        steps {
            git branch: 'main', url: 'https://github.com/Thi420168/idcard.git'
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
            echo 'Deploy using Ansible Playbook'
            sh 'echo ansible-playbook deploy.yml'
        }
    }
}

post {
    failure {
        mail(
            to: 'srengty@gmail.com',
            subject: "Build Failed: ${env.JOB_NAME}",
            body: "Build failed. Please check Jenkins console output."
        )
    }
}
```

}
