pipeline {
    agent any

    tools {
        maven 'maven'
        //jdk 'JDK-9'
    }

    stages {

        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
/*
        stage('############### REVISION DE CODIGO ##################') {
            steps {
                //
            }
        }*/
        /*
        stage('############### CHECKOUT ##################') {
            steps {
                checkout scm
                git 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git'
            }
        }*/


/*
        stage('############### GIT ##################') {
            steps {
                sh 'git fetch --all'
                sh 'git pull'
            }
        }*/
/*
        stage('############### COMPILE ##################') {
            def mvn_version = 'maven-3.6.2'
            withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] )
            {
                sh "mvn clean package"    
            }
        }*/

        stage ('############### BUILD ##################') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
            }
        }

        /*
        stage('############### CLEAN ##################') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('############### TEST ##################') {
            steps {
                sh 'mvn test'
            }
        }*/
        /*
        stage('############### PKG ##################') {
            steps {
                sh 'mvn package'
            }
        }*/

    
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



    } //fin stages


} //fin pipeline