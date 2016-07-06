package org.nmdp.hmltofhir;

public class Put {

	private String xml;

	public Put(String xml) {
		 this.xml=xml;
	}

	public void update() {
		//ArrayofIDS[]=getIds(xml);
		//Since they are in a certain order we can just call all the resource class imma make like in post and then do this
		//patient.setID(ArrayofIDS[0]);
		/*
		 * client.update()
   		.	resource(patient)
   			.execute();
		 	*/
		//
		//Rinse and repeat with all resources.
		//If i know how parsing works, parse through all methods of a inner class for each resource
		
	}
	public String[] getIDs(String xml)
	{
		//get ALL OF THE IDS!!
		//Its an xml but is it an attribute or element?
		return null;
		
	}

}
