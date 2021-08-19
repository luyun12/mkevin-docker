//优化后单机构建流程
pipeline {
      //agent用于指定执行job的节点，any为不做限制
      agent any

      environment {
        MVN_PATH = '/var/maven_home/bin/mvn'
        IMG = 'mkevin-docker'
        VER = '0.0.1'
        JAR = 'mkevin-docker-0.0.1-SNAPSHOT'
        SOURCE_DIR = '/var/maven_home/repo/net/mkevin/mkevin-docker/0.0.1-SNAPSHOT/'
        BUILD_DIR = '/var/jenkins_home/docker/build/'
        CLASS_DIR = '/var/jenkins_home/workspace/empty/target/classes'
      }

      //阶段
      stages {
        //每一个阶段
		stage('pull git') {
			steps{
			    /*从git拉取代码，可实用工具生成*/
				git credentialsId: '2226cf56-8ea7-4515-b0d0-c0f4090e87fb', url: 'https://gitee.com/mimaxueyuan/mkevin-docker.git'
			}

		}
		stage('maven 构建') {
			steps{
			    /*使用mvn构建，使用全路径模式*/
			    sh '${MVN_PATH} install'
			}
		}
		stage('docker 构建') {
            steps{
                /*创建文件夹，用于放入程序jar包和Dockerfile文件*/
                sh 'mkdir -p ${BUILD_DIR}${JAR}'
                /*copy 程序jar包*/
                sh 'cp ${SOURCE_DIR}${JAR}.jar ${BUILD_DIR}'
                /*copy Dockerfile*/
                sh 'cp ${CLASS_DIR}/Dockerfile ${BUILD_DIR}'
                /*Docker登录、镜像构建、push到私服、运行容器*/
                sh '''
                    docker login -u kevin -p kevin 10.1.18.202:5000
                    cd ${BUILD_DIR};
                    pwd;
                    docker build -t 10.1.18.202:5000/${IMG}:${VER} .
                    docker push 10.1.18.202:5000/${IMG}:${VER}
                    docker logout 10.1.18.202:5000
                    docker rm -f ${IMG}
                    docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
                '''
            }
        }
        stage('deploy 10.1.18.201'){
           agent{
               node{
                label 'node1'
               }
           }
           steps {
               sh '''
                   docker login -u kevin -p kevin 10.1.18.202:5000
                   docker pull 10.1.18.202:5000/${IMG}:${VER}
                   docker logout 10.1.18.202:5000
                   docker rm -f ${IMG}
                   docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
               '''
           }
        }
        stage('deploy 10.1.18.201'){
           agent{
               node{
                label 'node1'
               }
           }
           steps {
               sh '''
                   docker login -u kevin -p kevin 10.1.18.202:5000
                   docker pull 10.1.18.202:5000/${IMG}:${VER}
                   docker logout 10.1.18.202:5000
                   docker rm -f ${IMG}
                   docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
               '''
           }
        }
   }
}

//原始单机构建流程
pipeline {
      //agent用于指定执行job的节点，any为不做限制
      agent any
      //阶段
      stages {
           //每一个阶段
		stage('pull git') {
			steps{
			    /*从git拉取代码，可实用工具生成*/
				git credentialsId: '2226cf56-8ea7-4515-b0d0-c0f4090e87fb', url: 'https://gitee.com/mimaxueyuan/mkevin-docker.git'
			}

		}
		stage('maven 构建') {
			steps{
			    /*使用mvn构建，使用全路径模式*/
			    sh '/var/maven_home/bin/mvn install'
			}
		}
		stage('docker 构建') {
            steps{
                /*创建文件夹，用于放入程序jar包和Dockerfile文件*/
                sh 'mkdir -p /var/jenkins_home/docker/构建/mkevin-docker-0.0.1-SNAPSHOT'
                /*copy 程序jar包*/
                sh 'cp /var/maven_home/repo/net/mkevin/mkevin-docker/0.0.1-SNAPSHOT/mkevin-docker-0.0.1-SNAPSHOT.jar /var/jenkins_home/docker/构建/mkevin-docker-0.0.1-SNAPSHOT'
                /*copy Dockerfile*/
                sh 'cp /var/jenkins_home/workspace/empty/target/classes/Dockerfile /var/jenkins_home/docker/构建/mkevin-docker-0.0.1-SNAPSHOT'
                /*Docker登录、镜像构建、push到私服、运行容器*/
                sh '''
                    docker login -u kevin -p kevin 10.1.18.202:5000
                    cd /var/jenkins_home/docker/构建/mkevin-docker-0.0.1-SNAPSHOT/;
                    pwd;
                    docker 构建 -t 10.1.18.202:5000/mkevin-docker:0.0.1 .
                    docker push 10.1.18.202:5000/mkevin-docker:0.0.1
                    docker logout 10.1.18.202:5000
                    docker run --name mkevin-docker -p 8080:8080 -d 10.1.18.202:5000/mkevin-docker:0.0.1
                '''
            }
        }
   }
}

