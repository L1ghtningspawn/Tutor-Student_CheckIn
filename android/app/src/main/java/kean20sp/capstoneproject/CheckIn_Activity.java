package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import kean20sp.capstoneproject.http.CheckinHandler;
import kean20sp.capstoneproject.http.TutorCourseListHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.CheckUserInput;
import kean20sp.capstoneproject.util.GetUserInfo;
import kean20sp.capstoneproject.util.QRUtil;

public class CheckIn_Activity extends AppCompatActivity {
    TextView email_tv, checkin_tv, logout_tv, clockin_time_tv, clockin_duration_tv, session_history_tv;
    ImageView qrcode;

    String session_id, email, user_roles, str_clockin_time, clockin_id, user_role_id;
    //volatile boolean recalculate_clockin_duration = true;
    //Thread asshole;

    String[] courses, courseIDs;


    public void stop_asshole(){
        //recalculate_clockin_duration = false;
        try{
            //asshole.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_);
        //AppState.Debug.log_All();

        email_tv = (TextView) findViewById(R.id.email);
        checkin_tv = (TextView) findViewById(R.id.checkin);
        logout_tv = (TextView) findViewById(R.id.logout);
        clockin_time_tv = (TextView) findViewById(R.id.clockin_time);
        clockin_duration_tv = (TextView) findViewById(R.id.clockin_duration);
        session_history_tv = (TextView) findViewById(R.id.session_history);
        qrcode = (ImageView) findViewById(R.id.qrcode);

        clockin_duration_tv.setText("");

//        Log.d("mailman","email_tv --> "+(email_tv == null));
//        Log.d("mailman","checkin_tv --> "+(checkin_tv == null));
//        Log.d("mailman","logout_tv --> "+(logout_tv == null));
//        Log.d("mailman","clockin_time_tv --> "+(clockin_time_tv == null));
//        Log.d("mailman","clockin_duration_tv --> "+(clockin_duration_tv == null));
//        Log.d("mailman","session_history_tv --> "+(session_history_tv == null));
//        Log.d("mailman","qrcode --> "+(qrcode == null));

        Typeface light_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        email_tv.setTypeface(regular_font);
        checkin_tv.setTypeface(regular_font);
        clockin_time_tv.setTypeface(regular_font);
        clockin_duration_tv.setTypeface(regular_font);
        logout_tv.setTypeface(light_font);

        session_id = AppState.Session.id;
        email = AppState.UserInfo.email;
        str_clockin_time = AppState.Clock.in_datetime;
        clockin_id = AppState.Clock.id;
        user_role_id = AppState.UserInfo.user_role_id;

        Date date_time = new Date(Long.parseLong(str_clockin_time)*1000);
        SimpleDateFormat time_format = new SimpleDateFormat("hh:mm a");
        String formatted_time = time_format.format(date_time);
        clockin_time_tv.setText(formatted_time);

        //clockin_time_tv.setText(ct_time);
//        asshole = new Thread() {
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
//                    //Log.d("mailman", "duration = "+duration);
//                    //Log.d("mailman", "textview = "+(clockin_duration_tv == null));
//                    clockin_duration_tv.setText(duration);
//                }
//            }
//        };
//        asshole.start();

        try{
            String qrcode_value = QRUtil.genkey();
            QRGEncoder qrgEncoder = new QRGEncoder(qrcode_value,null,QRGContents.Type.TEXT,150) ;
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ImageView img = (ImageView) findViewById(R.id.qrcode);
            img.setImageBitmap(bitmap);
        } catch(Exception e){
            e.printStackTrace();
        }

        checkin_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean isGoodInput = true;
                if(!CheckUserInput.isValidEmail(email_tv.getText().toString())){
                    Toast.makeText(v.getContext(),"That Email is Not Valid",Toast.LENGTH_SHORT).show();
                    isGoodInput = false;
                }
                if(isGoodInput) {
                    CheckinHandler chandler = new CheckinHandler();
                    String result = chandler.checkin_email(AppState.UserInfo.user_role_id,email_tv.getText().toString(),"tutor");
                    if(result.equals(CheckinHandler.CHECKIN_SUCCESSFUL)) {
                        AppState.TutorSession.student_id = GetUserInfo.get_student_id(email_tv.getText().toString());
                        AppState.TutorSession.student_email = email_tv.getText().toString();

                        stop_asshole();
                        Intent it = new Intent(CheckIn_Activity.this, Tutor_Checked_In_Activity.class);
                        // Send over the student's email
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        int ct_hour = cal.get(Calendar.HOUR_OF_DAY);
                        int ct_minute = cal.get(Calendar.MINUTE);
                        String ct_ampm = (cal.get(Calendar.AM_PM) == 0 ? "am" : "pm");
                        String ct_time = (ct_hour < 10 ? "0"+ct_hour: ct_hour) + ":" +
                                (ct_minute < 10 ? "0" + ct_minute : ct_minute) + " " + ct_ampm;

                        AppState.TutorSession.in_datetime = Long.toString(cal.getTimeInMillis()/1000);
                        //Log.d("checkin_time", AppState.TutorSession.in_datetime);
                        startActivity(it);
                    } else if(result.equals(CheckinHandler.INVALID_SESSION)){
                        Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                    } else if(result.equals(CheckinHandler.TUTOR_SESSION_EXISTS)){
                        Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        session_history_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_asshole();
                Intent intent = new Intent(CheckIn_Activity.this, Tutor_Active_Session_Activity.class);
                startActivity(intent);
            }
        });

    }

    public void start_asshole(){
        //recalculate_clockin_duration = true;
        //asshole.start();
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
        stop_asshole();
        super.onBackPressed();
    }
}
