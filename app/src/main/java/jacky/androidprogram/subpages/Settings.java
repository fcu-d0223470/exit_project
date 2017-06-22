package jacky.androidprogram.subpages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.subpages.Common.ConnectivityReceiver;
import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.MainActivity;
import jacky.androidprogram.subpages.Common.MyApplication;
import jacky.androidprogram.R;

public class Settings extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    String user_name = "";
    int user_id;
    User Owner_data;
    Button edit_userbt, change_phonebt;
    DBHandler db;
    Vibrator vibe;
    TextView WelcomeText;
    private String uriAPI = "http://www.ebizlearning.com.my/web/phone/change_phone.php";
    protected static final int REFRESH_DATA = 0x00000001;

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
                        Toast.makeText(Settings.this, result, Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        edit_userbt = (Button) findViewById(R.id.setmyacc);
        change_phonebt = (Button) findViewById(R.id.setcphone);
        WelcomeText = (TextView)findViewById(R.id.setting_tv);
        db = new DBHandler(this);

        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        user_id = bundle.getInt("user_id");
        Owner_data=db.getUserbyid(user_id);

        WelcomeText.setText("Hi, "+Owner_data.getFname()+" "+Owner_data.getLname());
        edit_userbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                Intent Q = new Intent(Settings.this,Update_Account.class);
                Bundle bundle=new Bundle();
                bundle.putString("user_name",Owner_data.getUsername());
                Q.putExtras(bundle);
                startActivity(Q);}
                else {
                    edit_userbt.setError("Please connect to the internet");
                    Toast.makeText(Settings.this,"Please connect to the Internet",Toast.LENGTH_SHORT).show();
                }
            }
        });
        change_phonebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
                    // set title
                    alertDialogBuilder.setTitle("Warning");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Confirm change phone?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Thread t = new Thread(new sendPostRunnable(String.valueOf(Owner_data.getId())));
                                    t.start();
                                    Owner_data.setUser_phone_id("");
                                    db.updateUser(Owner_data);
                                    Intent i = new Intent(Settings.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
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
                }
                else {
                    change_phonebt.setError("Please connect to the internet");
                    Toast.makeText(Settings.this,"Please connect to the Internet",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String sendPostDataToInternet(String id)
    {

      /* 建立HTTP Post連線 */

        HttpPost httpRequest = new HttpPost(uriAPI);

      /*

       * Post運作傳送變數必須用NameValuePair[]陣列儲存

       */

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", id));
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
        String Id=null;
        // 建構子，設定要傳的字串

        public sendPostRunnable(String id)
        {
            this.Id=id;
        }
        @Override
        public void run()
        {
            String result = sendPostDataToInternet(String.valueOf(Owner_data.getId()));
            mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
        }

    }
    private boolean isConnected() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
    private void showConnection(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        showToast(isConnected);
    }
    private void showToast(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Good! Connected to Internet";
        } else {
            message = "Sorry! Not connected to internet";
        }
        Toast.makeText(Settings.this, message, Toast.LENGTH_LONG).show();
    }
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    public void onNetworkConnectionChanged(boolean isConnected) {
        showToast(isConnected);
    }
}