//并行构建
pipeline {
   //agent用于指定执行job的节点，any为不做限制
   agent any
   //阶段
   stages {
        //每一个阶段
		stage('pull git') {
			steps{
                sh 'echo pull git'
			}
		}
		stage('maven 构建') {
			steps{
                sh 'echo maven 构建'
			}
		}
		stage('docker 构建') {
            steps{
                sh 'echo docker 构建'
            }
        }
        stage('parallel run') {
            //设置哪些阶段是并行的
            parallel{
                stage('构建:Module1') {
                    steps {
                        sh 'echo 构建 Module1 stage ...'
                    }
                }
                stage('构建:Module2') {
                    steps {
                        sh 'echo 构建 Module2 stage ...'
                    }
                }
                stage('构建:Module3') {
                    steps {
                        sh 'echo 构建 Module3 stage ...'
                    }
                }
            }
        }
   }
}

//下拉选项参数类型
pipeline {
  agent any

  //设置参数
  parameters {
    //设置选项
    choice(
      description: '你要选择哪个模块',
      name: 'model',
      choices: ['m1', 'm2', 'm3']
    )
  }

  stages {
        stage('构建') {
            steps {
                //${使用参数}
                echo "构建 stage: Module Selected : ${params.model} ..."
            }
        }
        stage('测试'){
            steps {
                echo "测试 stage Module Selected : ${params.model} ..."
            }
        }
        stage('部署') {
            steps {
                echo "部署 stage Module Selected : ${params.model} ..."
            }
        }
  }
}


//六种参数类型
pipeline {
  agent any
  parameters {
    choice(
      name: 'model',
      choices: ['m1', 'm2', 'm3'],
      description: '建哪个模块?'
    )

    string(
        name: 'hostname',
        defaultValue: '10.1.18.202',
        description: '主机地址 ?'
    )

    text(
        name: 'remark',
        defaultValue: 'name: kevin \n \
                sex: man \n \
                age: 32',
        description: '你的个人信息是什么 ?'
    )

    booleanParam(
        name: 'is_test',
        defaultValue: true,
        description: '需要测试么 ?'
    )

    password(
        name: 'password',
        defaultValue: 'kvin',
        description: '密码是什么 '
    )

    file(
        name: "config_file",
        description: "你需要输入的配置文件文件是什么 ?"
    )
  }

  stages {
        stage('构建') {
            steps {
                echo "构建 stage: 建模块为 : ${params.model} ..."
            }
        }
        stage('测试'){
            steps {
                echo "测试 stage: 测试: ${params.is_test} ..."
            }
        }
        stage('部署') {
            steps {
                echo "部署 stage: 主机名 : ${params.hostname} ..."
                echo "部署 stage: 密码 : ${params.password} ..."
                echo "部署 stage: 个人信息 : ${params.remark} ..."
            }
        }
  }
}

//when语句控制
pipeline {
    agent any
    //设置环境变量
    environment {
        EVN_FLAG = 'NO'
    }
    stages {
        stage('初始化') {
            steps {
              script {
                //定义变量
                RUN_FLAG = true
                PASSWORD = 'mkevin'
              }
            }
        }
        stage('构建') {
            when {
              //判断表达式是否为true,如果为true则执行
              expression { RUN_FLAG }
            }
            steps {
                sh 'echo 构建 stage ...'
            }
        }
        stage('测试'){
            when {
              //当时设置了EVN_FLAG环境变量,并且值为YES时
              environment name: 'EVN_FLAG',
              value: 'YES'
            }
            steps {
                sh 'echo 测试 stage ...'
            }
        }
        stage('部署') {
            when {
              //当密码匹配的时候执行, 顺序不能反
              equals expected: 'mkevin',
              actual: PASSWORD
            }
            steps {
                sh 'echo 部署 stage ...'
            }
        }
    }
}

//if语句控制
pipeline {
    agent any 

    environment {
      OFF_FLAG = 'YES'
    }
    stages {
        stage('Init') { 
            steps { 
              script {
                BUILD_FLAG = true
                DEPLOY_PASS = 'kevin'
              }
            }
        }
        stage('Build') { 
            steps { 
              script {
                if ( BUILD_FLAG ) {
                    sh 'echo Build stage ...' 
                }
              }
            }
        }
        
        stage('Test'){
            steps {
              script {
                if ( OFF_FLAG == 'YES' ) {
                  sh 'echo Test stage ...' 
                }
              }
            }
        }
        
        stage('Deploy') {
            steps {
              script {
                if ( DEPLOY_PASS == 'kevin' ) {
                  sh 'echo Deploy stage ...' 
                }
              }
            }
        }
    }
}

