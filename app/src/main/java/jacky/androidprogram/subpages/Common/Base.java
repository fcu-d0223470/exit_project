package jacky.androidprogram.subpages.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jacky.androidprogram.Database.DBHandler;
import jacky.androidprogram.Database.User;
import jacky.androidprogram.R;
import jacky.androidprogram.subpages.Packlist;
import jacky.androidprogram.subpages.Settings;
import jacky.androidprogram.subpages.home;

public class Base extends AppCompatActivity implements View.OnClickListener {

    DBHandler db;
    int user_id;
    String user_name;
    static User Owner_data;
    Button btnHome;
    Button btnSetting, btnMore;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }
    protected void mappingWidgets() {

        btnHome = (Button) findViewById(R.id.btnHome);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnMore = (Button) findViewById(R.id.btnMore);

        btnHome.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnMore.setOnClickListener(this);
    }

    protected void mappingWidgets(User Owner) {

        btnHome = (Button) findViewById(R.id.btnHome);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnMore = (Button) findViewById(R.id.btnMore);

        btnHome.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        /*btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Q = new Intent(mActivity, Packlist.class);
                Bundle bundle=new Bundle();
                bundle.putInt("user_id",Owner_data.getId());
                Q.putExtras(bundle);
                //String username = getIntent().getExtras().getString("owner");
                //Q.putExtra("owner",username);
                Q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Q);
            }
        });
        */


        Owner_data=Owner;
    }

    @Override
    public void onClick(View v) {
        /*if (v == null)
            throw new NullPointerException(
                    "You are refering null object. "
                            + "Please check weather you had called super class method mappingWidgets() or not");
         */
        if (v == btnHome) {
            Intent Q = new Intent(this, home.class);
            Bundle bundle=new Bundle();
            bundle.putInt("user_id",Owner_data.getId());
            Q.putExtras(bundle);
            //String username = getIntent().getExtras().getString("owner");
            //Q.putExtra("owner",username);
            //Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Q);
        } else if (v == btnSetting) {
            Intent Q = new Intent(this, Packlist.class);
            Bundle bundle=new Bundle();
            bundle.putInt("user_id",Owner_data.getId());
            Q.putExtras(bundle);
            //String username = getIntent().getExtras().getString("owner");
            //Q.putExtra("owner",username);
            //Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Q);
        }else if(v == btnMore) {
            Intent Q = new Intent(this, Settings.class);
            Bundle bundle=new Bundle();
            bundle.putInt("user_id",Owner_data.getId());
            Q.putExtras(bundle);
            //String username = getIntent().getExtras().getString("owner");
            //Q.putExtra("owner",username);
            //Q.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Q.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Q);
        }
    }
/*
    protected void handleBackgrounds(View v) {
        if (v == btnHome) {
            btnHome.setBackgroundResource(R.drawable.bottom_btn_hover);
            btnSetting.setBackgroundResource(R.drawable.bottom_btn_active);
            btnMore.setBackgroundResource(R.drawable.bottom_btn_active);

        } else if (v == btnSetting) {
            btnHome.setBackgroundResource(R.drawable.bottom_btn_active);
            btnSetting.setBackgroundResource(R.drawable.bottom_btn_hover);
            btnMore.setBackgroundResource(R.drawable.bottom_btn_active);

        } else if (v == btnMore) {
            btnHome.setBackgroundResource(R.drawable.bottom_btn_active);
            btnSetting.setBackgroundResource(R.drawable.bottom_btn_active);
            btnMore.setBackgroundResource(R.drawable.bottom_btn_hover);
        }
    }
*/
}
