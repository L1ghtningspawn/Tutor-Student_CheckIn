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

public class Student_Activity extends AppCompatActivity {
    TextView qr_check_in, email_check_in, tutor_mode, logout;

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
    }

    public void on_click_tutor_mode(View v){
        Intent it = new Intent(Student_Activity.this, Tutor_Activity.class);
        startActivity(it);
    }

}
