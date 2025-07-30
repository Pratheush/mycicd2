pipeline {
    
  parameters {
  booleanParam(name: 'CLEANUP_ENABLED', defaultValue: true, description: 'Clean up Docker images after push')
  }
  
  // Define the agent where the pipeline will run.
  // 'any' means Jenkins will pick any available agent.
  // If you have a specific label for your Windows 11 machine (e.g., 'windows-agent'),
  // you can use 'agent { label 'windows-agent' }' instead.
  // agent { label 'windows-docker' }
  agent any
  
  tools {
    maven "maven_3_9_5"
    // make sure this name matches the configured JDK name inside Jenkins this way we can specify jdk specifically an another version for a pipeline
    // jdk "jdk21"
  }
  
  // If you don't want Jenkins to do that auto-checkout and only want to rely on your git step, you could set:  skipDefaultCheckout(true)
  // skipDefaultCheckout(false) means: Jenkins will perform the default checkout scm at the start of the pipeline.
  options {
    skipDefaultCheckout(true)
  }

  // Define environment variables.
  // IMPORTANT: Replace 'your-dockerhub-username' and 'your-repo-name'
  // The 'docker-hub-credentials' should be a 'Secret text' credential in Jenkins or UsernameAndPassword type
  // storing your Docker Hub password/token.
  environment {
    DOCKER_IMAGE_NAME = 'pratheush/spring-customer-mgmt-jenkins-cicd'
    // Stored DockerHub Credentials In Jenkins credentials ID
    DOCKER_CREDENTIALS_ID = 'dockerhub-uname-pwd-token'
  }

  // Define triggers for the pipeline.
  // 'githubPush()' configures the pipeline to be triggered by GitHub push events.
  triggers {
    // This allows GitHub to trigger builds on push (needs webhook configured)
    // // Ensure you have configured a GitHub webhook in your repository settings pointing to your Jenkins instance.
    githubPush()
  }

  // Define the stages of your CI/CD pipeline.
  stages {
      
    // Stage 1: Checkout Source Code 
    stage('Checkout') {
      steps {
        echo 'Checking out source code...'
        // Checkout the SCM (Source Code Management) configured for this job. This typically points to your GitHub repository.
        git branch: 'master', url: 'https://github.com/Pratheush/mycicd2.git'
      }
    }

    // Stage 2: Build Spring Boot Application
    stage('Build & Test App') {
      steps {
        echo 'Building Spring Boot application with Maven...'
        // Use 'bat' for Windows
        // 'mvn clean package' cleans the target directory and packages the application into a JAR file (typically in the 'target' directory).
        // -DskipTests to skip tests during build, remove if you want tests to run
        // bat './mvnw clean package -DskipTests'
        // bat './mvnw clean verify'
        // bat './mvnw clean package'
        sh 'mvn clean verify'
      }
    }

    // Stage 3: Build Docker Image 
     stage('Build Docker Image') {
      steps {
        echo 'Building Docker image...'
        // Build the Docker image.
        // Assumes a Dockerfile exists in the root of your project.
        // .    the current directory where Dockerfile and project file lives
        // build   command passes to batch file which builds Docker Image
        // -t     tags the image with the name from the environment variable and gives it the latest tag
        // Uses multi-tag Docker image (short-commit and latest) — good practice.
        script {
            echo 'Building Docker image INSIDE SCRIPT SECTION'
          def shortCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          echo 'Building Docker image shortCommit ${shortCommit}'
          def imageTag    = "${DOCKER_IMAGE_NAME}:${shortCommit}"
          echo 'Building Docker image imageTag ${imageTag}'
          def latestTag   = "${DOCKER_IMAGE_NAME}:latest"
          echo 'Building Docker image latestTag ${latestTag}'
          sh "docker build -t ${imageTag} -t ${latestTag} ."
          echo ' Docker image BUILT '    
          
          // Save to environment for next stage
          env.IMAGE_TAG = imageTag
          env.LATEST_TAG = latestTag
          echo "✅ Built image: ${env.IMAGE_TAG} and tagged as latest"
        }
      }
    }
    
    // This defines a named pipeline stage that focuses on pushing a built Docker image to DockerHub.
    // Inside the steps block, the script section allows you to run Groovy-based custom logic
    // DOCKER_CREDENTIALS_ID refers to the Jenkins credentials ID configured in Jenkins to store your DockerHub username/password or token. 
    // Push Docker Image to DockerHub stage using withDockerRegistry {} is more robust and secure for Windows environments:
    // - Cross-platform compatible: No piping issues like you’d face with --password-stdin.

    // stage('Push Docker Image to DockerHub') {
    //   steps {
    //     echo 'Pushing Docker image... using withCredentials : usernamePassword Way'
    //     withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID,
    //                                       usernameVariable: 'USERNAME',
    //                                       passwordVariable: 'PASSWORD')]) {
    //       bat "echo %PASSWORD% | docker login -u %USERNAME% --password-stdin"
    //       bat "docker push ${env.IMAGE_TAG}"
    //       bat "docker push ${env.LATEST_TAG}"
    //     }
    //     echo "🚀 Docker image ${env.IMAGE_TAG} pushed to DockerHub successfully!"
    //     echo "✅ Tagged as latest: ${env.LATEST_TAG}"
    //   }
    // }
    
    stage('Push Docker Image to DockerHub') {
      steps {
        echo '🔐 Logging in to DockerHub using withDockerRegistry...'
        withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
        //sh "docker push ${env.IMAGE_TAG}"
        sh "docker push ${env.LATEST_TAG}"
      }
      echo "🚀 Docker image ${env.IMAGE_TAG} pushed to DockerHub successfully!"
      echo "✅ Tagged as latest: ${env.LATEST_TAG}"
    }
   }
   
   stage('Clean Docker Images (optional)') {
      when {
        expression { params.CLEANUP_ENABLED }
      }
      steps {
        echo "🧹 Cleaning up local Docker images as cleanup is enabled..."
        script {
          def status1 = sh(script: "docker rmi ${env.IMAGE_TAG}", returnStatus: true)
          def status2 = sh(script: "docker rmi ${env.LATEST_TAG}", returnStatus: true)
          echo "Cleanup status: IMAGE_TAG=${status1}, LATEST_TAG=${status2}"
        }
      }
   }

    
  }

  // Uses cleanWs() in post block — clean workspace ensures clean builds
  // cleanWs() is a predefined method provided by the Workspace Cleanup Plugin in Jenkins. It’s specifically designed for use in Declarative Pipelines, and you’re using it perfectly in the post section to clean up the workspace after every build.
  // Define post-build actions, e.g., send notifications. 
  post {
    always {
      echo "🏁 Cleaning Workspace."
      cleanWs()
      echo "🏁 Pipeline execution finished."
      // You can add more post-build actions here, e.g., email notifications, Slack messages.
      // For example:
      // mail to: 'your-email@example.com',
      //      subject: "Jenkins Build ${currentBuild.displayName}: ${currentBuild.currentResult}",
      //      body: "Build ${currentBuild.displayName} (${env.BUILD_URL}) finished with status ${currentBuild.currentResult}"
    }
    success {
      echo 'Pipeline executed successfully.'
      echo '🚀 Docker image successfully built and pushed!'
    }
    failure {
      echo "❌ CI/CD Pipeline failed!"
    }
  }
}