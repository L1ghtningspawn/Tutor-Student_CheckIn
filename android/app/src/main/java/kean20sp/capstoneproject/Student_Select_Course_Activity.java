package kean20sp.capstoneproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import kean20sp.capstoneproject.http.TutorCourseListHandler;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.GetTutorInfo;

public class Student_Select_Course_Activity extends AppCompatActivity {

    String[] courses, courseIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__select__course_);

        String student_email = AppState.UserInfo.email;
        String tutor_email;
        tutor_email = GetTutorInfo.get_email(student_email);

        TutorCourseListHandler tclhandler = new TutorCourseListHandler();
        String result = tclhandler.getTutorCourseList(tutor_email);
        if(result.equals(TutorCourseListHandler.SUCCESSFUL)){
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
            courses = tclhandler.courses;
            courseIDs = tclhandler.course_ids;
        } else {
            Toast.makeText(this,result, Toast.LENGTH_SHORT).show();
        }
        List<String> list_courses = Arrays.asList(courses);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_courses);

    }

}
