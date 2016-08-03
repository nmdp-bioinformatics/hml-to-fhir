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
    public static String [][] pat;
    public static String [][] sequence;
    public static String [][] diagnostic;
    public static String [][] observation;
    public static String [][]specimen;

	public Put(String structure, String lower, String update, String resource) {
        this.current=structure;
        this.update=update;
        this.resource = resource;
        this.lower=lower;
	}

	public void update() {
        //Need a way to KNOW which resource they want to update...cant update all of em.
         if( resource.equals("Patient"))
         {
             Patient updatePat=new Patient();
             //recreate array of resource
             //do as post in resource manager
             //for and if resourceName.xml matches
             //patientarray[Interger.parseInt(getattribute(position,"position")=update;
             //setstructures
             //updatePat.setID(patient.getID());
             //client.update().resource(patient).execute();
             
         }
		
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
