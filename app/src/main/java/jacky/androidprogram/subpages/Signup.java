package jacky.androidprogram.subpages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;

public class Signup extends Activity implements View.OnClickListener {


    private EditText tfname;
    private EditText tlname,tpassword,taccount,tphone,taddr,temail,cpassword;
    private Spinner mailtype;
    public TextView view;
    public String sex,phonesn;
    private Button sendBtn;

    DBHandler db;
    Vibrator vibe;

    private String uriAPI = "http://www.ebizlearning.com.my/web/phone/user_register.php";
    protected static final int REFRESH_DATA = 0x00000001;
    /** 建立UI Thread使用的Handler，來接收其他Thread來的訊息 */
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)

        {
            switch (msg.what)
            {
                // 顯示網路上抓取的資料
                case REFRESH_DATA:
                    String result = null;
                    if (msg.obj instanceof String)
                        result = (String) msg.obj;
                    if (result != null)
                        // 印出網路回傳的文字
                        Toast.makeText(Signup.this, result, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        db = new DBHandler(this);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        view = (TextView) findViewById(R.id.signview);
        tfname = (EditText) findViewById(R.id.sfname);
        tlname = (EditText) findViewById(R.id.slname);
        tpassword = (EditText) findViewById(R.id.spass);
        cpassword = (EditText)findViewById(R.id.confirmpass);
        taccount = (EditText) findViewById(R.id.sacc);
        tphone = (EditText) findViewById(R.id.sphone);
        taddr = (EditText) findViewById(R.id.saddr);
        temail = (EditText)findViewById(R.id.semail);

        phonesn= Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings
                .Secure.ANDROID_ID) + Build.SERIAL;
        sendBtn = (Button) findViewById(R.id.regbtn);

        addItemsOnSpinner2();


        if (sendBtn != null) sendBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v)
    {
        if (v == sendBtn)

        {
            // 擷取文字框上的文字

            String fname = tfname.getEditableText().toString();
            String lname = tlname.getEditableText().toString();
            String password = tpassword.getEditableText().toString();
            String account = taccount.getEditableText().toString();
            String phone = tphone.getEditableText().toString();
            String addr = taddr.getEditableText().toString();
            String email = temail.getEditableText().toString();
            //String email = temail.getEditableText().toString()+String.valueOf(mailtype.getSelectedItem());

            // 啟動一個Thread(執行緒)，將要傳送的資料放進Runnable中，讓Thread執行
            Thread t = new Thread(new sendPostRunnable(fname,lname,password,account,phone,addr,sex,email,phonesn));
            if(account.length()<6)taccount.setError("Account too short");
            if(password.length()<6)tpassword.setError("Password too short");
            if(phone.isEmpty())tpassword.setError("Please input phone number");
            if(addr.isEmpty())tpassword.setError("Please input address");

            if(cpassword.getText().toString().equals(tpassword.getText().toString()))
            {
                //t.start();
                ///*
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle("Confirmation");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Account will be fix on this phone, Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                t.start();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
               // */

            } else{
                    cpassword.setError("Password not match.");

            }

        }
        //this.finish();

    }
    private String sendPostDataToInternet(String strTxt,String lname,String password,String account,String phone,String addr,String sex,String email,String phonesn)
    {

      /* 建立HTTP Post連線 */

        HttpPost httpRequest = new HttpPost(uriAPI);

      /*

       * Post運作傳送變數必須用NameValuePair[]陣列儲存

       */

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("fname", strTxt));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("address", addr));
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("phone_number",phonesn));



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
                String succ = "Signup Sucessfully";
                boolean found = strResult.contains(succ);
                int user_id=Integer.parseInt(strResult.substring(20));
                String log = strResult+user_id+ "\n";
                Log.d("LOG: : ",log);
                  if (found){
                      log = "found"+ "\n";
                      Log.d("LOG: : ",log);
                      vibe.vibrate(100);
                      db.createUser(new User(user_id,account,password,strTxt,lname,email,phone,sex,addr,phonesn));
                        this.finish();
                        return succ;
                    }
                else
                  {   log = "fail"+ "\n";
                      Log.d("LOG: : ",log);
                      return strResult;}


            }

        } catch (Exception e)

        {

            e.printStackTrace();

        }

        return null;

    }

    class sendPostRunnable implements Runnable
    {

        String strTxt = null;
        String lname = null;
        String password = null;
        String account = null;
        String phone = null;
        String addr = null;
        String sex = null;
        String email = null;
        String phonesn=null;



        // 建構子，設定要傳的字串

        public sendPostRunnable(String strTxt, String lname, String password, String account, String phone, String addr, String sex,String email,String phonesn)

        {

            this.strTxt = strTxt;
            this.lname = lname;
            this.password = password;
            this.account = account;
            this.phone = phone;
            this.addr = addr;
            this.sex = sex;
            this.email=email;
            this.phonesn=phonesn;

        }

        @Override

        public void run()

        {

            String result = sendPostDataToInternet(strTxt,lname,password,account,phone,addr,sex,email,phonesn);

            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();

        }

    }
    public void onRadioButtonClicked(View view) throws UnsupportedEncodingException {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.maler:
                if (checked)
                sex=getResources().getString(R.string.male);
                    break;
            case R.id.femaler:
                if (checked)
                    sex=getResources().getString(R.string.female);
                    break;
        }
    }

    public void addItemsOnSpinner2() {

        mailtype = (Spinner) findViewById(R.id.mailtype);

        /*List<String> list = new ArrayList<String>();
        list.add("@gmail.com");
        list.add("@hotmail.com");
        list.add("@yahoo.com");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mailtype.setAdapter(dataAdapter);
        */
        String[] plants = new String[]{
                "@gmail.com",
                "@hotmail.com",
                "@yahoo.com"
        };

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mailtype.setAdapter(spinnerArrayAdapter);
    }


}
