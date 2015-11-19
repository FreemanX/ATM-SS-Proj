<?php
/* Author: Ting Ho Shing
 * Http handler for cash transfer request.
 * Requires 4 query parameters,
 * 		cardNo,
 *		toAcc,
 *		fromAcc,
 *		cred,
 *		amount.
 * return
 *		negative number - transfer failed
 *		>= 0 - transfer success
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");

$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$fromAcc = $_get_lower["fromacc"];
$toAcc = $_get_lower["toacc"];
$cred = $_get_lower["cred"];
$amount = floatval($_get_lower["amount"]);
//echo $_SERVER['REQUEST_URI'];
$requestResult = -1;
$verified = false;

if (!empty($cardNo) && !empty($fromAcc) && !empty($toAcc) && !empty($cred) && !empty($amount)) {
	// open connection to DB
	$conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
	mysql_select_db($dbname);

	// check valid cred info
	if (strcmp($cardNo, cred2card($cred)) == 0) { // valid
		// verify fromAcc
		$result = cardAccountRow($cardNo, $fromAcc);

		if (mysql_num_rows($result) == 1) { // should have 1 match only
			$row = mysql_fetch_assoc($result);
			$fromBalance = intval($row["balance"]);

			// verify toAcc
			$result = accountRow($toAcc);

			if (mysql_num_rows($result) == 1) { // should have 1 match only
				$verified = True;
			}

			// verify amount
			if ($fromBalance >= $amount && $amount >= 0 && $verified) {
				// update accounts balance
				if (updateBalance($fromAcc, $amount * -1) >= 0 && updateBalance($toAcc, $amount) >= 0) {
					$requestResult = $amount;
				}
			}
		}
	}
}
echo $requestResult;

mysql_close($conn);
?>