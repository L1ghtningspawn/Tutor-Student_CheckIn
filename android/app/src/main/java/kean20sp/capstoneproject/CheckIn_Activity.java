package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import kean20sp.capstoneproject.http.CheckinHandler;
import kean20sp.capstoneproject.http.TutorCourseListHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.QRUtil;

public class CheckIn_Activity extends AppCompatActivity {
    TextView email_tv, checkin_tv, logout_tv, clockin_time_tv, clockin_duration_tv;
    //Spinner courselist_sp;
    ImageView qrcode;

    String session_id, email, user_roles, str_clockin_time, clockin_id, user_role_id;
    boolean recalculate_clockin_duration = true;

    String[] courses, courseIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_);

        email_tv = (TextView) findViewById(R.id.email);
        checkin_tv = (TextView) findViewById(R.id.checkin);
        logout_tv = (TextView) findViewById(R.id.logout);
        clockin_time_tv = (TextView) findViewById(R.id.clockin_time);
        clockin_duration_tv = (TextView) findViewById(R.id.clockin_duration);
        //courselist_sp = (Spinner) findViewById(R.id.courselist);
        qrcode = (ImageView) findViewById(R.id.qrcode);

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

        long long_clockin_time = Long.parseLong(str_clockin_time) * 1000;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(long_clockin_time);
        clockin_time_tv.setText(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" "+(cal.get(Calendar.AM_PM) == 0 ? "am" : "pm"));

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
                    if (clockin_duration_tv.getText().equals(duration)) {
                        continue;
                    } else {
                        clockin_duration_tv.setText(duration);
                    }
                }
            }
        }.start();

        TutorCourseListHandler tclhandler = new TutorCourseListHandler();
        String result = tclhandler.getTutorCourseList(email,session_id,user_role_id);
        if(result.equals(TutorCourseListHandler.SUCCESSFUL)){
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
            courses = tclhandler.courses;
            courseIDs = tclhandler.course_ids;
        } else {
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
        }
//        List<String> list_courses = Arrays.asList(courses);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_courses);
//
//        //courselist_sp.setAdapter(adapter);
//        try{
//            String qrcode_value = QRUtil.genkey();
//            QRGEncoder qrgEncoder = new QRGEncoder(qrcode_value,null,QRGContents.Type.TEXT,2) ;
//            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
//            ImageView img = (ImageView) findViewById(R.id.qrcode);
//            img.setImageBitmap(bitmap);
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        checkin_tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                CheckinHandler chandler = new CheckinHandler();
                String result = chandler.checkin(email,email_tv.getText().toString());
                if(result.equals(CheckinHandler.CHECKIN_SUCCESSFUL)) {
                    Intent it = new Intent(CheckIn_Activity.this, Student_Checked_In_Activity.class);
//                    AppState.Session.id = session_id;
//                    AppState.UserInfo.email = email;
                    startActivity(it);
                } else if(result.equals(CheckinHandler.INVALID_SESSION)){
                    Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                } else if(result.equals(CheckinHandler.TUTOR_SESSION_EXISTS)){
                    Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CheckIn_Activity.this,result,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}