pipeline{
    agent any
    stages {  
        /*stage('first') {
            agent { label 'master' }
            steps {
            sh "printenv | sort"
            }
        }*/       
        stage('--- clean ---') {
            steps{
                withEnv(["PATH+MAVEN=${tool 'Maven'}/bin:JAVA_HOME/bin"]) {
                    sh "mvn clean"
                }
            }
        }
        stage('-- package --') {
            steps {
                withEnv(["PATH+MAVEN=${tool 'Maven'}/bin:JAVA_HOME/bin"]) {
                    sh "mvn package"
                }
            }
        }
        stage('-- sonar --') {
            steps {
                withEnv(["PATH+MAVEN=${tool 'Maven'}/bin:JAVA_HOME/bin","PATH+NODE=${tool 'Node'}/bin"]) {
                    sh "mvn sonar:sonar -Dsonar.jdbc.url=jdbc:h2:tcp://172.16.72.12:9000/sonar -Dsonar.host.url=http://172.16.72.12:9000"
                }
            }
        }
       
    }
    post {
        success {
            emailext to: 'castillo151148@unis.edu.gt',
            subject: "Finished Pipeline: ${currentBuild.fullDisplayName} - Success",
            body: "The build was successfull with ${env.BUILD_URL}"
        }
        failure {
            emailext to: 'castillo151148@unis.edu.gt',
            subject: "Finished Pipeline: ${currentBuild.fullDisplayName} - Failure",
            body: "There was a problem with ${env.BUILD_URL}"
        }
    }
}
