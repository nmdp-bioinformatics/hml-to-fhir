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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import ca.uhn.fhir.context.FhirContext;


import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.dstu3.model.Sequence.*;
import org.hl7.fhir.dstu3.model.Specimen.*;
import org.hl7.fhir.dstu3.model.Observation.*;
import org.hl7.fhir.dstu3.model.DiagnosticReport.*;

import ca.uhn.fhir.rest.client.IGenericClient;


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

public class ResourceManager {
    public static Specimen specimen;
    public static Sequence sequence;
    public static Observation observation;
    public static DiagnosticReport diagnosticReport;
    private static XMLParse parse;
    private static String xml;
    private static String patientID;
    private static Document resourceTemplate;
    public static String [][] seq = new String[20][20];
    public static String [][] spec = new String[20][20];
    public static String [][] obv = new String [20][20];
    public static String [][] diag = new String [20][20];
    private static int iteration = 0;
    public ResourceManager(String xml,String patientID)
    {
        parse= new XMLParse(xml,this);
        this.xml=xml;
        this.patientID=patientID;
        parse.grab();
    }

     
	public static void addResource(String node,String resource,String structure,String lower, String value) {
        /*
         Change these arrays to arrays of linked lists
         This allows for multiple of any structure to beb allowed
         */
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document template = builder.parse(new InputSource(new StringReader(parse.xmlToString("/org/nmdp/ResourceName.xml"))));
            resourceTemplate = template;
            Document xmlDOM = builder.parse(new InputSource(new StringReader(xml)));
            
        } catch (Exception e) {
            System.out.println("Error in handle Managing " + e);
        }
        NodeList positionList = resourceTemplate.getElementsByTagName("resourceName");

