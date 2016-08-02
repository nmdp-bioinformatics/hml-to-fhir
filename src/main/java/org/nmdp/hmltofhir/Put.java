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
    private String lower;
    public static DiganosticReport diag;
    public static ObservationReport obv;
    public static Specimen spec;
    public static Patient patient;
    public static Sequence seq;

	public Put(String structure, String lower, String update, String resource) {
        this.current=structure;
        this.update=update;
        this.resource = resource;
        this.lower=lower;
	}

	public void update() {
        //client.read gets a resource but still need the ID
        /* if( resource.equals(resourceName)
         {
         
        //set id as ("patient/"+ patientID)
        // public void Sequence(structure, update
         {
        //Sequence updateseq=new Sequence()
         updateseq.setStructures(seq.getStructures())
         //Needs to have same ID to update
         updateseq.setId(seq.getId())
         //set id
         //"Create" a new resource with structures
        //client.update().resource(sequence).execute();
         }*/
        
		
	}
    public void resources(DiagnosticReport diag, Observation obv, Specimen spec,Patient patient,Sequence seq)
    {
        this.diag=diag;
        this.obv=obv;
        this.spec=spec;
        this.patient=patient;
        this.seq=seq;
    }
    
		

}
