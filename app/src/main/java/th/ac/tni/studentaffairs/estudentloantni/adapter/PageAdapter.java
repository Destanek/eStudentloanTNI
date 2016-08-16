package th.ac.tni.studentaffairs.estudentloantni.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import th.ac.tni.studentaffairs.estudentloantni.activity.NewActivity;
import th.ac.tni.studentaffairs.estudentloantni.activity.DocumentActivity;

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
                NewActivity tab1 = new NewActivity();
                return tab1;
            case 1:
                DocumentActivity tab2 = new DocumentActivity();
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
