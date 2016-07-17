package th.ac.tni.studentaffairs.estudentloantni.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import th.ac.tni.studentaffairs.estudentloantni.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

//    @Bind(R.id.webView)
    private WebView webView;

//    @Bind(R.id.btnBack)
    private Button btnBack;

    String linkContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web_view);
        Intent intent = getIntent();
        linkContent = intent.getStringExtra("linkContent");
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(linkContent);

//        initialization();

        btnBack.setOnClickListener(this);

    }

//    private void initialization() {
//        Log.d("showLink", "MyLink : " + linkContent);
//
//    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        }
    }
}
