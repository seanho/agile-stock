package hk.reality.stock.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class StockDetail {
    private String quote;
    private String sourceUrl;
    private String volume;
    private BigDecimal price;
    private BigDecimal changePrice;
    private BigDecimal changePricePercent;
    private BigDecimal dayHigh;
    private BigDecimal dayLow;
    private Calendar updatedAt;

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the changePrice
     */
    public BigDecimal getChangePrice() {
        return changePrice;
    }

    /**
     * @param changePrice
     *            the changePrice to set
     */
    public void setChangePrice(BigDecimal changePrice) {
        this.changePrice = changePrice;
    }

    /**
     * @return the changePricePercent
     */
    public BigDecimal getChangePricePercent() {
        return changePricePercent;
    }

    /**
     * @param changePricePercent
     *            the changePricePercent to set
     */
    public void setChangePricePercent(BigDecimal changePricePercent) {
        this.changePricePercent = changePricePercent;
    }

    /**
     * @return the volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
     * @return the dayHigh
     */
    public BigDecimal getDayHigh() {
        return dayHigh;
    }

    /**
     * @param dayHigh
     *            the dayHigh to set
     */
    public void setDayHigh(BigDecimal dayHigh) {
        this.dayHigh = dayHigh;
    }

    /**
     * @return the dayLow
     */
    public BigDecimal getDayLow() {
        return dayLow;
    }

    /**
     * @param dayLow
     *            the dayLow to set
     */
    public void setDayLow(BigDecimal dayLow) {
        this.dayLow = dayLow;
    }

    /**
     * @return the updatedAt
     */
    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt
     *            the updatedAt to set
     */
    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the sourceUrl
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * @param sourceUrl the sourceUrl to set
     */
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    /**
     * @return the quote
     */
    public String getQuote() {
        return quote;
    }

    /**
     * @param quote the quote to set
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
        result = prime * result
                + ((updatedAt == null) ? 0 : updatedAt.hashCode());
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
        StockDetail other = (StockDetail) obj;
        if (quote == null) {
            if (other.quote != null)
                return false;
        } else if (!quote.equals(other.quote))
            return false;
        if (updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        } else if (!updatedAt.equals(other.updatedAt))
            return false;
        return true;
    }

}
