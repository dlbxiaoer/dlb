using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class testpay : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        payEntity pe = new payEntity();
        pe.orderNo = "201705110100000005";
        pe.aMount = "500.00";
        pe.companyID = "1";
        pe.description = "1司|2科|人1";
        string result = PayRender.GetPayUrl(pe);

        //result:返回结果

        payEntity.payResult pr = Newtonsoft.Json.JsonConvert.DeserializeObject<payEntity.payResult>(result);

        //{"data":{"url":"https://order.duolabao.cn/active/c?state=201705110000000005%7C10011014944661863281447%7C500.00%7C1%7CAPI"},"result":"success"}
        //{"error":{"errorCode":"tokenError","errorMsg":"5AE4281756E85161"},"result":"fail"}
        //自己解析
    }

}