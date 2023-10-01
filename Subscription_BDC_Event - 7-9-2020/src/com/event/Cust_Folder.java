package com.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
public class Cust_Folder implements EventActionHandler 
{
	FileReader fileReader = null;

 public Cust_Folder()
 {
 
 }
  public void onEvent(ObjectChangeEvent event, Id subId)
 {
 
	try {

      System.out.println("IntercomJavaEventHandler::onEvent()::Event received");
       System.out.println("IntercomJavaEventHandler::onEvent()::Event Name =" + event.get_Id());

       
  
       
       //get object ID
      Id  id = event.get_SourceObjectId(); 
      event.getClassName();
       System.out.println("IntercomJavaEventHandler::onEvent()::Object ID is : " + id);
       //get objectStore ID 
       ObjectStore  os = event.getObjectStore();
       PropertyFilter pf = new PropertyFilter();  
       // 
       
        Folder folder = Factory.Folder.fetchInstance(os,id,pf );
        //Define parent object type of folder to get PropertyDefinitions
		Folder parent = folder.get_Parent();
		
		// Define currFolder object type of folder to fetch definitions for folder
     	ClassDefinition currFolder = Factory.ClassDefinition.fetchInstance(os,folder.getClassName(),null);
     	// Define currentFolde object to get PropertyDefinitions for parent folder 
		PropertyDefinitionList currentFolder =  currFolder.get_PropertyDefinitions();
		// 
		ClassDefinition custFolder = Factory.ClassDefinition.fetchInstance(os,"CA_CustomerFolder",null);
		PropertyDefinitionList custFolderDefs =  custFolder.get_PropertyDefinitions();
	    System.out.println("Prop length of current folder  is :::::::::::::"+currentFolder.size());
		Properties props = folder.getProperties() ;
		while (!parent.getClassName().equalsIgnoreCase(" "))
		{
			 parent = parent.get_Parent();
			System.out.println("parent folder path is :::::::::::::::::: "+ parent.get_PathName());
			System.out.println("parent folder name is :::::::::::::::::: "+parent.get_FolderName());
			if(parent.getClassName().equalsIgnoreCase("CA_CustomerFolder"))
			{
				//this for to list metadate in the perant document calss
				for (Object property : currentFolder) {
					PropertyDefinition Custprop = (PropertyDefinition) property;
				
					//this for to list metadata in sub document class
					for(Object currProp : custFolderDefs)
					{
						PropertyDefinition currprop = (PropertyDefinition) currProp;
						//this if to compare match meta data between parent folder class and sub folder class 
						if((currprop.get_SymbolicName().equals(Custprop.get_SymbolicName()) &&( currprop.get_SymbolicName().equals("CA_CutomerNumber") ||  currprop.get_SymbolicName().equals("CA_CustomerName") || currprop.get_SymbolicName().equals("CA_BranchName") || currprop.get_SymbolicName().equals("CA_BranchCode") || currprop.get_SymbolicName().equals("CA_DueDate") || currprop.get_SymbolicName().equals("CA_FacilityType") || currprop.get_SymbolicName().equals("CA_WorthDegree") || currprop.get_SymbolicName().equals("CA_CustomerType")|| currprop.get_SymbolicName().equals("CA_ActivationDate")|| currprop.get_SymbolicName().equals("CA_CustomerAccountsBranchesNumbers") || currprop.get_SymbolicName().equals("CA_CustomerAccountsBranchesNames") || currprop.get_SymbolicName().equals("CA_Authority")))) 
						{
						System.out.println("current properties symbolic names ::::::::::  "+currprop.get_SymbolicName() +"   cust properties symbolic names ::::::::::  "+Custprop.get_SymbolicName() );
						Properties parentProps = parent.getProperties();
						System.out.println("current properties symbolic names ::::::::::  "+currprop.get_Name() +"   cust properties symbolic names ::::::::::  "+Custprop.get_Name() );
						if(currprop.get_SymbolicName().equals("CA_DueDate"))
						{
							props.putValue(currprop.get_SymbolicName(), parentProps.getDateTimeListValue(Custprop.get_SymbolicName()));
						}
						else {
						props.putValue(currprop.get_SymbolicName(),parentProps.getStringValue(Custprop.get_SymbolicName()));
						}

						}
					}
				}
		   folder.save(RefreshMode.REFRESH);
		break;
			}
		}
    }

     catch(Exception e)
    {
   e.printStackTrace();
   }}
}
