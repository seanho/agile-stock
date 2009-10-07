package hk.reality.stock;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;
import hk.reality.stock.service.FilePortfolioService;
import hk.reality.stock.service.PortfolioService;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;

public class StockApplication extends Application {
    private static final String TAG = "StockApplication";
    private static PortfolioService portfolioService;
    private static Portfolio currentPortfolio;
    
    @Override
    public void onCreate() {
        super.onCreate();
        portfolioService = new FilePortfolioService(this.getFilesDir());

        List<Portfolio> allPortfolios = portfolioService.list();
        if (allPortfolios.size() > 0) {
            Log.i(TAG, "selected first portfolio");
            currentPortfolio = allPortfolios.get(0);

        } else {
            Log.i(TAG, "created new portfolio");
            Portfolio p = new Portfolio();
            p.setName(getResources().getString(R.string.new_portfolio_label));
            p.setStocks(new ArrayList<Stock>());
            portfolioService.create(p);
            currentPortfolio = p;

        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static PortfolioService getPortfolioService() {
        return portfolioService;
    }

    public static Portfolio getCurrentPortfolio() {
        return currentPortfolio;
    }

    public static void setCurrentPortfolio(Portfolio currentPortfolio) {
        StockApplication.currentPortfolio = currentPortfolio;
    }
}
