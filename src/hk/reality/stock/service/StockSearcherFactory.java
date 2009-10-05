package hk.reality.stock.service;


public class StockSearcherFactory {
    public static StockSearcher getDefaultStockSearcher(String lang) {
        return new HkexStockSearcher(Lang.valueOf(lang));
    }
}
