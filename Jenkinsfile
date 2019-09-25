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
        
        /*
        stage('############### CHECKOUT ##################') {
            steps {
                checkout scm
                git 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git'
            }
        }*/


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
                deploy adapters: [tomcat9(credentialsId: 'tomcatcosa', path: '', url: 'http://localhost:8888/')], contextPath: null, war: 'target/proyectoDB2-Hospital1.war'
            }
        }


    



    } //fin stages

    post {
        always {
            echo 'I will always say Hello again!'
            
            emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"
            
        }
    } //fin post

} //fin pipeline



/*
        stage('############### DEPLOY AFTER ##################') {
            echo 'branch name: ' + env.BRANCH_NAME

            if(env.BRANCH_NAME.startsWith("Development")) {
                echo "Deploy hacia dev despues de build"
            } else if (env.BRANCH_NAME.startsWith("UAT")) {
                echo "Deploy hacia UAT despues de build"
            } else if (env.BRANCH_NAME.startsWith("QA")) {
                echo "Deploy hacia QA despues de build"
            } else if (env.BRANCH_NAME.startsWith("Production")) {
                echo "Deploy hacia Production despues de build"
            }
        }*/