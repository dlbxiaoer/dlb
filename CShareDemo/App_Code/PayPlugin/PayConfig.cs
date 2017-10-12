using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// PayConfig 的摘要说明
/// </summary>
public class PayConfig
{
	public PayConfig()
	{
		//
		// TODO: 在此处添加构造函数逻辑
		//
    }

    public static string url = "http://openapi.duolabao.cn";
    public static string customerNum = "10001114533598995853210";
    public static string shopNum = "10001214696702060035149";
    public static string machineNum = "10011014696701293260007 ";
    public static string callbackUrl = "http://XXXX.com/payResult.aspx";

    public static string accessKey = "62694e2e777844e39e2ddb75869ce8f9c0f999b8";
    public static string secretKey = "dba2df87597440fd8c0444dec3b65262dcbf3f38";
}