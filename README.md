# HKBU COMP4007 ATM-SS-Project Group 5

Hi, we are group 5 in the course COMP4007 2015 in Hong Kong Baptist Univeristy

This is project that built in the latest version of eclipse (Version: Mars.1 Release (4.5.1)).

## How to run the project
### Server part

1. Set up a server that support PHP and put everything in php/ to your server.

2. Set up mysql database server and run the script in sql_dump/ to build a test database with the necessary tables and records. E.g., *<code>mysql -u \<username\> -p \<db_name\> << group05_dmp.sql</code>*

3. Change the value of prefix in *atmss.bams.BAMSCommunicator* and *php/settings.php* to match with your server.

### Local part

Import the project into eclipse compile and run.

##Logs the system record

The system will record 3 log files during the run time.

###1. atmss_log(local) --> in the form of xml records all the operations that the user interact with hardware emulators.
###2. SessionLog(local) --> records the user behaviors after the user successfully log into the system.
###3. Server log(server) --> records all the requests from ATM machiens, can be viewd by log.html with the password of group05 log in password

##Notice

1. The ATM-SS_Demo_V1.0.jar we provide is a runnable java program and it will connect to our server.

	Databash infomation(22/11/2015):
		MariaDB [group05]> show tables;
		+-------------------+
		| Tables_in_group05 |
		+-------------------+
		| accounts          |
		| cards             |
		+-------------------+

		MariaDB [group05]> select * from accounts;
		+-------------+--------------+------------+
		| account_no  | card_no      | balance    |
		+-------------+--------------+------------+
		| 15494572221 | 981370846450 |     150000 |
		| 15498562384 | 981358459216 |      46044 |
		| 23651154897 | 981370846450 |      80000 |
		| 61945738628 | 981358459216 | 4992454.81 |
		| 79548621118 | 981370846450 |       8000 |
		| 95846285951 | 981398412504 |    6534520 |
		+-------------+--------------+------------+

		MariaDB [group05]> select * from cards;
		+--------------+--------+
		| card_no      | pin    |
		+--------------+--------+
		| 981358459216 | 222222 |
		| 981370846450 | 458496 |
		| 981398412504 | 804946 |
		+--------------+--------+
	The pin may be changed if someone tests changing password fucntion.
	Card Reader provides the first 2 cards if you want to play with the 3rd one, you need to type in manaualy.
	
2. For more details of the project please refer to Java documnet in doc/

