using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// payEntity 的摘要说明
/// </summary>
public class payEntity
{
	public payEntity()
	{
		//
		// TODO: 在此处添加构造函数逻辑
		//
	}
    /// <summary>
    /// 订单号：流水号：时间戳
    /// </summary>
    public string orderNo { get; set; }
    /// <summary>
    /// 订单金额
    /// </summary>
    public string aMount { get; set; }
    /// <summary>
    /// 订单企业编号
    /// </summary>
    public string companyID { get; set; }
    /// <summary>
    /// 订单描述：参加科目人数
    /// </summary>
    public string description { get; set; }

    public class payResult
    {
        public string result { get; set; }
        public data data { get; set; }
        public error error { get; set; }
    }
    public class data
    {
        public string url { get; set; }
    }
    public class error
    {
        public string errorCode { get; set; }
        public string errorMsg { get; set; }
    }
}