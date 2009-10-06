package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;

import java.io.Reader;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class PortfolioSerializer {
	public static String toXML(List<Portfolio> portfolios) {
		XStream xstream = getXStream();
		return xstream.toXML(portfolios);
	}

	@SuppressWarnings("unchecked")
	public static List<Portfolio> fromXML(String xml) {
		XStream xstream = getXStream();
		return (List<Portfolio>) xstream.fromXML(xml);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Portfolio> fromXML(Reader xml) {
		XStream xstream = getXStream();
		return (List<Portfolio>) xstream.fromXML(xml);
	}
	
	private static XStream getXStream() {
		XStream xstream = new XStream();
		xstream.alias("sk", Stock.class);
		xstream.alias("po", Portfolio.class);
		return xstream;
	}
}