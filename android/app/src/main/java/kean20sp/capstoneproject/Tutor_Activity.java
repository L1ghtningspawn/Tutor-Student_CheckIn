package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kean20sp.capstoneproject.http.ClockinHandler;
import kean20sp.capstoneproject.http.LogoutHandler;
import kean20sp.capstoneproject.http.TutorDeptListHandler;
import kean20sp.capstoneproject.util.AppState;

public class Tutor_Activity extends AppCompatActivity {
    String user_email, session_id, user_roles, clockin_date, clockin_id;
    Spinner select_program;
    TextView clock_in, student_mode, logout, session_history;
    String[] str_user_roles, departments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_);

        //AppState.Debug.log_All();

        select_program = (Spinner) findViewById(R.id.spinner);
        clock_in = (TextView) findViewById(R.id.sup);
        student_mode = (TextView) findViewById(R.id.sup2);
        logout = (TextView) findViewById(R.id.lin);
        session_history = (TextView) findViewById(R.id.session_history);

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        logout.setTypeface(light_font);
        student_mode.setTypeface(regular_font);
        clock_in.setTypeface(regular_font);

        user_email = AppState.UserInfo.email;
        session_id = AppState.Session.id;
        user_roles = AppState.UserInfo.roles;

        student_mode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(Tutor_Activity.this, Student_Activity.class);
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

        clock_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockin(v);
            }
        });

        session_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selected_dept = select_program.getSelectedItemPosition();
                    AppState.UserInfo.user_role_id = str_user_roles[selected_dept];

                    Intent intent = new Intent(Tutor_Activity.this, Tutor_Active_Session_Activity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Tutor_Activity.this,"Connection Failure",Toast.LENGTH_SHORT).show();
                }
            }
        });
    //AppState.Debug.log_All();
    }

    public void on_click_logout(View v){
        AppState.Session.activity_id = null;
        AppState.Session.logout_flag = true;
        LogoutHandler logouthandler = new LogoutHandler();
        String response = logouthandler.logout(user_email,session_id);

        AppState.IO.write(this);
        Intent it = new Intent(Tutor_Activity.this, login.class);

        startActivity(it);
    }

    public void clockin(View v){
        try {
            ClockinHandler clockinhandler = new ClockinHandler();
            int selected = select_program.getSelectedItemPosition();
            String user_role_id = str_user_roles[selected];
            String response = clockinhandler.clockin(user_email, session_id, user_role_id);

            clockin_date = clockinhandler.getClockinDate();
            clockin_id = clockinhandler.getClockin_id();
            if (response.equals(ClockinHandler.CLOCKIN_FAILURE)) {

            } else if (response.equals(ClockinHandler.CLOCKIN_SUCCESS)) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

                Intent it = new Intent(Tutor_Activity.this, ClockedIn_Activity.class);
                AppState.Clock.in_datetime = clockin_date;
                //Log.d("clockin_date",clockin_date);
                AppState.Clock.id = clockin_id;
                AppState.UserInfo.user_role_id = user_role_id;
                startActivity(it);

            } else if (response.equals(ClockinHandler.SESSION_EXPIRED)) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                on_click_logout(null);
            } else if (response.equals(ClockinHandler.UNKNOWN_FAILURE)) {
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();;
            Toast.makeText(this,"Connection Failure",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
       if(AppState.Clock.out_datetime != null){
           return;
       } else {
           super.onBackPressed();
       }
    }

    @Override
    public void onResume(){
        AppState.Session.activity_id = "Tutor_Activity";
        AppState.IO.write(this);
        super.onResume();
    }
}