        if(resource.equals("Sequence"))
        {
            System.out.println("Adding to Sequence");
            for (int i = 0; i < positionList.getLength(); i++) {
                NamedNodeMap positionAttribute = positionList.item(i).getAttributes();
                if(node.equals(parse.getAttribute(positionAttribute,"node"))&&structure.equals(parse.getAttribute(positionAttribute,"structure"))&&resource.equals(parse.getAttribute(positionAttribute,"resource"))&&lower.equals(parse.getAttribute(positionAttribute,"lowerStructure")))
                   {
                    seq[iteration][Integer.parseInt(parse.getAttribute(positionAttribute,"position"))]=isNotNull(value);
                    }
                
            }
                  
        }
        else if(resource.equals("Specimen"))
        {
            System.out.println("Adding to Specimen");

            for (int i = 0; i < positionList.getLength(); i++) {
                NamedNodeMap positionAttribute = positionList.item(i).getAttributes();
                if(node.equals(parse.getAttribute(positionAttribute,"node"))&&structure.equals(parse.getAttribute(positionAttribute,"structure"))&&resource.equals(parse.getAttribute(positionAttribute,"resource"))&&lower.equals(parse.getAttribute(positionAttribute,"lowerStructure")))
                   {
                    spec[iteration][Integer.parseInt(parse.getAttribute(positionAttribute,"position"))]=isNotNull(value);
                }
                   
                   }
            
            
        }
        else if(resource.equals("Observation"))
        {
            System.out.println("Adding to Observation");
   
            for (int i = 0; i < positionList.getLength(); i++) {
                NamedNodeMap positionAttribute = positionList.item(i).getAttributes();
                if(node.equals(parse.getAttribute(positionAttribute,"node"))&&structure.equals(parse.getAttribute(positionAttribute,"structure"))&&resource.equals(parse.getAttribute(positionAttribute,"resource"))&&lower.equals(parse.getAttribute(positionAttribute,"lowerStructure")))
                   {
                    obv[iteration][Integer.parseInt(parse.getAttribute(positionAttribute,"position"))]=isNotNull(value);
                }
                   
                   }
            
        }
        else if(resource.equals("DiagnosticReport"))
        {
            System.out.println("Adding to DR");
            for (int i = 0; i < positionList.getLength(); i++) {
                NamedNodeMap positionAttribute = positionList.item(i).getAttributes();
                if(node.equals(parse.getAttribute(positionAttribute,"node"))&&structure.equals(parse.getAttribute(positionAttribute,"structure"))&&resource.equals(parse.getAttribute(positionAttribute,"resource"))&&lower.equals(parse.getAttribute(positionAttribute,"lowerStructure")))
                   {
                    diag[iteration][Integer.parseInt(parse.getAttribute(positionAttribute,"position"))]=isNotNull(value);
                }
                   
                   }
        }
        else
        {
            System.out.println("Error: Couldnt find a resource");
        }
				
	}
    public static void createResources()
    {
        //Call all methods
        System.out.println("Making resources");
        try{
            //Make identifiers
        spec[iteration][19]="urn:uuid:"+UUID.randomUUID().toString();
        seq[iteration][19]="urn:uuid:"+UUID.randomUUID().toString();
        diag[iteration][19]="urn:uuid:"+UUID.randomUUID().toString();
        obv[iteration][19]="urn:uuid:"+UUID.randomUUID().toString();
        newSpecimen();
        newSequence();
        newObservation();
        newDiagonostic();
        }
        catch(Exception e)
        {
            System.out.println("Error? "+e);
            
        }
        
    }
    public static void newSpecimen()
    {                 //Appies for all resources
                    //Make an array of the resources with size of length of longest linked list in linked list array
                    //Specimen.addwhatever in a for loop through linked lists do a hard set at 10 but can change
                    //loop through linked list if it hits a null it doesnt matter'
        System.out.println("In Specimen");
        try{
            
        
            specimen=new Specimen();
            Reference[] ref = new Reference[]{new Reference()};
            ref[0].setReference(patientID);
            specimen.setCollection(SpecimenCollectionComponent.class.newInstance().setMethod(CodeableConcept.class.newInstance().setText(isNotNull(spec[iteration][0]))));
            specimen.setSubject(ref[0]);
        }
        catch(Exception e)
        {
            System.out.println("Specimen Exception: "+e);
        }
    }
    public static void newSequence()
    {
        System.out.println("In Sequence");
        try{
            sequence=new Sequence();
            Reference [] ref = new Reference[]{new Reference(),new Reference()};
            sequence.setReferenceSeq(SequenceReferenceSeqComponent.class.newInstance().setWindowStart(isNotNullInt(seq[iteration][1])).setWindowEnd(isNotNullInt(seq[iteration][2])).setReferenceSeqString(isNotNull(seq[iteration][3])));
            sequence.setObservedSeq(isNotNull(seq[iteration][4]));
            sequence.addVariant().setStart(isNotNullInt(seq[iteration][5])).setEnd(isNotNullInt(seq[iteration][6])).setObservedAllele(isNotNull(seq[iteration][7])).setReferenceAllele(isNotNull(seq[iteration][8]));
            sequence.addQuality().setStart(isNotNullInt(seq[iteration][9])).setEnd(isNotNullInt(seq[iteration][10])).setScore(Quantity.class.newInstance().setValue(isNotNullDouble(seq[iteration][11])));
            sequence.setType(SeqType(isNotNull(seq[iteration][0])));
            sequence.setCoordinateSystem(1);
            
            sequence.setSpecimen(ref[1].setReference(spec[iteration][19]));
            sequence.setPatient(ref[0].setReference(patientID));
        }
        catch(Exception e)
        {
             System.out.println("Sequence Exception: "+e);
        }
    }
    public static SequenceType SeqType(String type)
    {
                if(type.equals("DNA"))
                {
                    return SequenceType.DNA;
                }
                else if(type.equals("RNA"))
                {
                    return SequenceType.RNA;
                }
                else if(type.equals("AA"))
                {
                    return SequenceType.AA;
                }
                else{
                    return null;
                }
    }
    public static void newObservation()
    {
        System.out.println("In observation");
        try{
        observation=new Observation();
        Reference ref= new Reference();
        //observation.addRelated().setTarget(ref.setReference(seq[19]));
            observation.setStatus(ObservationStatus.FINAL);
            observation.setCode(CodeableConcept.class.newInstance().addCoding(Coding.class.newInstance().setCode("29463-7")));
        }
         catch(Exception e)
        {
            System.out.println("Observation Exception: "+e);
        }
    }
    public static void newDiagonostic()
    {
        System.out.println("In Diagon Alley");
        try{
        diagnosticReport=new DiagnosticReport();
        Reference [] ref = new Reference[] { new Reference(),new Reference(),new Reference()};
            diagnosticReport.addIdentifier().setValue(isNotNull(diag[iteration][0]))
            ;
        diagnosticReport.addSpecimen(ref[0].setReference(spec[iteration][19]));
        diagnosticReport.setSubject(ref[1].setReference(patientID));
        diagnosticReport.addResult(ref[2].setReference(obv[iteration][19]));
            diagnosticReport.setStatus(DiagnosticReportStatus.FINAL);
            diagnosticReport.setCode(CodeableConcept.class.newInstance().addCoding(Coding.class.newInstance().setCode("718-7")));
            Date date = new Date();
            System.out.println(date.toString());
            diagnosticReport.setIssued(date);
            diagnosticReport.setEffective(Period.class.newInstance().setStart(date));
        }
        catch(Exception e)
        {
            System.out.println("Diagons!! Exception: "+e);
        }
    
    
    }
    
    //Since Parse is old and cant accept null I made my own for parseing Ints and Doubles
    //When you make a structure is NEEDS to call one of these or make a new one depending on the object needed, HAPI doesnt do well with null statements so it is eaiser to make default values
    //If/when you make a default value update table.html with this and the explination.
    public static String isNotNull(String value)
    {
        System.out.println(value);
        return (value==null)|| (value.equals(""))? "null": value;
    }
    public static int isNotNullInt(String value)
    {System.out.println(value);
        return(value==null)||(value.equals(""))? -400:Integer.parseInt(value);
    }
    public static Double isNotNullDouble(String value)
    {System.out.println(value);
        return(value==null)||(value.equals(""))?-400:Double.parseDouble(value);
    }
    /**
     This method is called when we reach the end of a block telling us to start a new resource
     **/
    public static void iterate()
    {
        iteration++;
    }

    
}
