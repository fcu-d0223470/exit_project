package jacky.androidprogram.subpages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.Have;
import jacky.androidprogram.Database.Photo;
import jacky.androidprogram.Database.Story;
import jacky.androidprogram.Database.Task;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.subpages.Common.Base;
import jacky.androidprogram.subpages.Common.ConnectivityReceiver;
import jacky.androidprogram.subpages.Common.GPSTracker;

public class PackQuestDetail extends Base {

    String data = "";
    DBHandler db;
    User Owner_data;
    Task task_choosed;
    Have testhave;
    static Story storylist[];
    static Have havelist[];
    Have temp[];
    TextView tv_details;
    String test;
    Button getGPS_btn,getTask_btn,test_button,qrScan;
    GPSTracker gps;
    ImageView taskimg;
    TextView tname,tintro;
    ListView storylistview;
    Timer timer;
    TimerTask doTask;
    Handler timerhandler;
    double latitude,longitude;
    MyTimer myTimer;
    conDB2 writedb;
    View basebtn;
    double RADIUS = 0.001;
    int Selected,SelectedStorySerial;
    int i=0;
    private static final int CAMERA_REQUEST = 1888;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_quest_detail);

        timerhandler = new Handler();
        int i=0;

        db = new DBHandler(this);
        gps=new GPSTracker(this);
        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        Owner_data=db.getUserbyid(bundle.getInt("user_id"));
        task_choosed=db.getTaskbyId(String.valueOf(bundle.getInt("task_id")));
        List<Story> stories = db.getAllStorybyTaskId(String.valueOf(task_choosed.getTask_number()));
        storylist = new Story[stories.size()];
        for (Story story : stories)
        {
            storylist[i] = story;
            i++;
        }
        i=0;
        List<Have> haves = db.getAllHavebyIdOnTask(Owner_data.getId(),(task_choosed.getTask_number()));
        havelist = new Have[haves.size()];
        temp = new Have[haves.size()];
        for (Have have : haves)
        {
            havelist[i] = have;
            temp[i]=havelist[i].CopyOf();
            Log.i("<<<<<<Asset Code>>>>> ",
                    "<<<<Bar Code>>> " + have.getHave_number()+have.getHave_story_part()+have.getHave_story_flag());
            i++;
        }
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(PackQuestDetail.this));

        getTask_btn = (Button)findViewById(R.id.packdetail_gettaskb);
        test_button = (Button)findViewById(R.id.packdetail_test);
        qrScan=(Button)findViewById(R.id.packdetail_qr) ;
        taskimg = (ImageView)findViewById(R.id.packdetail_image);
        tname=(TextView)findViewById(R.id.packdetail_name);
        tintro=(TextView)findViewById(R.id.packdetail_intro);
        storylistview=(ListView)findViewById(R.id.packdetail_slist);
        super.mappingWidgets(Owner_data);

        File imgFile = new  File(task_choosed.getTask_picture_sd());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            taskimg.setImageBitmap(myBitmap);
        }
        //ImageLoader.getInstance().displayImage(task_choosed.getTask_picture_sd(), taskimg);
        if(task_choosed.getTask_name().length()>8)tname.setText(task_choosed.getTask_name().substring(0,8)+"...");
        else tname.setText(task_choosed.getTask_name());
        if(task_choosed.getTask_introduction().length()>60)tintro.setText(task_choosed.getTask_introduction().substring(0,60)+"...");
        else tintro.setText(task_choosed.getTask_introduction());

        getTask_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Have> haves = db.getAllHavebyIdOnTask(Owner_data.getId(),(task_choosed.getTask_number()));
                if(getTask_btn.getText().equals("unpack it"))
                {
                    for (Have have : haves)
                    {
                        have.setHave_flag(0);
                        db.updateHaveWeb(have);
                        getTask_btn.setText("pack it");
                    }
                    for(int i =0;i<havelist.length;i++){
                        havelist[i].setHave_flag(0);
                    }
                }else{
                    for (Have have : haves)
                    {
                        have.setHave_flag(1);
                        db.updateHaveWeb(have);
                        getTask_btn.setText("unpack it");
                    }
                    for(int i =0;i<havelist.length;i++){
                        havelist[i].setHave_flag(1);
                    }
                }
            }
        });
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+task_choosed.getTask_block()+"z=10");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
        qrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackQuestDetail.this, BarcodeScanner.class);
                Bundle bundle=new Bundle();
                bundle.putInt("task_id",task_choosed.getTask_number());
                intent.putExtras(bundle);
                startActivityForResult(intent, 10001);
            }
        });
        super.onClick(basebtn);

    }
    @Override
    protected void onStart() {
        super.onStart();
        ((ListView) storylistview).setAdapter(new ImageAdapter(PackQuestDetail.this));
        storylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Selected=position;
                if(havelist[position].getHave_story_flag()==1){
                    AlertDialog.Builder AD = new AlertDialog.Builder(PackQuestDetail.this);
                    String result = "";
                    result = storylist[position].getStory_data();
                    AD.setTitle(storylist[position].getStory_name());
                    AD.setMessage(result);
                    AD.show();
                }
                else {
                    if(gps.canGetLocation())
                    {
                        //new ClickUnlock().execute();
                        GetFlag(Selected);
                        if(havelist[Selected].getHave_story_flag()==0)Toast.makeText(PackQuestDetail.this,"Story not found, please try again",Toast.LENGTH_LONG).show();
                        else {
                            storylistview.removeAllViewsInLayout();
                            ((ListView) storylistview).setAdapter(new ImageAdapter(PackQuestDetail.this));
                            AlertDialog.Builder AD = new AlertDialog.Builder(PackQuestDetail.this);
                            String result = "";
                            result = "Story Found!";
                            AD.setTitle(storylist[position].getStory_name());
                            AD.setMessage(result);
                            AD.show();
                            if(IsTaskDone()){
                                result = "Congratulation! Task Accomplish!";
                                AD.setTitle(task_choosed.getTask_name());
                                AD.setMessage(result);
                                AD.show();
                            }
                        }

                    } else {
                        gps.showSettingsAlert();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        gps=new GPSTracker(PackQuestDetail.this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        PackQuestDetail.AnimateFirstDisplayListener.displayedImages.clear();
    }

    private class ImageAdapter extends BaseAdapter {

        //private static final String[] IMAGE_URLS = Constants.IMAGES;

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
            return storylist.length;
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
            final PackQuestDetail.ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                holder = new PackQuestDetail.ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.textaddr = (TextView) view.findViewById(R.id.textaddr);
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.overflow = (ImageView)view.findViewById(R.id.overflow);
                view.setTag(holder);
            } else {
                holder = (PackQuestDetail.ViewHolder) view.getTag();
            }

            if(storylist[position].getStory_name().length()>6)holder.text.setText(storylist[position].getStory_name().substring(0,6)+"...");
            else holder.text.setText(storylist[position].getStory_name());

            if(storylist[position].getStory_address().length()>15)holder.textaddr.setText(storylist[position].getStory_address().substring(0,15)+"...");
            else holder.textaddr.setText(storylist[position].getStory_address());

            File imgFile = new  File(storylist[position].getStory_picture_sd());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.image.setImageBitmap(myBitmap);
            }

            if(havelist[position].getHave_story_flag()==0){//black and white
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.image.setColorFilter(filter);
            }

            if(havelist[position].getHave_story_flag()==1)holder.text.setText(holder.text.getText()+" "+PackQuestDetail.this.getResources().getString(R.string.complete));
            //ImageLoader.getInstance().displayImage(storylist[position].getStory_picture_sd(), holder.image, options, animateFirstListener);
            //ImageLoader.getInstance().displayImage("http://www.ebizlearning.com.my/web/picture/2/1/main.jpg", holder.image, options, animateFirstListener);
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Selected=position;
                    if(havelist[position].getHave_story_flag()==1)showPopupMenu(holder.overflow);
                    //else Toast.makeText(PackQuestDetail.this,"Story Lock",Toast.LENGTH_LONG).show();
                    else showLockPopupMenu(holder.overflow);
                    //Toast.makeText(PackQuestDetail.this,"Overflow selected!!!",Toast.LENGTH_SHORT).show();
                }
            });
            return view;

        }

    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(PackQuestDetail.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int position=Selected;
            switch (menuItem.getItemId()) {
                case R.id.action_camera:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    return true;
                case R.id.action_my_album:
                    if(db.hasAlbum(storylist[position].getStory_serial_no())){
                        Intent Q = new Intent(PackQuestDetail.this,StoryPhoto.class);
                        Bundle bundle=new Bundle();
                        bundle.putLong("photo_serial",storylist[position].getStory_serial_no());
                        Q.putExtras(bundle);
                        Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(Q);
                    }
                    else {
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PackQuestDetail.this);
                        // set title
                        alertDialogBuilder.setTitle("No Album Detected");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Create new album?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Owner_data.setUser_phone_id("");
                                        db.updateUser(Owner_data);
                                        Intent Q = new Intent(PackQuestDetail.this,CustomPhotoGalleryActivity.class);
                                        Bundle bundle=new Bundle();
                                        bundle.putLong("photo_serial",storylist[position].getStory_serial_no());
                                        Q.putExtras(bundle);
                                        Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(Q);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                    //Bundle bundle=new Bundle();
                    //bundle.putInt("user_id",Owner_data.getId());
                    //Q.putExtras(bundle);
                    //String username = getIntent().getExtras().getString("owner");
                    //Q.putExtra("owner",username);
                    //startActivity(Q);
                    return true;
                case R.id.action_add_photo:
                    Intent Q = new Intent(PackQuestDetail.this,CustomPhotoGalleryActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putLong("photo_serial",storylist[position].getStory_serial_no());
                    Q.putExtras(bundle);
                    Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(Q);
                    return true;
                default:
            }
            return false;
        }
    }

    private void showLockPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(PackQuestDetail.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_lock_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyLockMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyLockMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyLockMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int position=Selected;
            switch (menuItem.getItemId()) {

                case R.id.unlock_story:
                    if(gps.canGetLocation())
                    {//new ClickUnlock().execute();
                        GetFlag(Selected);
                        if(havelist[Selected].getHave_story_flag()==0)Toast.makeText(PackQuestDetail.this,"Story not found, please try again",Toast.LENGTH_LONG).show();
                        else {
                            storylistview.removeAllViewsInLayout();
                            ((ListView) storylistview).setAdapter(new ImageAdapter(PackQuestDetail.this));
                            AlertDialog.Builder AD = new AlertDialog.Builder(PackQuestDetail.this);
                            String result = "";
                            result = "Story Found!";
                            AD.setTitle(storylist[position].getStory_name());
                            AD.setMessage(result);
                            AD.show();
                            if(IsTaskDone()){
                                result = "Congratulation! Task Accomplish!";
                                AD.setTitle(task_choosed.getTask_name());
                                AD.setMessage(result);
                                AD.show();
                            }
                        }
                    } else {
                        gps.showSettingsAlert();
                    }
                    return true;
                //////////////////////////////////////////////////////////////////////////////
                case R.id.location_hint:
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=loc:"+storylist[Selected].getStory_latitude()+","+storylist[Selected].getStory_longtitute()+"("+storylist[Selected].getStory_name()+")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }

                    return true;
//////////////////////////////////////////////////////////////////////////
                default:
            }
            return false;
        }
    }

    static class ViewHolder {
        TextView text;
        TextView textaddr;
        ImageView image;
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

    public class MyTimer extends TimerTask {

        @Override
        public void run() {
            timerhandler.post(new Runnable() {
                @Override
                public void run() {
                    try{
                        //method
                        gps = new GPSTracker(PackQuestDetail.this);
                        // check if GPS enabled
                        //if(gps.canGetLocation()){

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude +
                                "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Finding Location is - \nLat: " + storylist[0].getStory_latitude() +
                                "\nLong: " + storylist[0].getStory_longtitute(), Toast.LENGTH_SHORT).show();

                        if(i<storylist.length){
                            if(latitude<=Double.valueOf(storylist[i].getStory_latitude())+RADIUS
                                    &&latitude>=Double.valueOf(storylist[i].getStory_latitude())-RADIUS
                                    &&longitude<=Double.valueOf(storylist[i].getStory_longtitute())+RADIUS
                                    &&longitude>=Double.valueOf(storylist[i].getStory_longtitute())-RADIUS){

                                //Toast.makeText(getApplicationContext(), storylist[i].getStory_name()+" Location is FOUND!!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), storylist[i].getStory_part()+" part ======== have "+havelist[i].getHave_story_part(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), temp[i].getHave_story_flag()+" temp ======== have "+havelist[i].getHave_story_flag(), Toast.LENGTH_SHORT).show();

                                if(storylist[i].getStory_part()==havelist[i].getHave_story_part()){
                                    havelist[i].setHave_story_flag(1);
                                    Toast.makeText(getApplicationContext(), "have::"+havelist[i].getHave_story_flag()+"::temp::"+temp[i].getHave_story_flag(), Toast.LENGTH_SHORT).show();
                                    if(havelist[i].getHave_story_flag()!=temp[i].getHave_story_flag()){
                                        storylistview.removeAllViewsInLayout();
                                        ((ListView) storylistview).setAdapter(new ImageAdapter(PackQuestDetail.this));
                                        db.updateHaveFlag(havelist[i]);
                                        temp[i].setHave_story_flag(1);
                                        Toast.makeText(getApplicationContext(), "DB UPDATE::"+havelist[i].getHave_story_flag()+"::"+havelist[i].getHave_story_flag(), Toast.LENGTH_SHORT).show();
                                    }
                                    else Toast.makeText(getApplicationContext(),"Already UPDATED!",Toast.LENGTH_SHORT).show();
                                }
                            }i++;
                        }else{i=0;}

                    }catch (Exception e){

                    }
                }
            });
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        if(timer != null) {
            myTimer.cancel();
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if(checkConnection()){
            new PostToWeb().execute();
        }
        Intent i = new Intent(PackQuestDetail.this, Packlist.class);
        Bundle bundle=new Bundle();
        bundle.putInt("user_id",Owner_data.getId());
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
            UnlockFlag();
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            //((ListView) storylistview).setAdapter(new QuestDetails.ImageAdapter(PackQuestDetail.this));
            //if (pDialog.isShowing()) pDialog.dismiss();
        }

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

    public class ClickUnlock extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(PackQuestDetail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {
            GetFlag(Selected);
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();
        }

    }

    public final void GetFlag (int position){
        gps = new GPSTracker(PackQuestDetail.this);

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        if(latitude<=Double.valueOf(storylist[position].getStory_latitude())+RADIUS
                &&latitude>=Double.valueOf(storylist[position].getStory_latitude())-RADIUS
                &&longitude<=Double.valueOf(storylist[position].getStory_longtitute())+RADIUS
                &&longitude>=Double.valueOf(storylist[position].getStory_longtitute())-RADIUS){

            havelist[position].setHave_story_flag(1);
            db.updateHaveFlag(havelist[position]);
        }

    }

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
        Toast.makeText(PackQuestDetail.this, message, Toast.LENGTH_LONG).show();
    }

    public void onNetworkConnectionChanged(boolean isConnected) {
        showToast(isConnected);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == PackQuestDetail.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Toast.makeText(PackQuestDetail.this,String.valueOf(data.getExtras().get("data")),Toast.LENGTH_LONG).show();
            //imageView.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));
            String FileDir = getOriginalImagePath();
            Toast.makeText(PackQuestDetail.this,"Photo added to album",Toast.LENGTH_LONG).show();
            Photo photo_take;
            photo_take=new Photo(storylist[Selected].getStory_serial_no(),FileDir);

            if(db.PhotoBelongsToAlbum(FileDir,Selected)){//add to db
                db.updatePhoto(photo_take);
            }else{
                db.createPhoto(photo_take);
            }

            //System.out.println(mImageCaptureUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getOriginalImagePath() {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = PackQuestDetail.this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
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

}