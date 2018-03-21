<?php

/**
 * 作者：steven
 * 说明：哆啦宝支付APIdemo
 * 时间：2018-03-21
 * */
 
class Duolabaopay {
	
	private $config = array(
		"agentNum"=>"10001014472963095391100",
		"customerNum"=>"10001114533598995853210",
		"shopNum"=>"10001214696702060035149",
		"machineNum"=>"10011014696701293260007",
		"source"=>"API",
		"secret_key"=>"d9bb7fc0493647a48e937ded073e342280dd45ac",
		"access_key"=>"e84fab7878194925a28f5ee8e926e77f913cc646",
	);
	//创建支付链接
	public function order($param){
		$url_host = "https://openapi.duolabao.com";
		$url_path = "/v1/agent/order/payurl/create";

		$timestamp = time();

		$data = array(
			"agentNum"=>$this->config['agentNum'],
			"customerNum"=>$param['customerNum']?$param['customerNum']:$this->config['customerNum'],
			"shopNum"=>$param['shopNum']?$param['shopNum']:$this->config['shopNum'],
			"requestNum"=>$param['order_no'],
			"amount"=>$param['total_fee']/100,
			"source"=>$this->config['source'],
			"callbackUrl"=>$param['callbackUrl'],
			"completeUrl"=>$param['completeUrl']
		);
		$token = $this->dlb_sign($timestamp,$url_path,$data);
		
		$header = array(
			"Content-Type"=>"application/json",
			"accessKey"=>$this->config['access_key'],
			"timestamp"=>$timestamp,
			"token"=>$token,
		);
		$result = $this->post_func($url_host.$url_path,$data,$header);
		$data = json_decode($result,true);
		
		return $data;
	}
	
	//被扫
	public function passive($param){
		$url_host = "https://openapi.duolabao.com";
		$url_path = "/v1/agent/passive/create";

		$timestamp = time();
		

		$data = array(
			"agentNum"=>$this->config['agentNum'],
			"customerNum"=>$param['customerNum']?$param['customerNum']:$this->config['customerNum'],
			"shopNum"=>$param['shopNum']?$param['shopNum']:$this->config['shopNum'],
			"requestNum"=>$param['order_no'],
			"amount"=>$param['total_fee']/100,
			"source"=>$this->config['source'],
			"authCode"=>$param["authCode"]
		);
		$token = $this->dlb_sign($timestamp,$url_path,$data);
		
		$header = array(
			"Content-Type"=>"application/json",
			"accessKey"=>$this->config['access_key'],
			"timestamp"=>$timestamp,
			"token"=>$token,
		);
		$result = $this->post_func($url_host.$url_path,$data,$header);
		$data = json_decode($result,true);
		
		return $data;
	}
	
	//订单查询
	public function query($param){
		$url_host = "https://openapi.duolabao.com";
		$url_path = "/v1/agent/order/payresult";

		$timestamp = time();
		

		$data = array(
			"agentNum"=>$this->config['agentNum'],
			"customerNum"=>$param['customerNum']?$param['customerNum']:$this->config['customerNum'],
			"shopNum"=>$param['shopNum']?$param['shopNum']:$this->config['shopNum'],
			"requestNum"=>$param['order_no']
		);
		$get_param = "/".$data["agentNum"]."/".$data["customerNum"]."/".$data["shopNum"]."/".$data["requestNum"];
		$token = $this->dlb_sign($timestamp,$url_path.$get_param);
		
		$header = array(
			"accessKey"=>$this->config['access_key'],
			"timestamp"=>$timestamp,
			"token"=>$token,
		);
		
		$result = $this->post_func($url_host.$url_path.$get_param,'',$header,10,1);
		$data = json_decode($result,true);
		
		return $data;
	}
	
	//退款接口
	public function refund($param){
		$url_host = "https://openapi.duolabao.com";
		$url_path = "/v1/agent/order/refund";

		$timestamp = time();
		

		$data = array(
			"agentNum"=>$this->config['agentNum'],
			"customerNum"=>$param['customerNum']?$param['customerNum']:$this->config['customerNum'],
			"shopNum"=>$param['shopNum']?$param['shopNum']:$this->config['shopNum'],
			"requestNum"=>$param['order_no']
		);
		$token = $this->dlb_sign($timestamp,$url_path,$data);
		
		$header = array(
			"Content-Type"=>"application/json",
			"accessKey"=>$this->config['access_key'],
			"timestamp"=>$timestamp,
			"token"=>$token,
		);
		$result = $this->post_func($url_host.$url_path,$data,$header);
		$data = json_decode($result,true);
		
		return $data;
	}
	
	//签名方法
	private function dlb_sign($timestamp,$url_path,$data){
		$sign_data = array(
			"secretKey"=>$this->config['secret_key'],
			"timestamp"=>$timestamp,
			"path"=>$url_path
		);
		if($data){
			$sign_data["body"] = json_encode($data);
		}
		$o = '';
		
		foreach ( $sign_data as $k => $v ) 
		{ 
			 $o.="$k=".$v."&";
		} 
		$sign_data = substr ($o,0,-1);
		return strtoupper(sha1($sign_data));
	}
	
	//发起POST请求
	private function post_func($url, $post_data = '',$header_data = '',$timeout = 10,$type = ''){
		
		$ch = curl_init();

		
		curl_setopt ($ch, CURLOPT_URL, $url);
		
		if($type == ''){
			curl_setopt ($ch, CURLOPT_POST, 1);
		}else{
			curl_setopt ($ch, CURLOPT_POST, false);
		}
		
		
		if($post_data != ''){
			curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($post_data));
		}
		
		$o = array();
		foreach ( $header_data as $k => $v ) 
		{ 
			$o[] = "$k: $v";
		} 
		$header_data = $o;

		curl_setopt ($ch, CURLOPT_CONNECTTIMEOUT, $timeout);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_HEADER, false);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $header_data);

		$file_contents = curl_exec($ch);
		curl_close($ch);
		return $file_contents;
	}
	
	
		
	
}


