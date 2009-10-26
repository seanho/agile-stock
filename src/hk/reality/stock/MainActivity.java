package hk.reality.stock;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {    
    public static final String TAB_STOCK = "stock";
    public static final String TAB_INDEX = "index";    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabHost host = getTabHost();
        
        Drawable wSelector = getResources().getDrawable(R.drawable.wand);
        host.addTab(host
                .newTabSpec(TAB_STOCK)
                .setIndicator(getResources().getString(R.string.tab_stock), wSelector)
                .setContent(new Intent(this, PortfolioActivity.class)));

        Drawable wSelector = getResources().getDrawable(R.drawable.wand);
        host.addTab(host
                .newTabSpec(TAB_INDEX)
                .setIndicator(getResources().getString(R.string.tab_index), bmSelector)
                .setContent(new Intent(this, IndexActivity.class)));

    }
}
