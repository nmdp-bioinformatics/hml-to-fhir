# HMLtoFHIR
To add a resource:
Notes: It makes it easier if the resource is referenced by another resource

1) Edit ResourceName.xml and add the resource by following the schema. The position number should be in incremental order for ease of use
2)Edit ResourceManager.java 
    2a)make a new class called 
public static void newResourceName()
{
    //Look in HAPI 1.6 documentation on how to add structures to the specific resource name or email Anu
    resourcename.

}
    2b) add a resource string array as a local variable and add this loop to the addResource method
else if(resource.equals("ResourceName"))
{
System.out.println("Adding to ResourceName");

for (int i = 0; i < positionList.getLength(); i++) {
NamedNodeMap positionAttribute = positionList.item(i).getAttributes();
if(node.equals(parse.getAttribute(positionAttribute,"node"))&&structure.equals(parse.getAttribute(positionAttribute,"structure"))&&resource.equals(parse.getAttribute(positionAttribute,"resource"))&&lower.equals(parse.getAttribute(positionAttribute,"lowerStructure")))
{
resourceNameArray[iteration][Integer.parseInt(parse.getAttribute(positionAttribute,"position"))]=isNotNull(value);
}

}


}
2c) call 2a) in createResources() and make this line Note: index 19 will ALWAYS be the uuid for referencing
resourceNameArray[iteration][19]="urn:uuid:"+UUID.randomUUID().toString();
3) Edit postResource and add this line before the client.transaction.withBundle(bundle).execute()

bundle.addEntry().setFullUrl(manager.resourceNameArray[0][19]).setResource(manager.resourceName).getRequest().setUrl("ResourceName").setMethod(HTTPVerb.POST);

Last but not least
4) Edit Get and add this line to the end of the bundle captures 
Resource resourceName=client.read().resource(resource.class).withUrl(baseSite+referencetoresource.reference().getReference()).execute();
bundle.addEntry().setResource(resourceName);





