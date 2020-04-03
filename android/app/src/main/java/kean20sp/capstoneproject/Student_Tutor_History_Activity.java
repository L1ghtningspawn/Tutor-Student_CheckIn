package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.http.StudSessHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.ViewOptionsUtility;

import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import org.json.JSONArray;
import org.json.JSONObject;

public class Student_Tutor_History_Activity extends AppCompatActivity {
    TableLayout session_table;
    JSONArray session_data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__tutor__history_);

        session_table = (TableLayout) findViewById(R.id.session_table);
        session_table.addView(ViewOptionsUtility.newRow(this,new String[]{"tutor", "time in", "time out"}, true));

        StudSessHandler ssh = new StudSessHandler();
        String status = ssh.getStudentSessionHistory(AppState.UserInfo.user_role_id);
        if(status.equals(StudSessHandler.SUCCESS)){
            session_data = ssh.student_sessions;
        }

        if (session_data.isNull(0)){
            session_table.addView(ViewOptionsUtility.newRow(this,
                    new String[]{
                            "No Session History Exists."
                    }, false));
        } else{
            for(int x = 0; x < session_data.length(); x++){
                try{
                    final JSONObject record = session_data.getJSONObject(x);
                    String name = record.getString("fname") + " " + record.getString("lname");
                    String time_in = record.getString("time_in");
                    String time_out;

                    if (record.isNull("time_out")){
                        time_out = "";
                        AppState.TutorSession.course_id = record.getString("course_id");
                    } else{
                        time_out = record.getString("time_out");
                    };

                    TableRow row = ViewOptionsUtility.newRow(this,
                            new String[]{
                                    name, time_in, time_out
                            }, false);

                    if(record.isNull("time_out")){
                        record.put("time_out","");
                        row.setBackgroundColor(Color.YELLOW);
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //store tutor info
                                try {
                                    AppState.TutorSession.in_datetime = record.getString("time_in");
                                    AppState.TutorSession.tutor_id = record.getString("t_ur_id");
                                } catch(Exception e){
                                    e.printStackTrace();
                                }

                                //go to select-course if no course exists otherwise to checked-in
                                Log.d("issue24", "course_id = "+AppState.TutorSession.course_id);
                                if(AppState.TutorSession.course_id == null || AppState.TutorSession.course_id.equals("4")) {
                                    Intent intent = new Intent(Student_Tutor_History_Activity.this, Student_Select_Course_Activity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(Student_Tutor_History_Activity.this, Student_Checked_In_Activity.class);
                                    startActivity(intent);
                                }
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
    public void onBackPressed() {
        Intent intent = new Intent(Student_Tutor_History_Activity.this, Student_Activity.class);
        startActivity(intent);
    }
}
