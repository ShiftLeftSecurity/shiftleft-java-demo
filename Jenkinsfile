import groovy.json.*

node () {
   def mvnHome, commitId
    
   stage('Preparation') { // for display purposes
      // Get some code from a GitHub repository
      // git 'git@github.com:CMYanko/struts2-showcase-demo.git'
      checkout scm
      
      
      // Get the Maven tool.
      // ** NOTE: This 'M3' Maven tool must be configured
      // **       in the global configuration.           
      mvnHome = tool 'M3'
      
      // sh 'git rev-parse HEAD > commit'
      // commitId = readFile('commit').trim()
      // sh "echo my commitid ${commitId}"

   }
   stage('Build') {
      // Run the maven build
      try{
        if (isUnix()) {
          configFileProvider([configFile(fileId: 'f8c43603-b756-4195-9207-e327bb4e0ccc', variable: 'MY_SETTINGS_XML')]) {
           sh "./mvn -B clean package"
          }
              
        } else {
           bat(/mvn -B clean package/)
        }
        
        currentBuild.result = 'SUCCESS'

      }catch(Exception err){
        currentBuild.result = 'FAILURE'
      
      }

      sh "echo current build status ${currentBuild.result}"
      /*
      if (currentBuild.result == 'FAILURE') {
        postGitHub(commitId, 'failure', 'build', 'Build failed')
        return
      } else {
        postGitHub(commitId, 'success', 'build', 'Build succeeded')
      } */
      
   }

   stage('SLAnalyze') {
       {
           sh '/usr/local/bin/sl analyze --app HelloShiftLeft --java target/hello-shiftleft-*.jar'
       }
   }
   
}
