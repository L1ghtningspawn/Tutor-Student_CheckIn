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
import android.widget.Toast;

import kean20sp.capstoneproject.http.SignupHandler;

public class signup2 extends AppCompatActivity {
    EditText fname, lname, orgyear;
    TextView gotologin, signup;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        fname = (EditText) findViewById(R.id.firstname);
        lname = (EditText) findViewById(R.id.lastname);
        orgyear = (EditText) findViewById(R.id.schoolyear);
        gotologin = (TextView) findViewById(R.id.lin);
        signup = (TextView) findViewById(R.id.sup);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        gotologin.setTypeface(custom_font);
        fname.setTypeface(custom_font);
        lname.setTypeface(custom_font);
        orgyear.setTypeface(custom_font);
        signup.setTypeface(custom_font);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        gotologin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(signup2.this, login.class);
                startActivity(it);
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                SignupHandler shandler = new SignupHandler();
                String result = shandler.signup(email,password,fname.getText().toString(),lname.getText().toString(),orgyear.getText().toString());
                Toast.makeText(v.getContext(),result,Toast.LENGTH_LONG).show();
            }
        });
    }

}
