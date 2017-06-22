package jacky.androidprogram.subpages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Update_Account extends Activity implements View.OnClickListener {


    DBHandler db;
    private EditText tfname;
    private EditText tlname,tpassword,taccount,tphone,taddr,temail,cpassword;
    private Spinner mailtype;
    public TextView view,welcometv;
    public String sex;
    public String user_name;
    public User User_data;
    public RadioGroup sexrg;
    private Button sendBtn;
    private String user_id;
    Vibrator vibe;
    Intent Passbackintent;
    private String uriAPI = "http://www.ebizlearning.com.my/web/phone/user_rename.php";
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
                        Toast.makeText(Update_Account.this, result, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update__account);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        welcometv = (TextView) findViewById(R.id.update_tview);
        tfname = (EditText) findViewById(R.id.update_fname);
        tlname = (EditText) findViewById(R.id.update_lname);
        tpassword = (EditText) findViewById(R.id.update_pass);
        cpassword = (EditText)findViewById(R.id.update_confirmpass);
        taccount = (EditText) findViewById(R.id.update_acc);
        tphone = (EditText) findViewById(R.id.update_phone);
        taddr = (EditText) findViewById(R.id.update_addr);
        temail = (EditText)findViewById(R.id.update_email);
        sexrg = (RadioGroup)findViewById(R.id.update_sexr);
        //mailtype = (Spinner) findViewById(R.id.update_mailtype);


        db = new DBHandler(this);
        Passbackintent=new Intent();
        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        user_name = bundle.getString("user_name");
        User_data=db.getUserbyName(user_name);
        user_id=Integer.toString(User_data.getId());

        welcometv.setText(welcometv.getText()+" "+User_data.getFname()+User_data.getLname());

        taccount.setText(User_data.getUsername());
        tfname.setText(User_data.getFname());
        tlname.setText(User_data.getLname());
        tphone.setText(User_data.getPhone());
        taddr.setText(User_data.getAddress());
        temail.setText(User_data.getEmail());
        if(User_data.getSex().equals(new String(getResources().getString(R.string.female)))){
            sexrg.check(R.id.femaler);
            sex=getResources().getString(R.string.female);
        }else if(User_data.getSex().equals(new String(getResources().getString(R.string.male)))){
            sexrg.check(R.id.maler);
            sex=getResources().getString(R.string.male);
        }
        sendBtn = (Button) findViewById(R.id.update_btn);

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

            // 啟動一個Thread(執行緒)，將要傳送的資料放進Runnable中，讓Thread執行

            Thread t = new Thread(new sendPostRunnable(user_id,fname,lname,password,account,phone,addr,sex,email));
            if(password.length()==0)tpassword.setError("Please input password");
            else if(cpassword.getText().toString().equals(tpassword.getText().toString())) {
                t.start();
                User_data.setUserAllInfoUpdate(account,password,fname,lname,email,phone,sex,addr);
                db.updateUser(User_data);
                Intent i = new Intent(Update_Account.this, home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", User_data.getId());
                i.putExtras(bundle);
                startActivity(i);

            }else{
                cpassword.setError("Password not match.");
            }
        }
        //this.finish();
    }
    private String sendPostDataToInternet(String id,String strTxt,String lname,String password,String account,String phone,String addr,String sex,String email)
    {

      /* 建立HTTP Post連線 */

        HttpPost httpRequest = new HttpPost(uriAPI);

      /*

       * Post運作傳送變數必須用NameValuePair[]陣列儲存

       */

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("fname", strTxt));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("address", addr));
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
                String succ = "Update Sucessfully";
                if (strResult.equals(succ)){
                    vibe.vibrate(100);
                    User_data.setUserAllInfoUpdate(taccount.getEditableText().toString(),
                            tpassword.getEditableText().toString(),
                            tfname.getEditableText().toString(),
                            tlname.getEditableText().toString(),
                            temail.getEditableText().toString(),
                            tphone.getEditableText().toString(), sex,
                            taddr.getEditableText().toString());
                    db.updateUser(User_data);
                    this.finish();
                    return strResult;
                }

                // 回傳回應字串
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
        String id=null;
        String strTxt = null;
        String lname = null;
        String password = null;
        String account = null;
        String phone = null;
        String addr = null;
        String sex = null;
        String email = null;



        // 建構子，設定要傳的字串

        public sendPostRunnable(String id,String strTxt, String lname, String password, String account, String phone, String addr, String sex,String email)

        {
            this.id=id;
            this.strTxt = strTxt;
            this.lname = lname;
            this.password = password;
            this.account = account;
            this.phone = phone;
            this.addr = addr;
            this.sex = sex;
            this.email=email;

        }

        @Override

        public void run()

        {

            String result = sendPostDataToInternet(user_id,strTxt,lname,password,account,phone,addr,sex,email);

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


}



