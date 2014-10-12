package derigible.saves;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLBuilder {
	private Document dom;
	
	public XMLBuilder(File src) throws SAXException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(src);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public XMLBuilder(String root) throws SAXException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(
					new InputSource(new StringReader("<?xml version=\"1.0\" ?><"+root+"></"+root+">")));
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String getTextFromNode(String node) {
		NodeList nl = dom.getElementsByTagName(node);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			return nl.item(0).getFirstChild().getNodeValue();
		}
		return null;
	}

	public void setTextOfNode(String node, String value) {
		NodeList nl = dom.getElementsByTagName(node);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
			nl.item(0).getFirstChild().setNodeValue(value);
		} else {
			Node n = dom.createElement(node);
			n.setTextContent(value);
			dom.getFirstChild().appendChild(n);	
		}
	}
	
	public Document getDom(){
		return this.dom;
	}
}
