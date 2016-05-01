package gov.utah.va.vts.action;

import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.BenefitRecipient;
import gov.utah.va.vts.model.BenefitType;
import gov.utah.va.vts.model.Dashboard;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.Note;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranAttachment;
import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.model.VeteranNote;
import gov.utah.va.vts.model.VeteranServicePeriod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;

/**
 * Veteran action class.
 * 
 * @author HNGUYEN
 *
 */
public class VeteranAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	private static final int	SSN_LENGTH_NO_DASHES	= 9;
		
	private Veteran veteran;
	
	private VeteranAttachment veteranAttachment;
	
	private List<Veteran> veterans;
	
	private boolean fromDashboard = false;
	
	private File attachment;
	private String attachmentFileName;
	private String attachmentContentType;
	
	private File attachmentTxt;
	private String attachmentTxtFileName;
	private String attachmentTxtContentType;
	
	private String veteranId;	// used in saveAttachments for action redirect parameter edtitVeteran.action?id=102 
	private String dd214 = "";	// used for toggleAttachmentTxt - id value of all DD214 forms separated by ,
		
	public String newEntry() throws Exception {
		
		logger.debug("entering newEntry() ...");
		
		// pre-set values	
		veteran = new Veteran();
		veteran.setVetPageTitle(getText("veteran.new"));
		if (veteran.getMailingCity() != null && veteran.getMailingCity().length() > 1) {
			veteran.setMailingAddr("y");
		} else {
			veteran.setMailingAddr("n");
		}
		
		RecordType recordType = new RecordType();
		recordType.setId(6L);	// Manual record type (source)
		veteran.setRecordType(recordType);
		
		veteran.setSsn("000-00-0000");	// temp ssn
		veteran.setPercentDisability(new Integer(0));
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(false));
		veteran.setSourceDate(new Date());
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setReviewed(new Integer(1));	// reviewed
		veteran.setActive(new Integer(1));
		veteran.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		
		service.saveVeteran(veteran);	// persist veteran in advanced so ajax calls will work.

		return INPUT;
	}
	
	public String save() throws Exception {
		
		logger.debug("entering save()...");
		
		// gather values for veteran object before persist to db
		gatherValues();
		
		// retrieve benefit type and recipient records
		List<VeteranBenefit> veteranBenefits = benefitRecipientOrphan();
		
		// persist veteran
		setRuralCrosswalk(veteran);
		service.saveVeteran(veteran);
		
		// re-insert benefit recipients
		benefitRecipientReinsert(veteranBenefits);
		
		// clear note text when redisplay
		veteran.setNoteText("");

		addActionMessage(getText("message.save", Util.getStringArray(getText("veteran"))));
		
		// check if save for New Entry or Edit Veteran page
		if (getText("veteran.new").equals(veteran.getVetPageTitle())) {		// New Entry
			
			// check if ssn does not already exists, create current veteran record.
			if (service.findVeteransBySsn(veteran.getSsn()).size() == 1 && veteran.getVaCurrent().intValue() == 0) {	// new entry has been saved in advanced.
				// create current veteran
				Veteran currentVeteran = new Veteran();
				BeanUtils.copyProperties(currentVeteran, veteran);
								
				currentVeteran.setId(null);
				currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
				
				service.saveVeteran(currentVeteran);
				
				// copy other data from veteran to current veteran 
				doCopy(currentVeteran.getId(), veteran.getId());
				
				BeanUtils.copyProperties(veteran, currentVeteran);	// information lost after doCopy, re-copy for display.
			}
			
			return SUCCESS;
		} else {	// Edit Veteran
			return getText("SEARCH_RESULT");
		}
	}
	
	public String saveEditCompare() throws Exception {
		
		logger.debug("entering saveEditCompare ...");
		
		if (veteran.isFromDashboard()) {
			this.setFromDashboard(true);
		}
		
		// gather values for veteran object before persist to db
		gatherValues_editCompare();
		
		// retrieve benefit type and recipient records
		List<VeteranBenefit> veteranBenefits = benefitRecipientOrphan();
		
		// persist veteran (proposed or current record)
		setRuralCrosswalk(veteran);
		service.saveVeteran(veteran);
		
		// re-insert benefit recipients
		benefitRecipientReinsert(veteranBenefits);
						
		// clear note text when redisplay
		veteran.setNoteText("");

		List<Veteran> tmpVeterans = service.findVeteransBySsn(veteran.getSsn());
		
		for (Veteran v : tmpVeterans) {
			if (v.getVaCurrent().intValue() == 1) {	// get current veteran for proposed tab (see editCompare).
				veteran = new Veteran();
				BeanUtils.copyProperties(veteran, v);				
			} else if (v.getReviewed().intValue() == 0) {	// mark record reviewed
				Veteran newV = new Veteran();
				BeanUtils.copyProperties(newV, v);
				newV.setReviewed(new Integer(1));	
				service.saveVeteran(newV);									
			}			
		}
		
		// retrieve servicePeriodList and benefitTypeList for display in current tab
		veteran.setServicePeriodList(service.findVeteranServicePeriods(veteran.getId()));
		List<BenefitType> benefitTypes = new ArrayList<BenefitType>();
		for (VeteranBenefit veteranBenefit : veteranBenefits) {
			benefitTypes.add(veteranBenefit.getBenefitType());
		}
		veteran.setBenefitTypeList(benefitTypes);
		
		// decoration and medal
		Long[] decorationMedals = new Long[veteran.getDecorationMedalList().size()];
		int i = 0;
		for (DecorationMedal dm : veteran.getDecorationMedalList()) {
			decorationMedals[i] = dm.getId();
			i++;
		}
		veteran.setDecorationMedals(decorationMedals);
		
		// redmine 12083 #4 - benefit recipients
		for (BenefitType b : veteran.getBenefitTypeList()) {
			VeteranBenefit veteranBenefit = service.findVeteranBenefitType(veteran.getId(), b.getId());
			b.setBenefitRecipients(service.findVeteranBenefitRecipients(veteranBenefit.getId()));
		}
		
		if (isFromDashboard()) {	// Redmine 12095 comment #3 - edit/compare the next available
			String nextSsn = getNextEditCompare(veteran.getSsn());
			if ("".equals(nextSsn)) {
				return getText("LANDING_PAGE");		// return to landing page if no more records to edit/compare
			} else {
				doEditCompare(nextSsn);
				veteran.setFromDashboard(true);
				return getText("EDIT_COMPARE");
			}
		} else {
			addActionMessage(getText("message.save", Util.getStringArray(getText("veteran"))));
			return getText("EDIT_COMPARE");
		}
	}
		
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
				
		Long id = new Long(request.getParameter("id"));
		veteran = service.findVeteranById(id);
		
		// put note list on ses so they can be retrieved when saved in edit page.
		request.getSession().setAttribute(getText("TEMP_LIST_SES"), veteran.getNoteList());
		
		// populate values
		populateValues();
		
		return SUCCESS;
	}
	
	public String view() throws Exception {
		
		logger.debug("entering view ...");
		
		Long id = new Long(request.getParameter("id"));
		veteran = service.findVeteranById(id);
		
		// populate values
		populateValues();
		
		return getText("VIEW_VETERAN");
	}
	
	public String editCompare() throws Exception {
		// get ssn from id for security issue - no ssn on url (Get request).
		veteran = service.findVeteranById(Long.parseLong(request.getParameter("id")));
		doEditCompare(veteran.getSsn());
		
		return getText("EDIT_COMPARE");
	}
	
	public String editCompareSsn() throws Exception {
		String ssn = request.getParameter("ssn");
		doEditCompare(ssn);
		
		return getText("EDIT_COMPARE");
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		veteran = service.findVeteranById(id);
		
		// 1a. get note id list first before deleting veteran.
		List<Note> notesTemp = veteran.getNoteList();
		List<Note> notes = new ArrayList<Note>();
		for (Note note : notesTemp) {
			Note n = new Note();
			n.setId(note.getId());
			notes.add(n);
		}
		
		// 1.b delete all veteranServicePeriod records (OneToMany) belong to this veteran
		for (VeteranServicePeriod veteranServicePeriod : veteran.getServicePeriodList()) {
			service.deleteVeteranServicePeriod(veteranServicePeriod);
		}
		veteran.setServicePeriodList(null);
		
		// 1.c delete all VeteranBenefit and BenefitRecipient records belong to this veteran (ManyToMany).
		List<VeteranBenefit> veteranBenefits = service.findVeteranBenefitTypes(veteran.getId());
		for (VeteranBenefit veteranBenefit : veteranBenefits) {
			service.deleteVeteranBenefitRecipientsByVeteranBenefitId(veteranBenefit.getId());
		}
		service.deleteVeteranBenefitByVeteranId(veteran.getId());
		
		// 1.d delete all external attachment files if needed
		List<VeteranAttachment> attachedFiles = veteran.getAttachmentsList();
		if (attachedFiles != null) {
			for (VeteranAttachment attachedFile : attachedFiles) {
				File imageFile = new File(getText("external.rootPath") + attachedFile.getAttachmentPointer());
				imageFile.delete();
			}
		}
		
		// 2. delete veteran
		service.deleteVeteran(veteran);		
		
		// 3. delete all notes (note tables) belong to this veteran.
		for (Note note : notes) {
			service.deleteNote(note);
		}			
				
		addActionMessage(getText("message.delete", Util.getStringArray(getText("veteran"))));
		return getText("SEARCH_RESULT");
	}
	
	/**
	 * Called from copyAll in Edit/Compare page. Copies everything from a veteran to current veteran.
	 * @return
	 * @throws Exception
	 */
	public String copyData() throws Exception {
		
		logger.debug("entering copyData()...");
		
		Long currentVeteranId = Long.parseLong(request.getParameter("currentVeteranId"));
		Long origVeteranId = Long.parseLong(request.getParameter("origVeteranId"));
		
		// copy everything from orig veteran to current veteran
		doCopy(currentVeteranId, origVeteranId);
		
		addActionMessage(getText("message.copy"));
		return getText("EDIT_COMPARE");
	}
	
	public String copyNote() throws Exception {
		
		logger.debug("entering copyNoteText ...");

		Long currentVeteranId = Long.parseLong(request.getParameter("currentVeteranId"));
		Long noteId = Long.parseLong(request.getParameter("noteId"));
		
		// copy note from origVeteran to currentVeteran
		Note oldNote = service.findNoteById(noteId);
		Note newNote = new Note();
		BeanUtils.copyProperties(newNote, oldNote);
		newNote.setId(null);
		newNote.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		newNote.setInsertTimestamp(new Date());
		newNote.setUpdateTimestamp(new Date());
		service.saveNote(newNote);
		
		Veteran v = new Veteran();
		v.setId(currentVeteranId);
		
		VeteranNote veteranNote = new VeteranNote();
		veteranNote.setVeteran(v);
		veteranNote.setNote(newNote);
		veteranNote.setActive(new Integer(1));
		veteranNote.setInsertTimestamp(new Date());
		veteranNote.setUpdateTimestamp(new Date());
		
		service.saveVeteranNote(veteranNote);
		
		// run editCompare method 
		String ssn = request.getParameter("ssn");
		doEditCompare(ssn);
		
		addActionMessage(getText("message.copyNote"));
		return getText("EDIT_COMPARE");
	}
	
	/**
	 * There 2 cases for DD214 form:
	 * 		1) New entry screen: has 2 cases:
	 * 			1a)	SSN does not exist: Create current and non-current records with image and OCR text file in current record. 
	 * 				Populate OCR text to current record.
	 * 			1b) SSN already exists: Create unreviewed non-current record which will be displayed on dashboard for review. 
	 * 				Populate OCR text to the created unreviewed non-current record. Associate image and OCR text file to the existing 
	 * 				current record. 
	 * 		2) Edit screen (on current record only, Add Document button will be hidden in non-current record): Populate OCR text to 
	 * 		   overwrite current record data. Associate image and OCR text file to current record.
	 * 
	 * Display current record on exit.
	 * 
	 * @return string
	 * @throws Exception
	 */
	public String saveAttachments() throws Exception {
		
		logger.debug("Entering saveAttachments ...");

		Long newVeteranId = null;
		RecordType dd214Record = new RecordType();
		dd214Record.setId(new Long(getText("SOURCE_DD_214")));

		// 1. upload OCR text file (attachmentTxt) and populate OCR text if it's DD214 (contains 'Form 214' string) doc type
		if (dd214.contains(veteranAttachment.getDocType().getId().toString())) {
			
			// make sure it's text content
			if (!attachmentTxtContentType.contains("text")) {
				throw new Exception("Content type of text file is " + attachmentContentType + ". It must be text file.");
			}
			
			// 1a. save attachmentTxt with veteran id
			VeteranAttachment ocrAttachment = new VeteranAttachment();
			BeanUtils.copyProperties(ocrAttachment, veteranAttachment);
			ocrAttachment.setId(null);
			
			FileInputStream fisOcr = null;
			try {
				byte[] baOcr = new byte[(int)attachmentTxt.length()];
				fisOcr = new FileInputStream(attachmentTxt);
				fisOcr.read(baOcr);
				String filePath = saveToExternal(baOcr, attachmentTxtFileName.substring(attachmentTxtFileName.indexOf('.')));
				if (filePath == null) {
					throw new Exception("Error: Failed to save OCR Text attachment to the external drive.");
				}
				
				ocrAttachment.setAttachmentPointer(filePath);
			}  finally {
				fisOcr.close();
			}
			
			ocrAttachment.setAttachmentFileName(attachmentTxtFileName);
			ocrAttachment.setAttachmentContentType(attachmentTxtContentType);
			
			ocrAttachment.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			ocrAttachment.setInsertTimestamp(new Date());
			ocrAttachment.setUpdateTimestamp(new Date());
			
			service.saveVeteranAttachment(ocrAttachment);
			
			// 1b. save veteran with OCR data
			veteran = service.findVeteranById(veteranAttachment.getVeteran().getId());
			veteran.setRecordType(dd214Record);
			veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
			veteran.setUpdateTimestamp(new Date());

			List<String> rows = FileUtils.readLines(attachmentTxt);
			int i = 0;
			VeteranServicePeriod servicePeriod = new VeteranServicePeriod();
			while (i < rows.size()) {
				String row = rows.get(i);
				row = row.toUpperCase();
				i = i + 1;
				try {
					if (row.contains("MIDDLE")) {	// get and set last first middle name
						String[] names = null;
						String fullName = row.substring(row.indexOf("MIDDLE)") + 7);
						if (fullName != null && Util.removeSpaces(fullName).length() > 3) {	// full name is on same line
							names = fullName.trim().split(" ");
						} else {	// full name is on next line
							fullName = rows.get(i);
							names = fullName.trim().split(" ");
							i = i + 1;
						}
						veteran.setLastName(names[0]);
						veteran.setFirstName(names[1]);
						veteran.setMiddleName(names[2]);
						
						continue;	// skip the rest
					}					
				} catch (Exception e) {}	// ignore wacky situation
				
				try {
					if (row.contains("BRANCH")) {	// get and set service branch
						Long branchId = null;
						String branchName = row.substring(row.indexOf("BRANCH") + 6);
						if (branchName != null && Util.removeSpaces(branchName).length() > 3) {	// branch name is on same line
							branchId = getBranchId(Util.removeSpaces(branchName));
						} else {	// branch name is on next line
							branchId = getBranchId(Util.removeSpaces(rows.get(i)));
							i = i + 1;
						}
						
						if (branchId != null) {
							ServiceBranch sb = new ServiceBranch();
							sb.setId(branchId);
							servicePeriod.setServiceBranch(sb);
							servicePeriod.setActive(new Integer(1));
							servicePeriod.setInsertTimestamp(new Date());
							servicePeriod.setUpdateTimestamp(new Date());							
						}
						
						continue;	// skip the rest
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("NUMBER")) {	// get and set ssn
						String ssn = row.substring(row.indexOf("NUMBER") + 5);
						if (ssn != null && Util.removeSpaces(ssn).length() > 3) {	// ssn value is on same line
							veteran.setSsn(Util.formatSsn(Util.removeNonNumeric(ssn)));
						} else {	// ssn values is on next line
							veteran.setSsn(Util.formatSsn(Util.removeNonNumeric(rows.get(i))));
							i = i + 1;
						}
						
						continue;
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("MMDD)")) {	// get and set dob
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						String dob = row.substring(row.indexOf("MMDD)") + 5);
						if (dob != null && Util.removeSpaces(dob).length() > 3) {
							veteran.setDateOfBirth(sdf.parse(Util.removeSpaces(dob)));
						} else {
							veteran.setDateOfBirth(sdf.parse(Util.removeSpaces(rows.get(i))));
							i = i + 1;
						}

						continue;
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("DECORATIONS")) {		// get and set purple heart decoration
						String decorations = rows.get(i).toUpperCase();	// it's in the next line
						if (decorations.contains("PURPLE HEART")) {
							DecorationMedal dm = new DecorationMedal();
							dm.setId(getPurpleHeartId());
							List<DecorationMedal> dms = new ArrayList<DecorationMedal>();
							dms.add(dm);
							
							veteran.setDecorationMedalList(dms);
							
							i = i + 1;
						}
						
						continue;
					}					
				} catch (Exception e) {}
				
				// get and set physical address assuming standard address: 660 E EAGLE RIDGE DR, N SALT LAKE, UT 84054
				// changed to 660 E Eagle Ridge Dr <new line> N Salt Lake <new line> UT 84054 (10/12/2012)
				try {
					if (row.contains("MAILING")) {
						String streetAddr = (row.substring(row.indexOf("CODE)") + 5)).trim();
						String city = (rows.get(i)).trim();
						String[] stateZip = (rows.get(i + 1)).trim().split(" "); 
						veteran.setAddress1(streetAddr);
						veteran.setCity(city);
						veteran.setState((stateZip.length == 2) ? twoCharsState(stateZip[0]) : twoCharsState(stateZip[0] + " " + stateZip[1]));
						veteran.setZip((stateZip.length == 2) ? stateZip[1] : stateZip[2]);

						i = i + 2;						
						continue;
					}
				} catch (Exception e) {}
				
				// get and set nearest relative and address assuming: full name, street address, city, state zip
				try {
					if (row.contains("RELATIVE")) {
						String[] contactName = (row.substring(row.indexOf("CODE)") + 5)).trim().split(" ");
						String streetAddr = (rows.get(i)).trim();
						String city = (rows.get(i + 1)).trim();
						String[] stateZip = (rows.get(i + 2)).trim().split(" "); 
						veteran.setAltContactFirstName(contactName[0]);
						veteran.setAltContactLastName(contactName[1]);
						veteran.setAltContactAddr1(streetAddr);
						veteran.setAltContactCity(city);
						veteran.setAltContactState((stateZip.length == 2) ? twoCharsState(stateZip[0]) : twoCharsState(stateZip[0] + " " + stateZip[1]));
						veteran.setAltContactZip((stateZip.length == 2) ? stateZip[1] : stateZip[2]);
						
						i = i + 3;
					}
				} catch (Exception e) {}
				
			}
			
			// 1c. check if it's new entry or edit screen
			if (getText("veteran.new").equals(veteranAttachment.getPageTitle())) {		// New Entry
				if (service.findVeteransBySsn(veteran.getSsn()).size() == 1) {	// ssn does not exist, change it current, save, and create non-current record
					veteran.setVaCurrent(new Integer(1));
					veteran.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
					veteran.setInsertTimestamp(new Date());
					veteran.setVerified(new Integer(1)); 	// make it verified
					veteran.setReviewed(new Integer(1));	// make it reviewed
					service.saveVeteran(veteran);
					
					Veteran nonCurrentVet = new Veteran();
					BeanUtils.copyProperties(nonCurrentVet, veteran);
					nonCurrentVet.setId(null);
					nonCurrentVet.setVaCurrent(new Integer(0));
					nonCurrentVet.setAttachmentsList(null);
					nonCurrentVet.setBenefitTypeList(null);
					nonCurrentVet.setDecorationMedalList(null);
					nonCurrentVet.setNoteList(null);
					nonCurrentVet.setServicePeriodList(null);
					service.saveVeteran(nonCurrentVet);
				} else {	// ** Mostly the case -- ssn already exists, save record as new non-current, unreviewed (so it will be displayed in Dashboard) and update existed current record. 
					veteran.setVaCurrent(new Integer(0)); 	// set it non current record
					veteran.setReviewed(new Integer(0)); 	// set unreviewed
					veteran.setRecordType(dd214Record);
					veteran.setVerified(new Integer(1));
					service.saveVeteran(veteran);
					
					Veteran currExistVet = getCurrentVeteran(veteran.getSsn());
					currExistVet.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
					currExistVet.setUpdateTimestamp(new Date());
					service.saveVeteran(currExistVet);		
					newVeteranId = currExistVet.getId();	// so it will be used below to save image and re-display it on exit
					
					// save attachmentTxt to current record
					ocrAttachment.setVeteran(currExistVet);
					service.saveVeteranAttachment(ocrAttachment);
				}
				
			} else {	// edit screen (on current record only) - just save it
				veteran.setVerified(new Integer(1));
				veteran.setReviewed(new Integer(1));
				service.saveVeteran(veteran);				
			}
			
			if (servicePeriod.getServiceBranch() != null) {
				servicePeriod.setVeteran(veteran);
				service.saveVeteranServicePeriod(servicePeriod);				
			}
		} else {	// make sure form was saved first before attaching file.
			Veteran tmp = service.findVeteranById(veteranAttachment.getVeteran().getId());
			if ("000-00-0000".equals(tmp.getSsn())) {
				throw new Exception("Please save the form first before attaching this type of document.");
			}			
		}
		
		// 2. upload image or any other type of document (attachment): pdf, word, excel, etc.	
		FileInputStream fisImage = null;
		try {
			byte[] baImage = new byte[(int)attachment.length()];
			fisImage = new FileInputStream(attachment);
			fisImage.read(baImage);
			String filePath = saveToExternal(baImage, attachmentFileName.substring(attachmentFileName.indexOf('.')));
			
			if (filePath == null) {
				throw new Exception("Error: Failed to save attachment to the external drive.");
			}
			 
			veteranAttachment.setAttachmentPointer(filePath);
		} finally {
			fisImage.close();
		}
		
		veteranAttachment.setAttachmentFileName(attachmentFileName);
		veteranAttachment.setAttachmentContentType(attachmentContentType);
				
		veteranAttachment.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteranAttachment.setInsertTimestamp(new Date());
		veteranAttachment.setUpdateTimestamp(new Date());
		
		if (newVeteranId != null) {
			Veteran newVeteran = new Veteran();
			newVeteran.setId(newVeteranId);
			veteranAttachment.setVeteran(newVeteran);
		}
		
		service.saveVeteranAttachment(veteranAttachment);
		
		// 2a. if it's dd214 image only and no OCR text, update record to verified, and reviewed
		if (isDocTypeNameContains214(veteranAttachment.getDocType().getId()) && !dd214.contains(veteranAttachment.getDocType().getId().toString())) {
			veteran = service.findVeteranById(veteranAttachment.getVeteran().getId());
			veteran.setVerified(new Integer(1));
			veteran.setReviewed(new Integer(1));
			
			service.saveVeteran(veteran);
		}
				
		// 3. re-display veteran
		if (newVeteranId != null) {
			veteranId = newVeteranId.toString();
		} else {
			veteranId = veteranAttachment.getVeteran().getId().toString();
		}
		
		return SUCCESS;
	}
	
	/**
	 * Shows attachment image from external drive.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showAttachment() throws Exception {
		logger.debug("Entering showAttachment ...");
		ServletOutputStream sos = null;
		try {
			Long id = new Long(request.getParameter("id"));
			veteranAttachment = service.findVeteranAttachmentById(id); 
			File imageFile = new File(getText("external.rootPath") + veteranAttachment.getAttachmentPointer());
			byte[] imageBytes = convertFileToBytes(imageFile);
			
			sos = response.getOutputStream();
			response.setContentLength(imageBytes.length);
			response.setContentType(veteranAttachment.getAttachmentContentType());
            response.setHeader("Content-disposition", "attachment; filename=" + veteranAttachment.getAttachmentFileName());
			
			sos.write(imageBytes);		
			return null;
		} finally {
			sos.flush();
			sos.close();
		}
	}
	
	public String deleteAttachment() throws Exception {
		
		logger.debug("Entering deleteAttachment ...");
		
		Long id = new Long(request.getParameter("id"));
		veteranId =  request.getParameter("veteranId");
		veteranAttachment = service.findVeteranAttachmentById(id); 
		
		// delete attachment image from external drive
		File imageFile = new File(getText("external.rootPath") + veteranAttachment.getAttachmentPointer());
		imageFile.delete();
		
		// delete veteran attachment record in database
		service.deteteVeteranAttachment(veteranAttachment);
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		
		// validate form when Save button is clicked in veteran.jsp but not in editCompare.jsp which uses javascript validation.
		if (request.getParameter("save") != null) {	
			if (Validate.isEmpty(veteran.getSsn1()) || Validate.isEmpty(veteran.getSsn2()) || Validate.isEmpty(veteran.getSsn3())) {
				addActionError(getText("error.required", Util.getStringArray(getText("veteran.ssn"))));
			} else {
				if (veteran.getSsn1().equalsIgnoreCase("WW2")) {	// redmine 12397
					veteran.setSsn1("WW2");
					if (!Validate.isSSNValid("000" + veteran.getSsn2() + veteran.getSsn3()))
						addActionError(getText("error.notValidSsn", Util.getStringArray(getText("veteran.ssn"))));
				} else {
					if (!Validate.isSSNValid(veteran.getSsn1() + veteran.getSsn2() + veteran.getSsn3()))
						addActionError(getText("error.notValidSsn", Util.getStringArray(getText("veteran.ssn"))));					
				}
			}
			
			if (veteran.getMonth() == null && veteran.getDay() == null && veteran.getYear() == null) {
				// pass
			} else {
				if (veteran.getMonth() == null || veteran.getDay() == null || veteran.getYear() == null) {
					addActionError(getText("error.notValidDate", Util.getStringArray(getText("veteran.dob"))));
				} else {
					try {
						veteran.setDateOfBirth(Util.convertToDate(veteran.getMonth().toString(), veteran.getDay().toString(), veteran.getYear().toString()));
					} catch (Exception e) {}
				}		
			}
			
			if (Validate.isEmpty(veteran.getEmail())) {
				// pass
			} else {
				if (!Validate.isEmailValid(veteran.getEmail())) addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.email"))));
			}
			
			if (veteran.getMonthD() == null && veteran.getDayD() == null && veteran.getYearD() == null) {
				// pass
			} else {
				if (veteran.getMonthD() == null || veteran.getDayD() == null || veteran.getYearD() == null) {
					addActionError(getText("error.notValidDate", Util.getStringArray(getText("veteran.dod"))));
				} else {
					try {
						veteran.setDateOfDeath(Util.convertToDate(veteran.getMonthD().toString(), veteran.getDayD().toString(), veteran.getYearD().toString()));
					} catch (Exception e) {}
				}		
			}
			
			if (veteran.getDateOfBirth() != null && veteran.getDateOfDeath() != null) {
				if (Validate.compareDate(veteran.getDateOfDeath(), veteran.getDateOfBirth()) == -1) 
					addActionError(getText("error.dodBeforeDob"));
			}
			
			if (!Validate.isPhoneNumberValid(veteran.getPrimaryPhone1() + veteran.getPrimaryPhone2() + veteran.getPrimaryPhone3()))
				addActionError(getText("error.notValidPhone", Util.getStringArray(getText("veteran.primaryPhone"))));

			if (!Validate.isPhoneNumberValid(veteran.getAltPhone1() + veteran.getAltPhone2() + veteran.getAltPhone3()))
				addActionError(getText("error.notValidPhone", Util.getStringArray(getText("veteran.altPhone"))));

			if (!Validate.isPhoneNumberValid(veteran.getAltContactPhone1() + veteran.getAltContactPhone2() + veteran.getAltContactPhone3()))
				addActionError(getText("error.notValidPhone", Util.getStringArray(getText("veteran.altContactPhone"))));

			if (!Validate.isEmpty(veteran.getZip()) && !Validate.isZipValid(veteran.getZip()))
				addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.streetZip"))));
			
			if (!Validate.isEmpty(veteran.getMailingZip()) && !Validate.isZipValid(veteran.getMailingZip()))
				addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.mailingZip"))));

		}
		
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public Veteran getVeteran() {
		return veteran;
	}
	
	public VeteranAttachment getVeteranAttachment() {
		return veteranAttachment;
	}

	public void setVeteranAttachment(VeteranAttachment veteranAttachment) {
		this.veteranAttachment = veteranAttachment;
	}

	public List<Veteran> getVeterans() {
		return veterans;
	}

	public void setVeterans(List<Veteran> veterans) {
		this.veterans = veterans;
	}
	
	public boolean isFromDashboard() {
		return fromDashboard;
	}

	public void setFromDashboard(boolean fromDashboard) {
		this.fromDashboard = fromDashboard;
	}
	   
	public File getAttachment() {
		return attachment;
	}

	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}
	
	public String getAttachmentContentType() {
		return attachmentContentType;
	}

	public void setAttachmentContentType(String attachmentContentType) {
		this.attachmentContentType = attachmentContentType;
	}
	
	public File getAttachmentTxt() {
		return attachmentTxt;
	}

	public void setAttachmentTxt(File attachmentTxt) {
		this.attachmentTxt = attachmentTxt;
	}

	public String getAttachmentTxtFileName() {
		return attachmentTxtFileName;
	}

	public void setAttachmentTxtFileName(String attachmentTxtFileName) {
		this.attachmentTxtFileName = attachmentTxtFileName;
	}

	public String getAttachmentTxtContentType() {
		return attachmentTxtContentType;
	}

	public void setAttachmentTxtContentType(String attachmentTxtContentType) {
		this.attachmentTxtContentType = attachmentTxtContentType;
	}

	public String getVeteranId() {
		return veteranId;
	}

	public void setVeteranId(String veteranId) {
		this.veteranId = veteranId;
	}

	public String getDd214() {

		if ("".equals(dd214)) {
			for (NameValue nv : getDocTypeList()) {
				if (nv.getValue().contains(getText("DD214"))) {
				//if (getText("DD214").equals(nv.getValue())) {
					dd214 = dd214 + nv.getId().toString() + ",";
				}
			}			
		}
		
		return dd214;
	}

	public void setDd214(String dd214) {
		this.dd214 = dd214;
	}
	
	@SuppressWarnings("unchecked")
	private void gatherValues() throws Exception {
    	
		// redmine 15899
		veteran.setFirstName(Util.toUpperCaseNull(veteran.getFirstName()));
		veteran.setLastName(Util.toUpperCaseNull(veteran.getLastName()));
		veteran.setMiddleName(Util.toUpperCaseNull(veteran.getMiddleName()));
		
		veteran.setSsn(veteran.getSsn1()+ "-" + veteran.getSsn2() + "-" + veteran.getSsn3());
		try {
			veteran.setDateOfBirth(Util.convertToDate(veteran.getMonth().toString(), veteran.getDay().toString(), veteran.getYear().toString()));
		} catch(NullPointerException e) {
			veteran.setDateOfBirth(null);
		}
		
		if (veteran.getEthnicity().getId() == null) veteran.setEthnicity(null);
		
		veteran.setPrimaryPhone(veteran.getPrimaryPhone1() + "-" + veteran.getPrimaryPhone2() + "-" + veteran.getPrimaryPhone3());
		if (veteran.getPrimaryPhone().length() != 12) veteran.setPrimaryPhone(null);
		
		veteran.setAltPhone(veteran.getAltPhone1() + "-" + veteran.getAltPhone2() + "-" + veteran.getAltPhone3());
		if (veteran.getAltPhone().length() != 12) veteran.setAltPhone(null);
		
		veteran.setAltContactPhone(veteran.getAltContactPhone1() + "-" + veteran.getAltContactPhone2() + "-" + veteran.getAltContactPhone3());
		if (veteran.getAltContactPhone().length() != 12) veteran.setAltContactPhone(null);
		
		if (veteran.getRelation().getId() == null) veteran.setRelation(null);
		
		try {
			veteran.setDateOfDeath(Util.convertToDate(veteran.getMonthD().toString(), veteran.getDayD().toString(), veteran.getYearD().toString()));
		} catch(NullPointerException e) {
			veteran.setDateOfDeath(null);
		}

		// noteList @ManyToMany
		if (veteran.getNoteText() != null && veteran.getNoteText().length() > 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Note note = new Note();
			note.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText(sdf.format(note.getInsertTimestamp()) + " -- Created by " + note.getCreatedBy().getFirstName() +
					" " + note.getCreatedBy().getLastName() + "<br>" + veteran.getNoteText() + "<br>----------");
			service.saveNote(note);
			
			List<Note> noteList;
			if (getText("veteran.new").equals(veteran.getVetPageTitle())) {		// New Entry
				noteList = new ArrayList<Note>();
				noteList.add(note);
				veteran.setNoteList(noteList);				
			} else {	// Edit Veteran
				noteList = (List<Note>)request.getSession().getAttribute(getText("TEMP_LIST_SES"));
				noteList.add(note);
				veteran.setNoteList(noteList);
			}
		} else {
			if (!getText("veteran.new").equals(veteran.getVetPageTitle())) { // Edit Veteran
				veteran.setNoteList((List<Note>)request.getSession().getAttribute(getText("TEMP_LIST_SES")));
			}
		}
		
		// decoration and medal
		List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
		for (Long decorationMedalLong : veteran.getDecorationMedals()) {
			DecorationMedal dm = new DecorationMedal();
			dm.setId(decorationMedalLong);
			decorationMedals.add(dm);
		}
		veteran.setDecorationMedalList(decorationMedals);
		
		// mark record reviewed.
		veteran.setReviewed(new Integer(1));
				
		veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setUpdateTimestamp(new Date());
				
    }
    
	/*
	 * Populates veteran related data
	 */
	private void populateValues() throws Exception {
    	
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		veteran.setVetPageTitle(getText("veteran.edit"));
		
		populateVeteranSsn();

		if (veteran.getDateOfBirth() != null) {
			String[] dob = sdf.format(veteran.getDateOfBirth()).split("/");
			veteran.setMonth(new Integer(dob[0]));
			veteran.setDay(new Integer(dob[1]));
			veteran.setYear(new Integer(dob[2]));			
		}
		if (veteran.getDateOfDeath() != null) {
			String[] dod =sdf.format(veteran.getDateOfDeath()).split("/");
			veteran.setMonthD(new Integer(dod[0]));
			veteran.setDayD(new Integer(dod[1]));
			veteran.setYearD(new Integer(dod[2]));
		}
		if (veteran.getPrimaryPhone() != null) {
			String[] primaryPhone = veteran.getPrimaryPhone().split("-");
			veteran.setPrimaryPhone1(primaryPhone[0]);
			veteran.setPrimaryPhone2(primaryPhone[1]);
			veteran.setPrimaryPhone3(primaryPhone[2]);
		}
		if (veteran.getAltPhone() != null) {
			String[] altPhone = veteran.getAltPhone().split("-");
			veteran.setAltPhone1(altPhone[0]);
			veteran.setAltPhone2(altPhone[1]);
			veteran.setAltPhone3(altPhone[2]);
		}
		if (veteran.getAltContactPhone() != null) {
			String[] altContactPhone = veteran.getAltContactPhone().split("-");
			veteran.setAltContactPhone1(altContactPhone[0]);
			veteran.setAltContactPhone2(altContactPhone[1]);
			veteran.setAltContactPhone3(altContactPhone[2]);
		}
		if (veteran.getMailingAddr1() != null || veteran.getMailingAddr2() != null) {
			veteran.setMailingAddr("y");
		} else {
			veteran.setMailingAddr("n");
		}
		
		// decoration and medal
		Long[] decorationMedals = new Long[veteran.getDecorationMedalList().size()];
		int i = 0;
		for (DecorationMedal dm : veteran.getDecorationMedalList()) {
			decorationMedals[i] = dm.getId();
			i++;
		}
		veteran.setDecorationMedals(decorationMedals);
		
    }
	
	/*
	 * Populates the 3 transient pieces of the veteran SSN
	 */
	private void populateVeteranSsn() {

		if (veteran.getSsn() != null && veteran.getSsn().contains("-")) {
			String[] ssn = veteran.getSsn().split("-");
			veteran.setSsn1(ssn[0]);
			veteran.setSsn2(ssn[1]);
			veteran.setSsn3(ssn[2]);
		} else if (veteran.getSsn() != null && veteran.getSsn().length() == SSN_LENGTH_NO_DASHES) {
			veteran.setSsn1(veteran.getSsn().substring(0, 3));
			veteran.setSsn2(veteran.getSsn().substring(3, 5));
			veteran.setSsn3(veteran.getSsn().substring(5));
		}
	}

	@SuppressWarnings("unchecked")
	private void gatherValues_editCompare() throws Exception {
    	
		// redmine 15899
		veteran.setFirstName(Util.toUpperCaseNull(veteran.getFirstName()));
		veteran.setLastName(Util.toUpperCaseNull(veteran.getLastName()));
		veteran.setMiddleName(Util.toUpperCaseNull(veteran.getMiddleName()));
		
		if (veteran.getNoteText() != null && veteran.getNoteText().length() > 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Note note = new Note();
			note.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText(sdf.format(note.getInsertTimestamp()) + " -- Created by " + note.getCreatedBy().getFirstName() +
					" " + note.getCreatedBy().getLastName() + "<br>" + veteran.getNoteText() + "<br>----------");
			service.saveNote(note);
			
			List<Note> noteList = (List<Note>)request.getSession().getAttribute(getText("TEMP_LIST_SES"));
			noteList.add(note);
			veteran.setNoteList(noteList);
		} else {
			veteran.setNoteList((List<Note>)request.getSession().getAttribute(getText("TEMP_LIST_SES")));
		}

		if (veteran.getRelation().getId() == null) veteran.setRelation(null);
		
		// decoration and medal
		List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
		for (Long decorationMedalLong : veteran.getDecorationMedals()) {
			DecorationMedal dm = new DecorationMedal();
			dm.setId(decorationMedalLong);
			decorationMedals.add(dm);
		}
		veteran.setDecorationMedalList(decorationMedals);
		
		// mark record reviewed.
		veteran.setReviewed(new Integer(1));
		
		if (veteran.getEthnicity().getId() == null) veteran.setEthnicity(null);
		veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setUpdateTimestamp(new Date());
    }
	
	private List<VeteranBenefit> benefitRecipientOrphan() {
		
		List<VeteranBenefit> veteranBenefits = service.findVeteranBenefitTypes(veteran.getId());
		for (VeteranBenefit veteranBenefit : veteranBenefits) {
			veteranBenefit.setBenefitRecipientList(service.findVeteranBenefitRecipients(veteranBenefit.getId()));
			service.deleteVeteranBenefitRecipientsByVeteranBenefitId(veteranBenefit.getId());
		}
		
		return veteranBenefits;
	}
	
	private void benefitRecipientReinsert(List<VeteranBenefit> veteranBenefits) throws Exception {
		
		for (VeteranBenefit veteranBenefit : veteranBenefits) {
			VeteranBenefit newVeteranBenefit = new VeteranBenefit();
			BeanUtils.copyProperties(newVeteranBenefit, veteranBenefit);
			newVeteranBenefit.setId(null);
			service.saveVeteranBenefitType(newVeteranBenefit);
			for (BenefitRecipient benefitRecipient : veteranBenefit.getBenefitRecipientList()) {
				BenefitRecipient newBenefitRecipient = new BenefitRecipient();
				BeanUtils.copyProperties(newBenefitRecipient, benefitRecipient);
				newBenefitRecipient.setId(null);
				newBenefitRecipient.setVeteranBenefit(newVeteranBenefit);
				service.saveVeteranBenefitRecipient(newBenefitRecipient);
			}
		}
	}
	
	/**
	 * Copies everthing from original veteran to current veteran. Called from Edit/Compare (copyData()) and New Entry pages.
	 * 
	 * @param currentVeteranId
	 * @param origVeteranId
	 * @throws Exception
	 */
	private void doCopy(Long currentVeteranId, Long origVeteranId) throws Exception {
		
		// get old notes and vsp records in current veteran which will be deleted later, otherwise they're orphan.
		Veteran currentVeteran = service.findVeteranById(currentVeteranId);
		
		/* see *** old notes *** below
		List<Note> orphanNotes = new ArrayList<Note>();
		if (currentVeteran.getNoteList() != null) {
			List<Note> notesTemp = currentVeteran.getNoteList();
			for (Note note : notesTemp) {
				Note n = new Note();
				n.setId(note.getId());
				orphanNotes.add(n);
			}			
		}*/
		
		List<VeteranServicePeriod> orphanVsp = new ArrayList<VeteranServicePeriod>();
		if (currentVeteran.getServicePeriodList() != null) {
			List<VeteranServicePeriod> vspTemps = currentVeteran.getServicePeriodList();
			for (VeteranServicePeriod vspTemp : vspTemps) {
				VeteranServicePeriod vsp = new VeteranServicePeriod();
				vsp.setId(vspTemp.getId());
				orphanVsp.add(vsp);
			}			
		}
		
		// begin copy process:
		Veteran origVeteran = service.findVeteranById(origVeteranId);
		origVeteran.setServicePeriodList(service.findVeteranServicePeriods(origVeteranId));	// needed for save method (new entry); work fine for copyData (copyAll in edit/compare).
		
		veteran = new Veteran();
		BeanUtils.copyProperties(veteran, origVeteran);
		
		veteran.setId(currentVeteranId);
		veteran.setVaCurrent(new Integer(1));
		
		// delete old service periods before attaching new service periods (OneToMany: will merge (add) service periods.)
		service.deleteVeteranServicePeriodsByVeteranId(veteran.getId());
		List<VeteranServicePeriod> displayServicePeriod = new ArrayList<VeteranServicePeriod>();
		if (veteran.getServicePeriodList() != null) {
			for (VeteranServicePeriod veteranServicePeriod : veteran.getServicePeriodList()) {
				VeteranServicePeriod newVsp = new VeteranServicePeriod();
				BeanUtils.copyProperties(newVsp, veteranServicePeriod);
				newVsp.setId(null);
				newVsp.setVeteran(veteran);

				service.saveVeteranServicePeriod(newVsp);
			}
			displayServicePeriod = veteran.getServicePeriodList(); // save for later jsp display use
			veteran.setServicePeriodList(null);			
		}
		
		// delete old notes - don't need
		
		// delete old (current) benefit types and recipients
		List<VeteranBenefit> currentVeteranBenefits = service.findVeteranBenefitTypes(currentVeteranId);
		for (VeteranBenefit currentVeteranBenefit : currentVeteranBenefits) {
			service.deleteVeteranBenefitRecipientsByVeteranBenefitId(currentVeteranBenefit.getId());
		}
		service.deleteVeteranBenefitByVeteranId(currentVeteranId);
		veteran.setBenefitTypeList(null);
		
		// insert orig notes
		if (veteran.getNoteList() != null) {
			List<Note> newNotes = new ArrayList<Note>();
			for (Note oldNote : veteran.getNoteList()) {
				Note newNote = new Note();
				BeanUtils.copyProperties(newNote, oldNote);
				newNote.setId(null);
				service.saveNote(newNote);
				newNotes.add(newNote);
			}
			veteran.setNoteList(newNotes);			
		}
		
		service.saveVeteran(veteran);
		
		// insert orig benefit types and recipients
		List<VeteranBenefit> origVeteranBenefits = service.findVeteranBenefitTypes(origVeteranId); 
		for (VeteranBenefit origVeteranBenefit : origVeteranBenefits) {
			VeteranBenefit newVeteranBenefit = new VeteranBenefit();
			BeanUtils.copyProperties(newVeteranBenefit, origVeteranBenefit);
			newVeteranBenefit.setId(null);
			newVeteranBenefit.setVeteran(veteran);
			service.saveVeteranBenefitType(newVeteranBenefit);
			List<BenefitRecipient> origBenefitRecipients = service.findVeteranBenefitRecipients(origVeteranBenefit.getId());
			for (BenefitRecipient origBenefitRecipient : origBenefitRecipients) {
				BenefitRecipient newBenefitRecipient = new BenefitRecipient();
				BeanUtils.copyProperties(newBenefitRecipient, origBenefitRecipient);
				newBenefitRecipient.setId(null);
				newBenefitRecipient.setVeteranBenefit(newVeteranBenefit);
				service.saveVeteranBenefitRecipient(newBenefitRecipient);
			}
		}
				
		veterans = service.findVeteransBySsn(origVeteran.getSsn());
		for (Veteran v : veterans) {
			if (v.getVaCurrent().intValue() == 1) {				
				// delete current record from list, it'll be populated using proposed record in jsp
				veterans.remove(v);
				break;
			}
		}
				
		veteran.setServicePeriodList(displayServicePeriod); // for display
		request.getSession().setAttribute(getText("TEMP_LIST_SES"), veteran.getNoteList());
		
		List<BenefitType> benefitTypes = new ArrayList<BenefitType>();
		for (VeteranBenefit veteranBenefit : origVeteranBenefits) {
			benefitTypes.add(veteranBenefit.getBenefitType());
		}
		veteran.setBenefitTypeList(benefitTypes);	
		
		// end of copy process.
		
		// delete orphan old notes and vsp_dm records
		/* *** Old Notes *** No need to delete orphanNotes in New Entry screen.
		for (Note note : orphanNotes) {
			service.deleteNote(note);
		}*/
		
		for (VeteranServicePeriod vsp : orphanVsp) {
			service.deleteVspDm(vsp.getId());
		}
	}
	
	private void doEditCompare(String ssn) throws Exception {
		
		List<Veteran> tmp2Veterans = service.findVeteransBySsn(ssn);
		// redmine 12083 #4 - benefit recipients
		List<Veteran> tmpVeterans = new ArrayList<Veteran>();
		for (Veteran oldV : tmp2Veterans) {
			Veteran newV = new Veteran();
			BeanUtils.copyProperties(newV, oldV);
			List<BenefitType> newBenefitTypeList = new ArrayList<BenefitType>();
			for (BenefitType oldB : newV.getBenefitTypeList()) {
				BenefitType newB = new BenefitType();
				BeanUtils.copyProperties(newB, oldB);
				newB.setBenefitRecipients(getRecipients(newV.getId(), oldB.getId()));
				newBenefitTypeList.add(newB);
				
				newV.setBenefitTypeList(newBenefitTypeList);
			}
			tmpVeterans.add(newV);
		}				
		
		veterans = new ArrayList<Veteran>();
		for (Veteran v : tmpVeterans) {			
			if (v.getVaCurrent().intValue() == 1) {	// get current veteran for proposed tab.
				veteran = new Veteran();
				BeanUtils.copyProperties(veteran, v);
				
				// decoration and medal
				Long[] decorationMedals = new Long[veteran.getDecorationMedalList().size()];
				int i = 0;
				for (DecorationMedal dm : veteran.getDecorationMedalList()) {
					decorationMedals[i] = dm.getId();
					i++;
				}
				veteran.setDecorationMedals(decorationMedals);
			} else if (v.getReviewed().intValue() == 0) {	// add only un-reviewed record to list
				veterans.add(v);
			}
		}
		
		if (veteran == null) {	// there is no current record - impossible!
			veteran = new Veteran();
			BeanUtils.copyProperties(veteran, veterans.get(0));
		}
				
		// put note list on ses so they can be retrieved when saved in edit page.
		request.getSession().setAttribute(getText("TEMP_LIST_SES"), veteran.getNoteList());
		
		// Redmine 12095 comment #3
		if (request.getParameter("fromDashboard") != null) {
			veteran.setFromDashboard(true);
		}
	}
	
	private String getNextEditCompare(String currentSsn) throws Exception {
		
		String nextSsn = "";
		List<Dashboard> unreviews = service.findVeterans_unreviewed();
		for (Dashboard unreview : unreviews) {
			if (!currentSsn.equals(unreview.getSsn())) {
				nextSsn = unreview.getSsn();
				break;
			}
		}
		
		return nextSsn;
	}

	private List<BenefitRecipient> getRecipients(Long veteranId, Long benefitTypeId) {
		
		Long veteranBenefitId = service.findVeteranBenefitTypeId(veteranId, benefitTypeId);
		List<BenefitRecipient> retList = service.findVeteranBenefitRecipients(veteranBenefitId);
		if (retList.size() > 0) {
			return retList;
		} else {
			return null;
		}
		
	}
	
	private Long getBranchId(String branchName) {
		
		Long branchId = null;
		Map<String, String> branchLookup = service.findLkupBranches();
		List<ServiceBranch> serviceBranches = service.findAllServiceBranches();
		
		String suggestedBranchName = branchLookup.get(branchName);
		for (ServiceBranch branch : serviceBranches) {
			if (suggestedBranchName != null && suggestedBranchName.equalsIgnoreCase(branch.getName())) {
				branchId = branch.getId();
				break;
			}
		}
		
		return branchId;
	}
		
	private Veteran getCurrentVeteran(String ssn) {
		
		List<Veteran> results = service.findVeteransBySsn(ssn);
		Veteran ret = null;
		for (Veteran v : results) {
			if (v.getVaCurrent().equals(new Integer(1))) {
				ret = v;
				break;
			}
		}
		
		return ret;
	}
	
	private boolean isDocTypeNameContains214(Long docTypeId) {
		
		boolean ret = false;
		for (NameValue nv : getDocTypeList()) {
			if (docTypeId.equals(nv.getId()) && nv.getValue().contains("214")) {
				ret = true;
				break;
			}
		}			
		
		return ret;
	}
	
	private String twoCharsState(String state) {
		if (state != null && state.length() == 2) {
			return state;
		}
		
		if (state != null && state.length() > 2) {
			return Util.getKeyByValue(super.getStateMap(), Util.initCap(state));
		}
		
		return null;
	}
	
	private byte[] convertFileToBytes(File file) throws Exception {
		FileInputStream fileInputStream = null;
		try {
			byte[] bytes = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytes);
			
			return bytes;
		} finally {
			fileInputStream.close();
		}
	}
	
	/**
	 * Saves file to external storage.
	 * 
	 * @param baImage - byte array
	 * @param fileNameExt - string
	 * @return file path pointer which points to the image 
	 * @throws Exception
	 */
	private String saveToExternal(byte[] baImage, String fileNameExt) throws Exception {
		// 1) determine attachment pointer info to build file path name
		Map<String, Long> dfc = service.findDirFileCount();
		
		String rootPath = getText("external.rootPath");
		long maxFileCount = Long.parseLong(getText("external.maxFileCount"));
		String filePathName = null;
		Long directoryCount = null;
		Long fileCount = null;
		Long fileNumber = null;
		if (dfc != null) {
			if ( (new Long(maxFileCount - 1L)).equals(dfc.get("fileCount")) ) {
				filePathName = rootPath + "/documents/" + leftPadString(dfc.get("directoryCount").toString()) + "/" + dfc.get("fileNumber").toString() + fileNameExt;
				
				directoryCount = new Long(dfc.get("directoryCount").longValue() + maxFileCount);
				fileCount = new Long(0L);
				new File(rootPath + "/documents/" + leftPadString(directoryCount.toString())).mkdir();
			} else {
				filePathName = rootPath + "/documents/" + leftPadString(dfc.get("directoryCount").toString()) + "/" + dfc.get("fileNumber").toString() + fileNameExt;
				
				directoryCount = dfc.get("directoryCount");
				fileCount = new Long(dfc.get("fileCount").longValue() + 1L);
			}
			
			fileNumber = new Long(dfc.get("fileNumber").longValue() + 1L);
		} else { // initial external storage - create first directory and file
			filePathName = rootPath + "/documents/00000000/0" + fileNameExt;
			directoryCount = new Long(0L);
			fileCount = new Long(1L);
			fileNumber = new Long(1L);
			
			new File(rootPath + "/documents/00000000").mkdir();
		}
		
		// 2) save image to file path name
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePathName);
			fos.write(baImage);
		} finally {
			fos.close();
		}
		
		// 3) save new attachment pointer info to db
		if (dfc != null) {
			dfc.put("directoryCount", directoryCount);
			dfc.put("fileCount", fileCount);
			dfc.put("fileNumber", fileNumber);
			service.updateDirFileCount(dfc);
		} else {
			dfc = new HashMap<String, Long>();
			dfc.put("directoryCount", directoryCount);
			dfc.put("fileCount", fileCount);
			dfc.put("fileNumber", fileNumber);
			service.saveDirFileCount(dfc);
		}
		
		return filePathName.replace(getText("external.rootPath"), "");
	}
	
	/**
	 * Pads with leading 0's to make 8 numeric chars. Ex. 00001000, ...
	 * @return
	 */
	private String leftPadString(String numStr) {
		if (numStr.length() >= 8) {
			return numStr;
		} else {
			String zeros = "";
			for (int i = 1; i <= 8 - numStr.length(); i++) {
				zeros = zeros + "0";
			}
			
			return zeros + numStr;
		}
	}
	
}
