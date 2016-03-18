package th.ac.tni.studentaffairs.estudentloantni;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.IOException;

import butterknife.ButterKnife;

public class MainActivity extends Activity implements View.OnClickListener {

    private SlidingMenu menu;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);

        Title t2 = new Title();
        t2.execute();
    }

    private class Title extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                org.jsoup.nodes.Document document = Jsoup.connect(url).get();
                // Get the html document title

                Elements Content1 = document.select("div:has(h2.entry-title) .entry-title a");
                for (Element div : Content1) {
                    listContent.add(div.attr("title").substring(18));
                }
//                for (int i = 0; i < listContent.size(); i++) {
//                    Log.d("myLog", "listContent: " + listContent.get(i));
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            getDataInList();
            lvDetail.setAdapter(new MyBaseAdapter(context, myList));
            mProgressDialog.dismiss();
        }
    }

    private void getDataInList() {
//        ListData ld = new ListData();
        for (int i = 0; i < listContent.size(); i++) {
            // Create a new object for each list item
//            ld.setContent(listContent.get(i));
            // Add this object into the ArrayList myList
            myList.add(new ListData(listContent.get(i)));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
        }
    }
/*
    private void initSlidingMenu() {
//        setContentView(R.layout.content);
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(100);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.menu);
    }

    @OnClick(R.id.btnMenu)
    public void onClick() {
        menu.toggle();
    }*/
}
