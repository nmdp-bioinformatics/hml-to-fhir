package org.nmdp.hmltofhir;

import java.util.HashSet;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Specimen;

import ca.uhn.fhir.rest.client.IGenericClient;

public class Get {
    
    private String id;
    
    public Get(String id) {
        this.id=id;
    }
    //id will be created in the POST method
    public String search() {
        FhirContext ctx = FhirContext.forDstu3();
        IGenericClient client = ctx.newRestfulGenericClient("http://nmdp-fhir-dev.us-east-1.elasticbeanstalk.com/baseDstu2");
        System.out.println("Connected to server");
        //Due to lack of transaction bundle search support this is the only way I could think of getting all the bundles.
        Bundle bundle= client.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().identifier(id)).returnBundle(Bundle.class).execute();
        
        Bundle newbundle = client.search().forResource(Specimen.class).where(Specimen.IDENTIFIER.exactly().identifier(id)).returnBundle(Bundle.class).execute();
        
        System.out.println("Bundle Found");
        //After research, there is no proper way to loop through all resources.
        String fullBundle=ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle)+ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(newbundle);
        System.out.println("Bundle....bundled...sigh");
        return fullBundle;
    }
}