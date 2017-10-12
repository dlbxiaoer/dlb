testpay.aspx  测试请求页面
payResult.aspx  测试回调页面

App_Code\PayPlugin\PayRender.cs    请求POST请求之前，对请求的响应头/参数进行Render
App_Code\PayPlugin\DoRequest.cs    执行post请求的类
App_Code\PayPlugin\payEntity.cs    请求创建支付链接后返回的结果集，采用Newtonsoft.Json.JsonConvert转换成对象
App_Code\PayPlugin\PayConfig.cs    参数配置类

注：测试需要放在服务器上测试，因为回调页面，需要是域访问级别才能测试和使用
    PayConfig 的配置信息 是按照 开发文档 中最后的测试账户
    商户级非代理商