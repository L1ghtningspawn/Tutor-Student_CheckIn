package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kean20sp.capstoneproject.http.SignupHandler;
import kean20sp.capstoneproject.util.AppState;

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

                String session_id = shandler.session_id();
                String available_roles = shandler.available_rolse();
                if(result.equals(SignupHandler.SIGNUP_SUCCESSFUL)) {
                    Intent it = new Intent(signup2.this, Student_Activity.class);
                    AppState.Session.id = session_id;
                    AppState.UserInfo.roles  = available_roles;
                    AppState.UserInfo.email = email.trim();
                    startActivity(it);
                } else {
                    Toast.makeText(signup2.this,result,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
