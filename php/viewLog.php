<?php
require_once("LogHelper.php");

// _GET lowercase
$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$pattern = "/".$_get_lower["pattern"]."/";
$log = new LogHelper();
$result = "";

foreach ($log->getLogs() as $line) {
	if (preg_match($pattern, $line)) {
		$result .= $line."<br>";
	}
}

echo $result;
?>