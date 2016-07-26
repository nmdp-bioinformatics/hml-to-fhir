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
        String baseSite= "http://fhirtest.uhn.ca/baseDstu3/";
        IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
        System.out.println("Connected to server");
        Bundle bundle = client.search().forResource(DiagnosticReport.class).where(DiagnosticReport.IDENTIFIER.exactly().identifier(id)).returnBundle(Bundle.class).execute();
        
        DiagnosticReport diag =(DiagnosticReport)bundle.getEntry().get(0).getResource();
        //When there are more references, loop through the list for each one and add to ubundle in loop
        Observation obv = client.read().resource(Observation.class).withUrl(baseSite+diag.getResult().get(0).getReference()).execute();
        //Sequence seq = client.read().resource(Sequence.class).withUrl(baseSite+obv.getRelated().get(0).getTarget()).execute();
        Specimen spec = client.read().resource(Specimen.class).withUrl(baseSite+diag.getSpecimen().get(0).getReference()).execute();
        Patient patient=client.read().resource(Patient.class).withUrl(baseSite+diag.getSubject().getReference()).execute();
        //Add to bundle
        Bundle finalbundle=new Bundle();
        bundle.addEntry().setResource(diag);
        bundle.addEntry().setResource(obv);
        //bundle.addEntry().setResource(seq);
        bundle.addEntry().setResource(spec);
        bundle.addEntry().setResource(patient);
        
        
        System.out.println("Bundle....bundled...sigh");
        String finalBundle=ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
        System.out.println(finalBundle);
        return finalBundle;
    }
}