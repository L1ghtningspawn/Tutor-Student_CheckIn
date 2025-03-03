package kean20sp.capstoneproject.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import kean20sp.capstoneproject.R;
import kean20sp.capstoneproject.views.CourseTextView;

public class ViewOptionsUtility {

    public static int DPUnits(Context context, int dps){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    public static CourseTextView newCourse(Context context, LinearLayout course_list, String value){
        CourseTextView tv = new CourseTextView(context);
        tv.setText(value);

        tv.setLayoutParams(course_list.getLayoutParams());
        tv.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        int dps = ViewOptionsUtility.DPUnits(context,15);
        tv.setPadding(dps,dps,dps,dps);

        course_list.addView(tv);

        //int dps2 = ViewOptionsUtility.DPUnits(context, 5);

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewOptionsUtility.DPUnits(context,1));
        //params.leftMargin = dps2;
        //params.rightMargin = dps2;
        layout.setLayoutParams(params);
        layout.setBackgroundColor(Color.BLACK);


        course_list.addView(layout);

        return tv;
    }

    public static TableRow newRow(Context context, String[] columns, boolean isHeader ){
        TableRow row = new TableRow(context);

        int dps = ViewOptionsUtility.DPUnits(context,10);

        for(String string : columns){
            TextView view1 = new TextView(context);
            view1.setText(string);
            view1.setPadding(dps,dps,dps,dps);
            row.addView(view1);
        }

        row.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        if(isHeader){
            row.setBackgroundResource(R.drawable.rectangle_border);
            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 0.25f);
            row.setLayoutParams(lp);
        }

        return row;
    }

}
