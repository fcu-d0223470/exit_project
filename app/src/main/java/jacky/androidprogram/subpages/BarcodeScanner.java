package jacky.androidprogram.subpages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.Have;
import jacky.androidprogram.Database.Story;
import jacky.androidprogram.Database.Task;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.subpages.questfragment.Array_StoryList;
import jacky.androidprogram.subpages.questfragment.Array_TaskList;
import me.dm7.barcodescanner.core.CameraPreview;

public class BarcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    CameraPreviews mPreview;
    private Handler autoFocusHandler;

    private Button scanButton;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    String ScannerResult="";
    String data="";
    String getReward="星享道歡迎您入住,獲得\n50店家兌換卷 *1\n50 積分 *1";
    DBHandler db;
    Have get_Have;
    User temp;
    String domain = "http://www.ebizlearning.com.my/web";
    Array_TaskList TaskSelected;
    Task task_choosed;
    conDB2 writedb;
    static Array_StoryList[] StoryList;
    static String[] Storyurl;
    static Have havelist[];
    Have temphave[];
    int taskId,storyId;

    private String uriAPI = "http://www.ebizlearning.com.my/web/phone/update_flag.php";
    protected static final int REFRESH_DATA = 0x00000001;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();

        db = new DBHandler(this);
        temp=db.getFirstRow();
        task_choosed=db.getTaskbyId(String.valueOf(bundle.getInt("task_id")));
        Log.i("<<<<<<Asset Code>>>>> ",
                "<<<<Bar Code2222>>> ");
        int j=0;
        List<Have> haves = db.getAllHavebyIdOnTask(temp.getId(),(task_choosed.getTask_number()));
        havelist = new Have[haves.size()];
        temphave = new Have[haves.size()];
        for (Have have : haves)
        {
            havelist[j] = have;
            temphave[j]=havelist[j].CopyOf();
            Log.i("<<<<<<Asset Code>>>>> ",
                    "<<<<Bar Code2222>>> " + have.getHave_number()+have.getHave_story_part()+have.getHave_story_flag());
            j++;
        }

        initControls();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreviews(BarcodeScanner.this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button) findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();
                    ScannerResult=String.valueOf(scanResult);
                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + ScannerResult);

                    new PostToWeb().execute();

                    //showAlertDialog(scanResult);
                    //Toast.makeText(BarcodeScanner.this, scanResult,Toast.LENGTH_SHORT).show();

                    /*
                    parseJSON(ScannerResult);

                    if(taskId!=0){
                        Thread s = new Thread(new BarcodeScanner.sendRoomRunnable(String.valueOf(temp.getId())));
                        s.start();
                    }
                    */


                    barcodeScanned = true;

