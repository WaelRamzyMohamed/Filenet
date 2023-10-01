package com.event;

import java.io.FileReader;
import java.util.Iterator;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;

 
public class Update_Folder implements EventActionHandler  {
	
public  Update_Folder() {
}
public void onEvent(ObjectChangeEvent event, Id subId)
{
	
	try {

	
		FileReader fileReader = null;
	//get connection 
	Id  id = event.get_SourceObjectId();
	ObjectStore os = event.getObjectStore();
	Folder custFolder = Factory.Folder.fetchInstance(os,id,null);
    FolderSet  current  = hasFolder(custFolder,os,id);
	}
    		
	catch(Exception e)
	   {
	  e.printStackTrace();
	  }

    
}

@SuppressWarnings("unchecked")


public static FolderSet hasFolder(Folder folder,ObjectStore os,Id id) {
	
	
	
	FolderSet folders = folder.get_SubFolders();
	Iterator foldIt = folders.iterator();
	Folder custFolder = Factory.Folder.fetchInstance(os,id,null);
	Properties custprops = custFolder.getProperties();
	while(foldIt.hasNext())
	{
		
		try {
		folder = (Folder)foldIt.next();

		System.out.println("under subfolder  ::::::::::::::::"+ folder.get_FolderName());	
		// action to be placed here 
		ClassDefinition currFolder = Factory.ClassDefinition.fetchInstance(os,folder.getClassName(),null);
		PropertyDefinitionList currFoldDefs =  currFolder.get_PropertyDefinitions();
		Properties foldprops = folder.getProperties();

		ClassDefinition custFolddef = Factory.ClassDefinition.fetchInstance(os,"CA_CustomerFolder",null);
		PropertyDefinitionList custFolderDefs =  custFolddef.get_PropertyDefinitions();
		DocumentSet documents = folder.get_ContainedDocuments();

		Iterator<Document> documentIt = documents.iterator();

		while (documentIt.hasNext()) {
			
			try {

			Document doc = documentIt.next();
			ClassDefinition docdef = Factory.ClassDefinition.fetchInstance(os,doc.getClassName(),null);
			PropertyDefinitionList docDefs =  docdef.get_PropertyDefinitions();
			if (docdef.getClassName().equals("TreasuryDocuments")){
				System.out.println("Never Ubdate this Class");
			}else{
			Properties docprops = doc.getProperties();
			
			
			for (Object property : docDefs) {
				PropertyDefinition docpropdef = (PropertyDefinition) property;

				//this for to list metadata in sub document class
				for(Object custProp : custFolderDefs)
				{
					PropertyDefinition custpropdef = (PropertyDefinition) custProp;
					//this if to compare match meta data between parent folder class and sub folder class 
					
					 if((custpropdef.get_SymbolicName().equals(docpropdef.get_SymbolicName()) &&( docpropdef.get_SymbolicName().equals("CA_CutomerNumber") || 
							docpropdef.get_SymbolicName().equals("CA_CustomerName") || 
							docpropdef.get_SymbolicName().equals("CA_BranchName") || docpropdef.get_SymbolicName().equals("CA_BranchCode") || 
							docpropdef.get_SymbolicName().equals("CA_DueDate") || docpropdef.get_SymbolicName().equals("CA_FacilityType") || 
							docpropdef.get_SymbolicName().equals("CA_WorthDegree") || docpropdef.get_SymbolicName().equals("CA_CustomerType")||
							docpropdef.get_SymbolicName().equals("CA_ActivationDate")|| docpropdef.get_SymbolicName().equals("CA_CustomerAccountsBranchesNumbers") ||
							docpropdef.get_SymbolicName().equals("CA_CustomerAccountsBranchesNames") || docpropdef.get_SymbolicName().equals("CA_Authority")))) 
					{
						System.out.println("current properties symbolic names ::::::::::  "+docpropdef.get_SymbolicName() +"   cust properties symbolic names ::::::::::  "+custpropdef.get_SymbolicName() );
					//	Properties custProps = currprop.getProperties();
						//System.out.println("current properties symbolic names ::::::::::  "+currprop.get_Name() +"   cust properties symbolic names ::::::::::  "+docpropdef.get_Name() );
						if(docpropdef.get_SymbolicName().equals("CA_DueDate"))
						{
							docprops.putValue(docpropdef.get_SymbolicName(), custprops.getDateTimeListValue(custpropdef.get_SymbolicName()));
						}
						else {
							//System.out.println("document is :::::::::::::::::::::::::::::"+);
							docprops.putValue(docpropdef.get_SymbolicName(),custprops.getStringValue(custpropdef.get_SymbolicName()));
						}

					}
				}
			}
			doc.save(RefreshMode.NO_REFRESH);
			}}
			catch(Exception e)
			   {
			  e.printStackTrace();
			  }
			
		}
		

		for (Object custproperty : custFolderDefs ) {
			PropertyDefinition foldpropdef = (PropertyDefinition) custproperty;

			//this for to list metadata in sub document class
			for(Object currpropdef : currFoldDefs)
			{
				PropertyDefinition currprop = (PropertyDefinition) currpropdef;
				//this if to compare match meta data between parent folder class and sub folder class 
				if((currprop.get_SymbolicName().equals(foldpropdef.get_SymbolicName()) &&( currprop.get_SymbolicName().equals("CA_CutomerNumber") ||  
						currprop.get_SymbolicName().equals("CA_CustomerName") || currprop.get_SymbolicName().equals("CA_BranchName") ||
						currprop.get_SymbolicName().equals("CA_BranchCode") || currprop.get_SymbolicName().equals("CA_DueDate") || 
						currprop.get_SymbolicName().equals("CA_FacilityType") || currprop.get_SymbolicName().equals("CA_WorthDegree") ||
						currprop.get_SymbolicName().equals("CA_CustomerType")|| currprop.get_SymbolicName().equals("CA_ActivationDate")|| 
						currprop.get_SymbolicName().equals("CA_CustomerAccountsBranchesNumbers") || 
						currprop.get_SymbolicName().equals("CA_CustomerAccountsBranchesNames") || 
						currprop.get_SymbolicName().equals("CA_Authority")))) 
				{
					System.out.println("current properties symbolic names ::::::::::  "+currprop.get_SymbolicName() +"   cust properties symbolic names ::::::::::  "+currprop.get_SymbolicName() );
					//Properties custProp = folder.getProperties();

					if(currprop.get_SymbolicName().equals("CA_DueDate"))
					{
						foldprops.putValue(currprop.get_SymbolicName(), custprops.getDateTimeListValue(foldpropdef.get_SymbolicName()));
					}
					else {
						//System.out.println("document is :::::::::::::::::::::::::::::"+);
						foldprops.putValue(currprop.get_SymbolicName(),custprops.getStringValue(foldpropdef.get_SymbolicName()));
					}

				}
			}
		}
		
		folder.save(RefreshMode.NO_REFRESH);
		hasFolder(folder, os , id);
		}
		catch(Exception e)
		   {
		  e.printStackTrace();
		  }
		
		
		}
		return folders;	
		}
   
	
}



	




