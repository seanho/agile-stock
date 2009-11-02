package hk.reality.stock.service.fetcher;

import hk.reality.stock.IndexActivity;
import hk.reality.stock.model.Index;
import hk.reality.stock.view.IndexAdapter;
import hk.reality.utils.NetworkDetector;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

public class IndexesUpdateTask extends AsyncTask<Void, Integer, Boolean> {
    public static final String TAG = "IndexesUpdateTask";
    private IndexActivity activity;
    private List<Index> results;

    private Error error;
    enum Error {
        ERROR_NO_NET, ERROR_DOWNLOAD, ERROR_PARSE, ERROR_UNKNOWN
    }
    
    public IndexesUpdateTask(IndexActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(Void ... ignored) {
        Log.i(TAG, "running indexes update in background");
        if (!NetworkDetector.hasValidNetwork(activity)) {
            error = Error.ERROR_NO_NET;
            return Boolean.FALSE;
        }

        Log.i(TAG, "start fetcher");
        IndexesFetcher fetcher = IndexesFetcherFactory.getIndexesFetcher(activity);
        results = fetcher.fetch();

        return Boolean.TRUE;
    }

    private void updateIndexes(List<Index> indexes) {
        IndexAdapter adapter = activity.getIndexAdapter();
        adapter.clear();
        for(Index i : indexes) {
            adapter.add(i);
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onPreExecute() {
        activity.getParent().setProgressBarVisibility(true);
        activity.getParent().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onCancelled() {
        activity.getParent().setProgressBarVisibility(false);
        activity.getParent().setProgressBarIndeterminateVisibility(false);
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        activity.getParent().setProgressBarVisibility(false);
        activity.getParent().setProgressBarIndeterminateVisibility(false);
        if (result) {
            Log.i(TAG, "update success, number of results ..." + results.size());
            updateIndexes(results);
        } else {
            Log.i(TAG, "update failure");
        }
    }
}
