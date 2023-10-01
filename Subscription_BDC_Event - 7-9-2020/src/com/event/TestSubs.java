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
import com.filenet.api.collection.DateTimeList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.DynamicReferentialContainmentRelationshipSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Factory.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory.GetContentEvent;
import com.filenet.api.core.Factory.ReferentialContainmentRelationship;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;
public class TestSubs {
	   private static int i =0;
	   static String uri =  "http://10.3.23.65:9080/wsi/FNCEWS40MTOM";
		static String username = "fnadmin";
		static String password = "P@ssw0rd";
		static String ObjectStore = "XEROXOS";
		static Domain domain;
		static ObjectStore os ;
	public static void main(String[] args) {
		
		//TestClass.CustomerInfo();
		//System.out.println("get testclass :::::::::::"+TestClass.class);
    
		
		FileReader fileReader = null;
	//get connection 
		com.filenet.api.core.Connection conn = Factory.Connection.getConnection(uri);
	UserContext uc = UserContext.get();
		uc.pushSubject(UserContext.createSubject(conn,username,password,"FileNetP8WSI" ));	
		 domain = Factory.Domain.getInstance(conn, null);
		 os = Factory.ObjectStore.fetchInstance(domain,ObjectStore, null);
		 PropertyFilter pf = new PropertyFilter();
		System.out.println("Objec Store ID======== "+os.get_Id());
		Document doc = Factory.Document.fetchInstance(os,"{30522077-0000-C21D-BC2B-1B964D08B918}",null);
		PropertyDescriptionList Custprop = doc.get_ClassDescription().get_PropertyDescriptions();
        Iterator it = Custprop.iterator();
        System.out.println("properties list size is :::::  " + Custprop.size());
        while(it.hasNext())
        {
        	PropertyDescription prop = (PropertyDescription) it.next();
        	System.out.println("Property symbolic name is ::::::::: " + prop.get_SymbolicName());
        	if(prop.get_SymbolicName().equals("RecipientName"))
        	{
        		 System.out.println("Property setability before is ::::::::: " + prop.get_Settability());
        		PropertySettability propdef = prop.get_Settability();
        	//	System.out.println("Property Definition Symbolic ::::::::: " + propdef.get_DisplayName());
        	//  propdef.(PropertySettability.READ_WRITE);
        	  System.out.println("Property setability is ::::::::: " + prop.get_Settability());
        	}
        	
        }
	  doc.save(RefreshMode.REFRESH);
	}
}
	
//
//}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
