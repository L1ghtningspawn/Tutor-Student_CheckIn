package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import kean20sp.capstoneproject.http.LogoutHandler;
import kean20sp.capstoneproject.qrcode.BarcodeCaptureActivity;
import kean20sp.capstoneproject.qrcode.BarcodeCaptureActivity2;
import kean20sp.capstoneproject.util.AppState;

public class Student_Activity extends AppCompatActivity {
    TextView qr_check_in, email_check_in, tutor_mode, logout;
    String session_id;
    String user_email;
    String user_roles;

    public static final int RESULT_CODE_QR_CHECKIN = 314;

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

        session_id = AppState.Session.id;
        user_email = AppState.UserInfo.email;
        user_roles = AppState.UserInfo.roles;

        TextView session_table = (TextView) findViewById(R.id.session_history);
        session_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Activity.this, Student_Tutor_History_Activity.class);
                startActivity(intent);
            }
        });
    }

    public void on_click_tutor_mode(View v){
        if(user_roles.contains("Tu")) {
            Intent it = new Intent(Student_Activity.this, Tutor_Activity.class);
            AppState.Session.id = session_id;
            AppState.UserInfo.roles  = user_roles;
            AppState.UserInfo.email = user_email;
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

    public void qr_check_in(View v){
        Intent intent = new Intent(this, BarcodeCaptureActivity2.class);
        startActivityForResult(intent, RESULT_CODE_QR_CHECKIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == RESULT_CODE_QR_CHECKIN){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    Toast.makeText(this,"A Session Is Being Started...",Toast.LENGTH_SHORT).show();

                    String[] session_info = barcode.displayValue.split("#");
                    AppState.TutorSession.student_id = AppState.UserInfo.user_role_id;
                    AppState.TutorSession.tutor_id = session_info[0];
                    AppState.TutorSession.qr_server_key = session_info[1];

                    //goto course list
                    Intent intent = new Intent(Student_Activity.this, Student_Select_Course_Activity.class);
                    startActivity(intent);
                }
            }
        } else {
            Log.e("Student_Activity.qrcode", CommonStatusCodes.getStatusCodeString(resultCode));
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
