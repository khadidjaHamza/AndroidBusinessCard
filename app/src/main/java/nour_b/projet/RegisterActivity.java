package nour_b.projet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import nour_b.projet.localDatabase.DBRegister;
import nour_b.projet.model.Card;

import static nour_b.projet.MainActivity.LOGIN;
import static nour_b.projet.utils.ErrorMessages.*;
import static nour_b.projet.utils.MediaHandler.*;
import static nour_b.projet.utils.DataCardHandler.*;

public class RegisterActivity extends AppCompatActivity {

    String photo;
    private ImageView register_photo;
    private EditText register_name;
    private EditText register_surname;
    private EditText register_email;
    private EditText register_email_verif;
    private EditText register_password;
    private EditText register_password_verif;
    private EditText register_address;
    private EditText register_phone1;
    private EditText register_phone2;
    private EditText register_website;
    private TextView title;

    private CheckBox checkBox_mail;
    private CheckBox checkBox_site;
    private CheckBox checkBox_address;
    private CheckBox checkBox_tel1;
    private CheckBox checkBox_tel2;


    private Button register_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        title = (TextView) findViewById(R.id.title);

        register_photo = (ImageView) findViewById(R.id.register_photo);
        register_name = (EditText) findViewById(R.id.register_name);
        register_surname = (EditText) findViewById(R.id.register_surname);
        register_email = (EditText) findViewById(R.id.register_email);
        register_email_verif = (EditText) findViewById(R.id.register_email_verif);
        register_password = (EditText) findViewById(R.id.register_password);
        register_password_verif = (EditText) findViewById(R.id.register_password_verif);
        register_address = (EditText) findViewById(R.id.register_address);
        register_phone1 = (EditText) findViewById(R.id.register_phone1);
        register_phone2 = (EditText) findViewById(R.id.register_phone2);
        register_website = (EditText) findViewById(R.id.register_site);

        checkBox_mail = (CheckBox) findViewById(R.id.checkBox_mail);
        checkBox_site = (CheckBox) findViewById(R.id.checkBox_site);
        checkBox_address = (CheckBox) findViewById(R.id.checkBox_address);
        checkBox_tel1 = (CheckBox) findViewById(R.id.checkBox_tel1);
        checkBox_tel2 = (CheckBox) findViewById(R.id.checkBox_tel2);

        register_ok = (Button) findViewById(R.id.register_ok);

        if(LOGIN) {

            title.setText(R.string.title_editing);
            register_ok.setText(R.string.button_editing);

            Bundle bundle = getIntent().getExtras();

            if(bundle.getString("eMAIL")!= null) {

                setStateCheckedBox(getApplicationContext(), checkBox_address, checkBox_tel1, checkBox_tel2, checkBox_mail, checkBox_site);

                DBRegister db = new DBRegister(this);
                Card card = db.getCard(bundle.getString("eMAIL"));

                if(card.getPhoto() != null) {
                    File imgFile = new  File(card.getPhoto().toString());
                    if(imgFile.exists()){
                        Bitmap myBitmap = decodeFile(imgFile.getAbsolutePath());
                        register_photo.setImageBitmap(myBitmap);
                    }
                }

                register_name.setText(card.getName());
                register_surname.setText(card.getSurname());
                register_email.setText(card.getMail());
                register_email_verif.setText(card.getMail());
                register_password.setText(card.getPassword());
                register_password_verif.setText(card.getPassword());
                register_address.setText(card.getAddress());
                register_phone1.setText(card.getTel1());
                register_phone2.setText(card.getTel2());
                register_website.setText(card.getWebsite());


            }
        } else {
            title.setText(R.string.title_register);
        }

        register_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoFromGallery(RegisterActivity.this);
            }
        });

        register_ok.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                String name = register_name.getText().toString();
                String surname = register_surname.getText().toString();
                String email = register_email.getText().toString();
                String email_verif = register_email_verif.getText().toString();
                String password = register_password.getText().toString();
                String password_verif = register_password_verif.getText().toString();
                String address = register_address.getText().toString();
                String phone1 = register_phone1.getText().toString();
                String phone2 = register_phone2.getText().toString();
                String website = register_website.getText().toString();

                boolean [] validation = {   textValidationMandatory(name),textValidationMandatory(surname),
                                            mailValidation(email, email_verif), passwordValidation(password, password_verif),
                                            textValidation(address), telValidation(phone1), telValidation(phone2),
                                            textValidation(website) } ;

                if(registerOk(validation)) {
                    Card card = new Card(email, password, name, surname);
                    card.setPhoto(photo);
                    card.setAddress(address);
                    card.setTel1(phone1);
                    card.setTel2(phone2);
                    card.setWebsite(website);

                    DBRegister db = new DBRegister(getApplicationContext());
                    if(db.exist(email)) {
                        db.updateCard(card);
                    } else {
                        db.storeCard(card);
                    }

                    LOGIN = true ;
                    Intent i = new Intent(RegisterActivity.this, PersonalCardActivity.class);
                    i.putExtra("eMAIL", card.getMail());
                    startActivity(i);
                } else {
                    printErrorValidation(getApplicationContext(), validation);
                }

                storeStateCheckedBox(getApplicationContext(), checkBox_address, checkBox_tel1, checkBox_tel2, checkBox_mail, checkBox_site);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    try {
                        register_photo.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), imageReturnedIntent.getData()));
                        photo = getRealPathFromURI(getApplicationContext(), imageReturnedIntent.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError outOfMemoryError) {
                        outOfMemoryError.printStackTrace();
                    }
                }
                break;
        }
    }

    // Mise en place du menu dans l'ActionBar (items répertoriés dans menu_main.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LOGIN){
            getMenuInflater().inflate(R.menu.menu_editing, menu);
        }
        return true;
    }

    // Gestion des actions à réaliser selon choix d'un item du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_card) {
            if(LOGIN) {
                Intent intent = new Intent(RegisterActivity.this, PersonalCardActivity.class);
                intent.putExtra("eMAIL", register_email.getText().toString());
                startActivity(intent);
                finish();
            } else {
                pbConnexion(this);
            }
            return true;
        }

        if (id == R.id.action_login) {
            LOGIN = false;
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
