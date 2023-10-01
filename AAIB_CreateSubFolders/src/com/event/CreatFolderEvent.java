package com.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import com.filenet.api.collection.AccessPermissionList;
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
public class CreatFolderEvent implements EventActionHandler 
{
FileReader fileReader = null;
final static int SECURITY_Delete = AccessRight.DELETE.getValue();
	// access for IP Matter
final static int SECURITY_View = AccessRight.VIEW_CONTENT.getValue() | AccessRight.READ.getValue()|AccessRight.CREATE_CHILD.getValue()|AccessRight.CREATE_INSTANCE.getValue()|AccessRight.DELETE.getValue();

 public CreatFolderEvent()
 {
 } @SuppressWarnings("unchecked")
public void onEvent(ObjectChangeEvent event, Id subId)
 {
	   
	 
	try {
		
		try
		{
			
			
      //get Event ID
      System.out.println("IntercomJavaEventHandler::onEvent()::Event received");
       System.out.println("IntercomJavaEventHandler::onEvent()::Event Name =" + event.get_Id());

       //get object ID
      Id  id = event.get_SourceObjectId(); 
      event.getClassName();
       System.out.println("IntercomJavaEventHandler::onEvent()::Object ID is : " + id);
       //get objectStore ID 
       ObjectStore  os = event.getObjectStore();
       PropertyFilter pf = new PropertyFilter(); 
       //String folderPath = "/BDC_Archiving/قطاع متابعة ومراقبة الإئتمان ومتابعة مخاطر المحفظة/الفروع/";
   
  Folder folder1 = Factory.Folder.fetchInstance(os,id,pf );	 
/////////////////////////////////////////////////////////////////////////////////////
  Folder sub1 = null;
  Folder sub5 = null;
  
  
 {
	 InputStream stream = CreatFolderEvent.class.getResourceAsStream("/AAIB_CreateSubFolders/configFiles/subfolder.txt");
     System.out.println(stream != null);
     stream = CreatFolderEvent.class.getClassLoader().getResourceAsStream("subfolder.txt");
	
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			int x =1;

 	    try {
				while ((line = bufferedReader.readLine()) != null) {
					try {
						System.out.println("IntercomJavaEventHandler::onEvent():: Inside try of reading file values :: " + line.length());
						
						if(line.contains("Credit Facilities")){
					  sub1=Factory.Folder.createInstance(os,"Folder");
					// com.filenet.api.property.Properties sub_prop = sub.getProperties();
				      sub1.set_FolderName(line);
					  sub1.set_Parent(folder1);
					  sub1.save(RefreshMode.REFRESH);
					  AccessPermissionList permissions = sub1.get_Permissions();
					  
					  
					
						for (int i = 0; i < permissions.size(); i++) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
							AccessPermission ap_security = (AccessPermission) permissions.get(i);
							String SecName = ap_security.get_GranteeName();
//
							if (SecName.contains("CreditAdmin_Maker")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_Maker is view");
								permissions.remove(i);
								//sub2.save(RefreshMode.REFRESH);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_InheritableDepth(new Integer(-1));
								ap_security.set_GranteeName(SecName);
								permissions.add(i,ap_security);
								//sub1.set_Permissions(permissions);
							}
						}
					
						
					  sub1.save(RefreshMode.REFRESH);
						}
						
						if(folder1.equals("CA_CustomerFolder")){
							
							 AccessPermissionList permissions = folder1.get_Permissions();
							  
							  
								
								for (int i = 0; i < permissions.size(); i++) {
									System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
									AccessPermission ap_security = (AccessPermission) permissions.get(i);
									String SecName = ap_security.get_GranteeName();
		//
									if (SecName.contains("CreditAdmin_Maker")) {
										System.out.println(
												"IntercomJavaEventHandler::onEvent():: CreditAdmin_Maker is view");
										permissions.remove(i);
										//sub2.save(RefreshMode.REFRESH);
										ap_security = Factory.AccessPermission.createInstance();
										ap_security.set_AccessType(AccessType.ALLOW);
										ap_security.set_AccessMask(SECURITY_View);
										ap_security.set_InheritableDepth(new Integer(-1));
										ap_security.set_GranteeName(SecName);
										permissions.add(i,ap_security);
										//sub1.set_Permissions(permissions);
									}
								}
								
								folder1.save(RefreshMode.REFRESH);
							
						}
						
						
//						 if(line.equals("اداره الاستخدامات")){
//							  sub2=Factory.Folder.createInstance(os,"Folder");
//							// com.filenet.api.property.Properties sub_prop = sub.getProperties();
//						      sub2.set_FolderName(line);
//							  sub2.set_Parent(folder1); 
//							sub2.save(RefreshMode.REFRESH);
//							  AccessPermissionList permissions = sub2.get_Permissions();
//								for (int i = 0; i < permissions.size(); i++) {
//									System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
//									AccessPermission ap_security = (AccessPermission) permissions.get(i);
//									String SecName = ap_security.get_GranteeName();
//
//									if (SecName.contains("HeadOfCreditDaily_Delete")) {
//										System.out.println(
//												"IntercomJavaEventHandler::onEvent():: HeadOfCreditDaily_Delete is view");
//										permissions.remove(i);
//										//sub2.save(RefreshMode.REFRESH);
//										ap_security = Factory.AccessPermission.createInstance();
//										ap_security.set_AccessType(AccessType.ALLOW);
//										ap_security.set_AccessMask(SECURITY_Delete);
//										ap_security.set_InheritableDepth(new Integer(-1));
//										ap_security.set_GranteeName(SecName);
//										permissions.add(i,ap_security);
//										sub2.set_Permissions(permissions);
										
										
										////////////////////////////////////////
//										
//										   AccessPermission permission = Factory.AccessPermission.createInstance();
//								            permission.set_GranteeName("User");
//								            permission.set_AccessType(AccessType.ALLOW);
//								            permission.set_InheritableDepth(new Integer(-1));
//								            permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_FOLDER_AS_INT));
//								           
//								            AccessPermissionList permissions = folderOj.get_Permissions();
//								            permissions.add(permission);
//								            folderOj.set_Permissions(permissions);
//								            folderOj.save(RefreshMode.REFRESH);   
//								            System.out.println("Done");
										
//									}
//								}
//								sub2.save(RefreshMode.REFRESH);
//								}
//						if(line.equals("معاملات يوميه")||line.equals("عمليات يوميه")
//								||line.equals("مستندات عامه")||line.equals("القرارات")||
//								line.equals("خطاب تفعيل")||line.equals("اخرى")){
//							  sub3=Factory.Folder.createInstance(os,"Folder");
//							// com.filenet.api.property.Properties sub_prop = sub.getProperties();
//						      sub3.set_FolderName(line);
//							  sub3.set_Parent(sub1); 
//							  sub3.save(RefreshMode.REFRESH);
//								}
//						if( line.equals("أصول مستندات العميل")){
//							  sub5=Factory.Folder.createInstance(os,"Originals");
//							// com.filenet.api.property.Properties sub_prop = sub.getProperties();
//						      sub5.set_FolderName(line);
//							  sub5.set_Parent(sub1); 
//							  sub5.save(RefreshMode.REFRESH);
//								}
//						if(line.equals("النماذج")||line.equals("قوائم الموردين")||line.equals("الملحوظات")) {
//							  sub4=Factory.Folder.createInstance(os,"Folder");
//							// com.filenet.api.property.Properties sub_prop = sub.getProperties();
//						      sub4.set_FolderName(line);
//							  sub4.set_Parent(sub2); 
//							  sub4.save(RefreshMode.REFRESH);
//								}
						} catch (Exception e) {
						
						
				            //fail to create directory
				        	System.out.println("special char error");
				            e.printStackTrace();
				        }

    	    
    	    }
 	    
    	    fileReader.close();
			System.out.println("Contents of file:");
//			System.out.println(stringBuffer.toString());
   	}catch(Exception e)
 	    {
   		 e.printStackTrace();
 	    }
	}
 
  // 	}



     
}
		catch (Exception e)
		{
			System.out.println("In Cach");
			}
		}
	catch (Exception e)
    {   e.printStackTrace();
       throw new RuntimeException(e);
    }
	
 }
}



    

