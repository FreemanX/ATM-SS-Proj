<?php
class LogHelper {

	private $logDir = "../bams_logs/";

	public function write($ident, $param, $result) {
		date_default_timezone_set("Asia/Hong_Kong");
		$msg = date("Y-m-d H:i:s")." | ".$ident.", param:".$this->arrToString($param).", result:".$result;

		$logFile = fopen($this->logDir."log.txt", "a");
		$f = fwrite($logFile, $msg.PHP_EOL);
		fclose($f);
		fclose($logFile);
	}

	public function getLogs() {

		$logLines = array();
		
		$logFile = fopen($this->logDir."log.txt", "r");

		if ($logFile) {
			while (($line = fgets($logFile)) != false) {
				array_push($logLines, $line);
			}
			fclose($logFile);
		}
		

		return $logLines;
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