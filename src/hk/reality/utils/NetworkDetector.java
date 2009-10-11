package hk.reality.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

public class NetworkDetector {
    private static final String TAG = "NetworkDetector";
    public static boolean hasValidNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            Log.d(TAG, "no network found");
            return false;
        } else if (info.getState().equals(State.CONNECTED)) {
            Log.d(TAG, "network state = CONNECTED");
            return true;
        } else {
            Log.d(TAG, "network state != CONNECTED, " + info.getState().toString());
            return false;
        }
    }
}
