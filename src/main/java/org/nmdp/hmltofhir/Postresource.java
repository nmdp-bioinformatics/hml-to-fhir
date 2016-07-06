package org.nmdp.hmltofhir;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.rest.client.IGenericClient;
public class Postresource {
    
    
    private String patientFamilyName;
    private String patientGivenName;
    private String patientGender;
    private String patientBirth;
    private String patientID;
    private String xml;
    
    public Postresource(String[] data) {
        this.patientFamilyName=data[0];
        this.patientGivenName=data[1];
        this.patientGender=data[2].toUpperCase();
        this.patientBirth=data[3];
        //this.patientID=data[4];
        this.patientID=UUID.randomUUID().toString();
        this.xml=data[4];
        
        //Make this string array 0-n is patient data n+1 is the xml
    }
    public String sendResources() {
        FhirContext ctx = FhirContext.forDstu2();
        //serverbase=server url
        IGenericClient client = ctx.newRestfulGenericClient("http://nmdp-fhir-dev.us-east-1.elasticbeanstalk.com/baseDstu2");
        System.out.println("Conneted to Server");
        Patient patient = new Patient();
        patient.addIdentifier().setValue(patientID);
        patient.addName().addFamily(patientFamilyName).addGiven(patientGivenName);
        if(patientGender.equals("MALE"))
        {
            patient.setGender(AdministrativeGenderEnum.MALE);
        }
        else if(patientGender.equals("FEMALE"))
        {
            patient.setGender(AdministrativeGenderEnum.FEMALE);
        }
        else if(patientGender.equals("UNKNOWN"))
        {
            patient.setGender(AdministrativeGenderEnum.UNKNOWN);
        }
        else if(patientGender.equals("OTHER"))
        {
            patient.setGender(AdministrativeGenderEnum.OTHER);
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date undate = df.parse(patientBirth);
            String date=df.format(undate);
            DateDt formatDate = new DateDt(date);
            patient.setBirthDate(formatDate);
        } catch (Exception e) {

            e.printStackTrace();
        }
        Bundle bundle= new Bundle();
        bundle.setType(BundleTypeEnum.TRANSACTION);
        System.out.println("Send Resources");
         System.out.println("Patient Created");
        bundle.addEntry().setFullUrl(patient.getId().getValue()).setResource(patient).getRequest().setUrl("Patient").setMethod(HTTPVerbEnum.POST);
        client.transaction().withBundle(bundle).execute();
        return patientID;
    }
    
}
