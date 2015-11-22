<?php
	function card2cred($card) {
		$result = strrev($card);
	    return $result;
	}

	function cred2card($cred) {
		$result = strrev($cred);
	    return $result;
	}
?>