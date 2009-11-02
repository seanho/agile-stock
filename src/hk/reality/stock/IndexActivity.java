package hk.reality.stock;

import hk.reality.stock.service.fetcher.IndexesUpdateTask;
import hk.reality.stock.view.IndexAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

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
        
        TextView empty = (TextView) findViewById(android.R.id.empty);
        empty.setText(R.string.msg_loading);
        
        Log.i(TAG, "start index activity");
        IndexesUpdateTask task = new IndexesUpdateTask(this);
        task.execute();
    }
    
    public IndexAdapter getIndexAdapter() {
        return adapter;
    }
}
