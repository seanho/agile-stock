package hk.reality.stock;

import hk.reality.stock.view.IndexAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class IndexActivity extends ListActivity {
    public static final String TAG = "IndexActivity";
    private IndexAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.listview);
        
        adapter = new IndexAdapter(this);
        setListAdapter(adapter);
    }
    
    public void updateIndexes() {
        Log.d(TAG, "update index");
    }
}
