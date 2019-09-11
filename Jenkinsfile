node{
    stage('SCM Checkout'){
       
        git 'https://github.com/miguelgonch/ProyectoDB2-Hospital.git'
    }
    stage('SYNC Git'){
       
        sh 'git fetch --all'
                   
        sh 'git merge master'
    }
    stage('Compile-Package'){
        //obtener el maven
         def mvnHome = tool name: 'Maven 3.6.2', type: 'maven'

        sh "${mvnHome}/bin/mvn package"
    }
    /*
    agent any
    
    stages {
        stage('Ok') {
            steps {
                echo "Ok"
            }
        }
    }
    post {
        always {
            emailext body: 'A Test EMail', recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: 'Test'
        }
    }
*/
}