//input交互使用
pipeline {
    agent any
    stages {
        stage('Init') {
            /*
            message，必选项，提示信息，用于提示用户提交相关的input条件。
            id，可选项，默认等于stage的名称
            ok，可选项, 在ok按钮上显示文本
            submitter，可选项，可以写多个用户名称或者组名称，用逗号隔开。只有这写名称的对应用户登陆jenkins，才能提交这个input动作，如果不写，默认是任何人都可以提交input。
            parameters，可选项，与parameters没有区别，可以定义一些参数。
            */
            input {
                message "确定继续初始化么?"
                ok "确定初始化"
                submitter "admin,kevin"
                parameters {
                    string(name: 'name', defaultValue: 'kevin1', description: 'hello world')
                }
            }
            steps {
              echo "正在初始化... ${name} $name";
            }
        }
        stage('Build') {
            input {
                message "确定继续初构建么?"
                ok "确定构建"
                submitter "admin,kevin"
                parameters {
                    string(name: 'name', defaultValue: 'kevin2', description: 'hello world')
                }
            }
            steps {
              echo "正在构建... ${name} $name";
            }
        }
        stage('Deploy') {
            input {
                message "确定继续发布么?"
                ok "确定发布"
                submitter "admin,kevin"
                parameters {
                    string(name: 'name', defaultValue: 'kevin3', description: 'hello world')
                }
            }
            steps {
              echo "正在发布... ${name} $name";
            }
        }
    }
}

//集群发布
pipeline {
      //agent用于指定执行job的节点，any为不做限制
      agent any

      environment {
        MVN_PATH = '/var/maven_home/bin/mvn'
        IMG = 'mkevin-docker'
        VER = '0.0.1'
        JAR = 'mkevin-docker-0.0.1-SNAPSHOT'
        SOURCE_DIR = '/var/maven_home/repo/net/mkevin/mkevin-docker/0.0.1-SNAPSHOT/'
        BUILD_DIR = '/var/jenkins_home/docker/build/'
        CLASS_DIR = '/var/jenkins_home/workspace/empty/target/classes'
      }

      //阶段
      stages {
        //每一个阶段
		stage('pull git') {
		    agent{
               node{
                label 'master'
               }
            }
			steps{
			    /*从git拉取代码，可实用工具生成*/
				git credentialsId: '2226cf56-8ea7-4515-b0d0-c0f4090e87fb', url: 'https://gitee.com/mimaxueyuan/mkevin-docker.git'
			}

		}
		stage('maven 构建') {
			agent{
               node{
                label 'master'
               }
            }
			steps{
			    /*使用mvn构建，使用全路径模式*/
			    sh '${MVN_PATH} install'
			}
		}
		stage('docker 构建') {
			agent{
               node{
                label 'master'
               }
            }
            steps{
                /*创建文件夹，用于放入程序jar包和Dockerfile文件*/
                sh 'mkdir -p ${BUILD_DIR}${JAR}'
                /*copy 程序jar包*/
                sh 'cp ${SOURCE_DIR}${JAR}.jar ${BUILD_DIR}'
                /*copy Dockerfile*/
                sh 'cp ${CLASS_DIR}/Dockerfile ${BUILD_DIR}'
                /*Docker登录、镜像构建、push到私服、运行容器*/
                sh '''
                    docker login -u kevin -p kevin 10.1.18.202:5000
                    cd ${BUILD_DIR};
                    pwd;
                    docker build -t 10.1.18.202:5000/${IMG}:${VER} .
                    docker push 10.1.18.202:5000/${IMG}:${VER}
                    docker logout 10.1.18.202:5000
                '''
            }
        }
        stage('deploy main'){
           agent{
               node{
                label 'node1'
               }
           }
           steps {
               sh '''
                   docker login -u kevin -p kevin 10.1.18.202:5000
                   docker pull 10.1.18.202:5000/${IMG}:${VER}
                   docker logout 10.1.18.202:5000
                   if docker ps -a | grep ${IMG}; then
						docker rm -f ${IMG}
						echo "删除容器: ${IMG}"
				   fi
                   docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
               '''
           }
        }

		stage('parallel deploy') {
			parallel{
				stage('deploy node2'){
				   agent{
					   node{
						label 'node2'
					   }
				   }
				   steps {
					   sh '''
						   docker login -u kevin -p kevin 10.1.18.202:5000
						   docker pull 10.1.18.202:5000/${IMG}:${VER}
						   docker logout 10.1.18.202:5000
						   if docker ps -a | grep ${IMG}; then
								docker rm -f ${IMG}
								echo "删除容器: ${IMG}"
						   fi
						   docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
					   '''
				   }
				}
				stage('deploy node3'){
				   agent{
					   node{
						label 'node3'
					   }
				   }
				   steps {
					   sh '''
						   docker login -u kevin -p kevin 10.1.18.202:5000
						   docker pull 10.1.18.202:5000/${IMG}:${VER}
						   docker logout 10.1.18.202:5000
						   if docker ps -a | grep ${IMG}; then
								docker rm -f ${IMG}
								echo "删除容器: ${IMG}"
						   fi
						   docker run --name ${IMG} -p 8080:8080 -d 10.1.18.202:5000/${IMG}:${VER}
					   '''
				   }
				}
			}
		}
   }
}