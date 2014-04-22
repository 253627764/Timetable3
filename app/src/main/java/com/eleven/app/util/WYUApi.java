package com.eleven.app.util;

/**
 * Created by eleven on 14-4-16.
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.util.Log;

public class WYUApi {
    private String              mUserID;

    private String              mUserPwd;

    private static String       mHtml = null;

    private static String       mRecords = null;

    private CookieStore         mCookieStore;

    private DefaultHttpClient   mHttpClient;

    private HttpResponse        mResponse;

    static private String TAG = WYUApi.class.getSimpleName();

    /**
     *
     * @param userid
     *            学号
     * @param userpwd
     *            密码
     */
    public WYUApi(String userid, String userpwd) {
        mUserID = userid;
        mUserPwd = userpwd;

        mHttpClient = new DefaultHttpClient();

        mHttpClient.getParams().setIntParameter("http.socket.timeout", 10000);
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     *            Context
     * @return 如果网络可用返回true, 否则返回false
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null) {
            return false;
        } else {
            return true;
        }
    }

    /***
     * 登陆系统
     *
     * @param context
     *            Context
     * @return 如果登陆成功返回true, 否则返回false
     */
    public boolean login(Context context) throws ClientProtocolException,
            IOException {
        // Create session页面
        //mHttpClient.execute(new HttpGet("http://jwc.wyu.edu.cn/student/"));
        //mHttpClient.execute(new HttpGet("http://jwc.wyu.edu.cn/student/createsession_a.asp"));
        //mHttpClient.execute(new HttpGet("http://jwc.wyu.edu.cn/student/createsession_b.asp"));
        // 请求验证码页面
        mResponse = mHttpClient.execute(new HttpGet("http://jwc.wyu.edu.cn/student/rndnum.asp"));

        Header h = mResponse.getFirstHeader("Set-Cookie");
        HeaderElement[] he = h.getElements();
        String randomNumber = he[0].getValue();
        Log.v(TAG, "randomNumber=" + randomNumber);


        // 构建post报头
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("UserCode", mUserID));
        params.add(new BasicNameValuePair("UserPwd", mUserPwd));
        params.add(new BasicNameValuePair("Validate", randomNumber));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");

        // 设置httppost
        HttpPost httpPost = new HttpPost(
                "http://jwc.wyu.edu.cn/student/logon.asp");
        httpPost.setEntity(entity);
        httpPost.setHeader("Referer", "http://jwc.wyu.edu.cn/student/body.htm");

        // 设置httpClient参数，不自动重定向
        HttpParams httpParams = new BasicHttpParams();
        HttpClientParams.setRedirecting(httpParams, true);


        HttpContext localContext = new BasicHttpContext();

        // 登录
        mResponse = mHttpClient.execute(httpPost, localContext);
        //getResponseContent(mResponse);
        Log.v(TAG, mResponse.getStatusLine().toString());

        if (mResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            Log.v(TAG, "登录失败");
            return false;
        } else {
            mCookieStore = mHttpClient.getCookieStore();
            Log.v(TAG, "登录成功");
            return true;
        }
    }

    public List<ClassInfo> getTimetable() throws IOException {
        // 请求课程表(f3.app)页面
        HttpGet httpGet = new HttpGet("http://jwc.wyu.edu.cn/student/f3.asp");
        httpGet.setHeader("Referer", "http://jwc.wyu.edu.cn/student/body.htm");
        try {
            mResponse = mHttpClient.execute(httpGet);
            Log.v(TAG, "f3 " + mResponse.getStatusLine().toString());
            HttpEntity entity = mResponse.getEntity();
            mHtml = getResponseContent(mResponse);
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        if (mHtml.length() <= 127) {
            return null;
        }
        return WYUParser.parseTimetable(mHtml);

    }

    public String getResponseContent(HttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String content = new String(EntityUtils.toString(entity).getBytes("ISO-8859-1"), "GB2312");
                Log.v(TAG, "html_size:" + content.length());
                Log.v(TAG, content);
                return content;
            } else {
                return null;
            }
        } catch(IOException e){
            System.out.print(e.getMessage());
        }
        return null;
    }

    // 返回课程表html文档
    public String getHtml() {
        return mHtml;
    }

    // 返回成绩表html文档
    public String getRecords() {
        return mRecords;
    }
}

