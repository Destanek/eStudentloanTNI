package th.co.dest.anek.studentloan.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import th.co.dest.anek.studentloan.R;
import th.co.dest.anek.studentloan.databinding.FragmentEditAccountBinding;

public class EditAccountFragment extends Fragment {

    private FragmentEditAccountBinding binding;

    String email, current_fname, current_lname, current_stuid, uid;
    String new_fname, new_lname, new_stuid, password;

    private Firebase usersRef, ref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_account,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    private void initialization() {
        ref = new Firebase("https://sizzling-torch-4935.firebaseio.com");
        AuthData authData = ref.getAuth();
        uid = authData.getUid();

        ref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> newPost = (Map<String, String>) dataSnapshot.getValue();
                current_fname = newPost.get("firstname");
                current_lname = newPost.get("lastname");
                current_stuid = newPost.get("stuid");
                email = newPost.get("email");

                binding.fname.setText(current_fname, TextView.BufferType.EDITABLE);
                binding.lname.setText(current_lname, TextView.BufferType.EDITABLE);
                binding.stuid.setText(current_stuid, TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new DocumentFragment()).commit();
            }
        });
    }

    public void edit() {
        if (!validate()) {
            onEditFailed();
            return;
        }

        binding.btnSave.setEnabled(false);

        final SpotsDialog spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialogTransaction);
        spotDialog.show();

        new_fname = binding.fname.getText().toString();
        new_lname = binding.lname.getText().toString();
        password = binding.password.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onEditSuccess();
                        // onSignupFailed();
                        spotDialog.dismiss();
                    }
                }, 3000);
    }

    public void onEditSuccess() {
        binding.btnSave.setEnabled(true);
        getActivity().setResult(Activity.RESULT_OK, null);

        ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
        final Map<String, Object> userInfoMap = new HashMap<String, Object>();
        userInfoMap.put("firstname", new_fname);
        userInfoMap.put("lastname", new_lname);
        userInfoMap.put("stuid", new_stuid);

        ref.changePassword(email, password, password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                usersRef = ref.child("users").child(uid);
                usersRef.updateChildren(userInfoMap);
                Toast.makeText(getActivity(), R.string.edit_account_success, Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new DocumentFragment()).commit();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                Toast.makeText(getActivity(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onEditFailed() {
        Toast.makeText(getActivity(), R.string.edit_account_failed, Toast.LENGTH_LONG).show();
        binding.btnSave.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        new_fname = binding.fname.getText().toString();
        new_lname = binding.lname.getText().toString();
        new_stuid = binding.stuid.getText().toString();
        password = binding.password.getText().toString();

        if (new_fname.isEmpty()) {
            binding.fname.setError(getResources().getString(R.string.valid_first_name_input));
            valid = false;
        } else {
            binding.fname.setError(null);
        }

        if (new_lname.isEmpty()) {
            binding.lname.setError(getResources().getString(R.string.valid_last_name_input));
            valid = false;
        } else {
            binding.lname.setError(null);
        }

        if (new_stuid.isEmpty()) {
            binding.stuid.setError(getResources().getString(R.string.valid_stuid_input));
            valid = false;
        } else {
            binding.stuid.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            binding.password.setError(getResources().getString(R.string.valid_password_input));
            valid = false;
        } else {
            binding.password.setError(null);
        }

        return valid;
    }
}
