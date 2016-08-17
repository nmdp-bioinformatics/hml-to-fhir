/*
 
 HMLTOFHIR Resource Transfer for HLA Data Transfer
 Copyright (c) 2015 National Marrow Donor Program (NMDP)
 
 This library is free software; you can redistribute it and/or modify it
 under the terms of the GNU Lesser General Public License as published
 by the Free Software Foundation; either version 3 of the License, or (at
 your option) any later version.
 
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 License for more details.
 
 You should have received a copy of the GNU Lesser General Public License
 along with this library;  if not, write to the Free Software Foundation,
 Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.
 
 > http://www.gnu.org/licenses/lgpl.html
 
 */
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
/* Simple function to call the parser*/
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
            System.out.println("Error in handle Grabbing ");
            e.printStackTrace();
		}
	}
/* iterates through the the HML and compares all nodes and attributes to ResourceName.xml*/
	private void sendResources(Document xmlDOM) {
		NodeList resourceList = resourceTemplate.getElementsByTagName("resourceName");
		NodeList xmlAttributes = xmlDOM.getElementsByTagName("*");//Gets All nodes
        boolean found = false;
        int j=0;
        /*
         ResouceName node looks like this
         node
         attribute
         resource
         structure
         lowerStructure
         position
         */

		for (j = 0; j < xmlAttributes.getLength(); j++) {
            System.out.println("break");
            NamedNodeMap xmlAttribute = xmlAttributes.item(j).getAttributes();
            found=false;
			for (int h = 0; h < xmlAttribute.getLength(); h++) {
                for (int i = 0; i < resourceList.getLength(); i++) {
					NamedNodeMap resourceAttribute = resourceList.item(i).getAttributes();//All attributes within the node
                    
                
					if ((xmlAttribute.item(h).getNodeName().equals(getAttribute(resourceAttribute, "attribute"))) && xmlAttributes.item(j).getNodeName().equals(getAttribute(resourceAttribute,"node"))) {// If Attribute matches a resource
                        System.out.println("Found Attribute");
                        resources.addResource(getAttribute(resourceAttribute,"node"),getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),ifLowerExists(getAttribute(resourceAttribute,"lowerStructure")),xmlAttribute.item(h).getNodeValue());
					}
                    if(j>0&&!found&&(xmlAttributes.item(j-1).getNodeName().equals(getAttribute(resourceAttribute,"node")))&& (getAttribute(resourceAttribute,"attribute")==null)){//If a Node text content matches the needs a resource
                        found=true;
                        System.out.println("found node");
                    resources.addResource(getAttribute(resourceAttribute,"node"),getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),ifLowerExists(getAttribute(resourceAttribute,"lowerStructure")),getFirstLevelTextContent(xmlAttributes.item(j-1)));

                    }
				}
            }
        }
        for (int i = 0; i < resourceList.getLength(); i++) {
            NamedNodeMap resourceAttribute = resourceList.item(i).getAttributes();//All attributes within the node
            
            if(j>0&&!found&&(xmlAttributes.item(j-1).getNodeName().equals(getAttribute(resourceAttribute,"node")))&& (getAttribute(resourceAttribute,"attribute")==null)){//If a Node text content matches the needs a resource
                found=true;
                System.out.println("found node");
                resources.addResource(getAttribute(resourceAttribute,"node"),getAttribute(resourceAttribute,"resource"),getAttribute(resourceAttribute,"structure"),ifLowerExists(getAttribute(resourceAttribute,"lowerStructure")),getFirstLevelTextContent(xmlAttributes.item(j-1)));
                
            }
        }
        //this needs to be positioned or reworked
        //resources.iterate()
        resources.createResources();

	}
/*
 Converts the xml document to a string
 */
 
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
/*Gets attribute from ResourceName.xml
 */
	public static String getAttribute(NamedNodeMap resourceAttributes, String attributeName) {
		Node resource = resourceAttributes.getNamedItem(attributeName);
        
		String foundAttribute = resource != null ? resource.getNodeValue() : null;
        return foundAttribute;
	}
    /*
     Gets the text content from a node
     */
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
    //If a more primative structure exists based on HL7 fhir resources and resourceName.xml
    public static String ifLowerExists(String value)
    {
        return ((value==null)||(value.equals("")))?"null":value;
    }

}
