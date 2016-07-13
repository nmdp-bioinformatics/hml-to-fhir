import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
public class Post {

	
    private String patientFamilyName;
    private String patientGivenName;
    private String patientGender;
    private static String patientBirth;
    private String patientID;
    private String xml;
    
	public Post(String[] data) {
        this.patientFamilyName=data[0];
        this.patientGivenName=data[1];
        this.patientGender=data[2].toUpperCase();
        this.patientBirth=data[3];
        //this.patientID=data[4];
        this.patientID=UUID.randomUUID().toString(); 
        this.xml=data[5];

		//Make this string array 0-n is patient data n+1 is the xml
	}
	public void sendResources() {
		FhirContext ctx = FhirContext.forDstu2();
		//serverbase=server url
		IGenericClient client = ctx.newRestfulGenericClient("http://nmdp-fhir-dev.us-east-1.elasticbeanstalk.com/baseDstu2");
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
        else{}
        
        //Not working skip for now String->DateDt
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
			Date undate = df.parse(patientBirth); 
			String date=df.format(undate);
        DateDt formatDate = new DateDt(date);
        patient.setBirthDate(formatDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       //is it bad practice to set all ID's with the same ID? If its univerisally unique why not right?
        patient.setId(new IdDt(patientID));
       
		
		Bundle bundle= new Bundle();
		bundle.setType(BundleTypeEnum.TRANSACTION);
		System.out.println("Send Resources");
		bundle.addEntry().setFullUrl(patient.getId().getValue()).setResource(patient).getRequest().setUrl("Patient").setMethod(HTTPVerbEnum.POST);
		
		// Send in resource template 
		XMLParse parse = new XMLParse(xml);
		System.out.print("Parsing XML");
		parse.grab();
		ResourceManager resource= new ResourceManager();
		//Hopefully someone gets this reference
		System.out.println("Constructing additional Resources");
		//Make resource
		String placeholder=resource.getPatientStructure(0);
		Person person;
		//Add entries
		//bundle.addEntry() pllus all the other stuff.
		//.resource() will alawys be in a certain order
		//If there is only a  finite things thats fine but if there are a lot could get messy. Not anymore!!
		//client.transaction().withBundle(bundle);
	}

}
