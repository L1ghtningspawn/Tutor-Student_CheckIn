package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity {
    EditText email;
    TextView buttontext, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = (EditText) findViewById(R.id.email);
        buttontext = (TextView) findViewById(R.id.sup);
        login = (TextView) findViewById(R.id.lin);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");

        buttontext.setTypeface(custom_font);
        login.setTypeface(custom_font);
        email.setTypeface(custom_font);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(ForgotPassword.this, login.class);
                startActivity(it);
            }
        });
    }

}
