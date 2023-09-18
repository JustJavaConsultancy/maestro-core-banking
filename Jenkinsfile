pipeline{
    agent any
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '4')
        disableConcurrentBuilds abortPrevious: true
        office365ConnectorWebhooks([[name: 'Pouchii', notifyAborted: true, notifyBackToNormal: true, notifyFailure: true, notifyNotBuilt: true, notifyRepeatedFailure: true, notifySuccess: true, notifyUnstable: true, startNotification: true, url: 'https://systemspecscomng.webhook.office.com/webhookb2/152360f1-c1fe-428f-99eb-20b06beebae7@fe7d54bd-6a8e-4004-b537-90dce799175c/JenkinsCI/26b54541f01547c5b977645032ce511c/0a82652b-435b-4b45-8aaa-2cd89fa22428']])
    }
    triggers {
        cron('H 4 * * 6')
    }
    tools{
        maven 'maven'
    }
    stages{
        stage("Initialising"){
            steps{
                sh "sudo newman --version"
                script{
                    if (env.BRANCH_NAME == 'main') {
                        sh "sudo curl -X POST https://wallet.remita.net/management/shutdown"
                        }  else  if (env.BRANCH_NAME == 'serverbranch' || env.BRANCH_NAME == 'version_2'){
                            sh "sudo curl -X POST https://walletdemo.remita.net/management/shutdown"
                        }
                }
            }
        }
        stage("Maven Clean"){
            steps{
                sh "sudo bash ./mvnw clean"
            }
        }
        stage("Installing yarn"){
            steps{
                sh "sudo yarn install"
            }
        }
        stage("Run webpack"){
            steps{
                script{
                    if (env.BRANCH_NAME == 'main') {
                        sh "sudo yarn run webpack:prod"
                        }  else  if (env.BRANCH_NAME == 'serverbranch' || env.BRANCH_NAME == 'version_2'){
                            sh "sudo yarn run webpack:build"
                            }
                }
            }
        }
        // stage("Build jar"){
        //     steps{
        //         script{
        //             if (env.BRANCH_NAME == 'main') {
        //                 sh "sudo bash ./mvnw package"
        //                     } else  if (env.BRANCH_NAME == 'serverbranch' || env.BRANCH_NAME == 'version_2'){
        //                         sh "sudo mvn package"
        //                     }
        //         }
        //     }
        // }
        // stage("Generating Artifacts"){
        //     steps{
        //         archiveArtifacts artifacts: '*/target/**/*.jar', followSymlinks: false
        //         echo "Artifacts generated successfully"
        //     }
        // }
        // stage("Copy to secondary server"){
        //     steps{
        //         sh "echo 'Copying Jar file to Secondary Server'"
        //         sshPublisher(publishers: [
        //                         sshPublisherDesc(
        //                             configName: 'secondary-server',
        //                             transfers: [
        //                                 sshTransfer(
        //                                     cleanRemote: false,
        //                                     excludes: '',
        //                                     execCommand: '',
        //                                     execTimeout: 120000,
        //                                     flatten: false,
        //                                     makeEmptyDirs: false,
        //                                     noDefaultExcludes: false,
        //                                     patternSeparator: '[, ]+',
        //                                     remoteDirectory: '',
        //                                     remoteDirectorySDF: false,
        //                                     removePrefix: '',
        //                                     sourceFiles: '*/target/**/*.jar'
        //                                     )
        //                                     ],
        //                                     usePromotionTimestamp: false,
        //                                     useWorkspaceInPromotion: false,
        //                                     verbose: true
        //                                     )
        //                     ]
        //                     )
        //     }
        // }
        stage("Staging"){
            steps{
                script{
                    if (env.BRANCH_NAME == 'main') {
                        sh "sudo bash ./mvnw -Pprod"
                        //sh "sudo bash ./mvnw -Pprod -Dserver.port=8006 http://admin:admin@192.168.17.20:8761/config"
                        }  else  if (env.BRANCH_NAME == 'serverbranch'){
                            sh "sudo mvn -Pstaging"
                            } else  if (env.BRANCH_NAME == 'version_2'){
                                sh "sudo mvn -Pstaging -Dserver.port=8006"
                                //sh "sudo mvn -Pstaging -Dserver.port=8006"
                            }
                }
            }
        }
    }
    post {
        always{
            mail bcc: '', body: "Job ${env.JOB_NAME}: ${currentBuild.currentResult} \nMore Info can be found here: ${env.BUILD_URL}", cc: '', from: '', replyTo: '', subject: "Jenkins build:${currentBuild.currentResult} ${env.JOB_NAME}", to: "apanisile@systemspecs.com.ng, akinrinde@systemspecs.com.ng, abdulwasii@gmail.com"
        }
    }
}