//                    showAlertDialog(getReward);

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage("Unlock "+TaskSelected.getTask_name()+ "\nPart "+StoryList[0].getStory_part()+": "+StoryList[0].getStory_name())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                })

                .show();
    }

    private String sendPostRoomToInternet(String id) {

      /* 建立HTTP Post連線 */

        HttpPost httpRequest = new HttpPost(uriAPI);

      /*

       * Post運作傳送變數必須用NameValuePair[]陣列儲存

       */

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("have_id",id));
        params.add(new BasicNameValuePair("have_number", String.valueOf(taskId)));
        params.add(new BasicNameValuePair("have_flag", "1"));
        params.add(new BasicNameValuePair("have_task_flag", "0"));
        params.add(new BasicNameValuePair("have_story_flag", "1"));
        params.add(new BasicNameValuePair("story_part", String.valueOf(storyId)));
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

    class sendRoomRunnable implements Runnable {
        String Id=null;
        // 建構子，設定要傳的字串

        public sendRoomRunnable(String id)
        {
            this.Id=id;
        }
        @Override
        public void run()
        {
            String result = sendPostRoomToInternet(String.valueOf(temp.getId()));
            data=result;
        }

    }

    public class PostToWeb extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            /*
            pDialog=new ProgressDialog(PackQuestDetail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }
        @Override
        protected Void doInBackground(Void... params) {
            data = writedb.getQuestPiece("SELECT * FROM USER ;",ScannerResult);
            Log.i("<<<<<<Asset Code>>>>> ",
                    "<<<<DATA>>> " + data);
            parseJSON(data);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Toast.makeText(BarcodeScanner.this,"Task: "+task_choosed.getTask_name()+" Story Part: "+StoryList[0].getStory_part(),Toast.LENGTH_LONG).show();
            //((ListView) storylistview).setAdapter(new QuestDetails.ImageAdapter(PackQuestDetail.this));
            //if (pDialog.isShowing()) pDialog.dismiss();
            //get_Have=db.getHavebyData(String.valueOf(temp.getId()),String.valueOf(taskId),String.valueOf(storyId));


            Toast.makeText(BarcodeScanner.this,"::"+
                            String.valueOf(havelist[0].getHave_id())+"::"+
                            String.valueOf(havelist[0].getHave_number())+"::"+
                            String.valueOf(havelist[0].getHave_story_flag())+"::"+
                            String.valueOf(havelist[0].getHave_story_part())+"::"+
                            String.valueOf(havelist[0].getHave_task_flag())+"::"+
                            String.valueOf(havelist[0].getHave_story_flag())+"::",
                    Toast.LENGTH_LONG
            ).show();
            for(int i=0; i<havelist.length;i++){
                if(temphave[i].getHave_story_part()==StoryList[0].getStory_part())
                {
                    get_Have=temphave[i];
                    get_Have.setHave_story_flag(1);
                    db.updateHaveFlag(get_Have);

                    writedb.executeUpdateHave(
                            "SELECT * FROM USER ;",
                            "http://www.ebizlearning.com.my/web/phone/update_flag.php",
                            String.valueOf(get_Have.getHave_id()),
                            String.valueOf(get_Have.getHave_number()),
                            String.valueOf(get_Have.getHave_story_part()),
                            String.valueOf(get_Have.getHave_flag()),
                            String.valueOf(get_Have.getHave_task_flag()),
                            String.valueOf(get_Have.getHave_story_flag()));

                    Toast.makeText(BarcodeScanner.this,"::"+
                            String.valueOf(get_Have.getHave_id())+"::"+
                            String.valueOf(get_Have.getHave_number())+"::"+
                            String.valueOf(get_Have.getHave_story_flag())+"::"+
                            String.valueOf(get_Have.getHave_story_part())+"::"+
                            String.valueOf(get_Have.getHave_task_flag())+"::"+
                            String.valueOf(get_Have.getHave_story_flag())+"::",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }



        }

    }
    public final void parseJSON02 (String result){
        int i;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jdata;
            JSONArray task,story;
            task=jsonObject.getJSONArray("task");
            story=jsonObject.getJSONArray("story");

            jdata=task.getJSONObject(0);
            TaskSelected = new Array_TaskList(
                    jdata.getInt("task_number"),
                    jdata.getString("task_name"),
                    jdata.getString("task_block"),
                    domain+jdata.getString("task_picture").substring(2),
                    jdata.getString("task_Introduction"),
                    jdata.getInt("task_type"),
                    jdata.getInt("task_hot"),
                    jdata.getString("task_date"));

            Array_StoryList[] sl = new Array_StoryList[story.length()];
            String[] urls = new String[story.length()];
            for (i = 0; i < story.length(); i++) {//get tasklist
                jdata=story.getJSONObject(i);
                Array_StoryList Story = new Array_StoryList(
                        jdata.getString("story_name"),
                        jdata.getInt("story_part"),
                        domain+jdata.getString("story_picture").substring(2),
                        jdata.getString("story_address")
                );

                sl[i]=Story;
                urls[i]=Story.getStory_picture();
            }
            taskId=jdata.getInt("task_number");
            storyId=jdata.getInt("story_part");
            StoryList = sl;
            Storyurl=urls;

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }
    public final void parseJSON (String result){
        int i;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jdata;
            JSONArray task,story;
            task=jsonObject.getJSONArray("task");
            story=jsonObject.getJSONArray("story");

            jdata=task.getJSONObject(0);
            TaskSelected = new Array_TaskList(
                    jdata.getInt("task_number"),
                    jdata.getString("task_name"),
                    jdata.getString("task_block"),
                    domain+jdata.getString("task_picture").substring(2),
                    jdata.getString("task_Introduction"),
                    jdata.getInt("task_type"),
                    jdata.getInt("task_hot"),
                    jdata.getString("task_date"));

            Array_StoryList[] sl = new Array_StoryList[story.length()];
            String[] urls = new String[story.length()];
            for (i = 0; i < story.length(); i++) {//get tasklist
                jdata=story.getJSONObject(i);
                Array_StoryList Story = new Array_StoryList(
                        jdata.getString("story_name"),
                        jdata.getInt("story_part"),
                        domain+jdata.getString("story_picture").substring(2),
                        jdata.getString("story_address")
                );
                sl[i]=Story;
                urls[i]=Story.getStory_picture();
            }
            StoryList = sl;
            Storyurl=urls;
            taskId=jdata.getInt("task_number");
            storyId=jdata.getInt("story_part");

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public boolean IsTaskDone(){
        int count=0;
        for(int i=0;i<havelist.length;i++){//Count story flag
            if(havelist[i].getHave_story_flag()==1){
                count+=1;
            }
        }
        if(count==havelist.length){//set Task flag
            for(int i=0;i<havelist.length;i++){
                havelist[i].setHave_task_flag(1);
            }
        }
        if(count==havelist.length)return true;
        else return false;
    }
    public final void UnlockFlag (){
        int count=0;
        for(int i=0;i<havelist.length;i++){//Count story flag
            if(havelist[i].getHave_story_flag()==1){
                count+=1;
            }
        }
        if(count==havelist.length){//set Task flag
            for(int i=0;i<havelist.length;i++){
                havelist[i].setHave_task_flag(1);
            }
        }
        for(int i=0;i<havelist.length;i++){
            writedb.executeUpdateHave(
                    "",
                    "http://www.ebizlearning.com.my/web/phone/update_flag.php",
                    String.valueOf(havelist[i].getHave_id()),
                    String.valueOf(havelist[i].getHave_number()),
                    String.valueOf(havelist[i].getHave_story_part()),
                    String.valueOf(havelist[i].getHave_flag()),
                    String.valueOf(havelist[i].getHave_task_flag()),
                    String.valueOf(havelist[i].getHave_story_flag()));
        }
    }

}

