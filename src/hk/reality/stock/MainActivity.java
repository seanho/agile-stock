package hk.reality.stock;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

public class MainActivity extends TabActivity {    
    public static final String TAB_STOCK = "stock";
    public static final String TAB_INDEX = "index";
    public static final String TAB_QUICK = "quick";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
        TabHost host = getTabHost();

        host.addTab(host
                .newTabSpec(TAB_STOCK)
                .setIndicator(getResources().getString(R.string.tab_stock))
                .setContent(new Intent(this, PortfolioActivity.class)));

        host.addTab(host
                .newTabSpec(TAB_INDEX)
                .setIndicator(getResources().getString(R.string.tab_index))
                .setContent(new Intent(this, IndexActivity.class)));
        
//        host.addTab(host
//                .newTabSpec(TAB_QUICK)
//                .setIndicator(getResources().getString(R.string.tab_quick))
//                .setContent(new Intent(this, QuickStockActivity.class)));
    }
}
