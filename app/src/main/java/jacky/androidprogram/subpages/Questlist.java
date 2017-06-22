package jacky.androidprogram.subpages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.connectionDB.conDB2;
import jacky.androidprogram.subpages.Common.Base;
import jacky.androidprogram.subpages.questfragment.Array_TaskList;
import jacky.androidprogram.subpages.questfragment.PagerAdapter;

public class Questlist extends Base {

    String data="";
    String message = "";
    String domain = "http://www.ebizlearning.com.my/web";
    Array_TaskList webTask[];
    User Owner_data;
    DBHandler db;
    ViewStub stub;
    View basebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHandler(this);

        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        Owner_data=db.getUserbyid(bundle.getInt("user_id"));
        super.mappingWidgets(Owner_data);

        new getWebTask().execute();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Top 10"));
        /*
        tabLayout.addTab(tabLayout.newTab().setText("Hot"));
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Sub"));
        tabLayout.addTab(tabLayout.newTab().setText("Special"));
        */
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(),Owner_data.getId());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        super.onClick(basebtn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            webTask = tasklist;
            for(i=0;i<webTask.length;i++){System.out.println(webTask[i].getTask_name());}

        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }
    }

    public class getWebTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog(Questlist.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            data=new conDB2().executeQuery("SELECT * FROM USER ;", "http://www.ebizlearning.com.my/web/phone/task_list.php","" ,"","");
            parseJSON(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            super.onPostExecute(result);
            if (pDialog.isShowing()) pDialog.dismiss();
        }
    }
}
