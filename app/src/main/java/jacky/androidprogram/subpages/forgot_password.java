package jacky.androidprogram.subpages;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.R;

public class forgot_password extends Activity implements View.OnClickListener {

    private Button fsendbt;
    private EditText faccount,femail;
    Vibrator vibe;
    private String uriAPI = "http://www.ebizlearning.com.my/web/phone/user_forget.php";
    protected static final int REFRESH_DATA = 0x00000001;
    /** 建立UI Thread使用的Handler，來接收其他Thread來的訊息 */
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)

        {
            AlertDialog.Builder AD = new AlertDialog.Builder(forgot_password.this);
            switch (msg.what)
            {
                // 顯示網路上抓取的資料
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if (result != null)
                        // 印出網路回傳的文字
                        //Toast.makeText(forgot_password.this, result, Toast.LENGTH_LONG).show();
                        AD.setTitle("");
                        AD.setMessage(result);
                        AD.show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fsendbt=(Button)findViewById(R.id.forgot_sendbt);
        faccount=(EditText)findViewById(R.id.forgot_user);
        femail=(EditText)findViewById(R.id.forgot_email);

        if (fsendbt != null) fsendbt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

        if (v == fsendbt)

        {
            // 擷取文字框上的文字
            String account = faccount.getEditableText().toString();
            String email = femail.getEditableText().toString();
            //String email = temail.getEditableText().toString()+String.valueOf(mailtype.getSelectedItem());

            // 啟動一個Thread(執行緒)，將要傳送的資料放進Runnable中，讓Thread執行
            Thread t = new Thread(new sendPostRunnable(account,email));


            //if(account.getText().toString().equals(tpassword.getText().toString())) t.start();
            if (faccount.length()==0) faccount.setError("Account is blank.");
            else if(femail.length()==0) femail.setError("Email is blank.");
            else t.start();

        }
        //this.finish();

    }
    private String sendPostDataToInternet(String account,String email)
    {

      /* 建立HTTP Post連線 */

        HttpPost httpRequest = new HttpPost(uriAPI);

      /*

       * Post運作傳送變數必須用NameValuePair[]陣列儲存

       */

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("email",email));

        try
        {
          /* 發出HTTP request */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
          /* 取得HTTP response */
            HttpResponse httpResponse = new DefaultHttpClient()
                    .execute(httpRequest);
          /* 若狀態碼為200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
             /* 取出回應字串 */
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                //char test;
                //String s = "";
                /*String succ = "Signup Sucessfully";
                if (strResult.equals(succ)){
                    vibe.vibrate(100);
                    this.finish();
                    return strResult;
                } */

                // 回傳回應字串
                //test =strResult.charAt(5);
                //s = s+test;
                //return s;
                return strResult;

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    class sendPostRunnable implements Runnable
    {
        String account = null;
        String email = null;
        // 建構子，設定要傳的字串
        public sendPostRunnable(String account,String email)
        {
            this.account = account;
            this.email=email;
        }

        @Override

        public void run()

        {
            String result = sendPostDataToInternet(account,email);
            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();

        }

    }
}
