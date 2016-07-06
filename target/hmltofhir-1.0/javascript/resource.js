/**
 * 
 */
function Post()
{
	var validate=window.location.href + "transfer/toFHIR/Validate/";
	var xml = document.getElementById("patientHML").value;
   
	//var results = $.post(validate,
      //                   {xml:xml},
	   //     function(response)
	    //    {
	           // var resultXml = new XMLSerializer().serializeToString(response);
	            //result/Xml = decodeURIComponent(resultXml);
//alert("in function validate")
	          //  if(isHMLCompliant(response))
                        /// {
                        // alert("Validated");
                        var post = window.location.href + "transfer/toFHIR/POST";
    var names = [document.getElementById("patientFamilyName").value,document.getElementById("patientGivenName").value,document.getElementById("patientGender").value,document.getElementById("patientDate").value//,document.getElementById("patientID").value
                 ,xml];
    var postResult = $.post(post,{names:names})
	            	.done(function(uuid)
	            	        {
	    	            alert( "Information has been posted:" );
                          alert("ID: "+ uuid);
	    	        })
	    	        .fail(function(response)
	    	        {
	    	         //Alerts user about critical server errors due to the file
	    	            alert( "Error.  Something wrong happened2.");
	    	              //replaces all tags, child errors and new lines with spaces
	    	            alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
	    	        })
	    	        .always(function() 
	    	        {
	    	            //alert( "Finished Attempt.  This should always be called after success or failure." );
	    	        }
	    	    );
	          //  }
	            //else
	            //{
	          //  	alert("HML was not compliant. Please visit miring.b12x.org to validate your HML");
	           // }
	        //
	        //})
	        //.done(function()
	       // {
	            //alert( "Function was completed successfully." );
	       // })
	       // .fail(function(response)
	       // {
	         //Alerts user about critical server errors due to the file
	       //     alert( "Error.  Something wrong happened1.");
	              //replaces all tags, child errors and new lines with spaces
	          //  alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
	       // })
	      //  .always(function()
	       // {
	       ///     //alert( "Finished Attempt.  This should always be called after success or failure." );
	       // }
	  //  );
}
function Get()
{
		var get = window.location.href + "transfer/toFHIR/GET/";
		var id = document.getElementById("patientIDGET").value;
        var result = $.post(get,{id:id},
        function(response)
        {
            document.getElementById("resultsText").value = response;
                        })
		  .done(function() 
	        {
	            //alert( "Function was completed successfully." );
                })
	        .fail(function(response)
	        {
	         //Alerts user about critical server errors due to the file
	            alert( "Error.  Something wrong happened.");
	              //replaces all tags, child errors and new lines with spaces
	            alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
	              clearText();
	        })
	        .always(function() 
	        {
	            //alert( "Finished Attempt.  This should always be called after success or failure." );
	        }
	    );
}
function Put()
{
    
    var put = window.location.href + "transfer/toFHIR/UPDATE/";
	var xml = getElementById("result").value;
    var result = $.post(put,{xml:xml},
			function(response)
	        {
	            //alert("This is called if there was a successful request.  Storing the response in the right text box.");
	            var resultXml = new XMLSerializer().serializeToString(response);
	            resultXml = decodeURIComponent(resultXml);
                        document.getElementById("result").value=resultXml;
	        })  
	.done(function() 
	        {
        //alert( "Function was completed successfully." );
    })
    .fail(function(response)
    {
     //Alerts user about critical server errors due to the file
        alert( "Error.  Something wrong happened.");
          //replaces all tags, child errors and new lines with spaces
        alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
    })
    .always(function() 
    {
        //alert( "Finished Attempt.  This should always be called after success or failure." );
    }
    );
    
}/*
function isHMLCompliant(xml)
{
    //Simple string operations to find true, false, or warn user if hml was rejected due to critical hml formatting error
    compliantBooleanBeginLocation = xml.indexOf("<miring-compliant>") + 18;
    compliantBooleanEndLocation = xml.indexOf("</miring-compliant>");
    compliantBoolean = xml.substring(compliantBooleanBeginLocation, compliantBooleanEndLocation);
    
    if(compliantBoolean == "true")
    {
        return true;
    }
    else if (compliantBoolean == "false")
    {
        return false;
    }
    else
    {
        alert("Error determining HML Compliance.");
        return false;
    }
    return false;
}*/