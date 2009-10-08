package hk.reality.stock.view;

import hk.reality.stock.R;
import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StockAdapter extends ArrayAdapter<Stock> {
    private java.text.DateFormat formatter;
    public StockAdapter(Context context) {
        super(context, 0);
        formatter = DateFormat.getTimeFormat(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (View) vi.inflate(R.layout.stock_item, null);
        }

        // prepare views
        TextView name = (TextView) v.findViewById(R.id.name);       
        TextView quote = (TextView) v.findViewById(R.id.quote);
        TextView price = (TextView) v.findViewById(R.id.price);
        TextView change = (TextView) v.findViewById(R.id.change);
        TextView volume = (TextView) v.findViewById(R.id.volume);
        TextView time = (TextView) v.findViewById(R.id.time);
        
        // set data
        Stock stock = getItem(position);
        StockDetail detail = stock.getDetail();
        if (detail != null) {
            time.setText(formatter.format(detail.getUpdatedAt().getTime()));
            volume.setText(detail.getVolume());
            name.setText(stock.getName());
            quote.setText(detail.getQuote());
            price.setText(String.format("%.03f", detail.getPrice()));
            change.setText(String.format("%+.03f (%.03f%%)", detail.getChangePrice(), detail.getChangePricePercent()));
            
            if (detail.getChangePrice().floatValue() > 0) {
                price.setTextColor(Color.rgb(0, 213, 65));
                change.setTextColor(Color.rgb(0, 213, 65));
            } else if (detail.getChangePrice().floatValue() < 0) {
                price.setTextColor(Color.rgb(238, 30, 0));
                change.setTextColor(Color.rgb(238, 30, 0));
            } else {
                price.setTextColor(Color.WHITE);
                change.setTextColor(Color.WHITE);
            }
        } else {
            time.setText("");
            volume.setText("---");
            name.setText(stock.getName());
            quote.setText(stock.getQuote());
            price.setText("----");
            change.setText("---- (---)");
        }        
        return v;
    }
}
