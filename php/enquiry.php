<?php
/* Author: Ting Ho Shing
 * Http handler for enquiry request.
 * Requires 4 query parameters,
 * 		cardNo,
 *		accNo,
 *		cred,
 * return
 *		negative number - enquiry failed
 *		>= 0 - account balance
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");

$cardNo = $_GET["cardno"];
$accNo = $_GET["accNo"];
$cred = $_GET["cred"];

$requestResult = -1;

if (!empty($cardNo) && !empty($accNo) && !empty($cred)) {
	// open connection to DB
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
	mysql_select_db($dbname);
	
	// check valid cred info
	if (strcmp($cardNo, cred2card($cred)) == 0) { // valid
		$result = cardAccountRow($cardNo, $accNo);
		if (mysql_num_rows($result) == 1) { // should have 1 match only
			$row = mysql_fetch_assoc($result);
			$balance = intval($row["balance"]);

			$requestResult = $balance;
		}
	}
}
echo $requestResult;

mysql_close($conn);

?>