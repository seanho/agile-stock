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
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PortfolioActivity extends ListActivity {
    private static final String TAG = "PortfolioActivity";

    public static final int DIALOG_ADD_STOCK = 100;
    public static final int DIALOG_ADD_IN_PROGRESS = 101;
    public static final int DIALOG_ABOUT = 102;
    public static final int DIALOG_DISCLAIMER = 103;
    
    public static final int DIALOG_ERR_DOWNLOAD_UPDATE = 400;
    public static final int DIALOG_ERR_QUOTE = 401;
    public static final int DIALOG_ERR_QUOTE_UPDATE = 402;
    public static final int DIALOG_ERR_UNEXPECTED = 403;
    public static final int DIALOG_ERR_DOWNLOAD_PORTFOLIO = 405;

    public static final int ID_EDIT_VIEW = 1200000;
    public static final int MENU_OPEN = 0; 
    public static final int MENU_DEL = 1;

    private StockAdapter adapter;
    private StockSearchTask searchTask;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview);
        adapter = new StockAdapter(this);        
        setListAdapter(adapter);
        
        TextView empty = (TextView) findViewById(android.R.id.empty);
        empty.setText(R.string.msg_add_stock);
        
        refreshStockList();
        if (!SettingsActivity.getDisclaimerShown(this)) {
            showDialog(DIALOG_DISCLAIMER);
        }
    }

    public void refreshStockList() {
        Log.d(TAG, "refresh stock list");
        LoadStockTask task = new LoadStockTask();
        task.execute();
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
        menu.getItem(2).setIcon(R.drawable.ic_menu_help);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            showDialog(DIALOG_ABOUT);
            return true;
        case R.id.refresh:
            updateStocks();
            return true;
        case R.id.add:
            showDialog(DIALOG_ADD_STOCK);
            return true;
        default:
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DISCLAIMER:
            AlertDialog disclaimerDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.disclaimer_label)
            .setMessage(R.string.msg_disclaimer)
            .setPositiveButton(R.string.ok_label, new OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SettingsActivity.setDisclaimerShown(PortfolioActivity.this, true);
                    dialog.dismiss();
                    
                }                    
            })
            .setCancelable(true)
            .create();
            return disclaimerDialog;
        case DIALOG_ERR_UNEXPECTED:
            AlertDialog unexpectedErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_unexpect)
                .setMessage(R.string.msg_error_unexpect_details)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
                .setCancelable(true)
                .create();
            return unexpectedErrDialog;
        case DIALOG_ERR_DOWNLOAD_UPDATE:
            AlertDialog downloadErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_download)
                .setMessage(R.string.msg_error_download_details)
                .setPositiveButton(R.string.retry_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStocks();
                        dialog.dismiss();
                    }                    
                })
                .setNegativeButton(R.string.cancel_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
                .setCancelable(true)
                .create();
            return downloadErrDialog;
        case DIALOG_ERR_DOWNLOAD_PORTFOLIO:
            AlertDialog downloadPortfolioErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_download)
                .setMessage(R.string.msg_error_download_details)
                .setPositiveButton(R.string.retry_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshStockList();
                        dialog.dismiss();
                    }                    
                })
                .setNegativeButton(R.string.cancel_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
                .setCancelable(true)
                .create();
            return downloadPortfolioErrDialog;
        case DIALOG_ERR_QUOTE:
            final AlertDialog quoteErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_unexpected)
                .setMessage(R.string.msg_error_unexpected_details)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
                .setCancelable(true)
                .create();
            return quoteErrDialog;
        case DIALOG_ABOUT:
            AlertDialog aboutDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.msg_about)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
            return aboutDialog;
        case DIALOG_ERR_QUOTE_UPDATE:
            final AlertDialog quoteUpdateErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_stock)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
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
            
            final AlertDialog addDialog = new AlertDialog.Builder(this)
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

            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setId(ID_EDIT_VIEW);
            input.setSingleLine();                
            input.setOnKeyListener(new OnKeyListener(){
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        String value = input.getText().toString();
                        Log.d(TAG, "entered quote: " + value);
                        searchTask = new StockSearchTask(PortfolioActivity.this);
                        searchTask.execute("CHI", value);
                        addDialog.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

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
                                Uri uri = Uri.parse(stock.getDetail().getSourceUrl()); 
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
                                startActivity(intent);
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



    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateStocks();
    }

    /**
     * @return the adapter
     */
    public StockAdapter getAdapter() {
        return adapter;
    }
    
    class LoadStockTask extends AsyncTask<Void, Void, List<Stock>> {
        @Override
        protected List<Stock> doInBackground(Void... arg) {
            return StockApplication.getCurrentPortfolio().getStocks();
        }

        @Override
        protected void onPostExecute(List<Stock> result) {
            if (result.size() == 0) {
                TextView empty = (TextView) findViewById(android.R.id.empty);
                empty.setText(R.string.msg_add_stock);
            }
            
            adapter.clear();
            for(Stock s : result) {
                adapter.add(s);
            }
            adapter.sort(new StockAdapter.StockQuoteSorter());
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            TextView empty = (TextView) findViewById(android.R.id.empty);
            empty.setText(R.string.msg_loading);
        }
    }
}