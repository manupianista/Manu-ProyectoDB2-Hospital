pipeline {
    agent any
    stages {

        stage('############### REVISION DE CODIGO ##################') {
            steps {
                
            }
        }

        stage('############### CHECKOUT ##################') {
            steps {
                checkout scm
                git 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git'
            }
        }

        stage('############### GIT ##################') {
            steps {
                sh 'git fetch --all'
            }
        }

        stage('############### COMPILE ##################') {
            def mvnHome = tool name:'Maven 3.6.2', type: 'maven'

            steps {
                sh "${mvnHome}/bin/mvn package"    
            }
        }

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
        }

        stage('############### BUILD ##################') {
            steps {
                //
            }
        }

        stage('############### TEST ##################') {
            steps {
                //
            }
        }

        stage('############### DEPLOY ##################') {
            steps {

            }
        }


    } //fin stages


} //fin pipeline