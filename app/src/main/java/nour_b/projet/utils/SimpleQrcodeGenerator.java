package nour_b.projet.utils;

/**
 * Created by Yasmine on 29/05/2017.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class SimpleQrcodeGenerator {

    public static BitMatrix generateMatrix(final String data, final int size) throws WriterException {
        final BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
        return bitMatrix;
    }

    public static void writeImage(final String outputFileName, final String imageFormat, final BitMatrix bitMatrix)
            throws FileNotFoundException,
            IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFileName));
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, fileOutputStream);
        fileOutputStream.close();
    }
}