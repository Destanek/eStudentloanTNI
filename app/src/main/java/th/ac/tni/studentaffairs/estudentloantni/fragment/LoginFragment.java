package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import dmax.dialog.SpotsDialog;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    String email_address, pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login,
                container,
                false);
        View rootView = binding.getRoot();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialization();
        return rootView;
    }

    private void initialization() {

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new RegisterFragment()).addToBackStack(LoginFragment.class.getName()).commit();
            }
        });

        binding.btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SpotsDialog spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialogLogin);
                spotDialog.show();

                final Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
                email_address = binding.edtEmail.getText().toString();
                pass = binding.edtPass.getText().toString();

                ref.authWithPassword(email_address, pass, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        spotDialog.dismiss();
                        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_LONG).show();
                        getFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new DocumentFragment()).commit();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        spotDialog.dismiss();
                        switch (firebaseError.getCode()) {
                            case FirebaseError.INVALID_PASSWORD:
                                // handle a non existing user
                                Toast.makeText(getActivity(), R.string.incorrect_password, Toast.LENGTH_LONG).show();
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
        });

        binding.forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Title");

                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_forget_password, (ViewGroup) getView(), false);

                final EditText input = (EditText) viewInflated.findViewById(R.id.edtEmail);

                builder.setView(viewInflated);
                builder.setTitle(R.string.new_password_email);

                // Set up the buttons
                builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final SpotsDialog spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialog);
                        spotDialog.show();
                        Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
                        String email = input.getText().toString();
                        ref.resetPassword(email, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                // password reset email sent
                                spotDialog.dismiss();
                                Toast.makeText(getActivity(), R.string.forget_password_success, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                // error encountered
                                spotDialog.dismiss();
                                Toast.makeText(getActivity(), firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

        });
    }

}
