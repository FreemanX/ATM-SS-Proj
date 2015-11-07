<?php
/* Author: Ting Ho Shing
 * Http handler for cash deposit request.
 * Requires 4 query parameters,
 * 		cardNo,
 *		accNo,
 *		cred,
 *		amount.
 * return
 *		negative number - deposit failed
 *		>= 0 - deposit success
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");

$cardNo = $_GET["cardno"];
$accNo = $_GET["accNo"];
$cred = $_GET["cred"];
$amount = intval($_GET["amount"]);

$requestResult = -1;

if (!empty($cardNo) && !empty($accNo) && !empty($cred) && !empty($amount)) {
	// open connection to DB
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
	mysql_select_db($dbname);
	
	// check valid cred info
	if (strcmp($cardNo, cred2card($cred)) == 0) { // valid
		// verify account
		$result = cardAccountRow($cardNo, $accNo);

		if (mysql_num_rows($result) == 1) { // should have 1 match only
			$row = mysql_fetch_assoc($result);
			$balance = intval($row["balance"]);

			// verify amount
			if ($amount >= 0) {
				// update balance
				if (updateBalance($accNo, $amount) >= 0) {
					$requestResult = $amount;
				}
			}
		}
	}
}
echo $requestResult;

mysql_close($conn);
?>