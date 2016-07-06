import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParse {

	private String xml;
	public static Document resourceTemplate = null;

	public XMLParse(String xml) {
		this.xml = xml;
	}

	public ResourceManager[] grab() {
		System.out.println("Getting addtional resources from HML");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document template = builder.parse(new InputSource(new StringReader(xmlToString("location to resource"))));
			resourceTemplate = template;
			Document xmlDOM = builder.parse(new InputSource(new StringReader(xml)));
			sendResources(xmlDOM);

		} catch (Exception e) {
			System.out.println("Error in handle Grabbing" + e);
		}
		return null;
	}

	private void sendResources(Document xmlDOM) {
		NodeList resourceList = resourceTemplate.getElementsByTagName("transfer");
		NodeList xmlAttributes = xmlDOM.getElementsByTagName("*");
		for (int j = 0; j < xmlAttributes.getLength(); j++) {
			NamedNodeMap xmlAttribute = xmlAttributes.item(j).getAttributes();
			for (int h = 0; h < xmlAttribute.getLength(); h++) {

				for (int i = 0; i < resourceList.getLength(); i++) {
					NamedNodeMap resourceAttribute = resourceList.item(i).getAttributes();
		
					if (xmlAttribute.item(h).toString().equals(getAttribute(resourceAttribute, "attribute"))) {
						ResourceManager.addResource(getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),xmlAttribute.item(h).getNodeValue());
					}

				}
			}
		}

	}

	private String xmlToString(String string) {
		File file = new File(string);
		BufferedReader xmlReader;
		try {
			xmlReader = new BufferedReader(new FileReader(file));
			StringBuilder xmlBuffer = new StringBuilder();
			String line = xmlReader.readLine();

			while (line != null) {
				xmlBuffer.append(line);
				xmlBuffer.append(System.lineSeparator());
				line = xmlReader.readLine();
			}
			xmlReader.close();

			String xmlText = xmlBuffer.toString();
			return xmlText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String getAttribute(NamedNodeMap resourceAttributes, String attributeName) {
		Node resource = resourceAttributes.getNamedItem(attributeName);
		String foundAttribute = resource != null ? resource.getNodeValue() : null;
		return foundAttribute;
	}

}
