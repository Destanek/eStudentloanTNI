package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.adapter.AdapterWebContent;
import th.ac.tni.studentaffairs.estudentloantni.dao.DataModelWebContent;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentAnnounceBinding;

public class AnnounceFragment extends Fragment {

    private FragmentAnnounceBinding binding;

    private ArrayList<DataModelWebContent> listWeb;
    private ArrayList<String> listDate;
    private AdapterWebContent adapterListWeb;

    android.app.AlertDialog spotDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_announce,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    private void checkData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Announce Data", null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<DataModelWebContent>>() {}.getType();
            listWeb = gson.fromJson(json, type);
        }
        else{
            if(haveNetworkConnection()){
                Title t2 = new Title();
                t2.execute();
            }
        }

    }

    private void initialization() {
        listWeb = new ArrayList<DataModelWebContent>();
        listDate = new ArrayList<String>();

        checkData();
        adapterListWeb = new AdapterWebContent(getActivity(), listWeb);
        binding.lvWeb.setAdapter(adapterListWeb);
        binding.lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getLink = adapterListWeb.getItem(position).getLink();
                Log.d("myListView", "onItemClick: " + getLink);

                Bundle b = new Bundle();
                b.putString("url", getLink);
                FragmentWebview f = new FragmentWebview();
                f.setArguments(b);

                getFragmentManager().beginTransaction().add(R.id.tap_fragment_1, f).hide(AnnounceFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();

            }
        });

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

    }

    private void refreshContent(){

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if(haveNetworkConnection()){
                    Title t2 = new Title();
                    t2.execute();
                }
                binding.lvWeb.setAdapter(null);
                adapterListWeb = new AdapterWebContent(getActivity(), listWeb);
                binding.lvWeb.setAdapter(adapterListWeb);
                binding.swipeContainer.setRefreshing(false);
            }
        }, 3000);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                haveConnectedWifi = true;
                Toast.makeText(getContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                haveConnectedMobile = true;
            }
        } else {
            Toast.makeText(getContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private class Title extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            spotDialog = new SpotsDialog(getActivity(),R.style.CustomDialog);
            spotDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                org.jsoup.nodes.Document document = Jsoup.connect("http://studentaffairs.tni.ac.th/home/?cat=7").get();
                // Get the html document title
                Elements Date = document.select("time.published");
                for (Element div : Date) {
                    listDate.add(div.text());
                }
                Elements Content1 = document.select("div:has(h2.entry-title) .entry-title a");
                int i=0;
                for (Element div : Content1) {
                    listWeb.add(new DataModelWebContent(div.attr("title"), div.attr("href"),listDate.get(i)));
                    i++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            spotDialog.dismiss();
            adapterListWeb.notifyDataSetChanged();
            //cache data into Preference
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String json = gson.toJson(listWeb);
            editor.putString("Announce Data", json);
            editor.commit();
        }
    }

}
