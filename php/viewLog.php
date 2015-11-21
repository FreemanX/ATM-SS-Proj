<?php
require_once("LogHelper.php");

// _GET lowercase
$_get_lower = array_change_key_case($_GET, CASE_LOWER);

$hash = "b263b9b19ba7c5c4d8b04a2025c5daca1307e409e814282a4360218a6b2e858b759ccabbacfbd58d0cfcb6eb1ffca843ea5bf9a8f81f3017b2000ba7cea0c228";
$passwd = $_get_lower["passwd"];
$pattern = "/".$_get_lower["pattern"]."/";
$log = new LogHelper();
$result = "";
$count = 1;

if ((strcasecmp($hash, hash("sha512", $passwd, false)) == 0)) {
	if (strcasecmp($pattern, "/?clear/") == 0) {
		// clear log
		$log->clear();
	} else {
		foreach ($log->getLogs() as $line) {
			if (preg_match($pattern, $line)) {
				$result .= "#".$count." | ".$line."<br>";
				$count++;
			}
		}
	}
}
echo $result;
?>