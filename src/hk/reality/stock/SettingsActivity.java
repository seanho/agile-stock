package hk.reality.stock;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_DISCLAIMER_SHOWN = "disclaimer.shown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    
    public static boolean getDisclaimerShown(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_DISCLAIMER_SHOWN, false);
    }
    
    public static void setDisclaimerShown(Context context, boolean shown) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putBoolean(KEY_DISCLAIMER_SHOWN, shown).commit();
    }
}
