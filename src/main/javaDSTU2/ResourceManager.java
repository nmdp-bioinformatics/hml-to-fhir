
public class ResourceManager {
	private static String patient[];
	public ResourceManager()
	{
		
	}
	public static void addResource(String resource,String strucutre, String value) {
		/*create resources
		 * another template
		 * schema example
		 * <positionSource>
		 * structureName=something
		 * position=0
		 * </>
		 * if else with resource if(resource.equals(resoucename)
		 * { 
		 * 	i.e 
		 * 	getelementbytagID(positionSource);
			 * for nodelist
			 * {
			 * if(structure.equals(XMLParser.getAttribute(newtemplate,"structureName")
			 * {
			 * resourcearray[Integer.parseInt(XMLParser.getAttribute(newtemplate,"postion"))]=isNotNull(value);
			 * }
		 * }
		 */
		
	}
	public static String isNotNull(String value)
	{
		return (value==null? null:value);
		//If we find a null, then when setting we need a convert from string to x for all data types Makes sense to have it here
	}
	//Rinse and repeat this method for all resources I mean also get rid of patient, already kinda handled that... 
	//but you get what I mean right future anu?
	public String getPatientStructure(int index)
	{
		return patient[index];
	}
	//String->data types methods
//All resource string arrays
	//generic constructor
	//AddResource method
	//Check Compliant- if null make send a default thing
}
