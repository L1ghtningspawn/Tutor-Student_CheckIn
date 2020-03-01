package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kean20sp.capstoneproject.http.LogoutHandler;

public class Student_Activity extends AppCompatActivity {
    TextView qr_check_in, email_check_in, tutor_mode, logout;
    String session_id;
    String user_email;
    String user_roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_);

        qr_check_in = (TextView) findViewById(R.id.qr_check_in);
        email_check_in = (TextView) findViewById(R.id.email_check_in);
        tutor_mode = (TextView) findViewById(R.id.tutor_mode);
        logout = (TextView) findViewById(R.id.logout);

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        logout.setTypeface(light_font);
        qr_check_in.setTypeface(regular_font);
        email_check_in.setTypeface(regular_font);
        tutor_mode.setTypeface(regular_font);

        session_id = getIntent().getStringExtra("session_id");
        user_email = getIntent().getStringExtra("email");
        user_roles = getIntent().getStringExtra("available_roles");
    }

    public void on_click_tutor_mode(View v){
        if(user_roles.contains("Tu")) {
            Intent it = new Intent(Student_Activity.this, Tutor_Activity.class);
            it.putExtra("session_id", session_id);
            it.putExtra("email", user_email);
            it.putExtra("available_roles", user_roles);
            startActivity(it);
        } else {
            Toast.makeText(Student_Activity.this, "You Are Not a Tutor", Toast.LENGTH_SHORT).show();
        }
    }

    public void on_click_logout(View v){
        LogoutHandler logouthandler = new LogoutHandler();
        String response = logouthandler.logout(user_email,session_id);

        Intent it = new Intent(Student_Activity.this, login.class);

        startActivity(it);
    }

}
