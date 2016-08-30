package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment implements View.OnClickListener {

    private FragmentContactBinding binding;
    private String facebookURL = "https://www.facebook.com/TNI.StudentLoan";
    private String websiteURL = "http://studentaffairs.tni.ac.th/home/?page_id=1336";
    private FragmentWebview WebView;
    private Bundle b = new Bundle();
    PackageManager pm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_contact,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    private void initialization() {
        WebView = new FragmentWebview();
        binding.facebook.setOnClickListener(this);
        binding.website.setOnClickListener(this);
        binding.phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook:
                b.putString("url", facebookURL);
                WebView.setArguments(b);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/228337813995344"));
                    startActivity(intent);
                } catch(Exception e) {
//                    getFragmentManager().beginTransaction().add(R.id.tap_fragment_2, WebView).hide(ContactFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL)));
                }

                break;
            case R.id.website:
                b.putString("url", websiteURL);
                WebView.setArguments(b);

                getFragmentManager().beginTransaction().add(R.id.tap_fragment_2, WebView).hide(ContactFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
                break;
            case R.id.phone:
                Intent intent = new Intent (Intent.ACTION_DIAL, Uri.parse( "tel:027632621"));
                startActivity(intent);
                break;
        }
    }

}
