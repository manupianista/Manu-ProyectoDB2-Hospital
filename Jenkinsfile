node{
    stage('SCM Checkout'){
       
        git 'https://github.com/miguelgonch/ProyectoDB2-Hospital.git'
    }
    stage('Compile-Package'){
        //obtener el maven
         def mvnHome = tool name: 'Maven 3.6.2', type: 'maven'

        sh "${mvnHome}/bin/mvn package"
    }
}