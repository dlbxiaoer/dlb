<?php
header("Access-Control-Allow-Origin: *");
	/**
     * 哆啦宝接口
	 *  zhaoyz
     */
class dlbpayController extends Controller {
    public function __construct() {
        parent::__construct();
    }
    
    public function index(){
        
    }
    
    /**
     * 创建订单接口
     */
    public function createDLBPay(){
        $data['dlb_customer_num'] = $_POST['customer_num'];
        $data['dlb_shop_num'] = $_POST['shop_num'];
        $data['request_num'] = $_POST['request_num']; //注：必须为18-32位纯数字
        $data['amount'] = $_POST['amount'];
        $data['callback_url'] = $_POST['callback_url'];
        $req_bak = $this->request_createpay($data);
        echo json_encode($req_bak);
    }
    
    /**
     * 退款接口
     */
    public function refundDLBPay(){
        $data['dlb_customer_num'] = $_POST['customer_num'];
        $data['dlb_shop_num'] = $_POST['shop_num'];
        $data['request_num'] = $_POST['request_num']; //注：必须为18-32位纯数字
        $req_bak = $this->request_refundpay($data);
        echo json_encode($req_bak);
    }
    
    /**
    * 创建哆啦宝微信支付
    */
    protected function request_createpay($data){
        $PayConfig = $this->config('dlbpay');
        if(is_array($PayConfig)){
            $pay_data['accesskey'] = $PayConfig['dlb_access_key'];
            $pay_data['secretkey'] = $PayConfig['dlb_secret_key'];
            $pay_data['timestamp'] = $this->getMillisecond();
            $pay_data['path'] = $PayConfig['dlb_path_createorder']; //'/v1/agent/order/payurl/create';
            $sign_data = array(
                'agentNum'=>$PayConfig['dlb_agent_num'],            // 代理商编号
                'amount'=>$data['amount'],                          // 金额--请求传递
                'callbackUrl'=>$data['callback_url'],               // 回调服务器链接--请求传递
                'customerNum'=>$data['dlb_customer_num'],           // 哆啦宝商户号--请求传递
                'requestNum'=>$data['request_num'],                 // 订单号--请求传递  注：必须为18-32位纯数字
                'shopNum'=>$data['dlb_shop_num'],                   // 哆啦宝店铺号--请求传递
                'source'=>'API',
            );
            $pay_data['body'] = json_encode($sign_data);
            $infoArr = $this->creatTokenPost($pay_data);
            switch ($infoArr['result']) {
                case 'success'://成功
                    $payurl = $infoArr['data']['url'];
                    return  array('code'=>200,'msg'=>'订单支付创建成功','url'=>array('payurl'=>$payurl));
                    break;
                case 'fail'://失败
                    return array('code'=>502,'msg'=>'订单支付创建失败');
                    break;
                case 'error'://异常
                    return array('code'=>501,'msg'=>'服务器繁忙，支付调用失败');
                    break;
                default:
                    break;
            }
        }else{
            return array('code'=>502,'msg'=>'订单支付创建失败');
        }
    }
    
    /**
    * 申请哆啦宝微信退款
    */
    protected function request_refundpay($data){
        echo "**".$data['dlb_customer_num']."**".$data['dlb_shop_num']."**".$data['request_num']."**";
        $PayConfig = $this->config('dlbpay');
        if(is_array($PayConfig)){
            $pay_data['accesskey'] = $PayConfig['dlb_access_key'];
            $pay_data['secretkey'] = $PayConfig['dlb_secret_key'];
            $pay_data['timestamp'] = $this->getMillisecond();
            $pay_data['path'] = $PayConfig['dlb_path_refund'];      //'/v1/agent/order/payurl/create';
            $sign_data = array(
                'agentNum'=>$PayConfig['dlb_agent_num'],            // 代理商编号
                'customerNum'=>$data['dlb_customer_num'],           // 哆啦宝商户号--请求传递
                'requestNum'=>$data['request_num'],                 // 订单号--请求传递  注：必须为18-32位纯数字
                'shopNum'=>$data['dlb_shop_num'],                   // 哆啦宝店铺号--请求传递
            );
            $pay_data['body'] = json_encode($sign_data);
            $infoArr = $this->creatTokenPost($pay_data);
            switch ($infoArr['result']) {
                case 'success'://成功
                    $payurl = $infoArr['data']['url'];
                    return array('code'=>200,'msg'=>'订单退款成功','url'=>array('payurl'=>$payurl));
                    break;
                case 'fail'://失败
                    return array('code'=>502,'msg'=>'订单退款失败');
                    break;
                case 'error'://异常
                    return array('code'=>501,'msg'=>'服务器繁忙，退款失败，请稍后再试试');
                    break;
                default:
                    break;
            }
        }else{
            return array('code'=>502,'msg'=>'订单退款失败');
        }
    }
    
    //生成token并提交
    protected function creatTokenPost($data) {
        $str = "secretKey={$data['secretkey']}&timestamp={$data['timestamp']}&path={$data['path']}&body={$data['body']}";
        $token = strtoupper(sha1($str));
        $url = 'http://openapi.duolabao.cn'.$data['path'];

        $post_data = $data['body'];
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, FALSE);
        curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (compatible; MSIE 5.01; Windows NT 5.0)');
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt($ch, CURLOPT_AUTOREFERER, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $post_data);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, array(
            'Content-Type: application/json',
            'accesskey: ' . $data['accesskey'],
            'timestamp: ' . $data['timestamp'],
            'token: ' . $token)
        );
        $info = curl_exec($ch);
        $infoArr = json_decode($info,true);
    //    put_contents('log/payurl_result',$infoArr,1);
        curl_close($ch);
        return $infoArr;
    }
    
    /**13位时间戳**/
    function getMillisecond() {
	list($t1, $t2) = explode(' ', microtime());
	return $t2.ceil( ($t1 * 1000) );
    }

}


