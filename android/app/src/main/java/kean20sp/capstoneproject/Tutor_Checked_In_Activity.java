package kean20sp.capstoneproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kean20sp.capstoneproject.http.TutorEndSessionHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.GetUserInfo;

public class Tutor_Checked_In_Activity extends AppCompatActivity {
    TextView student_name_tv, student_email_tv, checkin_time_tv, checkin_duration_tv, session_history, end_session_tv;
    String str_checkin_time;
    boolean recalculate_checkin_duration = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor__checked__in_);

        final String student_name = GetUserInfo.get_name(AppState.TutorSession.student_id);
        final String student_email = GetUserInfo.get_email(AppState.TutorSession.student_id);
        final String tutor_name = GetUserInfo.get_name(AppState.UserInfo.user_role_id);
        final String tutor_email = GetUserInfo.get_email(AppState.UserInfo.user_role_id);
        str_checkin_time = AppState.TutorSession.in_datetime;

//        AppState.Debug.log_TutorSession();
//        AppState.Debug.log_UserInfo();
//        Log.d("mailman", "student_name = "+student_name);
//        Log.d("mailman","student_email = "+student_email);
//        Log.d("mailman", "tutor_name = "+tutor_name);
//        Log.d("mailman", "tutor_email = "+tutor_email);
//        Log.d("mailman", "checkin_time = "+str_checkin_time);

        student_name_tv = (TextView) findViewById(R.id.student_name);
        student_name_tv.setText(student_name);
        student_email_tv = (TextView) findViewById(R.id.student_email);
        student_email_tv.setText(student_email);
        checkin_time_tv = (TextView) findViewById(R.id.checkin_time);
        checkin_duration_tv = (TextView) findViewById(R.id.clockin_duration);
        session_history = (TextView) findViewById(R.id.session_history);
        end_session_tv = (TextView) findViewById(R.id.check_out);

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        student_email_tv.setTypeface(regular_font);
        checkin_time_tv.setTypeface(regular_font);
        checkin_duration_tv.setTypeface(regular_font);
        session_history.setTypeface(regular_font);
        end_session_tv.setTypeface(regular_font);

        long long_checkin_time = Long.parseLong(str_checkin_time) * 1000;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(long_checkin_time);

        int ct_hour = cal.get(Calendar.HOUR_OF_DAY);
        int ct_minute = cal.get(Calendar.MINUTE);
        String ct_ampm = (cal.get(Calendar.AM_PM) == 0 ? "am" : "pm");
        String ct_time = (ct_hour < 10 ? "0"+ct_hour: ct_hour) + ":" +
                (ct_minute < 10 ? "0" + ct_minute : ct_minute) + " " + ct_ampm;

        checkin_time_tv.setText(ct_time);

        new Thread() {
            @Override
            public void run(){
                while(recalculate_checkin_duration){
                    long long_checkin_time = Long.parseLong(str_checkin_time) * 1000;
                    Long long_checkin_duration = System.currentTimeMillis() - long_checkin_time;
                    long hours = long_checkin_duration / 1000 / 60 / 60;
                    long minutes = ((long_checkin_duration) - (hours * 1000 * 60 * 60)) / 1000 / 60;
                    long seconds = ((long_checkin_duration) - (hours * 1000 * 60 * 60) - (minutes*1000*60)) / 1000;

                    String duration = (hours < 10 ? "0" : "")+hours+":"+
                            (minutes < 10 ? "0" : "")+minutes+":"+
                            (seconds < 10 ? "0" : "")+seconds;
                    if (checkin_duration_tv.getText().equals(duration)) {
                        continue;
                    } else {
                        checkin_duration_tv.setText(duration);
                    }
                }
            }
        }.start();

        session_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tutor_Checked_In_Activity.this, Tutor_Active_Session_Activity.class);
                startActivity(intent);
            }
        });

        end_session_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TutorEndSessionHandler tesh = new TutorEndSessionHandler();
                    String student_id = AppState.TutorSession.student_id;
                    String tutor_id = AppState.UserInfo.user_role_id;
                    String result = tesh.Individual_Session_End(student_id, tutor_id);
                    if (result.equals(TutorEndSessionHandler.SESSION_END_SUCCESS)) {
                        Intent it = new Intent(Tutor_Checked_In_Activity.this, Tutor_Active_Session_Activity.class);
                        startActivity(it);
                    } else {
                        Toast.makeText(Tutor_Checked_In_Activity.this,
                                result,
                                Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(Tutor_Checked_In_Activity.this,
                            "Something Unexpected Happened",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView email_image = (ImageView) findViewById(R.id.email);
        email_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{student_email});
                intent.putExtra(Intent.EXTRA_SUBJECT, tutor_name + ": Regarding Tutoring Session");
                try {
                    startActivity(Intent.createChooser(intent, "Sending mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Tutor_Checked_In_Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
