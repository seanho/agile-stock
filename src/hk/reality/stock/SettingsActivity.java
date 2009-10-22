package hk.reality.stock;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_DISCLAIMER_SHOWN = "disclaimer.shown";
    public static final String KEY_CONCURRENT = "concurrent.number";
    public static final String KEY_HTTP_TIMEOUT = "http.timeout";
    
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
    
    /**
     * get Http Timeout, in seconds
     * @param context
     * @return
     */
    public static int getHttpTimeout(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(KEY_HTTP_TIMEOUT, 10);
    }
    
    /**
     * set http timeout, in deconds
     * @param context
     * @param timeout
     */
    public static void setHttpTimeout(Context context, int timeout) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putInt(KEY_HTTP_TIMEOUT, timeout).commit();
    }
}
