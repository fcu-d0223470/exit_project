package jacky.androidprogram.subpages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.Have;
import jacky.androidprogram.Database.Story;
import jacky.androidprogram.Database.Task;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.subpages.Common.Base;
import jacky.androidprogram.subpages.Common.BasicImageDownloader;
import jacky.androidprogram.subpages.questfragment.Array_StoryList;
import jacky.androidprogram.subpages.questfragment.Array_TaskList;

public class QuestDetails extends Base {

    int User,Selected;
    User Owner_data;
    DBHandler db;
    String data="";
    String domain = "http://www.ebizlearning.com.my/web";
    String phoneDIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TreasureHunt/";

    int Owner_have=0;
    Array_TaskList TaskSelected;
    static Array_StoryList[] StoryList;
    static String[] Storyurl;

    ImageView taskimg;
    TextView tname,tintro;
    ListView storylist;
    Button getTaskButton;
    View basebtn;
    String Web2Sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_details);
        taskimg=(ImageView)findViewById(R.id.taskdetail_image);
        tname=(TextView)findViewById(R.id.taskdetail_name);
        tintro=(TextView)findViewById(R.id.taskdetail_intro);
        storylist=(ListView)findViewById(R.id.taskdetail_slist);
        getTaskButton=(Button)findViewById(R.id.taskdetail_gettaskb);

        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        User=bundle.getInt("User_id");
        Selected=bundle.getInt("Task_id");
        db=new DBHandler(this);
        Owner_data = db.getUserbyid(User);
        super.mappingWidgets(Owner_data);
        //Owner_data=db.getUserbyid(bundle.getInt("user_id"));
        if(db.hasHave(String.valueOf(Owner_data),String.valueOf(Selected),"1")) {
            if (db.getHavebyId(String.valueOf(Owner_data.getId()), String.valueOf(Selected)).getHave_flag() == 0)
                Owner_have = 1;
        }
        new getTaskDetail().execute();

        getTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Owner_have==0){
                    new getTasktoDB().execute();
                }
                else{
                    new conDB2().executeTasktoDB("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/get_task.php",String.valueOf(Owner_data.getId()),String.valueOf(TaskSelected.getTask_number()),"0");
                    List<Have> haves = db.getAllHavebyId(User);
                    for (Have have : haves){
                        if(have.getHave_number()==TaskSelected.getTask_number())
                        {
                            have.setHave_flag(0);
                            db.updateHaveWeb(have);
                        }
                    }
                    Toast.makeText(QuestDetails.this,"Unpack Task!",Toast.LENGTH_SHORT).show();
                    Owner_have=0;
                    getTaskButton.setText("Pack it");
                    getTaskButton.setTextColor(Color.WHITE);
                    getTaskButton.setBackgroundColor(Color.rgb(255,84,0));//orange
                }

            }
        });
        super.onClick(basebtn);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        AnimateFirstDisplayListener.displayedImages.clear();
    }

    public class getTasktoDB extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog;
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(QuestDetails.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            data=new conDB2().executeTasktoDB("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/get_task.php",String.valueOf(Owner_data.getId()),String.valueOf(TaskSelected.getTask_number()),"1");
            parseJSONtoDB(data);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            //ImageLoader.getInstance().destroy();
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();
            Toast.makeText(QuestDetails.this,"Added Task!",Toast.LENGTH_SHORT).show();

            Owner_have=1;
            getTaskButton.setText("Unpack it");
            getTaskButton.setTextColor(Color.WHITE);
            getTaskButton.setBackgroundColor(Color.GRAY);
        }
    }

    public final void parseJSONtoDB (String result){
        int i;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jdata;

            JSONArray gethave,gettask,getstory;
            gethave=jsonObject.getJSONArray("have");
            gettask=jsonObject.getJSONArray("task");
            getstory=jsonObject.getJSONArray("story");

            jdata=gettask.getJSONObject(0);
            Task task = new Task(
                    jdata.getInt("task_number"),
                    jdata.getString("task_name"),
                    jdata.getString("task_block"),
                    jdata.getInt("task_type"),
                    domain+jdata.getString("task_picture").substring(2),
                    phoneDIR+"/"+jdata.getInt("task_number")+"/main.jpeg",
                    jdata.getString("task_Introduction"),
                    jdata.getInt("task_part"),
                    jdata.getInt("task_hot"),
                    jdata.getString("task_developer"),
                    jdata.getInt("task_show"),
                    jdata.getString("task_date"));
            DownloadImageToPhone(Selected,"main",task.getTask_picture());
            //task.setTask_picture_sd(Web2Sd);
            if(db.hasTask(jdata.getString("task_name")))
            {
                db.updateTaskWeb(task);
            }else {
                db.createTaskWeb(task);
            }
            for ( i = 0; i < gethave.length(); i++) {//have add,update
                jdata=gethave.getJSONObject(i);
                Have have = new Have(
                                jdata.getString("have_id"),
                                jdata.getInt("have_number"),
                                jdata.getInt("have_story_part"),
                                jdata.getInt("have_flag"));
                if(db.hasHave(jdata.getString("have_id"),jdata.getString("have_number"),jdata.getString("have_story_part")))
                {
                    db.updateHaveWeb(have);
                }else {
                        db.createHave(have);
                }
            }

            for ( i = 0; i < getstory.length(); i++) {//story add,update
                jdata=getstory.getJSONObject(i);
                Story story = new Story(
                        jdata.getInt("story_serial_number"),
                        jdata.getInt("story_number"),
                        jdata.getString("story_name"),
                        jdata.getString("story_address"),
                        jdata.getString("story_longitude"),
                        jdata.getString("story_latitude"),
                        domain+jdata.getString("story_picture").substring(2),
                        phoneDIR+Selected+"/"+jdata.getInt("story_part")+".jpeg",
                        jdata.getString("story_data"),
                        jdata.getInt("story_part"),
                        jdata.getInt("story_show"),
                        jdata.getString("story_date"));
                DownloadImageToPhone(Selected,String.valueOf(story.getStory_part()),story.getStory_picture());
                //story.setStory_picture_sd(Web2Sd);
                if(db.hasStory(jdata.getString("story_name")))
                {
                    db.updateStoryWeb(story);
                }else {
                    db.createStoryWeb(story);
                }
            }

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public class getTaskDetail extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(QuestDetails.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            data=new conDB2().executeTaskDetail("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/task_detail.php",String.valueOf(User) ,String.valueOf(Selected));
            parseJSON(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //ImageLoader.getInstance().destroy();
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();

            if(TaskSelected.getTask_name().length()>8)tname.setText(TaskSelected.getTask_name().substring(0,8)+"...");
            else tname.setText(TaskSelected.getTask_name());
            if(TaskSelected.getTask_introduction().length()>60)tintro.setText(TaskSelected.getTask_introduction().substring(0,60)+"...");
            else tintro.setText(TaskSelected.getTask_introduction());

            ImageLoader.getInstance().displayImage(TaskSelected.getTask_picture(), taskimg);
            ((ListView) storylist).setAdapter(new ImageAdapter(QuestDetails.this));

            if(Owner_have==1){
                getTaskButton.setText("Unpack it");
                getTaskButton.setTextColor(Color.WHITE);
                getTaskButton.setBackgroundColor(Color.GRAY);
            } else{
                getTaskButton.setText("Pack it");
                getTaskButton.setTextColor(Color.WHITE);
                getTaskButton.setBackgroundColor(Color.rgb(255,84,0));//orange
            }
        }
    }

    public final void parseJSON (String result){
        int i;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jdata;
            JSONArray have,task,story;
            have=jsonObject.getJSONArray("have");
            task=jsonObject.getJSONArray("task");
            story=jsonObject.getJSONArray("story");

            jdata=have.getJSONObject(0);
            Owner_have = jdata.getInt("have_flag");

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

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    private static class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private DisplayImageOptions options;

        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
        }

        @Override
        public int getCount() {
            return Storyurl.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.textaddr = (TextView) view.findViewById(R.id.textaddr);
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.overflow = (ImageView)view.findViewById(R.id.overflow);
                holder.overflow.setVisibility(View.INVISIBLE);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if(StoryList[position].getStory_name().length()>8)holder.text.setText(StoryList[position].getStory_name().substring(0,8)+"...");
            else holder.text.setText(StoryList[position].getStory_name());

            if(StoryList[position].getStory_address().length()>20)holder.textaddr.setText(StoryList[position].getStory_address().substring(0,20)+"...");
            else holder.textaddr.setText(StoryList[position].getStory_address());

            ImageLoader.getInstance().displayImage(Storyurl[position], holder.image, options, animateFirstListener);

            return view;
        }
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
        TextView textaddr;
        ImageView overflow;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public void DownloadImageToPhone(int TaskNo, String file_name, String url){

         BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                Toast.makeText(QuestDetails.this, "Error code " + error.getErrorCode() + ": " +
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
                        Toast.makeText(QuestDetails.this, "Error code " + error.getErrorCode() + ": " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }, mFormat, true);
            }
        });

        downloader.download(url, true);
    }


}
