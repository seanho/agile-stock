package hk.reality.stock;

import hk.reality.stock.model.Stock;
import hk.reality.stock.service.fetcher.QuoteUpdateTask;
import hk.reality.stock.service.searcher.StockSearchTask;
import hk.reality.stock.view.StockAdapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

public class PortfolioActivity extends ListActivity {
    private static final String TAG = "PortfolioActivity";

    public static final int DIALOG_ADD_STOCK = 100;
    public static final int DIALOG_ADD_IN_PROGRESS = 101;
    public static final int DIALOG_ERR_DOWNLOAD = 400;
    public static final int DIALOG_ERR_QUOTE = 401;
    public static final int DIALOG_ERR_QUOTE_UPDATE = 402;
    public static final int ID_EDIT_VIEW = 1200000;
    public static final int MENU_OPEN = 0; 
    public static final int MENU_DEL = 1;

    private StockAdapter adapter;
    private StockSearchTask searchTask;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        adapter = new StockAdapter(this);        
        setListAdapter(adapter);
        refreshStockList();
    }

    public void refreshStockList() {
        Log.d(TAG, "refresh stock list");
        List<Stock> stocks = StockApplication.getCurrentPortfolio().getStocks();
        adapter.clear();
        for(Stock s : stocks) {
            adapter.add(s);
        }
    }
    
    public void updateStocks() {
        Log.d(TAG, "update stock quote");
        List<Stock> stocks = StockApplication.getCurrentPortfolio().getStocks();            
        QuoteUpdateTask task = new QuoteUpdateTask(this);
        task.execute(stocks.toArray(new Stock[0]));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.getItem(0).setIcon(R.drawable.ic_menu_rotate);
        menu.getItem(1).setIcon(R.drawable.ic_menu_add);        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh:
            updateStocks();
            return true;
        case R.id.add:
            this.showDialog(DIALOG_ADD_STOCK);
            return true;
        default:
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_ERR_DOWNLOAD:
            AlertDialog downloadErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_download)
                .setMessage(R.string.msg_error_download_details)
                .setCancelable(true)
                .create();
            return downloadErrDialog;
        case DIALOG_ERR_QUOTE:
            final AlertDialog quoteErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_unexpected)
                .setMessage(R.string.msg_error_unexpected_details)
                .setCancelable(true)
                .create();
            return quoteErrDialog;
        case DIALOG_ERR_QUOTE_UPDATE:
            final AlertDialog quoteUpdateErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_stock)
                .setMessage(R.string.msg_error_stock_details)
                .setCancelable(true)
                .create();
            return quoteUpdateErrDialog;
        case DIALOG_ADD_IN_PROGRESS:
            ProgressDialog pd = new ProgressDialog(this);
            pd.setIndeterminate(true);
            pd.setMessage(getResources().getString(R.string.msg_val_stock));
            pd.setCancelable(false);
            return pd;
        case DIALOG_ADD_STOCK:
            final EditText input = new EditText(this);
            input.setId(ID_EDIT_VIEW);
            AlertDialog addDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.add_stock)
                .setMessage(R.string.add_stock_detail)
                .setCancelable(true)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        Log.d(TAG, "entered quote: " + value);
                        searchTask = new StockSearchTask(PortfolioActivity.this);
                        searchTask.execute("CHI", value);
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(TAG, "cancelled");
                    }
                })
                .setView(input)
                .create();
            return addDialog;
        default:
        }
        return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog d) {
        switch (id) {
        case DIALOG_ADD_STOCK:
            EditText input = (EditText) d.findViewById(ID_EDIT_VIEW);;  
            input.setText("");
            break;
        default:
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Stock stock = adapter.getItem(position);
        final String name = stock.getDetail() == null ? stock.getQuote() : stock.getName();

        new AlertDialog.Builder(this)
            .setTitle(name)
            .setItems(R.array.stock_action,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            switch (i) {
                            case MENU_OPEN:
                                Log.d(TAG, "open url ...");
                                break;
                            case MENU_DEL:
                                Log.d(TAG, "delete stock ...");
                                StockApplication.getCurrentPortfolio().getStocks().remove(stock);
                                StockApplication.getPortfolioService().update(StockApplication.getCurrentPortfolio());
                                refreshStockList();
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "unhandled menu item" + i);
                            }
                        }
                    })
            .show();
    }
}