package org.nmdp.hmltofhir;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;*/
import java.util.Arrays;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path( "/toFHIR")
public class Client {
	Logger logger = LoggerFactory.getLogger(Client.class);
	private String cmdXML;
	@POST
	@Path("/Validate")
    //Uncomment when testing with real xml
    //@Produces("application/xml")
	public String validate(@FormParam("xml") String xml)
	{
        
        /*try {
            Runtime runtime = Runtime.getRuntime();
            String[] command = {"/usr/bin/curl", "-X", "POST", "--data-urlencode", "'xml=xml'", " http://miring.b12x.org/validator/ValidateMiring/"};
            Process process = runtime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String cmdXML="";
            String line=null;
            while((line=input.readLine())!=null)
            {
                cmdXML+=line;
            }
        } catch (IOException e) {
            logger.error("Something went wrong in validating "+ e);
            System.out.println("Error in validation");
        }
        System.out.println(cmdXML);*/
        return "stuff";
    }
	@POST
	@Path("/POST")
	public String toPOST(@FormParam("names[]") List <String> dataList)
	{
		System.out.println("Post");
        String[] data=dataList.toArray(new String[0]);
        System.out.println(Arrays.toString(data));
		Postresource post = new Postresource(data);
		String uuid=post.sendResources();
        return uuid;
	}
	@POST
	@Path("/GET")
	public String toGET(@FormParam("id")  String id)
	{
		System.out.println("Attempting to Get ID#= "+ id);
		Get get = new Get(id);
		String bundleXML=get.search();
		
		return bundleXML;
	}
	@POST
	@Path("/UPDATE")
    @Produces("text/plain")

	public String toPUT(@FormParam("xml")  String xml)
	{
		System.out.println("Post");
		/*logger.debug("Attempting to Update");
		//Post to screen user clicks "update"
		//get id of everything
		Put put = new Put(xml);
		put.update();*/
		return "catdog";
	}
}

