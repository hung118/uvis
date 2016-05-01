package gov.utah.va.vts.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gov.utah.dts.det.util.PdfProcessThread;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DocType;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranAttachment;
import gov.utah.va.vts.service.VtsService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Preparable;

/**
 * Short form action for veteran new entry.
 * @author hnguyen
 *
 */
public class ShortFormAction extends BaseActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;

	private final String SUPPORTED_DD214_1 = "DD FORM 214, AUG 2009";
	
	@Autowired
	VtsService service;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private Veteran veteran;
	
	private File attachment;
	private String attachmentFileName;
	private String attachmentContentType;
	private DocType docType;
	private String oldSsn;
	
	public void prepare() throws Exception {
		if (veteran != null && veteran.getId() != null) {
			veteran = service.findVeteranById(veteran.getId());
		}
	}
	
	@SkipValidation
	public String execute() throws Exception {
		logger.debug("Entering execute ...");
		veteran = new Veteran();
		veteran.setState("UT");
		setDocType(getFirstDd214DocType());
		return INPUT;
	}
	
	public String insert() throws Exception {
		logger.debug("Entering save ...");

		doInsert();
		
		oldSsn = veteran.getSsn();
		
		return SUCCESS;
	}
			
	public String update() throws Exception {
		logger.debug("Entering update ...");
		
		if (veteran.getSsn().equals(oldSsn)) {	// ssn was not changed - just save veteran
			if (veteran.getEthnicity().getId() == null) {
				veteran.setEthnicity(null);
			}
			setRuralCrosswalk(veteran);
			
			if (veteran.getDecorationMedals().length > 0) {	// decoration and medal - purple heart
				List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
				for (Long decorationMedalLong : veteran.getDecorationMedals()) {
					DecorationMedal dm = new DecorationMedal();
					dm.setId(decorationMedalLong);
					decorationMedals.add(dm);
				}
				veteran.setDecorationMedalList(decorationMedals);				
			}

			service.saveVeteran(veteran);
			addActionMessage(getText("message.save", Util.getStringArray(getText("veteran"))));
		} else {	// ssn was changed - 1. save veteran as a new insert and 2. delete the old ones
			service.clear();
			Veteran ov = service.findVeteranById(veteran.getId());
			List<VeteranAttachment> oAttachmentsList = new ArrayList<VeteranAttachment>(ov.getAttachmentsList());
			int oVaCurrent = ov.getVaCurrent().intValue();
			String oSsn = ov.getSsn();
			Long oVid = ov.getId();
			boolean isOldCurrent = true;
			
			// 1. save veteran as a new insert
			veteran.setId(null); // veteran object will be saved as new record
			doInsert();
			if (oVaCurrent == 1) {	// old record is current; it also has reviewed record.
				isOldCurrent = true;
				if (veteran.getVaCurrent().intValue() == 1) {	// get attachment from old and save
					for (VeteranAttachment va : oAttachmentsList) {
						dbAttachmentCopy(va, veteran);
					}
				} else {	// get attachment from old and save to the new current record
					List<Veteran> newVets = service.findVeteransBySsn(veteran.getSsn());
					for (Veteran newV : newVets) {
						if (newV.getVaCurrent().intValue() == 1) {
							for (VeteranAttachment va : oAttachmentsList) {
								dbAttachmentCopy(va, newV);
							}
							break;
						}
					}
				}
			} else {	// old record is un-reviewed record
				isOldCurrent = false;
				if (veteran.getVaCurrent().intValue() == 1) {	// move (copy/delete) attachment from old current over
					List<Veteran> oldVets = service.findVeteransBySsn(oSsn);
					for (Veteran oldV : oldVets) {
						if (oldV.getVaCurrent().intValue() == 1) {
							VeteranAttachment vamax = getMax(oldV.getAttachmentsList()); 
							dbAttachmentCopy(vamax, veteran);

							service.clear();
							service.deteteVeteranAttachment(vamax);
							
							break;
						}
					}
				} else {	// move (copy/delete) attachment from old current to new current
					List<Veteran> oldVets = service.findVeteransBySsn(oSsn);
					List<Veteran> newVets = service.findVeteransBySsn(veteran.getSsn());
					for (Veteran oldV : oldVets) {
						if (oldV.getVaCurrent().intValue() == 1) {
							for (Veteran newV : newVets) {
								if (newV.getVaCurrent().intValue() == 1) {
									VeteranAttachment vamax = getMax(oldV.getAttachmentsList());
									dbAttachmentCopy(vamax, newV);
									
									service.clear();
									service.deteteVeteranAttachment(vamax);

									break;
								}
							}
							break;
						}
					}
				}
			}
			
			// 2. delete old records
			if (isOldCurrent) {		// delete old current and reviewed records
				List<Veteran> oldVets = new ArrayList<Veteran>(service.findVeteransBySsn(oSsn));
				service.clear();
				for (Veteran oldV : oldVets) {
					service.deleteVeteran(oldV);
				}				
			} else {	// delete un-reviewed record
				Veteran oldUnreviewed = new Veteran();
				oldUnreviewed.setId(oVid);
				service.deleteVeteran(oldUnreviewed);
			}
		}
		
		return SUCCESS;
	}

	public void validateInsert() {
		doValidate();
	}
	
	public void validateUpdate() {
		doValidate();
	}
	
	public Veteran getVeteran() {
		return veteran;
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public VtsService getService() {
		return service;
	}

	public void setService(VtsService service) {
		this.service = service;
	}
	
	/**
	 * Override super method to show Purple Heart only. Redmine 14336
	 */
    public List<NameValue> getDecorationMedalList() throws Exception {
    	logger.debug("getDecorationMedalList 2 ...");
    	ArrayList<NameValue> decorationMedalList = new ArrayList<NameValue>();
    	List<DecorationMedal> decorationMedals = service.findAllDecorationMedalsActive();
    	for (DecorationMedal decorationMedal : decorationMedals) {
    		if ("Purple Heart".equals(decorationMedal.getName())) {
        		NameValue nv = new NameValue();
        		nv.setId(decorationMedal.getId());
        		nv.setValue(decorationMedal.getName());
        		decorationMedalList.add(nv);    			
    		}
		}
    	
    	return decorationMedalList;
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
    
	public DocType getDocType() {
		return docType;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public String getOldSsn() {
		return oldSsn;
	}

	public void setOldSsn(String oldSsn) {
		this.oldSsn = oldSsn;
	}

	private void doInsert() throws Exception {
		veteran.setContactable(new Integer(1));
		veteran.setVaMedEnrolled(new Boolean(false));
		veteran.setSourceDate(new Date());
		veteran.setVaCurrent(new Integer(0));	// non-current
		if (veteran.getRecordType().getId().longValue() == 2) {	// dd214 set verified to 1
			veteran.setVerified(new Integer(1));
		} else {
			veteran.setVerified(new Integer(0));
		}
		veteran.setActive(new Integer(1));
		veteran.setShareFederalVa(new Boolean(true));
		veteran.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		if (veteran.getEthnicity() == null || veteran.getEthnicity().getId() == null) {
			veteran.setEthnicity(null);
		}
				
		// process auto data populate for supported forms.
		if (attachment != null && "application/pdf".equals(attachmentContentType)) {
			if (SUPPORTED_DD214_1.equalsIgnoreCase(service.findDocNameById(docType.getId()))) {
				processAutoPopulate();
			}
		}
		
		veteran.setFirstName(Util.toUpperCaseNull(veteran.getFirstName()));
		veteran.setLastName(Util.toUpperCaseNull(veteran.getLastName()));
		veteran.setMiddleName(Util.toUpperCaseNull(veteran.getMiddleName()));
		
		List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
		if (!(attachment != null && "application/pdf".equals(attachmentContentType))) {	// get info from form
			if (veteran.getDecorationMedals().length > 0) {	// decoration and medal - purple heart
				for (Long decorationMedalLong : veteran.getDecorationMedals()) {
					DecorationMedal dm = new DecorationMedal();
					dm.setId(decorationMedalLong);
					decorationMedals.add(dm);
				}
				veteran.setDecorationMedalList(decorationMedals);	
			}
			
			if (veteran.getDateOfBirth() != null) {
				veteran.setDateOfBirthStr(sdf.format(veteran.getDateOfBirth()));
			}
		}
		
		// check if ssn does not already exists, create current veteran record.
		boolean review = false;
		List<Veteran> veterans = service.findVeteransBySsn(veteran.getSsn());
		setRuralCrosswalk(veteran);
		if (veterans.size() == 0) {
			veteran.setReviewed(new Integer(1));	// reviewed
			service.saveVeteran(veteran);
			
			// create current veteran
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setId(null);
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			
			veteran.setDecorationMedalList(decorationMedals);	// need to reset; otherwise got an error: Found shared references to a collection: gov.utah.va.vts.model.Veteran.decorationMedalList
			currentVeteran.setAttachmentsList(null);
			currentVeteran.setBenefitTypeList(null);
			currentVeteran.setNoteList(null);
			currentVeteran.setServicePeriodList(null);
			service.saveVeteran(currentVeteran);
			
			review = true;
			
			if (attachment != null) {
				uploadDocument(currentVeteran);
			}
			
			service.clear();
			veteran.setId(currentVeteran.getId());	// to display current veteran when return
			veteran.setVaCurrent(new Integer(1));
		} else {
			veteran.setReviewed(new Integer(0));	// un-reviewed
			service.saveVeteran(veteran);
			review = false;
			
			if (attachment != null) {
				for (Veteran v : veterans) {
					if (v.getVaCurrent().intValue() == 1) {
						uploadDocument(v);
						break;
					}
				}
			}
		}
				
		if (review) {
			addActionMessage(getText("message.save2", Util.getStringArray(getText("veteran"))));
		} else {
			addActionMessage(getText("message.save3", Util.getStringArray(getText("veteran"))));
		}
		
		if (veteran.getDecorationMedalList() != null) {	// make sure to show purple heart when re-display page for automatic PDF text retrival
			Long[] dms = new Long[veteran.getDecorationMedalList().size()];
			int i = 0;
			for (DecorationMedal dm : veteran.getDecorationMedalList()) {
				dms[i] = dm.getId();
				i++;
			}
			veteran.setDecorationMedals(dms);
		}
		
		if (veteran.getDateOfBirth() != null) {	// make sure to show dob when re-display page for automatic PDF text retrival
			veteran.setDateOfBirthStr(sdf.format(veteran.getDateOfBirth()));
		}
	}
	
	private void doValidate() {
		if (Validate.isEmpty(veteran.getSsn()) && attachment == null) {	
			addActionError(getText("error.required_3"));
		}
		if (!Validate.isEmpty(veteran.getSsn()) && attachment == null) {
			veteran.setSsn(veteran.getSsn().replaceAll(" \\| ", "-"));
			if (!Validate.isSSNValid(veteran.getSsn())) {	// not valid display error
				addActionError(getText("error.notValidSsn2", Util.getStringArray(getText("veteran.ssn"))));
			} else {	// valid - make sure format to ###-##-#### 
				if (veteran.getSsn().length() == 9) {
					veteran.setSsn(Util.formatSsn(veteran.getSsn()));
				}
			}
		}
		if (Validate.isEmpty(veteran.getSsn()) && attachment != null && "application/pdf".equals(attachmentContentType)) {	// check for supported DD214 PDF forms
			if (!SUPPORTED_DD214_1.equalsIgnoreCase(service.findDocNameById(docType.getId()))) {
				addActionError(getText("error.required_3"));
			}
		}
		
		if (!Validate.isEmpty(veteran.getDateOfBirthStr())) {
			// use reg
			if (!veteran.getDateOfBirthStr().matches("[1-2][0-9][0-9][0-9]([0-9]{2})([0-9]{2})")) {
				addActionError(getText("error.notValidDate2", Util.getStringArray(getText("veteran.dob"))));
			}
			
			/** not working for some cases. ignore it, user will update record manually. */
			try {
				veteran.setDateOfBirth(sdf.parse(veteran.getDateOfBirthStr()));
			} catch (Exception e) {
				// ignore
			}
		}
		if (!Validate.isEmpty(veteran.getZip()) && !Validate.isZipValid(veteran.getZip()))
			addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.streetZip"))));
		if (!Validate.isPhoneNumberValid(veteran.getPrimaryPhone()))
			addActionError(getText("error.notValidPhone", Util.getStringArray(getText("veteran.primaryPhone"))));
		if (!Validate.isEmpty(veteran.getEmail()) && !Validate.isEmailValid(veteran.getEmail())) 
			addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.email"))));
	}
	
	/**
	 * Saves attachment to io directory (external storage) instead of database.
	 * 
	 * @param v
	 * @throws Exception
	 */
	private void uploadDocument(Veteran v) throws Exception {
		FileInputStream fisImage = null;
		try {
			VeteranAttachment veteranAttachment = new VeteranAttachment();
			byte[] baImage = new byte[(int)attachment.length()];
			fisImage = new FileInputStream(attachment);
			fisImage.read(baImage);
			String filePath = saveToExternal(baImage, attachmentFileName.substring(attachmentFileName.indexOf('.')));
			
			if (filePath == null) {
				throw new Exception("Error: Failed to save attachment to the external drive.");
			}
			
			veteranAttachment.setAttachmentPointer(filePath);
			
			veteranAttachment.setAttachmentFileName(attachmentFileName);
			veteranAttachment.setAttachmentContentType(attachmentContentType);
					
			veteranAttachment.setDocType(docType);
			veteranAttachment.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			veteranAttachment.setInsertTimestamp(new Date());
			veteranAttachment.setUpdateTimestamp(new Date());
			
			veteranAttachment.setVeteran(v);
			
			service.saveVeteranAttachment(veteranAttachment);		
		} finally {
			if (fisImage != null) fisImage.close();
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
	 * Retrieves text info from PDF file and populate veteran object for saving to database. This method uses
	 * thread piped reader and writer.
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void processAutoPopulate_unused() throws Exception {
		PDDocument document = PDDocument.load(attachment);
		
		PipedWriter pw = new PipedWriter();
		PipedReader pr = new PipedReader(pw);
		
		PdfProcessThread thread1 = new PdfProcessThread(document, pr, pw);
		PdfProcessThread thread2 = new PdfProcessThread(null, pr, pw);
		
		thread1.start();
		Thread.sleep(2000);
		thread2.start();
		List<String> rows = thread2.getRows();		
		Thread.sleep(5000);
		
		if (rows.size() != 6) {
			throw new Exception("This PDF DD FORM 214 is not supported for automatic text retrieval!");
		}
		
		for (int i = 0; i < rows.size(); i++) {
			switch (i) {
				case 0:	// last first middle name
					try {
						String[] names = rows.get(i).split(" ");
						veteran.setLastName(names[0]);
						veteran.setFirstName(names[1]);
						veteran.setMiddleName(names[2]);
					} catch (Exception e) {}	// ignore wacky situation
					break;
				case 1:	// ssn
					veteran.setSsn(rows.get(i));
					break;
				case 2:	// dob
					try {
						veteran.setDateOfBirth(sdf.parse(rows.get(i)));
						veteran.setDateOfBirthStr(sdf.format(veteran.getDateOfBirth()));
					} catch (Exception e) {}	// ignore wacky situation
					break;
				case 3:	// decoration
					if ("PURPLE HEART".equals(rows.get(i))) {
						DecorationMedal dm = new DecorationMedal();
						dm.setId(getPurpleHeartId());
						List<DecorationMedal> dms = new ArrayList<DecorationMedal>();
						dms.add(dm);
						veteran.setDecorationMedalList(dms);
					}
					break;
				case 4:	// street address
					veteran.setAddress1(rows.get(i));
					break;
				case 5:	// city state zip 
					try {
						String[] csz = rows.get(i).split(" ");
						veteran.setZip(csz[csz.length-1]);
						veteran.setState(csz[csz.length-2]);
						veteran.setCity("");
						for (int j = 0; j < csz.length-2; j++) {
							veteran.setCity(veteran.getCity() + csz[j] + " ");
						}
						veteran.setCity(veteran.getCity().trim());
					} catch (Exception e) {}	// ignore wacky situation
				}
		}
	}
	
	/**
	 * Retrieves text info from PDF file and populate veteran object for saving to database. This method uses
	 * file io.
	 * @throws Exception
	 */
	private void processAutoPopulate() throws Exception {
		PDDocument in = PDDocument.load(attachment);
		String tmpFileName = getText("pdfSaveFile") + "/" + Util.getRandomString(12, true) + ".txt";
		File tmpFile = null;
		String tmpSsn;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			tmpFile = new File(tmpFileName);
			//stripper.writeText(in, new OutputStreamWriter(new FileOutputStream(tmpFileName)));	// not working... empty tmpFile
			FileUtils.writeStringToFile(tmpFile, stripper.getText(in));
			
			List<String> rows = FileUtils.readLines(tmpFile);
			int i = 0;
			boolean notSupportedFile = true;
			while (i < rows.size()) {
				String row = rows.get(i);
				row = row.trim().toUpperCase();
				i = i + 1;
				try {
					if (row.contains("1. NAME")) {	// next line will be last first middle name
						String[] names = null;
						String fullName = (Util.removeNonAlphaNumeric(rows.get(i))).trim();
						names = fullName.trim().split(" ");
						i = i + 1;

						veteran.setLastName(names[0]);
						veteran.setFirstName(names[1]);
						veteran.setMiddleName(names[2]);
						
						notSupportedFile = false;
						continue;	// skip the rest
					}					
				} catch (Exception e) {}	// ignore wacky situation
				
				try {
					if (row.contains("3. SOCIAL")) {	// next line will be ssn
						tmpSsn = rows.get(i).replaceAll(" 1 ","");	// cases with ssn like 526 1 97 1 9678 instead of 526 | 97 | 9678
						veteran.setSsn(Util.formatSsn(Util.removeNonNumeric(tmpSsn)));
						i = i + 1;
						
						notSupportedFile = false;
						continue;
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("5. DATE")) {
						String [] dob;
						if (rows.get(i).trim().length() == 8) {	// next line with pattern 19580622
							veteran.setDateOfBirth(sdf.parse(rows.get(i).trim()));
							i = i + 1;
						} else if (rows.get(i).contains("YYYYMMDD")) {	// next line with pattern 19580622 (YYYYMMDD) 00000000
							dob = Util.removeSpaces(rows.get(i)).split("(YYYYMMDD)");
							veteran.setDateOfBirth(sdf.parse(dob[0]));
							i = i + 1;
						} else {	// same line at the end
							dob = row.split(" ");
							veteran.setDateOfBirth(sdf.parse(dob[dob.length - 1]));
						}

						continue;
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("AWARDED OR AUTHORIZED")) {	// look for purple heart next line
						if (rows.get(i).toUpperCase().contains("PURPLE HEART")) {
							DecorationMedal dm = new DecorationMedal();
							dm.setId(getPurpleHeartId());
							List<DecorationMedal> dms = new ArrayList<DecorationMedal>();
							dms.add(dm);
							veteran.setDecorationMedalList(dms);
						}
						i = i + 1;
						
						continue;
					}					
				} catch (Exception e) {}
				
				try {
					if (row.contains("19A. MAILING")) {
						if (rows.get(i + 1).toUpperCase().contains("B. NEAREST")) {	// street, city, state, and zip are on next line
							String[] scsz = rows.get(i).trim().split(" ");
							if (scsz[scsz.length-1].length() < 5) {	// pattern [685, COUNTRY, CLUB, STANSBURY, PARK, UTAH, 34, 074]
								int k = 0;
								for (int j = scsz.length-1; j < scsz.length && j > 0; j--) {
									if (!Util.isNumber(scsz[j])) {
										k = j;
										break;
									}
								}
								veteran.setZip("");
								for (int l = k + 1; l < scsz.length; l++) {
									veteran.setZip(scsz[l] + veteran.getZip());
								}
								
								veteran.setState(scsz[k]);
								veteran.setCity(scsz[k-1]);
								
								veteran.setAddress1("");
								for (int j = 0; j < k - 1; j++) {
									veteran.setAddress1(veteran.getAddress1() + scsz[j] + " ");
								}
								veteran.setAddress1(veteran.getAddress1().trim());	
							} else {	// pattern [685, COUNTRY, CLUB, STANSBURY, PARK, UTAH, 34074]
								veteran.setZip(scsz[scsz.length-1]);
								veteran.setState(scsz[scsz.length-2]);
								veteran.setCity(scsz[scsz.length-3]);
								
								veteran.setAddress1("");
								for (int j = 0; j < scsz.length-3; j++) {
									veteran.setAddress1(veteran.getAddress1() + scsz[j] + " ");
								}
								veteran.setAddress1(veteran.getAddress1().trim());															
							}
						} else {	// street address on next line; city, state, and zip on next 2nd line
							veteran.setAddress1(rows.get(i).trim()); // street address
							
							String[] csz = rows.get(i + 1).trim().split(" ");	// city state zip
							if (csz[csz.length-1].length() < 5) {	// pattern [SARATOGA, SPRINGS, UTAH, 84, 04, 5]
								int k = 0;
								for (int j = csz.length-1; j < csz.length && j > 0; j--) {
									if (!Util.isNumber(csz[j])) {
										k = j;
										break;
									}
								}
								veteran.setZip("");
								for (int l = k + 1; l < csz.length; l++) {
									veteran.setZip(csz[l] + veteran.getZip());
								}

								veteran.setState(csz[k]);
								
								veteran.setCity("");
								for (int m = 0; m < k; m++) {
									veteran.setCity(veteran.getCity() + csz[m] + " ");
								}
								veteran.setCity(veteran.getCity().trim());
							} else {	// pattern [SARATOGA, SPRINGS, UTAH, 84045]
								veteran.setZip(csz[csz.length-1]);
								veteran.setState(csz[csz.length-2]);
							
								veteran.setCity("");
								for (int j = 0; j < csz.length-2; j++) {
									veteran.setCity(veteran.getCity() + csz[j] + " ");
								}
								veteran.setCity(veteran.getCity().trim());
							}
						}
						
						// check state and zip for db column size
						if (!veteran.getState().isEmpty() && veteran.getState().length() > 2) {
							veteran.setState(getAbreviateState(veteran.getState()));
						}
						if (!veteran.getZip().isEmpty() && veteran.getZip().length() > 10) {
							veteran.setZip("");
						}
						
						break; // no more info to retrieve
					}
				} catch (Exception e) {}
			}
			
			if (notSupportedFile) {
				throw new Exception("This PDF DD FORM 214 is not supported for automatic text retrieval!");			
			}
			
		} finally {
			in.close();
			if (tmpFile != null && tmpFile.delete()) {
				logger.info("tmp file was deleted successfully: " + tmpFileName);
			} else {
				logger.info("Failed to delete tmp file: " + tmpFileName);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getAbreviateState(String state) {
		Iterator it = super.getStateMap().entrySet().iterator();
		String ret = "";
		while (it.hasNext()) {
			Map.Entry<String, String> mapEntry = (Map.Entry)it.next();
			if (state.equalsIgnoreCase(mapEntry.getValue())) {
				ret = mapEntry.getKey();
			}
		}
		
		return ret;
	}
	
	private void dbAttachmentCopy(VeteranAttachment va, Veteran v) throws Exception {
		VeteranAttachment newAtt = new VeteranAttachment();
		BeanUtils.copyProperties(newAtt, va);
		newAtt.setVeteran((v));
		newAtt.setId(null);
		service.saveVeteranAttachment(newAtt);
	}
	
	private VeteranAttachment getMax(List<VeteranAttachment> valist) {
		VeteranAttachment vatmp = new VeteranAttachment();
		vatmp.setId(new Long(0L));
		for (VeteranAttachment va : valist) {
			if (vatmp != null && va.getId() > vatmp.getId()) {
				vatmp = va;
			}
		}
		
		return vatmp;
	}
	
	/**
	 * Pads with leading 0's to make 8 numeric chars. Ex. 00001000, ...
	 * @param s
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
