package kean20sp.capstoneproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import kean20sp.capstoneproject.util.AppState;

public class Tutor_Checked_In_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor__checked__in_);

        //R.id.check_out refers to xml
        TextView end_session = (TextView)findViewById(R.id.check_out);
        end_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
