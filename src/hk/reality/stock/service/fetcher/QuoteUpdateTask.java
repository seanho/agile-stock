package hk.reality.stock.service.fetcher;

import hk.reality.stock.PortfolioActivity;
import hk.reality.stock.R;
import hk.reality.stock.StockApplication;
import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class QuoteUpdateTask extends AsyncTask<Stock, Integer, Boolean> {
    private static final String TAG = "QuoteUpdateTask";
    private PortfolioActivity activity;
    private int total = 0;
    private int current = 0;
    private Error error;
    enum Error {
        ERROR_DOWNLOAD, ERROR_PARSE
    }
    
    public QuoteUpdateTask(PortfolioActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Stock... stocks) {
        total = stocks.length;
        QuoteFetcher fetcher = QuoteFetcherFactory.getQuoteFetcher();
        
        try {
            for(Stock s : stocks) {
                StockDetail detail = fetcher.fetch(s.getQuote());
                s.setDetail(detail);
                onProgressUpdate(++current);
            }

            StockApplication.getPortfolioService().update(StockApplication.getCurrentPortfolio());
            return Boolean.TRUE;
        } catch (DownloadException de) {
            Log.e(TAG, "error downloading stock", de);
            error = Error.ERROR_DOWNLOAD;
            return Boolean.FALSE;
        } catch (ParseException pe) {
            Log.e(TAG, "error parsing code", pe);
            error = Error.ERROR_PARSE;
            return Boolean.FALSE;
        }
    }

    @Override
    protected void onCancelled() {
        activity.setProgressBarVisibility(false);
        Toast.makeText(activity, R.string.msg_download_cancelled, Toast.LENGTH_SHORT);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        activity.setProgressBarVisibility(false);
        if (result) {
            activity.refreshStockList();
        } else {
            switch (error) {
            case ERROR_DOWNLOAD:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_DOWNLOAD);
                break;
            case ERROR_PARSE:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_QUOTE_UPDATE);
                break;
            default:
                break;
            }
        }
    }

    @Override
    protected void onPreExecute() {
        activity.setProgressBarVisibility(true);
        activity.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        float progress = ((float) values[0] / (float) total) * 10000;
        activity.setProgress((int) progress);
    }
}
