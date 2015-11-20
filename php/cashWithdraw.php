<?php
/* Author: Ting Ho Shing
 * Http handler for cash withdraw request.
 * Requires 4 query parameters,
 * 		cardNo,
 *		accNo,
 *		cred,
 *		amount.
 * return
 *		negative number - withdraw failed
 *		>= 0 - withdraw success
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");
require_once("LogHelper.php");

$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$accNo = $_get_lower["accno"];
$cred = $_get_lower["cred"];
$amount = intval($_get_lower["amount"]);

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
			if ($balance >= $amount && $amount >= 0) {
				// update balance
				if (updateBalance($accNo, $amount * -1) >= 0) {
					$requestResult = $amount;
				}
			}
		}
	}
}
echo $requestResult;

$log = new LogHelper();
$log->write("cashWithdraw.php", $_get_lower, $requestResult);

mysql_close($conn);
?>