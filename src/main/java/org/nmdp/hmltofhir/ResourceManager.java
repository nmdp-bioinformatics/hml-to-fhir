package org.nmdp.hmltofhir;

public class ResourceManager {
	
	public static void addResource(String resource,String strucutre, String value) {
		/*create resources
		 * another template
		 * schema example
		 * <resourceName>
		 * structureName=something
		 * position=0
		 * </resourceName>
		 * if else with resource if(resource.equals(resoucename)
		 * { 
		 * 	i.e 
		 * 	getelementbytagID(resourcename);
		 * for nodelist
		 * {
		 * if(structname.equals(XMLParser.getAttribute(newtemplate,"structureName")
		 * {
		 * resourcearray[Integer.parseInt(XMLParser.getAttribute(newtemplate,"postion"))]=isNotNull(value);
		 * }
		 * }
		 */
		
	}
    // Checks if string is null if so
	public static String isNotNull(String value)
	{
		return value==null? null: value;
	}
//All resource string arrays
	//generic constructor
	//AddResource method
	//Check Compliant- if null make send a default thing
}
