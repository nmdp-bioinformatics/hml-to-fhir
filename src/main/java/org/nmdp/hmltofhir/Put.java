package org.nmdp.hmltofhir;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Enumerations.*;

import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Put {

	private String current;
    private String update;
    private String resource;

	public Put(String structure, String update, String resource) {
        this.current=structure;
        this.update=update;
        this.resource = resource;
	}

	public void update() {
        //client.read gets a resource but still need the ID
        //set id as ("patient/"+ patientID)
        //
        //Patient ID .getID
        //.set ID
        //How each method is going to be
        //Squence.getID
        //Sequence[0]=Sequence.getwhateve
        //Rinse Repeat
        //Sequence[position number]=updated thing
        //set id
        //String to whatever is needed write these in resource manager
        //client.update().resource(sequence).execute();
        
		
	}
		

}
