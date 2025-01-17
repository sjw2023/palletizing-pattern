pipeline{
    agent any
    stages{
        stage('Build'){
            steps{
                sh(script: './gradlew build')
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