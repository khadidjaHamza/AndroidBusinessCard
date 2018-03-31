package nour_b.projet.utils.recordsNFC;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ParsedNdefRecord {

    String str();
    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent,
                        int offset);
}
