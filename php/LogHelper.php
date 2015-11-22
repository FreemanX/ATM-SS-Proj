<?php
/* Author: Ting Ho Shing
 * Helper class for log operations.
 */
class LogHelper {

	private $logDir = "../bams_logs/";

	// write new log entry into log file
	public function write($ident, $param, $result) {
		date_default_timezone_set("Asia/Hong_Kong");
		$msg = date("Y-m-d H:i:s")." | ".$ident.", param:".$this->arrToString($param).", result:".$result;

		$logFile = fopen($this->logDir."log", "a");
		$f = fwrite($logFile, $msg.PHP_EOL);
		fclose($f);
		fclose($logFile);
	}

	// get all log entries from log file
	public function getLogs() {
		$logLines = array();
		$logFile = fopen($this->logDir."log", "r");

		if ($logFile) {
			while (($line = fgets($logFile)) != false) {
				array_unshift($logLines, $line);
			}
			fclose($logFile);
		}
		
		return $logLines;
	}

	// remove all logs from log file
	public function clear() {
		fclose(fopen($this->logDir."log", "w"));
	}

	private function arrToString($assoArr) {
		$s = "{";

		foreach ($assoArr as $k => $v) {
			$s .= '"'.$k.'":"'.$v.'",';
		}

		$s = rtrim($s, ",");
		$s .= "}";

		return $s;
	}
}
?>