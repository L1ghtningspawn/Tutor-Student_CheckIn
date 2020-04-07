package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.http.StudSessHandler;
import kean20sp.capstoneproject.http.TuSessHandler;
import kean20sp.capstoneproject.http.TutorEndSessionHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.ViewOptionsUtility;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Tutor_Active_Session_Activity extends AppCompatActivity {
    TableLayout session_table;
    JSONArray session_data = null;
    boolean session_ended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor__active__session_);
        session_ended = getIntent().getBooleanExtra("session_ended",false);

        //AppState.Debug.log_All();

        session_table = (TableLayout) findViewById(R.id.session_table);
        session_table.addView(ViewOptionsUtility.newRow(this,new String[]{"student", "time in", "time out"}, true));

        TuSessHandler ssh = new TuSessHandler();
        String status = ssh.getTutorSessionHistory(AppState.UserInfo.user_role_id);
        if(status.equals(StudSessHandler.SUCCESS)){
            session_data = ssh.tutor_sessions;
        }

        final JSONObject session_record;
        if (session_data.isNull(0)){
            session_table.addView(ViewOptionsUtility.newRow(this,
                    new String[]{
                            "No Session History Exists."
                    }, false));
        } else {
            for(int x = 0; x < session_data.length(); x++){

                try{
                    final JSONObject record = session_data.getJSONObject(x);

                    String name = record.getString("fname") + " " + record.getString("lname");
                    String time_in = record.getString("time_in");
                    String time_out;

                    if (record.isNull("time_out")){
                        time_out = "END";
                    } else{
                        time_out = record.getString("time_out");
                    };

                    TableRow row = ViewOptionsUtility.newRow(this,
                            new String[]{
                                    name, time_in, time_out
                            }, false);

                    if(record.isNull("time_out")){
                        row.setBackgroundColor(Color.YELLOW);
                        row.getChildAt(2).setBackgroundColor(Color.RED);
                        ((TextView) row.getChildAt(2)).setTextColor(Color.WHITE);
                        row.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    TutorEndSessionHandler tesh = new TutorEndSessionHandler();
                                    String student_id = record.getString("s_ur_id");
                                    String tutor_id = AppState.UserInfo.user_role_id;
                                    String result = tesh.Individual_Session_End(student_id,tutor_id);
                                    if(result.equals(TutorEndSessionHandler.SESSION_END_SUCCESS)){
                                        Intent it = new Intent(Tutor_Active_Session_Activity.this, Tutor_Active_Session_Activity.class);
                                        it.putExtra("session_ended",true);
                                        startActivity(it);
                                        overridePendingTransition(0,0);
                                    } else {
                                        Toast.makeText(Tutor_Active_Session_Activity.this,
                                                        result,
                                                        Toast.LENGTH_SHORT).show();
                                    }
                                } catch(Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(Tutor_Active_Session_Activity.this,"Something Unexpected Happened!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //store tutor info
                                try {
                                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = date_format.parse(record.getString("time_in"));
                                    String unix_time = String.valueOf(date.getTime() / 1000);

                                    AppState.TutorSession.in_datetime = unix_time;
                                    AppState.TutorSession.student_id = record.getString("s_ur_id");
                                } catch(Exception e){
                                    e.printStackTrace();
                                }

                                //go to select-course if no course exists otherwise to checked-in
                                Intent intent = new Intent(Tutor_Active_Session_Activity.this, Tutor_Checked_In_Activity.class);
                                startActivity(intent);
                            }
                        });
                    }

                    session_table.addView(row);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        //AppState.Debug.log_All();
    }

    @Override
    public void onBackPressed(){
        if(session_ended){
            Intent intent = new Intent(Tutor_Active_Session_Activity.this, CheckIn_Activity.class);
            intent.putExtra("session_ended",session_ended);
            startActivity(intent);
        }else{
            super.onBackPressed();
        }
    }

}