package th.co.dest.anek.studentloan.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.co.dest.anek.studentloan.fragment.AnnounceFragment;
import th.co.dest.anek.studentloan.R;

public class NewActivity extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tap_fragment_1, container, false);

        getFragmentManager().beginTransaction().replace(R.id.tap_fragment_1, new AnnounceFragment()).commit();

        return view;
    }
}
