package kean20sp.capstoneproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import kean20sp.capstoneproject.util.ViewOptionsUtility;

import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Student_Tutor_History_Activity extends AppCompatActivity {
    TableLayout session_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__tutor__history_);

        session_table = (TableLayout) findViewById(R.id.session_table);
        session_table.addView(ViewOptionsUtility.newRow(this,new String[]{"date", "tutor", "course"}, true));
        session_table.addView(ViewOptionsUtility.newRow(this,
                new String[]{"01/01/2020",
                        "FartFace McTutor",
                        "Sex Ed 143 hitler studies"},
                false));
    }

}
