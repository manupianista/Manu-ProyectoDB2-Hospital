pipeline {
    
    agent any

    

    tools {
        maven 'maven'
        jdk 'JDK-9'
    }

    stages {

        stage ('############### Initialize ##################') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('############### GIT STUFF ##################') {
            steps {
                git 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git'
            }
        }

        
        /*
        stage('############### TEST ##################') {
            steps {
                sh 'mvn test'
            }
        }*/

        
        stage ('############### Sonarqube ##################') {
            steps {
                withSonarQubeEnv('Sonarqube') {
               sh 'mvn sonar:sonar -Dsonar.jdbc.url=jdbc:h2:tcp://172.18.0.1:9000/login?from=%2F/sonar -Dsonar.host.url=http://172.18.0.1:9000'
                }
            }
        }


    
        stage("############### Quality Gate ##################") {
            steps {
              timeout(time: 1, unit: 'HOURS') {
                  waitForQualityGate abortPipeline: true
                
                
              }
            }
        }
        
        
        stage('############### CLEAN ##################') {
            steps {
                sh 'mvn clean'
            }
        }

        stage ('############### BUILD ##################') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
            }
        }

        
        
        stage('############### DEPLOY ##################') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcatcosa', path: '', url: 'http://localhost:8888/')], contextPath: null, war: '**/*.war'
            }
        }


    



    } //fin stages

    post {
        always {
            echo 'sending email'
            mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, jflores@unis.edu.gt";
        }
        success {  
             echo 'This will run only if successful'  
             mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, jflores@unis.edu.gt";
         }  
         failure {  
            emailtext bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, jflores@unis.edu.gt";
         }  
         
    } //fin post

} //fin pipeline

//

