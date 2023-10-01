package com.event;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AccessRight;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;

public class WorkFlowChecker implements EventActionHandler {

	public WorkFlowChecker() {
	}

	final static int SECURITY_View = AccessRight.VIEW_CONTENT.getValue() | AccessRight.READ.getValue();
	final static int SECURITY_DIDataEntry = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() |  AccessRight.CHANGE_STATE.getValue()
			| AccessRight.CREATE_INSTANCE.getValue() | AccessRight.WRITE.getValue()
			| AccessRight.MAJOR_VERSION.getValue() | AccessRight.DELETE.getValue();

	final static int SECURITY_DIReviewers = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() | AccessRight.CHANGE_STATE.getValue()
			|  AccessRight.WRITE.getValue() | AccessRight.MAJOR_VERSION.getValue() |  AccessRight.PUBLISH.getValue() | AccessRight.PUBLISH_AS_INT;
	
	final static int SECURITY_DIReviewers2 = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() | AccessRight.CHANGE_STATE.getValue()
			|  AccessRight.WRITE.getValue() | AccessRight.MAJOR_VERSION.getValue();

	final static int SECURITY_DIApproved = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() 
			|  AccessRight.WRITE.getValue() | AccessRight.MAJOR_VERSION.getValue() |  AccessRight.PUBLISH.getValue() | AccessRight.PUBLISH_AS_INT;
	
	final static int SECURITY_OSApproved = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() 
			| AccessRight.UNLINK.getValue() | AccessRight.WRITE.getValue()|  AccessRight.PUBLISH.getValue() | AccessRight.PUBLISH_AS_INT;
	
