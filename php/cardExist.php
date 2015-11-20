<?php
/* Author: Ting Ho Shing
 * Http handler for verify card.
 * Requires 1 query parameters,
 *              cardNo
 * return
 *              "false" - card not exist
 *              "true" - card exist
 */

require_once("settings.php");
require_once("credManager.php");
require_once("sqlHelper.php");
require_once("LogHelper.php");

// _GET lowercase
$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$cardNo = $_get_lower["cardno"];
$requestResult = "false";

if (!empty($cardNo)) {
    // open connection to DB
    $conn = mysql_connect($dbhost, $dbuser, $dbpass) or die(mysql_error());
    mysql_select_db($dbname);

    $result = cardRow($cardNo);

    if (mysql_num_rows($result) == 1) { // exactly one card
        $requestResult = "true";
    }
}
echo $requestResult;

$log = new LogHelper();
$log->write("cashWithdraw.php", $_get_lower, $requestResult);

mysql_close($conn);
?>