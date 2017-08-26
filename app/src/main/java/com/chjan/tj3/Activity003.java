package com.chjan.tj3;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Activity003 extends AppCompatActivity{

    private static final int DO_TASK1 = 0;

    Spinner spiHttpType;
    EditText etUri;
    Button butSend;
    TextView tvHttpEntity;
    TextView tvHttpResult;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout003);

        spiHttpType = (Spinner)findViewById(R.id.spHttpType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.httpType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiHttpType.setAdapter(adapter);

        etUri = (EditText)findViewById(R.id.etUri);
        butSend = (Button)findViewById(R.id.butSend);
        tvHttpEntity = (TextView)findViewById(R.id.tvHttpEntity);
        tvHttpResult = (TextView)findViewById(R.id.tvHttpResult);

        butSend.setOnClickListener(sendlistener);

    }

    private View.OnClickListener sendlistener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(v == butSend){
                /* 這邊要用 Thread 是因為 Android 改版之後會對在主程式裡跑網路連接的程式碼做 Exception 的意外排除動作
                                   因此要把網路連線使用多執行緒的方式去運行，才不會被當成例外錯誤拋出
                                        */
                Thread thread = new Thread(mutiThread);
                thread.start();
            }
        }
    };

    private String returnData;
    private Runnable mutiThread = new Runnable(){
        public void run(){
            // 運行網路連線的程式
            String r = getHttpPostDate("https://admin.cyut.edu.tw/ip.asp");
            if (r != null)
            {
                Log.d("TError", r);
                returnData = r;
            }
            Message msg = new Message();
            msg.what = DO_TASK1;
            messageHandler.sendMessage(msg);
        }
    };

    Handler messageHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DO_TASK1:
                    tvHttpResult.setText("tvHttpResult:\n" + (returnData == null ? "(沒資料)" : returnData));
                    break;
                default:
                    break;
            }
        }
    };

    private String getHttpPostDate(String UrlString){
        String returnResult = "";
        try{
            //建立網址物件
            URL url = new URL(UrlString);

            //建立連線物件
            HttpsURLConnection conn = null;

            //連線
            conn = (HttpsURLConnection)url.openConnection();
            // 設定 request timeout
            conn.setReadTimeout(1500);
            conn.setConnectTimeout(1500);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 設定開啟自動轉址
            conn.setInstanceFollowRedirects(true);

            // 模擬 Chrome 的 user agent, 因為手機的網頁內容較不完整
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");

            //POST 參數
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("firstParam", "資料1")
                    .appendQueryParameter("secondParam", "資料2")
                    .appendQueryParameter("thirdParam", "資料3");
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);

            writer.flush();
            writer.close();
            os.close();

            // 若要求回傳 200 OK 表示成功取得網頁內容
            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){
                // 讀取網頁內容
                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader  = new BufferedReader( new InputStreamReader(inputStream) );

                String tempStrLine;
                StringBuffer stringBuffer = new StringBuffer();

                while( ( tempStrLine = bufferedReader.readLine() ) != null ) {
                    stringBuffer.append( tempStrLine );
                }

                bufferedReader.close();
                inputStream.close();

                // 取得網頁內容類型
                String  mime = conn.getContentType();
                boolean isMediaStream = false;

                // 判斷是否為串流檔案
                if( mime.indexOf("audio") == 0 ||  mime.indexOf("video") == 0 ){
                    isMediaStream = true;
                }

                // 網頁內容字串
                returnResult = stringBuffer.toString();
            }
        }catch (Exception e){
            Log.e("TError",e.toString());
            e.printStackTrace();
        }

        return returnResult;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            result.append(first ? "" : "&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            first = false;
        }
        return result.toString();
    }
}