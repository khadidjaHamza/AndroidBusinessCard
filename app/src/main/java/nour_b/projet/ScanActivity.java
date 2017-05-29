package nour_b.projet;

/**
 * Created by Yasmine on 19/05/2017.
 */
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.zxing.Result;
import android.app.AlertDialog;
import android.widget.ImageView;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import nour_b.projet.model.Card;
import nour_b.projet.utils.QRCodeHandler;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private int size = 600;

    private ImageView qr_code;
    private Card card;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        mScannerView = new ZXingScannerView(this); // Programmatically initialize the scanner view

        if(!getIntent().getExtras().getBoolean("generate")){
            setContentView(mScannerView);      // Set the scanner view as the content view
        } else if (getIntent().getSerializableExtra("Card") != null) {
            setContentView(R.layout.qr_code);
            try {
                Bitmap bitMatrix = QRCodeHandler.generateMatrix(getIntent().getSerializableExtra("Card").toString());
                qr_code = (ImageView) findViewById(R.id.qr_code_generation);
                qr_code.setImageBitmap(bitMatrix);
                card = (Card) getIntent().getSerializableExtra("Card");
                String str = card.toString(getApplicationContext());
                Log.i("Simple TEST ", str);
                Log.i("SimpleQrcodeGenerator ","FIN");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        Log.w("handleResult", result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
