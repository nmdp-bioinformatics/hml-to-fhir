package org.nmdp.hmltofhir;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;

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
        //serverbase=server url
        IGenericClient client = ctx.newRestfulGenericClient("http://nmdp-fhir-dev.us-east-1.elasticbeanstalk.com/baseDstu2");
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
        //is it bad practice to set all ID's with the same ID? If its univerisally unique why not right?
        patient.setId(patientID);
        
        
        Bundle bundle= new Bundle();
        bundle.setType(BundleType.TRANSACTION);
        System.out.println("Send Resources");
        bundle.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setUrl("Patient").setMethod(HTTPVerb.POST);
        
        // Send in resource template
        System.out.print("Parsing XML");
        ResourceManager manager= new ResourceManager(xml,patient);
        //Hopefully someone gets this reference
        //Replace "placeholder" with manager.resourceName.getId() its being  null right now for some reason
        bundle.addEntry().setFullUrl("Placeholder").setResource(manager.sequence).getRequest().setUrl("Sequence").setMethod(HTTPVerb.POST);
        bundle.addEntry().setFullUrl("Placeholder").setResource(manager.specimen).getRequest().setUrl("Specimen").setMethod(HTTPVerb.POST);
        bundle.addEntry().setFullUrl("Placeholder").setResource(manager.observation).getRequest().setUrl("Observation").setMethod(HTTPVerb.POST);
        bundle.addEntry().setFullUrl("Placeholder").setResource(manager.diagnosticReport).getRequest().setUrl("Diagonostic-Report").setMethod(HTTPVerb.POST);
        
        //client.transaction().withBundle(bundle);
        return manager.diag[0];
        }
    
}
