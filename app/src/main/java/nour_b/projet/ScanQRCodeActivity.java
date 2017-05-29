package nour_b.projet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.Result;
import android.widget.ImageView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import nour_b.projet.model.Card;
import nour_b.projet.utils.QRCodeHandler;

import static nour_b.projet.model.Card.getCardObject;

public class ScanQRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private ImageView qr_code;
    private Card card;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        mScannerView = new ZXingScannerView(this);

        if(!getIntent().getExtras().getBoolean("generate")){
            setContentView(mScannerView);

        } else if (getIntent().getSerializableExtra("Card") != null) {
            setContentView(R.layout.activity_qr_code);
            try {
                card = (Card) getIntent().getSerializableExtra("Card");
                Bitmap bitMatrix = QRCodeHandler.generateMatrix(card.toString(getApplicationContext()));
                qr_code = (ImageView) findViewById(R.id.qr_code_generation);
                qr_code.setImageBitmap(bitMatrix);
                card = (Card) getIntent().getSerializableExtra("Card");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Card card_result = getCardObject(result.getText().toString());
        Intent intent = new Intent(ScanQRCodeActivity.this, PersonalCardActivity.class);
        intent.putExtra("card", card_result);
        startActivity(intent);
    }
}
