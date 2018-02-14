package th.co.dest.anek.studentloan.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import th.co.dest.anek.studentloan.R;
import th.co.dest.anek.studentloan.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment implements View.OnClickListener {

    private FragmentContactBinding binding;
    private FragmentWebview WebView;
    private Bundle b = new Bundle();

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
        binding.feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook:
                String facebookURL = "https://www.facebook.com/TNI.StudentLoan";
                b.putString("url", facebookURL);
                WebView.setArguments(b);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/228337813995344"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURL)));
                }

                break;
            case R.id.website:
                String websiteURL = "http://studentaffairs.tni.ac.th/home/?page_id=1336";
                b.putString("url", websiteURL);
                WebView.setArguments(b);

                getFragmentManager().beginTransaction().add(R.id.tap_fragment_2, WebView).hide(ContactFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
                break;
            case R.id.phone:
                Intent intent = new Intent (Intent.ACTION_DIAL, Uri.parse( "tel:027632621"));
                startActivity(intent);
                break;
            case R.id.feedback:
                String feedbackURL = "https://docs.google.com/a/tni.ac.th/forms/d/e/1FAIpQLSe5xi4Gqcl2EooyZcD0ThCq3vw8Wa39srunVTLHU4Tor6DJjA/viewform";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feedbackURL)));
//                b.putString("url", feedbackURL);
//                WebView.setArguments(b);
//
//                getFragmentManager().beginTransaction().add(R.id.tap_fragment_2, WebView).hide(ContactFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
                break;
        }
    }

}
