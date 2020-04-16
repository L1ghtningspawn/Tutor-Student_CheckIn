package kean20sp.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kean20sp.capstoneproject.http.TutorCourseListHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.GetUserInfo;
import kean20sp.capstoneproject.util.ViewOptionsUtility;
import kean20sp.capstoneproject.views.CourseTextView;

public class Student_Checked_In_Activity extends AppCompatActivity {
    LinearLayout courses_list;
    TutorCourseListHandler tclh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__checked__in_);

        final String student_name = GetUserInfo.get_name(AppState.UserInfo.user_role_id);
        String tutor_name = GetUserInfo.get_name(AppState.TutorSession.tutor_id);
        final String tutor_email = GetUserInfo.get_email(AppState.UserInfo.user_role_id);

        TextView text_name = (TextView) findViewById(R.id.tutor_name);
        text_name.setText(tutor_name);

        courses_list = findViewById(R.id.courses_list);

        tclh = new TutorCourseListHandler();
        tclh.getTutorCourseList(AppState.TutorSession.tutor_id);

        for (int x = 0; x < tclh.size(); x++) {
            String course = tclh.getCourse(x);
            CourseTextView tv = ViewOptionsUtility.newCourse(this, courses_list, course);
            tv.course_id = tclh.getCourseID(x);
        }

        TextView session_history = (TextView) findViewById(R.id.session_history);
        session_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Checked_In_Activity.this, Student_Tutor_History_Activity.class);
                startActivity(intent);
            }
        });

        ImageView email_image = (ImageView) findViewById(R.id.email);
        email_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{tutor_email});
                intent.putExtra(Intent.EXTRA_SUBJECT, student_name + ": Tutoring Question");
                try {
                    startActivity(Intent.createChooser(intent, "Sending mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Student_Checked_In_Activity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume(){
        AppState.Session.activity_id = "Student_Checked_In_Activity";
        AppState.IO.write(this);
        super.onResume();
    }
}