package th.ac.tni.studentaffairs.estudentloantni;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tap1Activity extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tap_fragment_1, container, false);

        getFragmentManager().beginTransaction().replace(R.id.tap_fragment_1, new AnnounceFragment()).commit();

        return view;
    }
}
