<?php
error_reporting(E_ALL);
require_once("settings.php");
require_once("credManager.php");
require_once("LogHelper.php");

$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$pin = $_get_lower["pin"];

$credential = "ERROR";

if (!empty($cardNo) && !empty($pin)) {
	// open connection to DB
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
	mysql_select_db($dbname);

	// get records from "cards" table
	$sql = sprintf("SELECT * FROM cards WHERE card_no = '%s' AND pin = 
'%s'", mysql_real_escape_string($cardNo), mysql_real_escape_string($pin));
	$result = mysql_query($sql) or die(mysql_error());

	if (mysql_num_rows($result) == 1) {
		$credential = card2cred($cardNo);
	}
}

// return the credential result
echo $credential;

$log = new LogHelper();
$log->write("login.php", $_get_lower, $credential);

// close the connection to DB
mysql_close($conn);
?>
