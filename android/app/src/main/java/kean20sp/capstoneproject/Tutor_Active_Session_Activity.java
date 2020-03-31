package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.http.StudSessHandler;
import kean20sp.capstoneproject.http.TuSessHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.ViewOptionsUtility;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONObject;

public class Tutor_Active_Session_Activity extends AppCompatActivity {
    TableLayout session_table;
    JSONArray session_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor__active__session_);

        session_table = (TableLayout) findViewById(R.id.session_table);
        session_table.addView(ViewOptionsUtility.newRow(this,new String[]{"student", "time in", "time out"}, true));

        TuSessHandler ssh = new TuSessHandler();
        String status = ssh.getTutorSessionHistory(AppState.UserInfo.user_role_id);
        if(status.equals(StudSessHandler.SUCCESS)){
            session_data = ssh.tutor_sessions;
        }

        if(session_data == null){
            session_table.addView(ViewOptionsUtility.newRow(this,
                    new String[]{
                        "Oops! Something is borken. Restart the app and try again."
                    }, false));
        } else if(session_data.length() == 0){
            session_table.addView(ViewOptionsUtility.newRow(this,
                    new String[]{
                            "No Sessions Exist."
                    }, false));
        } else {
            for(int x = 0; x < session_data.length(); x++){
                try{
                    final JSONObject record = session_data.getJSONObject(x);

                    String name = record.getString("fname") + " " + record.getString("lname");
                    String time_in = record.getString("time_in");
                    String time_out;

                    if (record.isNull("time_out")){
                        time_out = "";
                    } else{
                        time_out = record.getString("time_out");
                    };

                    TableRow row = ViewOptionsUtility.newRow(this,
                            new String[]{
                                    name, time_in, time_out
                            }, false);
                    if(record.isNull("time_out")){
                        row.setBackgroundColor(Color.YELLOW);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //store tutor info
                                try {
                                    AppState.TutorSession.in_datetime = record.getString("time_in");
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
    }

}