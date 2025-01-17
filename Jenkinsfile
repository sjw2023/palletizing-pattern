pipeline{
    agent any
    stages{
        stage('Build'){
            steps{
                sh(script: 'gradle build')
                echo 'Building the project...'
            }
        }
        stage('Deploy'){
            steps{
                echo 'Deploying the project...'
            }

        }
    }
}