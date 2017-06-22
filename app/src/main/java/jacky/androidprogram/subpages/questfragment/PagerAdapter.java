package jacky.androidprogram.subpages.questfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import jacky.androidprogram.Database.DBHandler;

/**
 * Created by Jacky on 7/27/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    int Owner_id;
    DBHandler db;
    Bundle bundle = new Bundle();

    private Context context;
    private Intent intent;

    public PagerAdapter(FragmentManager fm, int NumOfTabs,int User_id) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.Owner_id=User_id;
    }
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab_fragment_1 tab1 = new tab_fragment_1();
                bundle.putInt("data",Owner_id);
                tab1.setArguments(bundle);
                return tab1;
           /*
            case 1:
                tab_fragment_2 tab2 = new tab_fragment_2();
                bundle.putInt("data",Owner_id);
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                tab_fragment_3 tab3 = new tab_fragment_3();
                bundle.putInt("data",Owner_id);
                tab3.setArguments(bundle);
                return tab3;
            case 3:
                tab_fragment_4 tab4 = new tab_fragment_4();
                bundle.putInt("data",Owner_id);
                tab4.setArguments(bundle);
                return tab4;
            case 4:
                tab_fragment_5 tab5 = new tab_fragment_5();
                bundle.putInt("data",Owner_id);
                tab5.setArguments(bundle);
                return tab5;

                */
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void getData(Bundle bundle){this.bundle = bundle;}

}
