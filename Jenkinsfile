pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    mvn clean package -B
                '''
      }
    }
    stage('Scan App - Build Container') {
      parallel {
        stage('IQ-BOM') {
          steps {
            nexusPolicyEvaluation(iqApplication: 'sljavademo', iqStage: 'build', iqScanPatterns: [[scanPattern: '']])
          }
        }
        stage('Static Analysis') {
          steps {
            sh '/usr/local/bin/sl analyze --app HelloShiftLeft --java target/hello-shiftleft-*.jar'
          }
        }
        stage('Build Container') {
          steps {
            echo '...need to learn the build process first'
          }
        }
      }
    }
    stage('Test Container') {
      steps {
        echo '...run container and test it'
      }
      post {
        success {
          echo '...the Test Scan Passed!'

        }

        failure {
          echo '...the Test  FAILED'
          error '...the Container Test FAILED'

        }

      }
    }
    stage('Scan Container') {
      steps {
        echo '...TODO scan container'
      }
      post {
        success {
          echo '...the IQ Scan PASSED'
          postGitHub(commitId, 'success', 'analysis', 'Nexus Lifecycle Container Analysis succeeded', "${policyEvaluationResult.applicationCompositionReportUrl}")

        }

        failure {
          echo '...the IQ Scan FAILED'
          postGitHub(commitId, 'failure', 'analysis', 'Nexus Lifecycle Containe Analysis failed', "${policyEvaluationResult.applicationCompositionReportUrl}")
          error '...the IQ Scan FAILED'

        }

      }
    }
    stage('Publish Container') {
      when {
        branch 'master'
      }
      steps {
        echo '...figure out container'
      }
    }
  }
  tools {
    jdk 'jdk8'
    maven 'M3'
  }
}
