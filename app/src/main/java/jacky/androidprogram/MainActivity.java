package jacky.androidprogram;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.Have;
import jacky.androidprogram.Database.Story;
import jacky.androidprogram.Database.Task;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.subpages.Common.BasicImageDownloader;
import jacky.androidprogram.subpages.Common.ConnectivityReceiver;
import jacky.androidprogram.subpages.Common.MyApplication;
import jacky.androidprogram.subpages.Signup;
import jacky.androidprogram.subpages.forgot_password;
import jacky.androidprogram.subpages.home;

import static jacky.androidprogram.R.id.user;


public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener,PermissionCallback,ErrorCallback {

    String data = "";
    String domain = "http://www.ebizlearning.com.my/web";
    String phoneDIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TreasureHunt/";
    private static final int REQUEST_PERMISSIONS = 20;
    DBHandler db;
    String pid="";
    User user_back;
    TextView userview;
    EditText userli, passli;
    String username,userpass;
    Intent intent;
    ImageView loginimg;
    conDB2 getdb;
    JSONObject json_data;
    User j_user;
    Have j_have;
    Task j_task;
    Story j_story;
    Have owner_havelist[];
    int loginsucess=0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //CHECK API
        int currentapiVersion = Build.VERSION.SDK_INT;

        userview = (TextView)findViewById(R.id.userview);
        userli = (EditText) findViewById(user);
        passli = (EditText) findViewById(R.id.pass);
        intent = new Intent(MainActivity.this, home.class);
        pid=Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings
                .Secure.ANDROID_ID) + Build.SERIAL;

        loginimg = (ImageView) findViewById(R.id.loginimg);
        loginimg.setImageResource(R.drawable.f1425630124689a);

        db = new DBHandler(this);

        Button login,signup,forgot;
        login = (Button)findViewById(R.id.loginbt);
        signup = (Button)findViewById(R.id.signuppg);
        forgot = (Button)findViewById(R.id.main_forgotbt);

        userview.setText(passli.getText().toString());

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username=userli.getEditableText().toString();
                userpass=passli.getEditableText().toString();
                List<User> users = db.getAllUserFull();

                if (userli.length()==0) userli.setError("User is blank.");
                else if (passli.length()==0) passli.setError("Password is blank.");
                else if (db.isUserempty()&& (!checkConnection())) {
                        Toast.makeText(MainActivity.this, "Please connect to Internet to update your data.", Toast.LENGTH_LONG).show();
                        showToast(checkConnection());
                    }
                else if(!checkConnection()) {
                    //Toast.makeText(MainActivity.this, "UserN: " + userli.getEditableText().toString(), Toast.LENGTH_LONG).show();
                    for (User user : users){
                    if (pid.equals(user.getUser_phone_id())&&userli.getEditableText().toString().equals(user.getUsername())&&passli.getEditableText().toString().equals(user.getUserpass()))
                        {
                                intent.setClass(MainActivity.this, home.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("user_id", db.getUserbyName(userli.getEditableText().toString()).getId());
                                intent.putExtras(bundle);
                                startActivity(intent);
                        } else Toast.makeText(MainActivity.this, "Connect to internet to update your data", Toast.LENGTH_LONG).show();
                    }
                }
                else if(checkConnection())
                {
                    new SignIn().execute();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()==true){
                    intent.setClass(MainActivity.this, Signup.class);
                    startActivity(intent);}
                else {showToast(checkConnection());}
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()==true) {
                    intent.setClass(MainActivity.this, forgot_password.class);
                    startActivity(intent);
                }else{showToast(checkConnection());}
            }
        });

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                reqPermission();
            }
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkConnection() {
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
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void onNetworkConnectionChanged(boolean isConnected) {
        showToast(isConnected);
    }

    public class SignIn extends AsyncTask<Void,Void,Void>{
        ArrayList<User> users;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            data = getdb.executeQuery("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/user_signint.php", username, userpass, pid);
            users=parseJSON(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            new PostToWeb().execute();
            List<User> users = db.getAllUserFull();
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();
            for (User user : users) {
                System.out.println(user.getUsername());
                if (user.getUsername().equals(username)&&user.getUserpass().equals(userpass)&&user.getUser_phone_id().equals(pid)) {
                    intent.setClass(MainActivity.this, home.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id", user.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    loginsucess=1;
                }
            }
            if(loginsucess==0)Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public final ArrayList<User>parseJSON(String result) {

        boolean gottask=false,gotstory=false;
        ArrayList<User> users = new ArrayList<User>();

        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONArray juser, jhave, jtask = null, jstory = null;

            juser = jsonObject.getJSONArray("user");
            jhave = jsonObject.getJSONArray("have");

            if(jsonObject.isNull("task")){}
            else {jtask = jsonObject.getJSONArray("task");gottask = true;}

            if(jsonObject.isNull("story")){}
            else {jstory = jsonObject.getJSONArray("story");gotstory = true;}


            for (int i = 0; i < jhave.length(); i++) {//have add,update
                JSONObject json_data = jhave.getJSONObject(i);
                if (!db.hasUser(username)||db.getUserbyName(username).getPhone().isEmpty()) {
                    j_have = new Have(
                            json_data.getString("have_id"),
                            json_data.getInt("have_number"),
                            json_data.getInt("have_story_part"),
                            json_data.getInt("have_flag"),
                            json_data.getInt("have_task_flag"),
                            json_data.getInt("have_story_flag"),
                            json_data.getString("have_date"));
                    if (db.hasHave(json_data.getString("have_id"), json_data.getString("have_number"), json_data.getString("have_story_part"))) {
                        db.updateHaveFlag(j_have);
                    } else {
                        db.createHave(j_have);
                    }
                }
                else {
                    j_have = new Have(json_data.getString("have_id"),
                            json_data.getInt("have_number"),
                            json_data.getInt("have_story_part"),
                            json_data.getInt("have_flag"));
                    if (db.hasHave(json_data.getString("have_id"), json_data.getString("have_number"), json_data.getString("have_story_part"))) {
                        db.updateHaveWeb(j_have);
                    } else {
                        db.createHave(j_have);
                    }
                }

            }
            for (int i = 0; i < juser.length(); i++) {//user add,update
                JSONObject json_data = juser.getJSONObject(i);
                j_user = new User(json_data.getInt("user_id"),
                        json_data.getString("user_account"),
                        json_data.getString("user_password"),
                        json_data.getString("user_fname"),
                        json_data.getString("user_lname"),
                        json_data.getString("user_email"),
                        json_data.getString("user_cellphone"),
                        json_data.getString("user_sex"),
                        json_data.getString("user_address"),
                        json_data.getString("user_phone_number"),
                        json_data.getString("user_date"));

                if (db.hasUser(json_data.getString("user_account"))) {
                    db.updateUserwithId(j_user.getId(), j_user);
                } else {
                    db.createUser(j_user);
                }

            }
            if (gottask) {
                for (int i = 0; i < jtask.length(); i++) {//task add,update
                    JSONObject json_data = jtask.getJSONObject(i);
                    if (json_data.getString("task_name").equals(null)) {}
                    else {
                        j_task = new Task(json_data.getInt("task_number"),
                                json_data.getString("task_name"),
                                json_data.getString("task_block"),
                                json_data.getInt("task_type"),
                                domain + json_data.getString("task_picture").substring(2),
                                phoneDIR+"/"+json_data.getInt("task_number")+"/main.jpeg",
                                json_data.getString("task_Introduction"),
                                json_data.getInt("task_part"),
                                json_data.getInt("task_hot"),
                                json_data.getString("task_developer"),
                                json_data.getInt("task_show"),
                                json_data.getString("task_date"));
                        DownloadImageToPhone(j_task.getTask_number(),"main",j_task.getTask_picture());
                        if (db.hasTask(json_data.getString("task_name"))) {
                            db.updateTaskWeb(j_task);
                        } else {
                            db.createTaskWeb(j_task);
                        }
                    }

                }
            } else {
            }

            if (gotstory){
                for (int i = 0; i < jstory.length(); i++) {//story add,update
                    JSONObject json_data = jstory.getJSONObject(i);
                    if (json_data.getString("story_name").equals(null)) {}
                    else {
                        j_story = new Story(json_data.getInt("story_serial_number"),
                                json_data.getInt("story_number"),
                                json_data.getString("story_name"),
                                json_data.getString("story_address"),
                                json_data.getString("story_longitude"),
                                json_data.getString("story_latitude"),
                                domain + json_data.getString("story_picture").substring(2),
                                phoneDIR+json_data.getInt("story_number")+"/"+json_data.getInt("story_part")+".jpeg",
                                json_data.getString("story_data"),
                                json_data.getInt("story_part"),
                                json_data.getInt("story_show"),
                                json_data.getString("story_date"));
                        DownloadImageToPhone(j_story.getStory_number(),String.valueOf(j_story.getStory_part()),j_story.getStory_picture());
                        if (db.hasStory(json_data.getString("story_name"))) {
                            db.updateStoryWeb(j_story);
                        } else {
                            db.createStoryWeb(j_story);
                        }
                    }

                }
        }
            else{}

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString() + result);
        }
        return users;
    }

    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public class PostToWeb extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            UnlockFlag();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();
        }

    }

    public final void UnlockFlag (){
        int count=0,i=0;
        List<Have> haves = db.getAllHavebyId(db.getUserbyName(username).getId());
        owner_havelist = new Have[haves.size()];
        for (Have have : haves)
        {
            owner_havelist[i] = have;
            System.out.println(i+"::"+owner_havelist[i].getHave_story_part()+"::"+owner_havelist[i].getHave_story_flag());
            i++;
        }
        for(i=0;i<owner_havelist.length;i++){//Update to web
            getdb.executeUpdateHave(
                    "",
                    "http://www.ebizlearning.com.my/web/phone/update_flag.php",
                    String.valueOf(owner_havelist[i].getHave_id()),
                    String.valueOf(owner_havelist[i].getHave_number()),
                    String.valueOf(owner_havelist[i].getHave_story_part()),
                    String.valueOf(owner_havelist[i].getHave_flag()),
                    String.valueOf(owner_havelist[i].getHave_task_flag()),
                    String.valueOf(owner_havelist[i].getHave_story_flag()));
        }
    }

    public void DownloadImageToPhone(int TaskNo, String file_name, String url){

        BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Toast.makeText(MainActivity.this, "Error code " + error.getErrorCode() + ": " +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }

            @Override
            public void onProgressChange(int percent) {
            }

            @Override
            public void onComplete(Bitmap result) {
                        /* save the image - I'm gonna use JPEG */
                final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                        /* don't forget to include the extension into the file name */
                final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "TreasureHunt" + File.separator + TaskNo + File.separator + file_name + "." + mFormat.name().toLowerCase());
                //File.separator + "image_test" + File.separator + name + "." + mFormat.name().toLowerCase());
                BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        //Toast.makeText(QuestDetails.this, "Image saved as: " + myImageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        //Web2Sd=myImageFile.getAbsolutePath();
                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        Toast.makeText(MainActivity.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }, mFormat, true);
            }
        });

        downloader.download(url, true);
    }

    ///////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void reqPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            new AskPermission.Builder(this).setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .setCallback(this)
                    .setErrorCallback(this)
                    .request(REQUEST_PERMISSIONS);
        }
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            new AskPermission.Builder(this).setPermissions(
                    Manifest.permission.CAMERA)
                    .setCallback(this)
                    .setErrorCallback(this)
                    .request(REQUEST_PERMISSIONS);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            new AskPermission.Builder(this).setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    .setCallback(this)
                    .setErrorCallback(this)
                    .request(REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
    }

}
