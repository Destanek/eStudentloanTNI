package th.ac.tni.studentaffairs.estudentloantni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.edtUser)
    EditText edtUser;
    @Bind(R.id.edtPass)
    EditText edtPass;
    @Bind(R.id.btnLog)
    Button btnLog;

    Button btnAnnounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnAnnounce = (Button) findViewById(R.id.btn_announce);

        btnAnnounce.setOnClickListener(this);
    }

    @OnClick(R.id.btnLog)
    public void onClick() {
        
    }

    @Override
    public void onClick(View v) {
        if (v == btnAnnounce) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
    }
}
