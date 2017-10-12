<?php

require_once "config.php";
include "include.php";
include "../common/function.php";
header("Content-type: text/html; charset=UTF-8");

$myfile = fopen("payBack.log", "a") or die("Unable to open file!");
 

 $headers = array(); 
foreach ($_SERVER as $key => $value) { 
    if ('HTTP_' == substr($key, 0, 5)) { 
        $headers[str_replace('_', '-', substr($key, 5))] = $value; 
    } 
}



$post_data = array(
	"requestNum"  => $_GET['requestNum'],
	"status"    => $_GET['status'],
);

//签名操作


fwrite($myfile, "--------------------------------start--------------------------------\n");

fwrite($myfile, date('y-m-d H:i:s',time())."接收到的数据:".json_encode($headers));
fwrite($myfile, "\n");

$str = "secretKey={$secretKey}&timestamp={$headers['TIMESTAMP']}";
$token = strtoupper(sha1($str));



fwrite($myfile, date('y-m-d H:i:s',time())."加密完成的字段:".$str."\n");

if($token==$headers['TOKEN']){
	fwrite($myfile, date('y-m-d H:i:s',time())."返回的数据:".json_encode($_GET)."success\n");

	$id = substr($post_data['requestNum'],18);
	$sql = "select * from " . DB_PREFIX . "order where id={$id}";
	$order = $db->getRow ( $sql );
	
	if($order['pay_status'] !='Y'){
			$sql="update ".DB_PREFIX."order set  pay_status='Y' where id='{$id}'";
		
			$db->query($sql);

			echo 'opstate=0';exit;
	}
	echo "success";
	
}else{
	echo "fail";
	fwrite($myfile, date('y-m-d H:i:s',time())."返回的数据:"."fail\n");
}

fwrite($myfile, "----------------------------------end--------------------------------\n");

fclose($myfile);



?>


