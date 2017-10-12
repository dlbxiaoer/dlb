using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;

/// <summary>
/// DoRequest 的摘要说明
/// </summary>
public class DoRequest
{
	public DoRequest()
	{
		//
		// TODO: 在此处添加构造函数逻辑
		//
	}

    /// <summary>
    /// TODO：2：post 连接，并得到返回结果
    /// </summary>
    /// <param name="postUrl"></param>
    /// <param name="token"></param>
    /// <param name="paramData"></param>
    /// <param name="timestamp"></param>
    /// <returns></returns>
    public static string PostWebRequest(string postUrl, string token, string paramData, string timestamp)
    {
        string ret = string.Empty;
        try
        {
            byte[] byteArray = Encoding.GetEncoding("utf-8").GetBytes(paramData); //转化
            HttpWebRequest webReq = (HttpWebRequest)WebRequest.Create(new Uri(postUrl));
            webReq.Method = "POST";
            webReq.ContentType = "application/json";
            webReq.Headers.Add("accessKey", PayConfig.accessKey);
            webReq.Headers.Add("timestamp", timestamp);
            webReq.Headers.Add("token", token);
            //webReq.UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
            //webReq.CachePolicy.Level = System.Net.Cache.RequestCacheLevel.NoCacheNoStore;
            webReq.ContentLength = byteArray.Length;
            Stream newStream = webReq.GetRequestStream();
            newStream.Write(byteArray, 0, byteArray.Length);//写入参数
            newStream.Close();
            HttpWebResponse response = (HttpWebResponse)webReq.GetResponse();
            StreamReader sr = new StreamReader(response.GetResponseStream(), Encoding.Default);
            ret = sr.ReadToEnd();
            sr.Close();
            response.Close();
            newStream.Close();
        }
        catch (Exception ex)
        {
            //MessageBox.Show(ex.Message);
        }
        return ret;
    }
}