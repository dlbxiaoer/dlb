using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class WCompany_payResult : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

        string timestamp = (Request.Headers["timestamp"] == null) ? "" : Request.Headers["timestamp"].ToString();
        string tokenT = (Request.Headers["token"] == null) ? "" : Request.Headers["token"].ToString();


        string param = string.Format("secretKey={0}&timestamp={1}"
            , PayConfig.secretKey, timestamp);

        string token = FormsAuthentication.HashPasswordForStoringInConfigFile(param, "SHA1");


        if (token == tokenT)
        {
            string requestNum = (Request["requestNum"] == null) ? "" : Request["requestNum"].ToString();
            string orderNum = (Request["orderNum"] == null) ? "" : Request["orderNum"].ToString();
            string orderAmount = (Request["orderAmount"] == null) ? "" : Request["orderAmount"].ToString();
            string status = (Request["status"] == null) ? "" : Request["status"].ToString();
            string completeTime = (Request["completeTime"] == null) ? "" : Request["completeTime"].ToString();


            ///
            ///TODO：获取到相关数据，做一系列操作
            ///
        }
    }
}