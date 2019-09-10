node{
    stage('SCM Checkout'){
        git 'https://github.com/miguelgonch/ProyectoDB2-Hospital.git'
    }
    stage('Compile-Package'){
        sh 'mvn package'
    }
}