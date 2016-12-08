package com.tdy.warehouse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tdy.warehouse.R;
import com.tdy.warehouse.utils.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangdayi on 16/5/7.
 * 作者:tangdayi
 * 日期:2016年05月07日20时28分
 * 文件:LoginActivty.java
 * 工程:warehouse
 */
public class LoginActivty extends Activity {

    private EditText etUsername,etPassword;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_login);

        etPassword = (EditText) findViewById(R.id.et_password);
        etUsername = (EditText)findViewById(R.id.et_username);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = new StringBuffer().append("''").append("\"data\"").toString();
                testConneect();
                Toast.makeText(getApplicationContext(),"提交成功!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    }

    private void testConneect() {
         new Thread(new Runnable() {
             @Override
             public void run() {
                 String username = etUsername.getText().toString();

                 String password = etPassword.getText().toString();

                 try {

                     URL url = new URL("http://10.1.4.155:8085/text/accept_android_data.php?username="+username+"&password="+password);

                     HttpURLConnection conn =  (HttpURLConnection) url.openConnection();

                     conn.setRequestMethod("GET");
                     conn.setRequestProperty("Charset","UTF-8");
                     conn.setReadTimeout(5000);

                     if(conn.getResponseCode() == 200){

                         InputStream is = conn.getInputStream();

                         BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                         String result = br.readLine();
                         System.out.println("->>>>>>>>:" + result);
                        // Toast.makeText(LoginActivty.this, result, Toast.LENGTH_SHORT).show();

                         conn.disconnect();

                     }


                 } catch (Exception e) {

                     // TODO Auto-generated catch block

                     e.printStackTrace();

                 }
             }
         }).start();

    }


    //收到短信 后 提交
    private void RecSmsToPost(String strRecSmsMsg){
        String strNowDateTime=getNowDateTime("yyyy-MM-dd|HH:mm:ss");//当前时间
        //参数
        Map<String,String> params = new HashMap<String,String>();
        params.put("RECSMSMSG", strRecSmsMsg);
        //params.put("name", "李四");

        //服务器请求路径
        String strUrlPath = "http://192.168.1.9:80/JJKSms/RecSms.php" +"?DateTime=" + strNowDateTime;
        String strResult= HttpUtils.submitPostData(strUrlPath, params, "utf-8");
        etUsername.setText(strResult);

        //openToast("提交完成");
    }




    //获取当前时间
    private String getNowDateTime(String strFormat){
        if(strFormat==""){
            strFormat="yyyy-MM-dd HH:mm:ss";
        }
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat(strFormat);//设置日期格式
        return df.format(now); // new Date()为获取当前系统时间
    }

    //弹出消息
    private void openToast(String strMsg){
        Toast.makeText(this, strMsg, Toast.LENGTH_LONG).show();
    }






}
