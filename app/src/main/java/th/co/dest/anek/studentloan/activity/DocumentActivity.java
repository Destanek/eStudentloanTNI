package th.co.dest.anek.studentloan.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import th.co.dest.anek.studentloan.R;
import th.co.dest.anek.studentloan.fragment.DocumentFragment;
import th.co.dest.anek.studentloan.fragment.LoginFragment;

public class DocumentActivity extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tap_fragment_2, container, false);
        Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
        AuthData authData = ref.getAuth();
        if (authData != null) {
            // user authenticated
            getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2,new DocumentFragment()).commit();
        } else {
            // no user authenticated
            getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2,new LoginFragment()).commit();
        }
        return view;
    }

}
