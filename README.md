# HKBU COMP4007 ATM-SS-Project Group 5

Hi, we are group 5 in the course COMP4007 2015 in Hong Kong Baptist Univeristy

This is project that built in the latest version of eclipse (Version: Mars.1 Release (4.5.1)).

## How to run the project
### Server part

1. Set up a server that support PHP and put everything in php/ to your server.

2. Set up mysql database server and run the script in sql_dump/ to build a test database with the necessary tables and records.

3. Change the value of prefix in atmss.bams.BAMSCommunicator to your own server.

### Local part

Import the project into eclipse compile and run.

##Logs the system record

The system will record 3 log files during the run time.

###1. atmss_log(local) --> in the form of xml records all the operations that the user interact with hardware emulators.
###2. SessionLog(local) --> records the user behaviors after the user successfully log into the system.
###3. Server log(server) --> records all the requests from ATM machiens, can be viewd by log.html with the password of group05 log in password

##Notice

1. The ATM-SS_Demo_V1.0.jar we provide is a runnable java program and it will connect to our server.
2. For more details of the project please refer to Java documnet in doc/