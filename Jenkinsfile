pipeline{
    agent any
    stages{
        stage('Build'){
            steps{
                gradle 'build'
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