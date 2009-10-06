package hk.reality.stock;

import hk.reality.stock.service.FilePortfolioService;
import hk.reality.stock.service.PortfolioService;
import android.app.Application;

public class StockApplication extends Application {
    private static PortfolioService portfolioService;
    
    @Override
    public void onCreate() {
        super.onCreate();
        portfolioService = new FilePortfolioService(this.getFilesDir()); 
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static PortfolioService getPortfolioService() {
        return portfolioService;
    }
}
