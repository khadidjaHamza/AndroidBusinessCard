package nour_b.projet;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import java.io.File;

import nour_b.projet.localDatabase.DBRegister;
import nour_b.projet.model.User;
import nour_b.projet.utils.SMSHandler;

import static nour_b.projet.utils.ErrorMessages.pbGeolocalisation;
import static nour_b.projet.utils.GPSHandler.getGeoLocation;

public class PersonalCardActivity extends AppCompatActivity {

    ImageView photo;
    TextView address;
    TextView mail;
    TextView name;
    TextView surname;
    TextView birth;
    TextView phone1;
    TextView phone2;
    TextView website;

    ImageView qr_code;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_card);

        photo = (ImageView) findViewById(R.id.photo);
        name = (TextView) findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        mail = (TextView) findViewById(R.id.email);
        birth = (TextView) findViewById(R.id.birth);
        address = (TextView) findViewById(R.id.address);
        phone1 = (TextView) findViewById(R.id.phone1);
        phone2 = (TextView) findViewById(R.id.phone2);
        website = (TextView) findViewById(R.id.site);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("eMAIL")!= null) {

            DBRegister db = new DBRegister(this);
            User u = db.getUser(bundle.getString("eMAIL"));

            if(u.getPhoto() != null) {
                File imgFile = new File(u.getPhoto());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    photo.setImageBitmap(myBitmap);
                }
            }

            name.setText(u.getName());
            surname.setText(u.getSurname());
            mail.setText(u.getMail());
            birth.setText(u.getBirth());
            address.setText(u.getAddress());
            phone1.setText(u.getTel1());
            phone2.setText(u.getTel2());
            website.setText(u.getWebsite());
        }

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(getGeoLocation(address.getText().toString()));
                } catch (android.content.ActivityNotFoundException ex) {
                    pbGeolocalisation(getApplicationContext());
                }
            }
        });

        qr_code = (ImageView) findViewById(R.id.qr_code);

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   // startActivity();
                } catch (android.content.ActivityNotFoundException ex) {
                  //
                }
            }
        });
    }

    // Mise en place du menu dans l'ActionBar (items répertoriés dans menu_main.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Gestion des actions à réaliser selon choix d'un item du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_sms) {
            try {
                startActivityForResult(SMSHandler.getContact(), SMSHandler.PICK_CONTACT_REQUEST);
                onActivityResult(SMSHandler.PICK_CONTACT_REQUEST,SMSHandler.CONTACT_PICKER , SMSHandler.getContact());
                Toast.makeText(PersonalCardActivity.this, "Finished sending SMS...", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(PersonalCardActivity.this, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(PersonalCardActivity.this, RegisterActivity.class);
            intent.putExtra("eMAIL", mail.getText().toString());
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_login) {
            MainActivity.LOGIN = false;
            Intent intent = new Intent(PersonalCardActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        // check whether the result is ok
        Log.i("le resultaaa===> ", "" + resultCode);
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple
            // startActivityForReslut
            switch (requestCode) {
                case SMSHandler.CONTACT_PICKER:
                    ContentResolver cr = getContentResolver();
                    String tel = SMSHandler.contactPicked(data, cr);
                    Log.i("le tel est ","==>"+tel);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
                  //  smsIntent.get
                    startActivity(smsIntent);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }



}
