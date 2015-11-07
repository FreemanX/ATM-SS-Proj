<?php
require_once("settings.php");
require_once("credManager.php");

$cardNo = $_GET["cardNo"];
$pin = $_GET["pin"];

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

// close the connection to DB
mysql_close($conn);
?>
