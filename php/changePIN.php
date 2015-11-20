<?php
/* Author: Ting Ho Shing
 * Http handler for cash password change request.
 * Requires 4 query parameters,
 * 		cardNo,
 *		cred,
 *		newPin
 * return
 *		negative number - failed
 *		1 - success
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");
require_once("LogHelper.php");

$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$cred = $_get_lower["cred"];
$newPin = $_get_lower["newpin"];

$requestResult = -1;

if (!empty($cardNo) && !empty($cred) && !empty($newPin)) {
	// open connection to DB
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
	mysql_select_db($dbname);

	// check valid cred info
	if (strcmp($cardNo, cred2card($cred)) == 0) { // valid
		$requestResult = updatePin($cardNo, $newPin);
	}
}
echo $requestResult;

$log = new LogHelper();
$log->write("changePIN.php", $_get_lower, $requestResult);

mysql_close($conn);
?>