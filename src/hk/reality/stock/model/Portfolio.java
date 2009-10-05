package hk.reality.stock.model;

import java.util.List;

public class Portfolio {
    private String name;
    private List<Stock> stocks;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the stocks
     */
    public List<Stock> getStocks() {
        return stocks;
    }

    /**
     * @param stocks
     *            the stocks to set
     */
    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
