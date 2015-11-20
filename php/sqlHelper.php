<?php
	function accountRow($accNo) {
		$sql = sprintf("SELECT * FROM accounts WHERE account_no = '%s'"
			, mysql_real_escape_string($accNo));
		return mysql_query($sql);
	}
	function cardAccountRow($cardNo, $accNo) {
		$sql = sprintf("SELECT * FROM accounts WHERE card_no = '%s' AND account_no = '%s'"
			, mysql_real_escape_string($cardNo)
			, mysql_real_escape_string($accNo));
		return mysql_query($sql);
	}
	function cardRow($cardNo) {
		$sql = sprintf("SELECT * FROM cards WHERE card_no = '%s'", $cardNo);
		return mysql_query($sql);
	}
	function getBalance($accNo) {
		$result = accountRow($accNo);
		if (mysql_num_rows($result) == 1) { // should have 1 match only
			$row = mysql_fetch_assoc($result);
			return intval($row["balance"]);
		}
		return -1;
	}
	function updateBalance($accNo, $change) {
		$balance = getBalance($accNo);
		$newBalance = $balance + $change;
		$sql = sprintf("UPDATE accounts SET balance = '%s' WHERE account_no = '%s'"
					, mysql_real_escape_string($newBalance)
					, mysql_real_escape_string($accNo));
		if (mysql_query($sql)) {
			return getBalance($accNo);
		}
		return -1;
	}
	function updatePin($cardNo, $pin) {
		$sql = sprintf("UPDATE cards SET pin = '%s' WHERE card_no = '%s'"
					, mysql_real_escape_string($pin)
					, mysql_real_escape_string($cardNo));
		if (mysql_query($sql)) {
			return 1;
		}
		return -1;
	}
	function getAccounts($cardNo) {
		$sql = sprintf("SELECT account_no FROM accounts WHERE card_no = '%s'", mysql_real_escape_string($cardNo));
		return mysql_query($sql);
	}
?>