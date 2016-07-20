package th.ac.tni.studentaffairs.estudentloantni;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AnnounceFragment extends Fragment {

    ListView lvWeb;
    private ArrayList<DataModelWebContent> listWeb;
    private AdapterWebContent adapterListWeb;

    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tap_fragment_1, container, false);
    }

    public void initialization() {
        listWeb = new ArrayList<DataModelWebContent>();
        adapterListWeb = new AdapterWebContent(this, listWeb);
        lvWeb.setAdapter(adapterListWeb);
        lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getLink = adapterListWeb.getItem(position).getLink();
                Log.d("myListView", "onItemClick: " + getLink);

                Intent intent = new Intent(getContext(),
                        WebViewActivity.class);
                intent.putExtra("linkContent", getLink);
                startActivity(intent);

            }
        });

        Title t2 = new Title();
        t2.execute();
    }

    public class Title extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                org.jsoup.nodes.Document document = Jsoup.connect("http://studentaffairs.tni.ac.th/home/?cat=7").get();
                // Get the html document title
                Elements Content1 = document.select("div:has(h2.entry-title) .entry-title a");
                for (Element div : Content1) {
                    listWeb.add(new DataModelWebContent(div.attr("title").substring(18), div.attr("href")));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            mProgressDialog.dismiss();
            adapterListWeb.notifyDataSetChanged();
        }
    }
}
