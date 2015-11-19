<?php
/* Author: Ting Ho Shing
 * Http handler for accounts query.
 * Requires 2 query parameters,
 *              cardNo,
 *              cred,
 * return
 *              "ERROR" - operation failed
 *              JSON array - accounts of the card
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");

// _GET lowercase
$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$cred = $_get_lower["cred"];
//echo $_SERVER['REQUEST_URI'];
$requestResult = "ERROR";
$verified = false;

if (!empty($cardNo) && !empty($cred)) {
        // open connection to DB
        $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
        mysql_select_db($dbname);

        // check valid cred info
        if (strcmp($cardNo, cred2card($cred)) == 0) { // valid
                $result = getAccounts($cardNo);

                if (mysql_num_rows($result) > 0) { // at least one account found
                        $requestResult = "";
                        while ($account = mysql_fetch_array($result)[0]) {
                                $requestResult .= $account.",";
                        }
                        $requestResult = rtrim($requestResult, ",");
                }
        }
}
echo $requestResult;

mysql_close($conn);
?>
