package kean20sp.capstoneproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.util.AppState;
import kean20sp.capstoneproject.util.GetUserInfo;

public class Student_Checked_In_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__checked__in_);

        String tutor_name = GetUserInfo.get_name(AppState.UserInfo.user_role_id);
        String tutor_email = GetUserInfo.get_email(AppState.UserInfo.user_role_id);

        TextView text_name = (TextView)findViewById(R.id.tutor_name);
        text_name.setText(tutor_name);
    }

}
