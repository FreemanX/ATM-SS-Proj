<?php 

	//------------------------------------------------------------
	// card2cred
	function card2cred($card) {
	    $result=strrev($card);
	    return $result;
	} // card2cred


	//------------------------------------------------------------
	// cred2card
	function cred2card($cred) {
	    $result=strrev($cred);
	    return $result;
	} // cred2card

	
	$cardNo = $_GET["cardNo"];
	$pin = $_GET["pin"];
	
	// open connection to DB
	$dbhost = 'localhost';
	$dbuser = 'root';
	$dbpass = '19940802';
	$dbname = 'test';
	$conn = mysql_connect($dbhost,$dbuser,$dbpass) or die();
	mysql_select_db($dbname);


	// get records from "cards" table
	$result = mysql_query("SELECT * FROM cards")
	or die(mysql_error());


	// search for a matching record
	$credential="ERROR";
	while($row = mysql_fetch_array($result)) {
	    if ($row['card_no']==$_GET["cardNo"] && $row['pin']==$_GET["pin"]) {
		$credential=card2cred($cardNo);
		break;
	    }
	}


	// return the credential result
	echo $credential;

	// close the connection to DB
	mysql_close($conn);


?>
