package hk.reality.stock;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class IndexActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.listview);
        
        
    }
}
