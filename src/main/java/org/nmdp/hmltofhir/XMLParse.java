package org.nmdp.hmltofhir;

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
    public static ResourceManager resources;

	public XMLParse(String xml, ResourceManager resources) {
		this.xml = xml;
        this.resources=resources;
	}

	public void grab() {
		System.out.println("Getting addtional resources from HML");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
            Document template = builder.parse(new InputSource(new StringReader(xmlToString("/org/nmdp/ResourceName.xml"))));
			resourceTemplate = template;
			Document xmlDOM = builder.parse(new InputSource(new StringReader(xml)));
			sendResources(xmlDOM);

		} catch (Exception e) {
			System.out.println("Error in handle Grabbing " + e);
		}
		//return null;
	}

	private void sendResources(Document xmlDOM) {
		NodeList resourceList = resourceTemplate.getElementsByTagName("resourceName");
		NodeList xmlAttributes = xmlDOM.getElementsByTagName("*");//Gets All nodes
        boolean found = false;
        int j=0;
		for (j = 0; j < xmlAttributes.getLength(); j++) {
            System.out.println("break");
            NamedNodeMap xmlAttribute = xmlAttributes.item(j).getAttributes();
            System.out.println("1 "+ xmlAttributes.item(j).getNodeName().toString());
            System.out.println("2 "+getFirstLevelTextContent(xmlAttributes.item(j)));
            found=false;
			for (int h = 0; h < xmlAttribute.getLength(); h++) {
                System.out.println("3 "+xmlAttribute.item(h).toString());
                System.out.println("4 "+xmlAttribute.item(h).getNodeValue());
                for (int i = 0; i < resourceList.getLength(); i++) {
					NamedNodeMap resourceAttribute = resourceList.item(i).getAttributes();//All attributes within the node
                    
                    //What should be in the resource XML
                    //Node
                    // Attribute
                    //Resource
                    //Structure
                    //Lowerstructure(if none then make empty make this optional
					
					if ((xmlAttribute.item(h).getNodeName().equals(getAttribute(resourceAttribute, "attribute"))) && xmlAttributes.item(j).getNodeName().equals(getAttribute(resourceAttribute,"node"))) {// If Attribute matches a resource
                        System.out.println("Found Attribute");
                        resources.addResource(getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),xmlAttribute.item(h).getNodeValue());
					}
                    if(j>0&&!found&&(xmlAttributes.item(j-1).getNodeName().equals(getAttribute(resourceAttribute,"node")))&& (getAttribute(resourceAttribute,"attribute")==null)){//If a Node text content matches the needs a resource
                        found=true;
                        System.out.println("found node");
                    resources.addResource(getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),getFirstLevelTextContent(xmlAttributes.item(j-1)));

                    }
				}
            }
        }
        for (int i = 0; i < resourceList.getLength(); i++) {
            NamedNodeMap resourceAttribute = resourceList.item(i).getAttributes();//All attributes within the node
            
            //What should be in the resource XML
            //Node
            // Attribute
            //Resource
            //Structure
            //Lowerstructure(if none then make empty make this optional
            if(j>0&&!found&&(xmlAttributes.item(j-1).getNodeName().equals(getAttribute(resourceAttribute,"node")))&& (getAttribute(resourceAttribute,"attribute")==null)){//If a Node text content matches the needs a resource
                found=true;
                System.out.println("found node");
                resources.addResource(getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),getFirstLevelTextContent(xmlAttributes.item(j-1)));
                
            }
        }

        resources.createResources();

	}

	public String xmlToString(String string) {

		try {
            File file = new File(Postresource.class.getResource(string).getFile());
            BufferedReader xmlReader;
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
    
    public static String getFirstLevelTextContent(Node node) {
        NodeList list = node.getChildNodes();
        StringBuilder textContent = new StringBuilder();
        for (int i = 0; i < list.getLength(); ++i) {
            Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
                textContent.append(child.getTextContent());
        }
        return textContent.toString();
    }

}