	final static int SECURITY_OC2Approved = AccessRight.READ.getValue() | AccessRight.WRITE_ACL.getValue()
			| AccessRight.VIEW_CONTENT.getValue() | AccessRight.LINK.getValue()
			| AccessRight.UNLINK.getValue() | AccessRight.WRITE.getValue() |  AccessRight.LINK.getValue() ;
	@SuppressWarnings({ "unchecked", "static-access" })
	public void onEvent(ObjectChangeEvent event, Id subId) {

		try {

			System.out.println("IntercomJavaEventHandler::onEvent()::Event received");
			System.out.println("IntercomJavaEventHandler::onEvent()::Event Name =" + event.get_Id());

			// get object ID
			Id id = event.get_SourceObjectId();
			System.out.println("IntercomJavaEventHandler::onEvent()::Object ID is : " + id);
			// // get objectStore ID
			ObjectStore os = event.getObjectStore();
			Domain domain = Factory.Domain.getInstance(event.getConnection(), null);
			PropertyFilter pf = new PropertyFilter();
			Document doc = Factory.Document.fetchInstance(os, id, null);
			Properties fnProperties = doc.getProperties();
			String flagCh = fnProperties.getStringValue("Checker");
			String flagMa = fnProperties.getStringValue("Maker");
			String flagCh2 = fnProperties.getStringValue("Checker2");
			String flagOS = fnProperties.getStringValue("Supervisor");
			
String BM_ATT_ROUTE = fnProperties.getStringValue("CA_DocumentStatus");
String BM_ATT_ROUTE2 = fnProperties.getStringValue("CA_CheckoutDocument");

 		///////////////////////////////////////sleep case of ÃƒËœÃ‚Â¥ÃƒËœÃ‚Â±ÃƒËœÃ‚Â³ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Å¾ Ãƒâ„¢Ã¢â‚¬Å¾Ãƒâ„¢Ã¢â‚¬Å¾Ãƒâ„¢Ã¢â‚¬Â¦ÃƒËœÃ‚Â±ÃƒËœÃ‚Â§ÃƒËœÃ‚Â¬ÃƒËœÃ‚Â¹ ////////////////////////////////
			
		 if (BM_ATT_ROUTE.equals("toReview") && flagCh.equals("true") && flagMa.equalsIgnoreCase("false") &&flagOS.equalsIgnoreCase("false")&&flagCh2.equalsIgnoreCase("false")) {
				System.out.println("IntercomJavaEventHandler::onEvent():: Subscription to Sleep");
			}
///////////////////////////////////////sleep case of ÃƒËœÃ‚ÂªÃƒâ„¢Ã¢â‚¬Â¦ ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â¥ÃƒËœÃ‚Â³ÃƒËœÃ‚ÂªÃƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Â¦ ////////////////////////////////
		 
				if (BM_ATT_ROUTE.equals("complete") && flagCh.equals("false") && flagMa.equalsIgnoreCase("false")&&flagOS.equalsIgnoreCase("false")&&flagCh2.equalsIgnoreCase("false")) {
					System.out.println("IntercomJavaEventHandler::onEvent():: Subscription to Sleep");
					
				}
				
				///////////////////////////////////////////
				if (BM_ATT_ROUTE2.equals("نعم") && flagCh.equals("false") && flagMa.equalsIgnoreCase("false")&& flagOS.equalsIgnoreCase("true")&&flagCh2.equalsIgnoreCase("false")) {
					System.out.println("IntercomJavaEventHandler::onEvent():: Subscription to Sleep");
					
				}
				
				///////////////////////////////////////////////////////////////////////////////////////
				if (BM_ATT_ROUTE2.equals("-") && flagCh.equals("false") && flagMa.equalsIgnoreCase("false")&& flagOS.equalsIgnoreCase("true")&&flagCh2.equalsIgnoreCase("false")) {
					System.out.println("IntercomJavaEventHandler::onEvent():: Subscription to Sleep");
					
				}
///////////////////////////////////////sleep case of ÃƒËœÃ‚Â±Ãƒâ„¢Ã¯Â¿Â½ÃƒËœÃ‚Â¶ ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â¥ÃƒËœÃ‚Â³ÃƒËœÃ‚ÂªÃƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Â¦ ////////////////////////////////
				
				if (BM_ATT_ROUTE.equals("toCorrect") && flagCh.equals("false") && flagMa.equalsIgnoreCase("true")&&flagOS.equalsIgnoreCase("false")&&flagCh2.equalsIgnoreCase("false")) {
					System.out.println("IntercomJavaEventHandler::onEvent():: Subscription to Sleep");
				}
				
				/////////////////////////////////////
				
				
///////////////////////////////////////case of ÃƒËœÃ‚Â¥ÃƒËœÃ‚Â±ÃƒËœÃ‚Â³ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Å¾ Ãƒâ„¢Ã¢â‚¬Å¾Ãƒâ„¢Ã¢â‚¬Å¾Ãƒâ„¢Ã¢â‚¬Â¦ÃƒËœÃ‚Â±ÃƒËœÃ‚Â§ÃƒËœÃ‚Â¬ÃƒËœÃ‚Â¹ ////////////////////////////////
				
			 if (BM_ATT_ROUTE.equals("toReview")&& ((flagCh.equals("false") && flagMa.equals("false")&& flagOS.equals("false")&&flagCh2.equalsIgnoreCase("false"))|| (flagCh.equals("false") && flagMa.equals("true")&& flagOS.equals("false")&&flagCh2.equalsIgnoreCase("false")) )) {
				try {
					if (BM_ATT_ROUTE.equals("toReview")&& flagCh.equals("false") && flagMa.equals("false")&& flagOS.equals("false")&&flagCh2.equalsIgnoreCase("false"))
							{
						fnProperties.putValue("RecipientName","null");
						DateFormat defFormat = new SimpleDateFormat("yyyy-MM-dd"); 
						 Date date = new Date(System.currentTimeMillis());
						 String DateNow = defFormat.format(date);
						 Date creationDate = defFormat.parse(DateNow);
						 System.out.println("date now is :::"+creationDate);
						 fnProperties.putValue("CA_DateAdded",creationDate);
						
//						String date = "2000-01-01";
//						DateFormat defFormat = new SimpleDateFormat("yyyy-MM-dd");
//						Date creationDate = defFormat.parse(date);
//						fnProperties.putValue("ReceivedDate",creationDate);
	
						//doc.save(RefreshMode.REFRESH);
					}
					
					AccessPermissionList permissions = doc.get_Permissions();
					for (int i = 0; i < permissions.size(); i++) {
						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
						AccessPermission ap_security = (AccessPermission) permissions.get(i);
						String SecName = ap_security.get_GranteeName();
						//String SecName1 = SecName;
					//	SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName);
						try {
							if (SecName.contains("CreditAdmin_OM")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
								
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}

						try {
							if (SecName.contains("CreditAdmin_OC")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_DIReviewers);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}
						try {
							if (SecName.contains("CreditAdmin_OC2")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}
						try {
							if (SecName.contains("CreditAdmin_OS")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}


//						System.out.println(":::::::::::::::::::::::: flag value for maker = "
//								+ fnProperties.getStringValue("Maker"));
					}
					String FCh = "true";
					String Fm = "false";
					String FOS = "false";
					String FCh2 ="false";
					fnProperties.putValue("Checker", FCh);
					fnProperties.putValue("Maker", Fm);	
					fnProperties.putValue("Checker2", FCh2);
					fnProperties.putValue("Supervisor", FOS);
					doc.save(RefreshMode.REFRESH);
					
					//////////////////////// Employee Numbers Subscription ////////////////////				
				//	EmployeeNumsMain(doc, os, domain);

				} catch (Exception ex) {
					System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
					ex.printStackTrace();
				}
				
			}
			 
			
			 
			 
///////////////////////////////////////case of ÃƒËœÃ‚ÂªÃƒâ„¢Ã¢â‚¬Â¦ ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â¥ÃƒËœÃ‚Â³ÃƒËœÃ‚ÂªÃƒâ„¢Ã¢â‚¬Å¾ÃƒËœÃ‚Â§Ãƒâ„¢Ã¢â‚¬Â¦ ////////////////////////////////
			 
			 if (BM_ATT_ROUTE.equals("complete")&& flagCh.equals("true") && flagMa.equals("false")&& flagOS.equals("false")&&flagCh2.equalsIgnoreCase("false")){

				try {
					 if (BM_ATT_ROUTE.equals("complete")&& flagCh.equals("true") && flagMa.equals("false")&& flagOS.equals("false"))
					 {
						// String date = "2020-01-01";
						 DateFormat defFormat = new SimpleDateFormat("yyyy-MM-dd"); 
						 Date date = new Date(System.currentTimeMillis());
						 String DateNow = defFormat.format(date);
						 Date creationDate = defFormat.parse(DateNow);
						 System.out.println("date now is :::"+creationDate);
						 fnProperties.putValue("ReceiveDate",creationDate);
							
		
					 }
					AccessPermissionList permissions = doc.get_Permissions();
					for (int i = 0; i < permissions.size(); i++) {
						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
						AccessPermission ap_security = (AccessPermission) permissions.get(i);
						String SecName = ap_security.get_GranteeName();
						//String SecName1 = SecName;
					//	SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName);
						try {
							if (SecName.contains("CreditAdmin_OM")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}

						try {
							if (SecName.contains("CreditAdmin_OC")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_View);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}
						try {
							if (SecName.contains("CreditAdmin_OC2")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_OC2Approved);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}
						
						try {
							if (SecName.contains("CreditAdmin_OS")) {
								System.out.println(
										"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
								permissions.remove(i);
								ap_security = Factory.AccessPermission.createInstance();
								ap_security.set_AccessType(AccessType.ALLOW);
								ap_security.set_AccessMask(SECURITY_OSApproved);
								ap_security.set_GranteeName(SecName);
								permissions.add(i, ap_security);
								doc.set_Permissions(permissions);
							}
						} catch (Exception ex) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
							ex.printStackTrace();
						}


					//						System.out.println(":::::::::::::::::::::::: flag value for maker = "
//								+ fnProperties.getStringValue("Maker"));
					}
					String FCh = "false";
					String Fm = "false";
					String FCh2="true";
					String FOS = "true";
					fnProperties.putValue("Checker", FCh);
					fnProperties.putValue("Maker", Fm);	
					fnProperties.putValue("Checker2", FCh2);
					fnProperties.putValue("Supervisor", FOS);
					doc.save(RefreshMode.REFRESH);
					
					//////////////////////// Employee Numbers Subscription ////////////////////				
				//	EmployeeNumsMain(doc, os, domain);

				} catch (Exception ex) {
					System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
					ex.printStackTrace();
				}
				
			 }
			 
			 ////////////////////////
			 
			 if (BM_ATT_ROUTE2.equals("نعم")&& flagCh.equals("false") && flagMa.equals("false") && flagOS.equals("true")&&flagCh2.equalsIgnoreCase("true")){

					try {
						if (BM_ATT_ROUTE2.equals("نعم")&& flagCh.equals("false") && flagMa.equals("false") && flagOS.equals("true")&&flagCh2.equalsIgnoreCase("true")){
						 DateFormat defFormat = new SimpleDateFormat("yyyy-MM-dd"); 
						 Date date = new Date(System.currentTimeMillis());
						 String DateNow = defFormat.format(date);
						 Date creationDate = defFormat.parse(DateNow);
						 System.out.println("date now is :::"+creationDate);
						 fnProperties.putValue("DateDocumentWithdrawn",creationDate);
						}
						AccessPermissionList permissions = doc.get_Permissions();
						for (int i = 0; i < permissions.size(); i++) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
							AccessPermission ap_security = (AccessPermission) permissions.get(i);
							String SecName = ap_security.get_GranteeName();
							//String SecName1 = SecName;
						//	SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
							System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName);
							try {
								if (SecName.contains("CreditAdmin_OM")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OC")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OC2")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OS")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_OSApproved);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}

//							System.out.println(":::::::::::::::::::::::: flag value for maker = "
//									+ fnProperties.getStringValue("Maker"));
						}
						String FCh = "false";
						String Fm = "false";
						String FCh2="false";
						String FOS= "true";
						fnProperties.putValue("Checker", FCh);
						fnProperties.putValue("Maker", Fm);
						fnProperties.putValue("Checker2",FCh2);
						fnProperties.putValue("Supervisor", FOS);
						doc.save(RefreshMode.REFRESH);
						
						//////////////////////// Employee Numbers Subscription ////////////////////				
					//	EmployeeNumsMain(doc, os, domain);

					} catch (Exception ex) {
						System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
						ex.printStackTrace();
					}
			
				 }
			 
			 ////////////////////////////////////////////////////////
			 if (BM_ATT_ROUTE2.equals("-")&& flagCh.equals("false") && flagMa.equals("false") && flagOS.equals("true")
					 &&flagCh2.equalsIgnoreCase("false")){

					try {

						AccessPermissionList permissions = doc.get_Permissions();
						for (int i = 0; i < permissions.size(); i++) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
							AccessPermission ap_security = (AccessPermission) permissions.get(i);
							String SecName = ap_security.get_GranteeName();
							//String SecName1 = SecName;
						//	SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
							System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName);
							try {
								if (SecName.contains("CreditAdmin_OM")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OC")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OC2")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_OC2Approved);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OS")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}

//							System.out.println(":::::::::::::::::::::::: flag value for maker = "
//									+ fnProperties.getStringValue("Maker"));
						}
						String FCh = "false";
						String Fm = "false";
						String FCh2= "true";
						String FOS= "false";
						fnProperties.putValue("Checker", FCh);
						fnProperties.putValue("Maker", Fm);
						fnProperties.putValue("Checker2",FCh2);
						fnProperties.putValue("Supervisor", FOS);
						doc.save(RefreshMode.REFRESH);
						
						//////////////////////// Employee Numbers Subscription ////////////////////				
					//	EmployeeNumsMain(doc, os, domain);

					} catch (Exception ex) {
						System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
						ex.printStackTrace();
					}
					 
				 }
	
///////////////////////////////////////////
			 if (BM_ATT_ROUTE.equals("toCorrect")&& flagCh.equals("true") && flagMa.equalsIgnoreCase("false")&& flagOS.equals("false") &&flagCh2.equalsIgnoreCase("false")){

					try {

						AccessPermissionList permissions = doc.get_Permissions();
						for (int i = 0; i < permissions.size(); i++) {
							System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
							AccessPermission ap_security = (AccessPermission) permissions.get(i);
							String SecName = ap_security.get_GranteeName();
							//String SecName1 = SecName;
						//	SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
							System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName);
							try {
								if (SecName.contains("CreditAdmin_OM")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_DIDataEntry);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}

							try {
								if (SecName.contains("CreditAdmin_OC")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OC of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OC2")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}
							try {
								if (SecName.contains("CreditAdmin_OS")) {
									System.out.println(
											"IntercomJavaEventHandler::onEvent():: CreditAdmin_OM of complete with false and false");
									permissions.remove(i);
									ap_security = Factory.AccessPermission.createInstance();
									ap_security.set_AccessType(AccessType.ALLOW);
									ap_security.set_AccessMask(SECURITY_View);
									ap_security.set_GranteeName(SecName);
									permissions.add(i, ap_security);
									doc.set_Permissions(permissions);
								}
							} catch (Exception ex) {
								System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
								ex.printStackTrace();
							}

//							System.out.println(":::::::::::::::::::::::: flag value for maker = "
//									+ fnProperties.getStringValue("Maker"));
						}
						String FCh = "false";
						String Fm = "true";
						String FCh2= "false";
						String FOS = "false";
						fnProperties.putValue("Checker", FCh);
						fnProperties.putValue("Maker", Fm);	
						fnProperties.putValue("Checker2",FCh2);
						fnProperties.putValue("Supervisor", FOS);
						
						doc.save(RefreshMode.REFRESH);
						
						//////////////////////// Employee Numbers Subscription ////////////////////				
					//	EmployeeNumsMain(doc, os, domain);

					} catch (Exception ex) {
						System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
						ex.printStackTrace();
					}
					
				 }
				 
			 
//
//			else if (BM_ATT_ROUTE.equals("toCorrect") && flagCh.equals("true") && flagMa.equals("true")) {
//
//				try {
//					AccessPermissionList permissions = doc.get_Permissions();
//
//					// Iterator<?> iter=permissions.iterator();
//					for (int i = 0; i < permissions.size(); i++) {
//						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
//						AccessPermission ap_security = (AccessPermission) permissions.get(i);
//						String SecName = ap_security.get_GranteeName();
//						String SecName1 = SecName;
//						SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
//						// System.out.println("2::::::::::::::::::"+SecName1);
//						// SecName=SecName.replaceAll(SecName2, "");
//						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName1);
//						try {
//							if (SecName1.equalsIgnoreCase("DE")) {
//								System.out.println(
//										"IntercomJavaEventHandler::onEvent():: DE of toCorrect with true and true");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_DIDataEntry);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//
//						try {
//							if (SecName1.equalsIgnoreCase("EV") || SecName1.equalsIgnoreCase("OR")
//									|| SecName1.equalsIgnoreCase("UP")) {
//								System.out.println(
//										"IntercomJavaEventHandler::onEvent():: REV of toCorrect with true and true");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_View);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						} 
//
//					}
//					String FCh = "false";
//					String Fm = "true";
//					fnProperties.putValue("Checker", FCh);
//					fnProperties.putValue("Maker", Fm);
//					doc.save(RefreshMode.REFRESH);
//
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//
//			else if (BM_ATT_ROUTE.equals("toCorrect") && flagCh.equals("false") && flagMa.equals("false")) {
//				try {
//					AccessPermissionList permissions = doc.get_Permissions();
//
//					// Iterator<?> iter=permissions.iterator();
//					for (int i = 0; i < permissions.size(); i++) {
//						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
//						AccessPermission ap_security = (AccessPermission) permissions.get(i);
//						String SecName = ap_security.get_GranteeName();
//						String SecName1 = SecName;
//						SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
//						// System.out.println("2::::::::::::::::::"+SecName1);
//						// SecName=SecName.replaceAll(SecName2, "");
//						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName1);
//						try {
//							if (SecName1.equalsIgnoreCase("DE")) {
//								System.out.println(
//										"IntercomJavaEventHandler::onEvent():: inside if condition DE of Tocorrect");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_DIDataEntry);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//						try {
//							if (SecName1.equalsIgnoreCase("EV") || SecName1.equalsIgnoreCase("OR")
//									|| SecName1.equalsIgnoreCase("UP")) {
//								System.out.println(
//										"IntercomJavaEventHandler::onEvent():: inside if condition of REV of Tocorrect");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_View);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							ex.printStackTrace();
//						}
//					}
//
//					String FCh = "false";
//					String FM = "true";
//					fnProperties.putValue("Checker", FCh);
//					fnProperties.putValue("Maker", FM);
//					doc.save(RefreshMode.REFRESH);
//					System.out.println(":::::::::::::::::::::::: flag value = " + fnProperties.getStringValue("flag"));
//
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			} else if (BM_ATT_ROUTE.equals("toReview") && flagCh.equals("false") && flagMa.equals("true")) {
//
//				try {
//					AccessPermissionList permissions = doc.get_Permissions();
//
//					// Iterator<?> iter=permissions.iterator();
//					for (int i = 0; i < permissions.size(); i++) {
//						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
//						AccessPermission ap_security = (AccessPermission) permissions.get(i);
//						String SecName = ap_security.get_GranteeName();
//						String SecName1 = SecName;
//						SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
//						// System.out.println("2::::::::::::::::::"+SecName1);
//						// SecName=SecName.replaceAll(SecName2, "");
//						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName1);
//						try {
//							if (SecName1.equalsIgnoreCase("DE")) {
//								System.out.println("IntercomJavaEventHandler::onEvent():: DE of toReview");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_View);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
//							ex.printStackTrace();
//						}
//
//						try {
//
//							if (SecName1.equalsIgnoreCase("EV") || SecName1.equalsIgnoreCase("OR")
//									|| SecName1.equalsIgnoreCase("UP")) {
//								System.out.println("IntercomJavaEventHandler::onEvent():: REV of toReview");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_DIReviewers);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
//							ex.printStackTrace();
//						}
//
//					}
//					String FCh = "true";
//					String FM = "false";
//					fnProperties.putValue("Checker", FCh);
//					fnProperties.putValue("Maker", FM);
//					doc.save(RefreshMode.REFRESH);
//					System.out
//							.println(":::::::::::::::::::::::: flag value = " + fnProperties.getStringValue("Checker"));
//				} catch (Exception ex) {
//					System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of setting flag");
//					ex.printStackTrace();
//				}
//			} else if (BM_ATT_ROUTE.equals("toCorrect") && flagCh.equals("true") && flagMa.equals("false")) {
//
//				try {
//					AccessPermissionList permissions = doc.get_Permissions();
//
//					// Iterator<?> iter=permissions.iterator();
//					for (int i = 0; i < permissions.size(); i++) {
//						System.out.println("IntercomJavaEventHandler::onEvent():: inside for loop");
//						AccessPermission ap_security = (AccessPermission) permissions.get(i);
//						String SecName = ap_security.get_GranteeName();
//						String SecName1 = SecName;
//						SecName1 = SecName1.substring(SecName.length() - 24, SecName.length() - 22);
//						// System.out.println("2::::::::::::::::::"+SecName1);
//						// SecName=SecName.replaceAll(SecName2, "");
//						System.out.println("IntercomJavaEventHandler::onEvent()::group name " + SecName1);
//						try {
//							if (SecName1.equalsIgnoreCase("DE")) {
//								System.out.println("IntercomJavaEventHandler::onEvent():: DE of toReview");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_DIDataEntry);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
//							ex.printStackTrace();
//						}
//
//						try {
//
//							if (SecName1.equalsIgnoreCase("EV") || SecName1.equalsIgnoreCase("OR")
//									|| SecName1.equalsIgnoreCase("UP")) {
//								System.out.println("IntercomJavaEventHandler::onEvent():: REV of toReview");
//								permissions.remove(i);
//								ap_security = Factory.AccessPermission.createInstance();
//								ap_security.set_AccessType(AccessType.ALLOW);
//								ap_security.set_AccessMask(SECURITY_View);
//								ap_security.set_GranteeName(SecName);
//								permissions.add(i, ap_security);
//								doc.set_Permissions(permissions);
//							}
//						} catch (Exception ex) {
//							System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of if condition");
//							ex.printStackTrace();
//						}
//
//					}
//					String FCh = "false";
//					String FM = "true";
//					fnProperties.putValue("Checker", FCh);
//					fnProperties.putValue("Maker", FM);
//					doc.save(RefreshMode.REFRESH);
//					System.out
//							.println(":::::::::::::::::::::::: flag value = " + fnProperties.getStringValue("Checker"));
//				} catch (Exception ex) {
//					System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of setting flag");
//					ex.printStackTrace();
//				}
//			}
//
				
		} catch (Exception ex) {
			System.out.println("IntercomJavaEventHandler::onEvent():: inside catch of setting flag");
			ex.printStackTrace();
		}

	}

}
