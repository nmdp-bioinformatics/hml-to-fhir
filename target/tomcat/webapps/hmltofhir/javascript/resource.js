/**
 *<!--
 
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
 
 -->
 */
function Post()
{
    var validate=window.location.href + "transfer/toFHIR/Validate/";
    var xml = document.getElementById("patientHML").value;
    
    var results = $.post(validate,
                         {xml:xml},
                         function(response)
                         {
                         var resultXml = new XMLSerializer().serializeToString(response);
                         resultXml = decodeURIComponent(resultXml);
                    
                         if(isHMLCompliant(resultXml))
                         {
                         var post = window.location.href + "transfer/toFHIR/POST";
                         var names = [document.getElementById("patientFamilyName").value,document.getElementById("patientGivenName").value,document.getElementById("patientGender").value,document.getElementById("patientDate").value,xml];
                         if(!/([0-9]{4}-[0-9]{2}-[0-9]{2})/.test(names[3]))
                         {
                         alert("Date not entered properly, please enter in yyyy-MM-dd format");
                         }
                         else if(names[0]==""||names[2]==""||names[1]==""||names[4]=="")
                         {
                         alert("Please fill out all forms. HML not processed");
                         }
                         else{
                         var postResult = $.post(post,{names:names})
                         .done(function(uuid)
                               {
                               alert( "Information has been posted\nID: "+uuid );
                               })
                         .fail(function(response)
                               {
                               //Alerts user about critical server errors due to the file
                               //replaces all tags, child errors and new lines with spaces
                               alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
                               })
                         .always(function()
                                 {
                                 //alert( "Finished Attempt.  This should always be called after success or failure." );
                                 }
                                 );
                         }
                         }
                         else
                         {
                            alert("HML was not compliant. Please visit miring.b12x.org to validate your HML");
                         }
                         
                        })
    .done(function()
          {
          //alert( "Function was completed successfully." );
          })
    .fail(function(response)
          {
          //Alerts user about critical server errors due to the file
          //replaces all tags, child errors and new lines with spaces
          alert("Error: " + response.responseText.replace(/{(.*?)}|<(.*?)>/g,"").replace(/^.*com.*$/gm,"").replace(/^.*org.*$/gm,"").replace(/\r?\n|\r/gm," "));
          })
    .always(function()
            {
            ///     //alert( "Finished Attempt.  This should always be called after success or failure." );
            }
            );
}
function Get()
{
    var get = window.location.href + "transfer/toFHIR/GET/";
    var id = document.getElementById("patientIDGET").value;
    if(id=="")
    {
        alert("No ID entered: Please enter an ID")
        
    }
    else{
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
}
function Put()
{
    
    var put = window.location.href + "transfer/toFHIR/UPDATE/";
    var resource = document.getElementById("resour").value;
    var structure = document.getElementById(document.getElementById("resour").value).value.split('-');
    var updateStructure=document.getElementById("changeTextArea").value;
    //0 is non-primative data type 1 is the primative data type
    var structureArr=[resource,structure[0],structure[1],updateStructure];
    if(updateStructure=="")
    {
        alert("Please enter a value to update");
    }
    else if(resource==0)
    {
        alert("Please choose a resource");
    }
    else if(structure==0)
    {
        alert("Please choose a structure");
    }
    else if(substruct==0)
    {
        alert("Please choose a structure");
    }
    var result = $.post(put,{structureArr:structureArr},
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
    
}
  function isHMLCompliant(xml)
  {
  //Simple string operations to find true, false, or warn user if hml was rejected due to critical hml formatting error
  compliantBooleanBeginLocation = xml.indexOf("<hml-compliant>") + 15;
  compliantBooleanEndLocation = xml.indexOf("</hml-compliant>");
  compliantBoolean = xml.substring(compliantBooleanBeginLocation, compliantBooleanEndLocation);
  
  if(compliantBoolean == "true")
  {
      return true;
  }
  else if (compliantBoolean == "false")
  {
      return false;
  }
  else if( compliantBoolean == "reject")
  {
      return false;
  }
  else
  {
      alert("Error determining HML Compliance.");
      return false;
  }
  return false;
  }