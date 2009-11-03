package hk.reality.stock.model;

import java.io.Serializable;
import java.util.List;

public class Portfolio implements Serializable {
	private static final long serialVersionUID = -5967634365697531599L;
	private String id;
	private String name;
    private List<Stock> stocks;
    
    public Portfolio() {
    }

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Portfolio other = (Portfolio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
