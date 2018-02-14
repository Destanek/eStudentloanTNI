package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmax.dialog.SpotsDialog;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentWebViewBinding;


public class FragmentWebview extends Fragment {

    private FragmentWebViewBinding binding;
    private android.app.AlertDialog spotDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialog);
        spotDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_web_view,
                container,
                false);
        View rootView = binding.getRoot();
        initInstances();
        return rootView;
    }

    private void initInstances() {
        String myurl = getArguments().getString("url", "empty");

        if (!myurl.equals("empty")) {
            binding.webView.loadUrl(myurl);
            spotDialog.dismiss();
        }
    }
}
