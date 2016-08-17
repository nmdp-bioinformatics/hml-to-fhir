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
    public static Put put;
    public static Sequence seq;
    public static DiagnosticReport diag;
    public static Specimen spec;
    public static Patient patient;
    public static Observation obv;
    
    public Get(String id) {
        this.id=id;
    }
    //id will be created in the POST method and is from the HML's hmlid
    public String search() {
        FhirContext ctx = FhirContext.forDstu3();
        String baseSite= "http://fhirtest.uhn.ca/baseDstu3/";
        IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
        System.out.println("Connected to server");
        Bundle bundle = client.search().forResource(DiagnosticReport.class).where(DiagnosticReport.IDENTIFIER.exactly().identifier(id)).returnBundle(Bundle.class).execute();
        
         diag =(DiagnosticReport)bundle.getEntry().get(0).getResource();
        //When there are more references, loop through the list for each one and add to ubundle in loop
         obv = client.read().resource(Observation.class).withUrl(baseSite+diag.getResult().get(0).getReference()).execute();
        //Uncomment when HAPI 2.0 comes out
        // seq = client.read().resource(Sequence.class).withUrl(baseSite+obv.getRelated().get(0).getTarget()).execute();
         spec = client.read().resource(Specimen.class).withUrl(baseSite+diag.getSpecimen().get(0).getReference()).execute();
         patient=client.read().resource(Patient.class).withUrl(baseSite+diag.getSubject().getReference()).execute();
        //Add to bundle
        Bundle finalbundle=new Bundle();
        bundle.addEntry().setResource(diag);
        bundle.addEntry().setResource(obv);
        //Uncomment when HAPI 2.0 comes out
        //bundle.addEntry().setResource(seq);
        bundle.addEntry().setResource(spec);
        bundle.addEntry().setResource(patient);
        
        // Converts bundle to string for text area
        System.out.println("Bundle....bundled...sigh");
        String finalBundle=ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
        return finalBundle;
    }
    public void update(Put put)
    {
        put.resources(diag,obv,spec,patient,seq);
    }
}