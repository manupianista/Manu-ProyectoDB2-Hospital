pipeline{
    agent any  
     stages {  
    stage('SCM Checkout'){
       steps{
        git 'https://github.com/miguelgonch/ProyectoDB2-Hospital.git'
       }
    }
    stage('SYNC Git'){
       steps{
        sh 'git fetch --all'   

        sh 'git merge master'
       }
    }
     
         stage('Test') {  
             steps {  
                 sh 'echo "Fail!"; exit 1'  
             }  
         }  
      
     post {  
         always {  
             echo 'This will always run'  
         }  
         success {  
             echo 'This will run only if successful'  
         }  
         failure {  
             mail bcc: '', body: "<b>Build</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt";  
         }  
         unstable {  
             echo 'This will run only if the run was marked as unstable'  
         }  
         changed {  
             echo 'success?'
         }  
     }
    stage('Compile-Package'){
        //obtener el maven
        
         def mvnHome = tool name: 'Maven 3.6.2', type: 'maven'
        steps{
        sh "${mvnHome}/bin/mvn package"
        }
    }
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