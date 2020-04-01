package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kean20sp.capstoneproject.http.ClockoutHandler;
import kean20sp.capstoneproject.http.LogoutHandler;
import kean20sp.capstoneproject.http.TutorEndAllSessions;
import kean20sp.capstoneproject.util.AppState;

public class ClockedIn_Activity extends AppCompatActivity {
    TextView checkin, clockout, logout, tutor_mode, clocked_in_at, clockin_time, clockin_duration;
    String session_id, email, user_roles, str_clockin_time, clockin_id, user_role_id;
    //volatile boolean recalculate_clockin_duration = true;
    //Thread update_duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clocked_in_);
        //AppState.Debug.log_All();

        checkin = (TextView) findViewById(R.id.checkin);
        clockout = (TextView) findViewById(R.id.clockout);
        logout = (TextView) findViewById(R.id.logout);
        tutor_mode = (TextView) findViewById(R.id.tutor_mode);
        clocked_in_at = (TextView) findViewById(R.id.clocked_in_at);
        clockin_time = (TextView) findViewById(R.id.clockin_time);
        clockin_duration = (TextView) findViewById(R.id.clockin_duration);
        clockin_duration.setText("");

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

        Date date_time = new Date(Long.parseLong(str_clockin_time)*1000);
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm a");
        String formatted_time = time_format.format(date_time);
        clockin_time.setText(formatted_time);

//        update_duration = new Thread() {
//            @Override
//            public void run(){
//                while(recalculate_clockin_duration){
//                    long long_clockin_time = Long.parseLong(str_clockin_time) * 1000;
//                    Long long_clockin_duration = System.currentTimeMillis() - long_clockin_time;
//                    long hours = long_clockin_duration / 1000 / 60 / 60;
//                    long minutes = ((long_clockin_duration) - (hours * 1000 * 60 * 60)) / 1000 / 60;
//                    long seconds = ((long_clockin_duration) - (hours * 1000 * 60 * 60) - (minutes*1000*60)) / 1000;
//
//                    String duration = (hours < 10 ? "0" : "")+hours+":"+
//                            (minutes < 10 ? "0" : "")+minutes+":"+
//                            (seconds < 10 ? "0" : "")+seconds;
//                    clockin_duration.setText(duration);
//                }
//            }
//        };
//
//        update_duration.start();

//        Log.d("mailman","email_tv --> "+(email == null));
//        Log.d("mailman","checkin_tv --> "+(checkin == null));
//        Log.d("mailman","clockout --> "+(clockout == null));
//        Log.d("mailman","clockin_time_tv --> "+(clockin_time == null));
//        Log.d("mailman","clocked_in_at --> "+(clocked_in_at == null));
//        Log.d("mailman","clockin_duration_tv --> "+(clockin_duration == null));
//        Log.d("mailman","user_roles --> "+(user_roles == null));
//        Log.d("mailman","str_clockin_time --> "+(str_clockin_time == null));
//        Log.d("mailman","user_role_id --> "+(user_role_id == null));
//        Log.d("mailman","session_id --> "+(session_id == null));
    }

    public void on_click_logout(View v){
        //recalculate_clockin_duration = false;
        LogoutHandler logouthandler = new LogoutHandler();
        String response = logouthandler.logout(email,session_id);

        Intent it = new Intent(ClockedIn_Activity.this, login.class);

        stop_update_duration_thread();
        startActivity(it);
    }

    public void stop_update_duration_thread(){
        //recalculate_clockin_duration = false;
        try{
            //update_duration.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void on_click_clockout(View v){
        //recalculate_clockin_duration = false;
        ClockoutHandler chandler = new ClockoutHandler();
        chandler.clockout(email,session_id,clockin_id);

        //end all active sessions
        TutorEndAllSessions gameover_bitch = new TutorEndAllSessions();
        String result = gameover_bitch.All_Session_End(AppState.UserInfo.user_role_id);
        if (result.equals(TutorEndAllSessions.SESSION_END_SUCCESS)) {
            Toast.makeText(this,"All Active Sessions Were Ended", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        }

        Intent it = new Intent(ClockedIn_Activity.this,Tutor_Activity.class);
        AppState.Clock.out_datetime = Long.toString(System.currentTimeMillis()/1000);

        stop_update_duration_thread();
        startActivity(it);
    }

    public void on_click_checkin(View v){
        Thread.currentThread().interrupt();
        Intent it = new Intent(ClockedIn_Activity.this, CheckIn_Activity.class);
        stop_update_duration_thread();
        //Log.d("asshole", "thread is running: "+update_duration.isAlive());
        startActivity(it);
    }

    public void start_asshole(){
        //recalculate_clockin_duration = true;
        //update_duration.start();
    }

    @Override
    public void onResume(){
        //assume asshole was already stopped
        //startup asshole again...
        start_asshole();
        super.onResume();
    }

    @Override
    public void onBackPressed(){
//        stop_asshole();
//        super.onBackPressed();
        return;
    }
}
