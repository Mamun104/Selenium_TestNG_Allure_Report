# Slenium_TestNG_Allure_Report


# Prerequisites

Install jdk 8 or any LTS version

Configure JAVA_HOME and GRADLE_HOME

Download Allure 2.17.2 and configure environment path

Stable internet connection

# How to run this project

Clone the repo

Open cmd in the root folder

Give following commands:

            gradle clean test
            
            allure generate allure-results --clean -o allure-report
            
            allure serve allure-results
