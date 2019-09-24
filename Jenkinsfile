pipeline {
    agent any
    stages {

        stage('############### REVISION DE CODIGO ##################') {
            steps {
                //
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
        stage('############### TEST ##################') {
            steps {
                sh 'mvn test'
            }
        }
        stage('############### PKG ##################') {
            steps {
                sh 'mvn package'
            }
        }


        stage('############### GIT ##################') {
            steps {
                sh 'git fetch --all'
            }
        }
        
        stage('############### MERGES ##################') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master'], [name: '*/manu']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'PreBuildMerge', options: [mergeTarget: '*/master']]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '9b1c73a6-a6fd-4352-b020-f49031d51a26', url: 'https://github.com/manupianista/Manu-ProyectoDB2-Hospital.git']]])
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