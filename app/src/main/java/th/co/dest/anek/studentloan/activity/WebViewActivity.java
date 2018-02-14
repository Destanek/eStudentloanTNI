package th.co.dest.anek.studentloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import th.co.dest.anek.studentloan.R;

public class WebViewActivity extends AppCompatActivity {

    String linkContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web_view);
        Intent intent = getIntent();
        linkContent = intent.getStringExtra("linkContent");
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(linkContent);

    }

}
