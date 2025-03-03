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

import java.util.Calendar;
import java.util.GregorianCalendar;

import kean20sp.capstoneproject.http.ClockoutHandler;
import kean20sp.capstoneproject.http.LogoutHandler;
import kean20sp.capstoneproject.util.AppState;

public class ClockedIn_Activity extends AppCompatActivity {
    TextView checkin, clockout, logout, tutor_mode, clocked_in_at, clockin_time, clockin_duration;
    String session_id, email, user_roles, str_clockin_time, clockin_id, user_role_id;
    volatile boolean recalculate_clockin_duration = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocked_in_);

        checkin = (TextView) findViewById(R.id.checkin);
        clockout = (TextView) findViewById(R.id.clockout);
        logout = (TextView) findViewById(R.id.logout);
        tutor_mode = (TextView) findViewById(R.id.tutor_mode);
        clocked_in_at = (TextView) findViewById(R.id.clocked_in_at);
        clockin_time = (TextView) findViewById(R.id.clockin_time);
        clockin_duration = (TextView) findViewById(R.id.clockin_duration);

        session_id = AppState.Session.id;
        email = AppState.UserInfo.email;
        user_roles = AppState.UserInfo.roles;
        str_clockin_time = AppState.Clock.in_datetime;
        clockin_id = AppState.Clock.id;
        user_role_id = AppState.UserInfo.user_role_id;

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        logout.setTypeface(light_font);
        tutor_mode.setTypeface(regular_font);
        checkin.setTypeface(regular_font);
        clockout.setTypeface(regular_font);
        clocked_in_at.setTypeface(regular_font);
        clockin_time.setTypeface(regular_font);
        clockin_duration.setTypeface(regular_font);

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_click_checkin(v);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_click_logout(v);
            }
        });

        clockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on_click_clockout(v);
            }
        });

        long long_clockin_time = Long.parseLong(str_clockin_time) * 1000;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(long_clockin_time);
        clockin_time.setText(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+(cal.get(Calendar.AM_PM) == 0 ? "am" : "pm"));

        new Thread() {
            @Override
            public void run(){
                while(recalculate_clockin_duration){
                    long long_clockin_time = Long.parseLong(str_clockin_time) * 1000;
                    Long long_clockin_duration = System.currentTimeMillis() - long_clockin_time;
                    long hours = long_clockin_duration / 1000 / 60 / 60;
                    long minutes = ((long_clockin_duration) - (hours * 1000 * 60 * 60)) / 1000 / 60;
                    long seconds = ((long_clockin_duration) - (hours * 1000 * 60 * 60) - (minutes*1000*60)) / 1000;

                    String duration = (hours < 10 ? "0" : "")+hours+":"+
                            (minutes < 10 ? "0" : "")+minutes+":"+
                            (seconds < 10 ? "0" : "")+seconds;
                    if (clockin_duration.getText().equals(duration)) {
                        continue;
                    } else {
                        clockin_duration.setText(duration);
                    }
                }
            }
        }.start();

    }

    public void on_click_logout(View v){
        recalculate_clockin_duration = false;
        LogoutHandler logouthandler = new LogoutHandler();
        String response = logouthandler.logout(email,session_id);

        Intent it = new Intent(ClockedIn_Activity.this, login.class);

        startActivity(it);
    }

    public void on_click_clockout(View v){
        recalculate_clockin_duration = false;
        ClockoutHandler chandler = new ClockoutHandler();
        chandler.clockout(email,session_id,clockin_id);

        Intent it = new Intent(ClockedIn_Activity.this,Tutor_Activity.class);
        AppState.Session.id = session_id;
        AppState.UserInfo.email = email;
        AppState.UserInfo.roles = user_roles;
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        return;
        }

    public void on_click_checkin(View v){
        Intent it = new Intent(ClockedIn_Activity.this, CheckIn_Activity.class);
        startActivity(it);
    }
}
