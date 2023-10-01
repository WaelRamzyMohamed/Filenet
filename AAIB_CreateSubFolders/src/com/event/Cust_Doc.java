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
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Factory.ReferentialContainmentRelationship;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
public class Cust_Doc implements EventActionHandler 
{
	FileReader fileReader = null;

	
 public  Cust_Doc()
 {
 } public void onEvent(ObjectChangeEvent event, Id subId)
 {
	   
	 
	 try {
		    //get object ID && //get objectStore ID 
		 System.out.println("IntercomJavaEventHandler::onEvent()::Event received");
	       System.out.println("IntercomJavaEventHandler::onEvent()::Event Name =" + event.get_Id());

	       //get object ID
	      Id  id = event.get_SourceObjectId(); 
	       System.out.println("IntercomJavaEventHandler::onEvent()::Object ID is : " + id);
	       //get objectStore ID 
	       ObjectStore  os = event.getObjectStore();
	       PropertyFilter pf = new PropertyFilter();
			Document doc = Factory.Document.fetchInstance(os,id,null);
			//
			Properties props = doc.getProperties() ;
			ClassDefinition currDoc = Factory.ClassDefinition.fetchInstance(os,doc.getClassName(),null);
			//list all meta data definition for parent folder class  
			PropertyDefinitionList currentDoc =  currDoc.get_PropertyDefinitions();
			//list all meta data definition for document class 
			ClassDefinition custFolder = Factory.ClassDefinition.fetchInstance(os,"CA_CustomerFolder",null);
			PropertyDefinitionList custFolderDefs =  custFolder.get_PropertyDefinitions();
		    FolderSet contFolders = doc.get_FoldersFiledIn();
		    Iterator contIt =contFolders.iterator();
		    Folder parentFold = null;
		     while(contIt.hasNext())
		     {
		    	 parentFold = (Folder) contIt.next();
		    	 System.out.println("Folder Name is ::::::::::::: "+parentFold.get_FolderName());
		    }
		     while (!parentFold.getClassName().equalsIgnoreCase(" "))
				{
		    	 parentFold = parentFold.get_Parent();
					System.out.println("parent folder path is :::::::::::::::::: "+ parentFold.get_PathName());
					System.out.println("parent folder name is :::::::::::::::::: "+ parentFold.get_FolderName());
					if(parentFold.getClassName().equalsIgnoreCase("CA_CustomerFolder"))
					{
						for (Object property : currentDoc) {
							PropertyDefinition Custprop = (PropertyDefinition) property;
							System.out.println("cust properties symbolic names ::::::::::"+Custprop.get_SymbolicName());
							//this for to list metadata in sub document class
							for(Object currProp : custFolderDefs)
							{
								PropertyDefinition currprop = (PropertyDefinition) currProp;
								//this if to compare match meta data between parent folder class and sub folder class 
								if((currprop.get_SymbolicName().equals(Custprop.get_SymbolicName()) &&( currprop.get_SymbolicName().equals("CIF") ||  currprop.get_SymbolicName().equals("English_CustomerName") || currprop.get_SymbolicName().equals("Arabic_CustomerName") 
										|| currprop.get_SymbolicName().equals("DOCTest") || currprop.get_SymbolicName().equals("Com_RegistrationNumber") || currprop.get_SymbolicName().equals("National_ID") 
										|| currprop.get_SymbolicName().equals("Customer_BrCode_BrName") || currprop.get_SymbolicName().equals("Customer_Type")))) 
								{
								System.out.println("current properties symbolic names ::::::::::  "+currprop.get_SymbolicName() +"   cust properties symbolic names ::::::::::  "+Custprop.get_SymbolicName() );
								Properties parentProps = parentFold.getProperties();
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
						doc.save(RefreshMode.REFRESH);
						break;
					}
				}
		}

		catch(Exception e)
		    {
			 e.printStackTrace();
		    }}
 }
