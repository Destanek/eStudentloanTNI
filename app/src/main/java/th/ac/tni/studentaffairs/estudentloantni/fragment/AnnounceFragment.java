package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        Title t2 = new Title();
        t2.execute();
        return rootView;
    }

    private void initialization() {
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        listWeb = new ArrayList<DataModelWebContent>();
        listDate = new ArrayList<String>();
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

    }

    private void refreshContent(){

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                adapterListWeb.clear();
                Title t2 = new Title();
                t2.execute();
                adapterListWeb = new AdapterWebContent(getActivity(), listWeb);
                binding.lvWeb.setAdapter(adapterListWeb);
                binding.swipeContainer.setRefreshing(false);
            }
        }, 5000);

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
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listWeb",listWeb);
        outState.putStringArrayList("listDate",listDate);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            Log.d("Restore", "Restored ");
        }
    }
}
