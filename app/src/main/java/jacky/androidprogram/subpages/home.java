package jacky.androidprogram.subpages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.HashMap;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.subpages.Common.Base;
import jacky.androidprogram.subpages.Common.ConnectivityReceiver;

public class home extends Base {

    SliderLayout mDemoSlider;
    String data = "";
    DBHandler db;
    Toolbar toolbar;
    TextView test;
    ViewStub stub;
    ImageView iv;
    int user_id;
    String user_name;
    User Owner_data;
    View basebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_bar);
//////////////////////////////////////////////////////////////////////////////////////////////////////
        //Inflate Home Activity
        stub = (ViewStub)findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.home);
        View inflated = stub.inflate();
//////////////////////////////////////////////////////////////////////////////////////////////////////
        //Toolbar
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//////////////////////////////////////////////////////////////////////////////////////////////////////
        //Slider
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("2", "http://www.ebizlearning.com.my/web/picture/2/2/main.jpg");
        url_maps.put("3", "http://www.ebizlearning.com.my/web/picture/2/3/main.jpg");
        url_maps.put("4", "http://www.ebizlearning.com.my/web/picture/2/4/main.jpg");
        url_maps.put("5", "http://www.ebizlearning.com.my/web/picture/2/5/main.jpg");
        for(String name : url_maps.keySet()){
            DefaultSliderView SliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            SliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            SliderView.bundle(new Bundle());
            SliderView.getBundle().putString("extra",name);
            mDemoSlider.addSlider(SliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
//////////////////////////////////////////////////////////////////////////////////////////////////////
        ImageButton QuestBtn = (ImageButton)findViewById(R.id.questbutton);
        ImageButton weather = (ImageButton)findViewById(R.id.weatherbutton);
        ImageButton setting = (ImageButton)findViewById(R.id.settingbutton);
        test = (TextView)findViewById(R.id.hometest);
        db = new DBHandler(this);

        Intent intentext =getIntent();
        Bundle bundle = intentext.getExtras();
        user_id = bundle.getInt("user_id");
        Owner_data=db.getUserbyid(user_id);
        super.mappingWidgets(Owner_data);

        //String owner = bundle.getString("owner");
        test.setText("Hi, "+user_id+"\n");
        test.setText(test.getText()+Owner_data.getUserpass().toString()+Owner_data.getFname().toString()+Owner_data.getLname().toString());
        QuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()){
                    Intent Q = new Intent(home.this, Questlist.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("user_id",Owner_data.getId());
                    Q.putExtras(bundle);
                    Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(Q);
                }else Toast.makeText(home.this,"Please connect to the internet to view quest",Toast.LENGTH_LONG).show();
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkConnection()){
                    String url = "http://www.cwb.gov.tw/m/f/taiwan/63.htm";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }else{
                    Toast.makeText(home.this,"Please connect to the internet to check weather",Toast.LENGTH_LONG).show();
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Q = new Intent(home.this,Settings.class);
                Bundle bundle=new Bundle();
                bundle.putInt("user_id",Owner_data.getId());
                Q.putExtras(bundle);
                Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Q);
            }
        });
        super.onClick(basebtn);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        /*
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        */
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_search){
            test.setText("Search Selected");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                user_name=data.getStringExtra("updated_account");
                test.setText("Hi, "+user_name.toString()+"\n");
                //Owner_data=db.getUserbyName(user_name);
                test.setText(test.getText()+Owner_data.getUserpass().toString()+Owner_data.getFname().toString()+Owner_data.getLname().toString());
            }
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
        Toast.makeText(home.this, message, Toast.LENGTH_LONG).show();
    }
    public void onNetworkConnectionChanged(boolean isConnected) {
        showToast(isConnected);
    }
}
