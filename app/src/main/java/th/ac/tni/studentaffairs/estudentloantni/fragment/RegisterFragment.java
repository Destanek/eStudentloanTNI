package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private Firebase usersRef, ref;
    String fname, lname, email, password, confirmPass, stuID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_register,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    public void initialization() {

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        binding.linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).commit();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).commit();
            }
        });

    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        binding.btnSignup.setEnabled(false);

        final SpotsDialog spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialogRegister);
        spotDialog.show();

        fname = binding.inputFname.getText().toString();
        lname = binding.inputLname.getText().toString();
        email = binding.inputEmail.getText().toString();
        password = binding.inputPassword.getText().toString();
        confirmPass = binding.confirmPassword.getText().toString();
        stuID = binding.inputStuid.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        spotDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        binding.btnSignup.setEnabled(true);
        getActivity().setResult(Activity.RESULT_OK, null);
        ref = new Firebase("https://sizzling-torch-4935.firebaseio.com");

        final Map<String, Object> userInfoMap = new HashMap<String, Object>();
        userInfoMap.put("firstname", fname);
        userInfoMap.put("lastname", lname);
        userInfoMap.put("email", email);
        userInfoMap.put("stuid", stuID);

        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                usersRef = ref.child("users").child(result.get("uid").toString());
                usersRef.updateChildren(userInfoMap);
                Toast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).commit();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                switch (firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        // handle a non existing user
                        Toast.makeText(getActivity(), R.string.email_already_use, Toast.LENGTH_LONG).show();
                        break;
                    case FirebaseError.NETWORK_ERROR:
                        // handle an invalid password
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        // handle other errors
                        Toast.makeText(getActivity(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), R.string.register_failed, Toast.LENGTH_LONG).show();
        binding.btnSignup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        fname = binding.inputFname.getText().toString();
        lname = binding.inputLname.getText().toString();
        email = binding.inputEmail.getText().toString();
        password = binding.inputPassword.getText().toString();
        confirmPass = binding.confirmPassword.getText().toString();
        stuID = binding.inputStuid.getText().toString();

        if (fname.isEmpty()) {
            binding.inputFname.setError(getResources().getString(R.string.no_input_text));
            valid = false;
        } else {
            binding.inputFname.setError(null);
        }

        if (lname.isEmpty()) {
            binding.inputLname.setError(getResources().getString(R.string.no_input_text));
            valid = false;
        } else {
            binding.inputLname.setError(null);
        }

        if (stuID.isEmpty()) {
            binding.inputStuid.setError(getResources().getString(R.string.no_input_text));
            valid = false;
        } else {
            binding.inputStuid.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError(getResources().getString(R.string.valid_email_input));
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            binding.inputPassword.setError(getResources().getString(R.string.valid_password_input));
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        if (!password.equals(confirmPass)) {
            binding.confirmPassword.setError(getResources().getString(R.string.invalid_confirm_password));
            valid = false;
        } else {
            binding.confirmPassword.setError(null);
        }

        return valid;
    }
}
