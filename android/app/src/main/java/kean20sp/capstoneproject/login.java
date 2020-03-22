package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kean20sp.capstoneproject.http.LoginHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.CheckUserInput;

public class login extends AppCompatActivity {
    EditText pswd,usrusr;
    TextView sup,lin,fgtpsswd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fgtpsswd = (TextView) findViewById(R.id.forgotpswd);
        lin = (TextView) findViewById(R.id.lin);
        usrusr = (EditText) findViewById(R.id.email);
        pswd = (EditText) findViewById(R.id.pswrdd);
        sup = (TextView) findViewById(R.id.sup);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        lin.setTypeface(custom_font1);
        sup.setTypeface(custom_font);
        fgtpsswd.setTypeface(custom_font);
        usrusr.setTypeface(custom_font);
        pswd.setTypeface(custom_font);
        fgtpsswd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(login.this, ForgotPassword.class);
                startActivity(it);
            }
        });
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(login.this, signup.class);
                startActivity(it);
            }
        });
        lin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                login(v);
            }
        });
    }

    public void login(View view){
        String pswd_text = pswd.getText().toString();
        String email_text = usrusr.getText().toString();

        boolean isGoodInput = true;
        if(!CheckUserInput.isValidEmail(email_text)){
            Toast.makeText(this,"That Email is Not Valid",Toast.LENGTH_SHORT).show();
            isGoodInput = false;
        }
        if(!CheckUserInput.isValidPassword(pswd_text)){
            Toast.makeText(this,"That Password is Not Valid",Toast.LENGTH_SHORT).show();
            isGoodInput = false;
        }

        if(isGoodInput) {
            //send post request
            LoginHandler h = new LoginHandler();
            String result = h.login(email_text, pswd_text);
            String available_roles = h.available_roles();
            String session_id = h.session_id();

            if(result.equals(LoginHandler.LOGIN_SUCCESSFUL)) {
                Intent it = new Intent(login.this, Student_Activity.class);
                AppState.Session.id = session_id;
                AppState.UserInfo.roles  = available_roles;
                AppState.UserInfo.email = email_text;
                startActivity(it);
            } else {
                Toast.makeText(this,result,Toast.LENGTH_LONG).show();
            }
        }
    }
}