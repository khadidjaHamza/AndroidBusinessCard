package nour_b.projet.utils;

import android.content.Intent;
import android.net.Uri;

public class GPSHandler {

    public static Intent getGeoLocation(String address) {
        address = address.replace(' ', '+');
        Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + address));
        return geoIntent;
    }
}
