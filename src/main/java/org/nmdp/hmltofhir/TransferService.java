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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;



@Path( "/toFHIR")
public class TransferService {
	Logger logger = LoggerFactory.getLogger(Client.class);
	private String cmdXML;
	@POST
	@Path("/Validate")
    @Produces("application/xml")
	public String validate(@FormParam("xml") String xml)
	{
        try {
            Client client = Client.create();
            
            WebResource webResource = client.resource("http://miring.b12x.org/validator/ValidateMiring/");
        
            //Post to validator service due to the nature of HTML forms URLEncoder.encode() decodes anything that would be a space or such into a stirng.
            ClientResponse response = webResource.accept("application/xml").post(ClientResponse.class, "xml=" + URLEncoder.encode(xml, "UTF-8"));
            // check response status code
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
            
            // display response to server
            String output = response.getEntity(String.class);
            System.out.println("Output from Server .... ");
            System.out.println(output + "\n");
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "Oops";
    }
	@POST
	@Path("/POST")
	public String toPOST(@FormParam("names[]") List <String> dataList)
	{
		System.out.println("Post");
        String[] data=dataList.toArray(new String[0]);
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

	public String toPUT(@FormParam("structureArr[]")  List<String> structureArr)
	{
		System.out.println("Post");
        String [] data=structureArr.toArray(new String[0]);
       // Put put = new Put(data[1],data[2],data[3],data[0]);
        //String update =  put.update();
		return "update";
	}
}

