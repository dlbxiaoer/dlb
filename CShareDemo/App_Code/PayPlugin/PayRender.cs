using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.Security;

/// <summary>
/// PayRender 的摘要说明
/// </summary>
public class PayRender
{

    static System.Web.Script.Serialization.JavaScriptSerializer jss = new System.Web.Script.Serialization.JavaScriptSerializer();
	public PayRender()
	{
		//
		// TODO: 在此处添加构造函数逻辑
		//
	}
    /// <summary>
    /// TODO：1：构造参数
    /// </summary>
    /// <param name="orderNo"></param>
    /// <returns></returns>
    public static string GetPayUrl(payEntity pe)
    {
        TimeSpan ts = DateTime.Now.Subtract(DateTime.Parse("1970-01-01 00:00:00"));

        string timestamp = Convert.ToInt64(ts.TotalSeconds).ToString();
        //StringBuilder sb = new StringBuilder();
        StringBuilder jsonSb = new StringBuilder("{");//string.Format("appId={0}", model.appid)

        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "customerNum", PayConfig.customerNum);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "shopNum", PayConfig.shopNum);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "machineNum", PayConfig.machineNum);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "requestNum", pe.orderNo);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "amount", pe.aMount);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "source", "API");
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "tableNum", pe.companyID);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "callbackUrl", PayConfig.callbackUrl);
        jsonSb.AppendFormat("\"{0}\":\"{1}\",", "extraInfo", pe.description);

        string param = jsonSb.ToString().TrimEnd(',') + "}";
        string param1 = string.Format("secretKey={0}&timestamp={1}&path={2}&body={3}"
            , PayConfig.secretKey, timestamp, "/v1/customer/order/payurl/create", param);

        string token = FormsAuthentication.HashPasswordForStoringInConfigFile(param1, "SHA1");


        //LogHelper.Log(string.Format("【代理商创建支付生成的param】{0}", param));

        //sb.AppendFormat("&key={0}", ConfigurationManager.AppSettings["WXPaySecret"]);
        string result = DoRequest.PostWebRequest(string.Format("{0}/v1/customer/order/payurl/create", PayConfig.url), token, param, timestamp);
        //LogHelper.Log(string.Format("【代理商创建支付生成的token】{0}", token));
        //LogHelper.Log(string.Format("【代理商创建支付链接】{0}", result));
        //var resultModel = jss.Deserialize<PayResponse>(result);
        //if (model != null)
        //{
        //    return model.errcode == "0";
        //}
        return result;
    }
}