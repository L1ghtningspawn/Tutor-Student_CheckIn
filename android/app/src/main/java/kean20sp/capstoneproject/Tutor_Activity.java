package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kean20sp.capstoneproject.http.LogoutHandler;
import kean20sp.capstoneproject.http.TutorDeptListHandler;

public class Tutor_Activity extends AppCompatActivity {
    String user_email, session_id, user_roles;
    Spinner select_program;
    TextView clock_in, student_mode, logout;
    String[] str_user_roles, departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_);

        select_program = (Spinner) findViewById(R.id.spinner);
        clock_in = (TextView) findViewById(R.id.sup);
        student_mode = (TextView) findViewById(R.id.sup2);
        logout = (TextView) findViewById(R.id.lin);

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        logout.setTypeface(light_font);
        student_mode.setTypeface(regular_font);
        clock_in.setTypeface(regular_font);

        user_email = getIntent().getStringExtra("email");
        session_id = getIntent().getStringExtra("session_id");
        user_roles = getIntent().getStringExtra("available_roles");

        student_mode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(Tutor_Activity.this, Student_Activity.class);
                it.putExtra("email",user_email);
                it.putExtra("session_id",session_id);
                it.putExtra("available_roles",user_roles);
                startActivity(it);
            }
        });

        TutorDeptListHandler tdlhandler = new TutorDeptListHandler();
        String response = tdlhandler.getTutorDeptList(user_email,session_id);
        if(response.equals(TutorDeptListHandler.GET_TUTOR_DEPT_LIST_SUCCESS)){
            str_user_roles = tdlhandler.user_roles;
            departments = tdlhandler.departments;
            List<String> list_depts = Arrays.asList(departments);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_depts);
            select_program.setAdapter(adapter);

            Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
        } else if(response.equals(TutorDeptListHandler.GET_TUTOR_DEPT_LIST_FAILURE)) {
            Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
        } else if(response.equals(TutorDeptListHandler.SESSION_TIMED_OUT)){
            on_click_logout(null);
        }
        else {
            Toast.makeText(this,TutorDeptListHandler.UNKNOWN_FAILURE,Toast.LENGTH_SHORT).show();
        }

    }

    public void on_click_logout(View v){
        LogoutHandler logouthandler = new LogoutHandler();
        String response = logouthandler.logout(user_email,session_id);

        Intent it = new Intent(Tutor_Activity.this, login.class);

        startActivity(it);
    }

}
