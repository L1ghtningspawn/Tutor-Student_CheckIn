package kean20sp.capstoneproject;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kean20sp.capstoneproject.util.CheckUserInput;

public class signup extends AppCompatActivity
{
    EditText mail,pswd;
    TextView lin,sup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sup = (TextView) findViewById(R.id.sup);
        lin = (TextView) findViewById(R.id.lin);
        pswd = (EditText) findViewById(R.id.pswrdd);
        mail = (EditText) findViewById(R.id.mail);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        sup.setTypeface(custom_font1);
        pswd.setTypeface(custom_font);
        lin.setTypeface(custom_font);
        mail.setTypeface(custom_font);
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean isGoodInput = true;
                if(!CheckUserInput.isValidEmail(mail.getText().toString())){
                    Toast.makeText(v.getContext(),"That Email is Not Valid",Toast.LENGTH_SHORT).show();
                    isGoodInput = false;
                }
                if(!CheckUserInput.isValidPassword(pswd.getText().toString())) {
                    Toast.makeText(v.getContext(), "That Password is Not Valid", Toast.LENGTH_SHORT).show();
                    isGoodInput = false;
                }

                if(isGoodInput) {
                    Intent it = new Intent(signup.this, signup2.class);
                    it.putExtra("email", mail.getText().toString());
                    it.putExtra("password", pswd.getText().toString());
                    startActivity(it);
                }
            }
        });
        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(signup.this,login.class);
                startActivity(it);
            }
        });
    }
}
