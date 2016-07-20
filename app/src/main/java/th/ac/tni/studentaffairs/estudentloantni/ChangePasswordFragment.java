package th.ac.tni.studentaffairs.estudentloantni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePasswordFragment extends Fragment {

    @Bind(R.id.edtEmail)
    EditText etEmail;
//    @Bind(R.id.btnSendPass)
//    Button btnSendPassword;
    private View view;
    String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, view);
//        initialization();
        return view;
    }

//    private void initialization(){
//        btnSendPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
//                email = etEmail.getText().toString();
//                ref.resetPassword(email, new Firebase.ResultHandler() {
//                    @Override
//                    public void onSuccess() {
//                        // password reset email sent
//                        Toast.makeText(getActivity(), "New password has been send to your email", Toast.LENGTH_LONG).show();
//                        getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).commit();
//                    }
//                    @Override
//                    public void onError(FirebaseError firebaseError) {
//                        // error encountered
//                        Toast.makeText(getActivity(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
}
