package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;

import com.thoughtworks.xstream.XStream;

public class PortfolioSerializer {
	public static String toXML(Portfolio portfolio) {
		XStream xstream = getXStream();
		return xstream.toXML(portfolio);
	}
	
	public static Portfolio fromXML(String xml) {
		XStream xstream = getXStream();
		return (Portfolio) xstream.fromXML(xml);
	}
	
	private static XStream getXStream() {
		XStream xstream = new XStream();
		xstream.alias("sk", Stock.class);
		xstream.alias("po", Portfolio.class);
		return xstream;
	}
}