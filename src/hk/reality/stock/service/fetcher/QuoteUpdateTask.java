package hk.reality.stock.service.fetcher;

import hk.reality.stock.PortfolioActivity;
import hk.reality.stock.R;
import hk.reality.stock.StockApplication;
import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;
import hk.reality.utils.NetworkDetector;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }
    
    public QuoteUpdateTask(PortfolioActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Stock... stocks) {        
        if (!NetworkDetector.hasValidNetwork(activity)) {
            error = Error.ERROR_NO_NET;
            return Boolean.FALSE;
        }
        
        total = stocks.length;
        QuoteFetcher fetcher = QuoteFetcherFactory.getQuoteFetcher();
        fetcher.getClient().getConnectionManager().closeExpiredConnections(); // close previously opened conn
        fetcher.getClient().getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);

        ExecutorService executor = StockApplication.getExecutor();
        try {
            ArrayList<Future<StockDetail>> results = new ArrayList<Future<StockDetail>>();
            for(Stock s : stocks) {
                UpdateSubTask task = new UpdateSubTask(fetcher, s);
                results.add(executor.submit(task));
            }
            
            for(Future<StockDetail> r : results) {
                try {
                    r.get();
                } catch (ExecutionException e) {
                    Throwable t = e.getCause();
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else {
                        Log.e(TAG, "unexpected error while update stock quote", t);
                        error = Error.ERROR_UNKNOWN;
                        return Boolean.FALSE;
                    }
                }
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
        } catch (InterruptedException e) {
            Log.e(TAG, "download timeout", e);
            error = Error.ERROR_DOWNLOAD;
            return Boolean.FALSE;
        } catch (RuntimeException re) {
            Log.e(TAG, "unexpected error while update stock quote", re);
            error = Error.ERROR_UNKNOWN;
            return Boolean.FALSE;
        } 
    }

    class UpdateSubTask implements Callable<StockDetail> {
        Stock stock;
        QuoteFetcher fetcher;

        public UpdateSubTask(QuoteFetcher fetcher, Stock s) {
            this.fetcher = fetcher;
            this.stock = s;
        }

        @Override
        public StockDetail call() throws Exception {
            StockDetail detail = fetcher.fetch(stock.getQuote());
            stock.setDetail(detail);
            publishProgress(++current);
            return detail;
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
            case ERROR_NO_NET:
                Toast.makeText(activity, R.string.msg_no_network, Toast.LENGTH_LONG).show();
                break;
            case ERROR_DOWNLOAD:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_DOWNLOAD_UPDATE);
                break;
            case ERROR_PARSE:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_QUOTE_UPDATE);
                break;
            case ERROR_UNKNOWN:
                activity.showDialog(PortfolioActivity.DIALOG_ERR_UNEXPECTED);
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
        Log.i(TAG, "downloaded " + values[0] + "/" + total + ", " + progress);
        activity.setProgress((int) progress);
        activity.getAdapter().notifyDataSetChanged();
    }
}
