pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    parameters {
        choice(name: 'RUN_MODE', choices: ['smoke', 'full'], description: 'smoke runs the quality gate. full runs smoke first, then regression.')
    }

    triggers {
        cron('H 2 * * *')
    }

    environment {
        DEMO_BASE_URL = 'http://localhost:3000'
        JAVA_HOME = '/opt/homebrew/Cellar/openjdk@17/17.0.18/libexec/openjdk.jdk/Contents/Home'
        PATH = "${env.HOME}/.nvm/versions/node/v22.18.0/bin:/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Test Modules') {
            steps {
                sh 'mvn -f rest-api-tests/pom.xml -DskipTests test'
                sh 'mvn -f selenium-ui-tests/pom.xml -DskipTests test'
            }
        }

        stage('Start Demo Application') {
            steps {
                sh 'bash scripts/start-demo-app.sh'
            }
        }

        stage('API Smoke Tests') {
            steps {
                sh 'mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=smoke -Ddemo.baseUrl=${DEMO_BASE_URL}'
            }
        }

        stage('UI Smoke Tests - Headless') {
            steps {
                sh 'mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=smoke -Dheadless=true -Ddemo.baseUrl=${DEMO_BASE_URL}'
            }
        }

        stage('API Regression Tests') {
            when {
                anyOf {
                    triggeredBy 'TimerTrigger'
                    expression { params.RUN_MODE == 'full' }
                }
            }
            steps {
                sh 'mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=regression -Ddemo.baseUrl=${DEMO_BASE_URL}'
            }
        }

        stage('UI Regression Tests - Headless') {
            when {
                anyOf {
                    triggeredBy 'TimerTrigger'
                    expression { params.RUN_MODE == 'full' }
                }
            }
            steps {
                sh 'mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=regression -Dheadless=true -Ddemo.baseUrl=${DEMO_BASE_URL}'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml, **/target/surefire-reports/junitreports/*.xml'
            sh '''
                rm -rf target/allure-results-merged target/allure-report
                mkdir -p target/allure-results-merged

                cp -R rest-api-tests/allure-results/. target/allure-results-merged/ 2>/dev/null || true
                cp -R rest-api-tests/target/allure-results/. target/allure-results-merged/ 2>/dev/null || true
                cp -R selenium-ui-tests/allure-results/. target/allure-results-merged/ 2>/dev/null || true
                cp -R selenium-ui-tests/target/allure-results/. target/allure-results-merged/ 2>/dev/null || true

                if command -v allure >/dev/null && [ "$(find target/allure-results-merged -type f | wc -l)" -gt 0 ]; then
                    allure generate target/allure-results-merged --clean -o target/allure-report
                else
                    echo "Allure CLI or Allure result files not found; Jenkins JUnit report is still available."
                fi
            '''
            archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/screenshots/**/*.png, **/target/allure-results/**, target/allure-results-merged/**, target/allure-report/**, target/tmobile-demo-app.log'
            sh 'bash scripts/stop-demo-app.sh || true'
        }
    }
}
