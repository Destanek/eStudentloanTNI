package th.co.dest.anek.studentloan.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import dmax.dialog.SpotsDialog;
import th.co.dest.anek.studentloan.R;
import th.co.dest.anek.studentloan.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    private String OldPassword;
    private String NewPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_change_password,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    private void initialization() {
        binding.btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new DocumentFragment()).commit();
            }
        });
    }

    public void change() {
        if (!validate()) {
            onChangeFailed();
            return;
        }

        binding.btnChangePass.setEnabled(false);

        final SpotsDialog spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialogTransaction);
        spotDialog.show();

        OldPassword = binding.oldPass.getText().toString();
        NewPassword = binding.newPass.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onChangeSuccess();
                        spotDialog.dismiss();
                    }
                }, 3000);
    }

    public void onChangeSuccess() {
        binding.btnChangePass.setEnabled(true);
        getActivity().setResult(Activity.RESULT_OK, null);
        Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
        AuthData authData = ref.getAuth();
        String email = authData.getProviderData().get("email").toString();
        ref.changePassword(email, OldPassword, NewPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), R.string.change_password_success, Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new DocumentFragment()).commit();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onChangeFailed() {
        Toast.makeText(getActivity(), R.string.change_password_failed, Toast.LENGTH_LONG).show();
        binding.btnChangePass.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        OldPassword = binding.oldPass.getText().toString();
        NewPassword = binding.newPass.getText().toString();
        String confirmPassword = binding.confirmNewPass.getText().toString();

        if (OldPassword.isEmpty()) {
            binding.oldPass.setError(getResources().getString(R.string.valid_password_input));
            valid = false;
        } else {
            binding.oldPass.setError(null);
        }

        if (NewPassword.isEmpty() || NewPassword.length() < 4 || NewPassword.length() > 10) {
            binding.newPass.setError(getResources().getString(R.string.valid_password_input));
            valid = false;
        } else {
            binding.newPass.setError(null);
        }

        if (!NewPassword.equals(confirmPassword)) {
            binding.confirmNewPass.setError(getResources().getString(R.string.invalid_confirm_password));
            valid = false;
        } else {
            binding.confirmNewPass.setError(null);
        }
        return valid;
    }

}
