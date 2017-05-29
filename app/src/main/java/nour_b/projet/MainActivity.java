package nour_b.projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nour_b.projet.localDatabase.DBRegister;
import nour_b.projet.utils.ErrorMessages;

public class MainActivity extends AppCompatActivity {

    public static boolean LOGIN = false;

    private Button login_button;
    private EditText login_email;
    private EditText login_password;

    private TextView login_register_link;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = (Button) findViewById(R.id.login_button);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBRegister db = new DBRegister(getApplicationContext());
                if(db.login(login_email.getText().toString(), login_password.getText().toString())) {
                    LOGIN = true;
                    Intent intent = new Intent(MainActivity.this, PersonalCardActivity.class);
                    intent.putExtra("eMAIL", login_email.getText().toString());
                    startActivity(intent);
                } else {
                    ErrorMessages.pbLogin(getApplicationContext());
                }
            }
        });


        login_register_link = (TextView) findViewById(R.id.login_register_link);
        login_register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
