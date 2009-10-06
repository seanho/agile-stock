package hk.reality.stock.model;

import java.io.Serializable;

public class Stock implements Serializable {
	private static final long serialVersionUID = -6452165113616479803L;
	private String name;
    private String quote;
    private StockDetail detail; 

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
     * @return the quote
     */
    public String getQuote() {
        return quote;
    }

    /**
     * @param quote
     *            the quote to set
     */
    public void setQuote(String quote) {
        this.quote = quote;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quote == null) ? 0 : quote.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stock other = (Stock) obj;
        if (quote == null) {
            if (other.quote != null)
                return false;
        } else if (!quote.equals(other.quote))
            return false;
        return true;
    }

    /**
     * @return the detail
     */
    public StockDetail getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(StockDetail detail) {
        this.detail = detail;
    }
}
