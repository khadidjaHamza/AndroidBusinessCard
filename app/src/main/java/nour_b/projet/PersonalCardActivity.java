package nour_b.projet;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.BitMatrix;

import java.io.File;

import nour_b.projet.localDatabase.DBRegister;
import nour_b.projet.model.Card;
import nour_b.projet.utils.SMSHandler;
import nour_b.projet.utils.SimpleQrcodeGenerator;

import static nour_b.projet.utils.DataCardHandler.setTextViewPersonalCard;
import static nour_b.projet.utils.ErrorMessages.pbGeolocalisation;
import static nour_b.projet.utils.GPSHandler.getGeoLocation;

public class PersonalCardActivity extends AppCompatActivity {

    Card card;

    ImageView photo;
    TextView address;
    TextView mail;
    TextView name;
    TextView surname;
    TextView phone1;
    TextView phone2;
    TextView website;

    ImageView qr_code;

    DBRegister db;

    boolean sms = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_card);

        photo = (ImageView) findViewById(R.id.photo);
        name = (TextView) findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        mail = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        phone1 = (TextView) findViewById(R.id.phone1);
        phone2 = (TextView) findViewById(R.id.phone2);
        website = (TextView) findViewById(R.id.site);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("eMAIL")!= null) {
            db = new DBRegister(this);
            card = db.getCard(bundle.getString("eMAIL"));

            if(card.getPhoto() != null) {
                File imgFile = new File(card.getPhoto());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    photo.setImageBitmap(myBitmap);
                }
            }

            name.setText(card.getName());
            surname.setText(card.getSurname());

            setTextViewPersonalCard(getApplicationContext(), address, phone1, phone2, mail, website);
            mail.setText(card.getMail());
            address.setText(card.getAddress());
            phone1.setText(card.getTel1());
            phone2.setText(card.getTel2());
            website.setText(card.getWebsite());
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
                    int size = 600;

                    Bitmap bitMatrix = SimpleQrcodeGenerator.generateMatrix(card.toString(), size);
                    qr_code.setImageBitmap(bitMatrix);

                    Log.i("SimpleQrcodeGenerator ","FIN");

                } catch (Exception ex) {
                    ex.printStackTrace();
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
            sms = true;
            try {
                startActivityForResult(SMSHandler.getContact(), SMSHandler.PICK_CONTACT_REQUEST);
                onActivityResult(SMSHandler.PICK_CONTACT_REQUEST,SMSHandler.CONTACT_PICKER , SMSHandler.getContact());
                Toast.makeText(PersonalCardActivity.this, "Finished sending SMS...", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(PersonalCardActivity.this, "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_import) {
            sms = false;
            try {
                startActivityForResult(SMSHandler.getContact(), SMSHandler.PICK_CONTACT_REQUEST);
                onActivityResult(SMSHandler.PICK_CONTACT_REQUEST,SMSHandler.CONTACT_PICKER , SMSHandler.getContact());
                Toast.makeText(PersonalCardActivity.this, "Finished importing contact...", Toast.LENGTH_SHORT).show();
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(PersonalCardActivity.this, "import contact faild, please try again later.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if(id == R.id.action_scan) {
            try {
                Intent intent = new Intent(PersonalCardActivity.this, ScanActivity.class);
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SMSHandler.CONTACT_PICKER :
                    if(sms){
                        ContentResolver cr = getContentResolver();
                        String tel = SMSHandler.contactPicked(data, cr).getTel1();
                        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
                        smsIntent.putExtra("sms_body", card.toString(getApplicationContext()));
                        startActivity(smsIntent);
                        break;
                    } else {
                        ContentResolver cr = getContentResolver();
                        Card card = SMSHandler.contactPicked(data, cr);
                        name.setText(card.getName());
                        surname.setText(card.getSurname());
                        mail.setText(card.getMail());
                        address.setText(card.getAddress());
                        phone1.setText(card.getTel1());
                        phone2.setText(card.getTel2());
                        website.setText(card.getWebsite());
                     //   db.storeCard(card);
                        break;
                    }
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }
}
