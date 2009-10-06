package hk.reality.stock;

import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import hk.reality.stock.view.StockAdapter;

import java.math.BigDecimal;
import java.util.Calendar;

import android.app.ListActivity;
import android.os.Bundle;

public class PortfolioActivity extends ListActivity {
    private StockAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        adapter = new StockAdapter(this);
        setListAdapter(adapter);
        
        Stock stock = new Stock();
        stock.setQuote("0005");
        
        StockDetail d = new StockDetail();
        d.setQuote("0005");
        d.setChangePrice(new BigDecimal("+1.2"));
        d.setChangePricePercent(new BigDecimal("+0.8"));
        d.setPrice(new BigDecimal("88.6"));
        d.setName("HSBC");
        d.setUpdatedAt(Calendar.getInstance());
        stock.setDetail(d);

        adapter.add(stock);
    }
}