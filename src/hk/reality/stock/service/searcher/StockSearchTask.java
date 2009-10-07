package hk.reality.stock.service.searcher;

import hk.reality.stock.PortfolioActivity;
import hk.reality.stock.R;
import hk.reality.stock.StockApplication;
import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;
import hk.reality.stock.service.Lang;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.util.List;

import org.apache.commons.lang.Validate;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class StockSearchTask extends AsyncTask<String, Void, Stock> {
    private static final String TAG = "StockSearchTask";
    private Error error;
    private PortfolioActivity activity;
    enum Error {
        ERROR_DOWNLOAD, ERROR_PARSE
    }
    
    public StockSearchTask(PortfolioActivity activity) {
        this.activity = activity;
    }
    
    @Override
    protected Stock doInBackground(String... params) {        
        Validate.notNull(params, "params cannot be null");
        Validate.notNull(params[0], "lang cannot be null");
        Validate.notNull(params[1], "quote cannot be null");

        String lang = params[0];
        String quote = params[1];
        
        try {
            StockSearcher searcher = new HkexStockSearcher(Lang.valueOf(lang));
            return searcher.searchStock(quote);
        } catch (DownloadException de) {
            Log.e(TAG, "error downloading stock info", de);
            error = Error.ERROR_DOWNLOAD;
        } catch (ParseException pe) {
            Log.e(TAG, "error parsing stock info", pe);
            error = Error.ERROR_PARSE;
        }
        return null;
    }
    
    @Override
    protected void onPreExecute() {
        activity.showDialog(PortfolioActivity.DIALOG_ADD_IN_PROGRESS);
    }
    
    @Override
    protected void onPostExecute(Stock result) {
        activity.dismissDialog(PortfolioActivity.DIALOG_ADD_IN_PROGRESS);

        if (result != null) {
            Portfolio portfolio = StockApplication.getCurrentPortfolio();
            List<Stock> stocks = portfolio.getStocks();
            if (stocks.contains(result)) {
                Toast.makeText(activity, R.string.msg_stock_existed, Toast.LENGTH_LONG).show();

            } else {
                stocks.add(result);
                StockApplication.getPortfolioService().update(portfolio);
                Toast.makeText(activity, R.string.msg_stock_added, Toast.LENGTH_LONG).show();

            }            
        } else {
            switch (error) {
            case ERROR_DOWNLOAD:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_DOWNLOAD);
                break;
            case ERROR_PARSE:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_QUOTE);
                break;
            default:
                break;
            }
        }
    }
}
