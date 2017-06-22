package jacky.androidprogram.subpages.questfragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.fragment.AbsListViewBaseFragment;
import jacky.androidprogram.subpages.QuestDetails;

public class tab_fragment_4  extends AbsListViewBaseFragment {

    User Owner_data;
    DBHandler db;
    String data="";
    String domain = "http://www.ebizlearning.com.my/web";
    View rootView;
    Array_TaskList WebList[]=new Array_TaskList[]{new Array_TaskList(0,"Testing","","",0,0,"")};
    Array_TaskList cWebList[]=new Array_TaskList[]{new Array_TaskList(0,"Changed","","",0,0,"")};
    String imageweb[]=new String[]{""};
    @Override
    public void onAttach(Context context){
        super.onAttach(getContext());
        //new getWebTask().execute();
        System.out.println("GET DATA!!!!!!!!!!");
        System.out.println("222222222222222222222222222222222222222222");
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        db = new DBHandler(getContext());
        if(getArguments()!=null) Owner_data=db.getUserbyid(getArguments().getInt("data"));
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //new getWebTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_tab_fragment_4, container, false);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
        listView = (ListView) rootView.findViewById(R.id.sub_list);
        new getWebTask().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), QuestDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("User_id",Owner_data.getId());
                bundle.putInt("Task_id",WebList[position].getTask_number());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        tab_fragment_4.AnimateFirstDisplayListener.displayedImages.clear();
        //ImageLoader.getInstance().destroy();
    }


    public class getWebTask extends AsyncTask<Void,Void,Void> {
        ImageAdapter imageadap;
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            data=new conDB2().executeQuery("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/task_list_type2.php","" ,"","");
            parseJSON(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            //ImageLoader.getInstance().destroy();
            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();

            ((ListView) listView).setAdapter(new tab_fragment_4.ImageAdapter(getActivity()));
        }
    }

    public final void parseJSON (String result){
        int i;
        data=new conDB2().executeQuery("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/task_list.php","" ,"","");
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray arrayList;
            arrayList=jsonObject.getJSONArray("web_list");
            Array_TaskList tasklist[] = new Array_TaskList[arrayList.length()];
            String taskimg[] = new String[arrayList.length()];
            for (i = 0; i < arrayList.length(); i++) {//get tasklist
                JSONObject jdata=arrayList.getJSONObject(i);
                Array_TaskList Task = new Array_TaskList(
                        jdata.getInt("task_number"),
                        jdata.getString("task_name"),
                        jdata.getString("task_block"),
                        domain+jdata.getString("task_picture").substring(2),
                        jdata.getInt("task_type"),
                        jdata.getInt("task_hot"),
                        jdata.getString("task_date")
                );
                tasklist[i]=Task;
                taskimg[i]=Task.getTask_picture();
            }
            WebList = tasklist;
            imageweb = taskimg;

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new tab_fragment_4.AnimateFirstDisplayListener();

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

        //@Override
        public int getCount() {

            return imageweb.length;
        }

        //@Override
        public Object getItem(int position) {
            return position;
        }

        //@Override
        public long getItemId(int position) {
            return position;
        }

        //@Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;
            final tab_fragment_4.ViewHolder holder;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_list_image, parent, false);
                holder = new tab_fragment_4.ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.text);
                holder.image = (ImageView) view.findViewById(R.id.image);
                holder.overflow = (ImageView)view.findViewById(R.id.overflow);
                holder.overflow.setVisibility(View.INVISIBLE);
                view.setTag(holder);
            }else {
                holder = (tab_fragment_4.ViewHolder) view.getTag();
            }
            if(WebList[position].getTask_name().length()>8)holder.text.setText(WebList[position].getTask_name().substring(0,8)+"...");
            else holder.text.setText(WebList[position].getTask_name());

            ImageLoader.getInstance().displayImage(imageweb[position], holder.image, options, animateFirstListener);
            return view;
        }
    }

    class ViewHolder {
        TextView text;
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

}
