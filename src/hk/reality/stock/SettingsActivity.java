package hk.reality.stock;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_DISCLAIMER_SHOWN = "disclaimer.shown";
    public static final String KEY_CONCURRENT = "concurrent.number";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    
    public static boolean getDisclaimerShown(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KEY_DISCLAIMER_SHOWN, false);
    }
    
    public static int getConcurrent(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(KEY_CONCURRENT, 4);
    }
    
    public static void setDisclaimerShown(Context context, boolean shown) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putBoolean(KEY_DISCLAIMER_SHOWN, shown).commit();
    }
    
    public static void setConcurrent(Context context, int concurrent) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putInt(KEY_CONCURRENT, concurrent).commit();
    }
}
