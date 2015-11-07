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

$cardNo = $_GET["cardNo"];
$cred = $_GET["cred"];
$newPin = $_GET["newPin"];

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

mysql_close($conn);
?>