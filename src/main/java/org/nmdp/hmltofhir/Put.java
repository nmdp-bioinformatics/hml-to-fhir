package org.nmdp.hmltofhir;

public class Put {

	private String current;
    private String update;

	public Put(String structure, String update) {
        this.current=structure;
        this.update=update;
	}

	public void update() {
        //client.read gets a resource but still need the ID
        //patient.setId(new IdDt(patientID)); in put then maybe able to use set id as ("patient/"+ patientID)
        // it should might be bhad practice though to have so many things be the same ID?
        //Patient ID .getID
        //.set ID
        //Two drop down menus?
        //First is resource
        //Second is structure
        //post that in to here
        //find position of structure
        //different methods for each resource would be easier
        //Gonna be hella trivial but damn it it will work]
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
