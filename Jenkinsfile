pipeline {
    
    agent any

    tools {
        maven 'mvn3'
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
                echo "current commit ${GIT_COMMIT}"
                echo "current commit author ${GIT_COMMITTER_NAME}"
                echo "current commit author email ${GIT_COMMITTER_EMAIL}"
                echo "previous succesful commit ${GIT_PREVIOUS_SUCCESFUL_COMMIT}"

                git 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git'
            }
        }

        stage('############### MERGE BRANCHES ##################') {
             when { expression { env.BRANCH_NAME != 'master' } }
            steps {
                sh 'mvn test'
            }
        }
        
        stage('############### TEST ##################') {
             when { expression { env.BRANCH_NAME != 'master' } }
            steps {
                sh 'mvn test'
            }
        }

        
        stage ('############### Sonarqube ##################') {
             when { expression { env.BRANCH_NAME != 'master' } }
            steps {
                withSonarQubeEnv('sonarqubescanner') {
               sh 'mvn sonar:sonar -Dsonar.jdbc.url=jdbc:h2:tcp://192.168.69.4:9000/sonar -Dsonar.host.url=http://192.168.69.4:9000'
                }
            }
        }


    
        stage("############### Quality Gate ##################") {
             when { expression { env.BRANCH_NAME != 'master' } }
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

        
     
        stage('############### TOMCAT DEPLOYMENT ##################') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat-dev', path: '', url: 'http://192.168.69.4:8888/')], contextPath: null, war: 'target/*.war'
                
            }
        }



    



    } //fin stages

    post {
        always {
            echo 'sending email'
            mail bcc: '', body: "<b>Build information</b><br>Project/branch: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Build url: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Build: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, manuelecastilloo@gmail.com";
        }
        success {  
             echo 'Build Success email sending'  
             mail bcc: '', body: "<b>Build information</b><br>Project/branch: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Build url: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Build success: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, manuelecastilloo@gmail.com";
         }  
         failure {  
            emailtext bcc: '', body: "<b>Build information</b><br>Project/branch: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> Build url: ${env.BUILD_URL}",
              cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Build failure: Project name -> ${env.JOB_NAME}", to: "castillo151148@unis.edu.gt, manuelecastilloo@gmail.com";
         }  
         
    }  //fin post

} //fin pipeline

//

