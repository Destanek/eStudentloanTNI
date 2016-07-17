package th.ac.tni.studentaffairs.estudentloantni.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.ac.tni.studentaffairs.estudentloantni.activity.Tap1Activity;
import th.ac.tni.studentaffairs.estudentloantni.activity.Tap2Activity;

public class PageAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tap1Activity tab1 = new Tap1Activity();
                return tab1;
            case 1:
                Tap2Activity tab2 = new Tap2Activity();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
