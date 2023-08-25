---
title: Jenkins
date: 2021-06-13 17:42:50
tags:
categories:
---

Jenkins流水线的使用
<!--more-->

# Jenkins

## 流水线

### 创建流水线

```groovy
pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}
```

重复、超时、部署后处理

```groovy
pipeline {
    agent any
    
    // 环境变量
    environment {
        DISABLE_AUTH = 'true'
        DB_ENGINE    = 'sqlite'
    }
    
    stages {
        stage('Deploy') {
            steps {
                // 重复3次
                retry(3) {
                    sh './flakey-deploy.sh'
                }
 				// 3分钟超时后，即部署失败
                timeout(time: 3, unit: 'MINUTES') {
                    sh './health-check.sh'
                }
            }
        }
        stage('Sanity check') {
            steps {
                input "Does the staging environment look ok?" // 人工确认
            }
        }
    }
    
    // pipeline后处理
    post {
        always {
            //通过 archiveArtifacts 步骤和文件匹配表达式可以很容易的完成构建结果记录和存储
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            junit 'build/reports/**/*.xml'  //记录测试
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
             mail to: 'team@example.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
        }
        unstable {
            echo 'This will run only if the run was marked as unstable'
        }
        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
```

## 构建JAVA程序

```shell
docker run --rm -u root -p 8080:8080 -v /Users/xuexuan/jenkins-data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v "$HOME":/home jenkinsci/blueocean
```

https://www.jenkins.io/zh/doc/tutorials/build-a-java-app-with-maven/
