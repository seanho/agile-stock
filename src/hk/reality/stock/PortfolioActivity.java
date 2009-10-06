package hk.reality.stock;

import android.app.ListActivity;
import android.os.Bundle;

public class PortfolioActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}