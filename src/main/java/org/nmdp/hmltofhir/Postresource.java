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

import ca.uhn.fhir.context.FhirContext;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.*;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.rest.client.IGenericClient;
public class Postresource {
    
    
    

    private String patientFamilyName;
    private String patientGivenName;
    private String patientGender;
    private String patientBirth;
    private String patientID;
    private String xml;
    public static Patient patient = new Patient();
    
    public Postresource(String[] data) {
        this.patientFamilyName=data[0];
        this.patientGivenName=data[1];
        this.patientGender=data[2].toUpperCase();
        this.patientBirth=data[3];
        this.patientID="urn:uuid:"+UUID.randomUUID().toString();
        this.xml=data[4];
        
        //Make this string array 0-n is patient data n+1 is the xml
    }
    public String sendResources() {
        FhirContext ctx = FhirContext.forDstu3();
        //change site once it is updated to DSTU3
        IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
        //Create patient
        patient.addName().addFamily(patientFamilyName).addGiven(patientGivenName);
        if(patientGender.equals("MALE"))
        {
            patient.setGender(AdministrativeGender.MALE);
        }
        else if(patientGender.equals("FEMALE"))
        {
            patient.setGender(AdministrativeGender.FEMALE);
        }
        else if(patientGender.equals("UNKNOWN"))
        {
            patient.setGender(AdministrativeGender.UNKNOWN);
        }
        else if(patientGender.equals("OTHER"))
        {
            patient.setGender(AdministrativeGender.OTHER);
        }
        else{
            patient.setGender(null);
        }
        
        //Not working skip for now String->DateDt
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date undate = df.parse(patientBirth);
            patient.setBirthDate(undate);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        patient.setId(patientID);
        
        
        System.out.print("Parsing XML");
        ResourceManager manager= new ResourceManager(xml,patientID);
        //Hopefully someone gets this reference
        //Replace "placeholder" with manager.resourceName.getId() its being  null right now for some reason
        //Add Resources to bundle
        Bundle bundle= new Bundle();
        bundle.setType(BundleType.TRANSACTION);
        System.out.println("Send Resources");
        bundle.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setUrl("Patient").setMethod(HTTPVerb.POST);
        
        bundle.addEntry().setFullUrl(manager.seq[19]).setResource(manager.sequence).getRequest().setUrl("Sequence").setMethod(HTTPVerb.POST);
        
        bundle.addEntry().setFullUrl(manager.spec[19]).setResource(manager.specimen).getRequest().setUrl("Specimen").setMethod(HTTPVerb.POST);
        
        bundle.addEntry().setFullUrl(manager.obv[19]).setResource(manager.observation).getRequest().setUrl("Observation").setMethod(HTTPVerb.POST);
        
        bundle.addEntry().setFullUrl(manager.diag[19]).setResource(manager.diagnosticReport).getRequest().setUrl("Diagonostic-Report").setMethod(HTTPVerb.POST);
        
        
       //client.transaction().withBundle(bundle).execute();
        //Return the hmlid so that they may use the GET function.
            return manager.diag[0];
        }
    
}
