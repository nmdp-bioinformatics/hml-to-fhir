package org.nmdp.hmltofhir;

import java.util.HashSet;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Get {
    
    private String id;
    
    public Get(String id) {
        this.id=id;
    }
    //id will be created in the POST method
    public String search() {
        FhirContext ctx = FhirContext.forDstu2();
        //serverbase=server url
        IGenericClient client = ctx.newRestfulGenericClient("http://nmdp-fhir-dev.us-east-1.elasticbeanstalk.com/baseDstu2");
        System.out.println("Connected to server");
        //Change address obvi
        Bundle bundle= client.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().identifier(id)).returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute();
        Bundle newbundle = client.search().forResource(Specimen.class).where(Specimen.IDENTIFIER.exactly().identifier(id)).returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute();
        System.out.print("Bundle Found");
        //This is a pretty bad way of doing it. it works well since this whole thing is fairly small but ugly.
        String fullBundle=ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle)+ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(newbundle);
        System.out.println("Bundle....bundled...sigh");
        //Need to either loop (which I would like) or rinse and repeat for all resources and
        return fullBundle;
    }
}
    