package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.http.CheckinHandler;
import kean20sp.capstoneproject.views.CourseTextView;
import kean20sp.capstoneproject.http.TutorCourseListHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.ViewOptionsUtility;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Student_Select_Course_Activity extends AppCompatActivity {
    LinearLayout course_list;
    TutorCourseListHandler tclh;
    TextView courseview_selected = null;
    String course_id = null;


    String[] courses, courseIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select__course_);

        course_list = findViewById(R.id.course_list);

        tclh = new TutorCourseListHandler();
        tclh.getTutorCourseList(AppState.TutorSession.tutor_id);

        for(int x=0; x< tclh.size(); x++){
        //for(int x = 0; x<10;x++){
            String course = tclh.getCourse(x);
            CourseTextView tv = ViewOptionsUtility.newCourse(this,course_list,course);
            tv.course_id = tclh.getCourseID(x);

            tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    CourseTextView tv = (CourseTextView) v;
                    if(courseview_selected != null){
                        courseview_selected.setBackgroundColor(Color.TRANSPARENT);
                        courseview_selected.setTextColor(Color.BLACK);
                    }
                    courseview_selected = tv;
                    course_id = tv.course_id;
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                }
            });
        }

        TextView button = (TextView) findViewById(R.id.select_course_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState.TutorSession.course_id = course_id;
                String status = "";

                if(AppState.TutorSession.qr_server_key != null) {
                    CheckinHandler ch = new CheckinHandler();
                    status = ch.checkin_qr(
                            AppState.TutorSession.tutor_id,
                            AppState.TutorSession.student_id,
                            AppState.TutorSession.qr_server_key,
                            AppState.TutorSession.course_id
                    );

                    Log.d("status",status);
                } else {
                    CheckinHandler ch = new CheckinHandler();
                    status = ch.checkin_email(
                            AppState.TutorSession.tutor_id,
                            AppState.UserInfo.user_role_id,
                            AppState.TutorSession.course_id
                    );
                    Log.d("status",status);
                }

                if(status.equals(CheckinHandler.CHECKIN_SUCCESSFUL)){
                    Intent intent = new Intent(Student_Select_Course_Activity.this, Student_Checked_In_Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Student_Select_Course_Activity.this,"Check-In Failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
