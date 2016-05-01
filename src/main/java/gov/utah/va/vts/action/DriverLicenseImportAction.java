package gov.utah.va.vts.action;

import gov.utah.dts.det.util.Encryption;
import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Handles SFTP connection and file upload data import.
 * 
 * @author HNGUYEN
 *
 */
public class DriverLicenseImportAction extends DataImportAction {

	private static final long serialVersionUID = 1L;

	private int badRec = 0;
	
	public String displayDld() throws Exception {
		
		logger.debug("entering displayDld ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DL"))));
		return INPUT;
	}
	
	public String displayDldFile() throws Exception {
		
		logger.debug("entering displayDldFile ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DL"))));
		return INPUT;		
	}
		
	public String importDld() throws Exception {
		
		logger.debug("entering importDld ...");
		
		// 1. get list of strings from sftp server via input stream.
		String filePath = getText("sftp.filePath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = getText("sftp.fileNamePrefix") + sdf.format(dataImport.getInsertTimestamp()) + getText("sftp.compressedExtension");
		
		List<String> rows = null;
		String retMsg = "Success";
		try {
			rows = getSftpInputStream(filePath + fileName);
		} catch (Exception e) {
        	retMsg = "SFTP server error: " + e.getMessage();
		}
			
		// 2. insert records to db from sftp input stream.
		if ("Success".equals(retMsg)) {
			try {
				dldDbInserts(rows);
			} catch (Exception e) {
	        	retMsg = "SFTP data import error: " + e.getMessage();
			}			
		}
        	
		// 3. insert recorded record for this import operation.
		dataImport.setFileName(fileName);	
		if (rows != null) {
			dataImport.setRecordCount(new Integer(rows.size()));
		} else {
			dataImport.setRecordCount(new Integer(0));
		}
		dataImport.setBadRec(new Integer(badRec));
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DL")));	// DLD record type (source)
		dataImport.setRecordType(recordType);
		
		dataImport.setStatus(retMsg);
		
		if ((User)request.getSession().getAttribute(getText("USER")) == null) {	// default system user
			User defaultUser = new User();
			defaultUser.setId(new Long(getText("SYSTEM_USER")));
			dataImport.setCreatedBy(defaultUser);
		} else {
			dataImport.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		}		
		
		dataImport.setActive(new Integer(1));
		dataImport.setUpdateTimestamp(new Date());
		
		service.saveDataImport(dataImport);
        
		// 4. return page
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DL"))));
		dataImport = null;
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.dldImportSuccess"));
        	return SUCCESS;
        } else {
        	dataImport = null;
        	addActionError(retMsg);
        	return INPUT;
        }
	}
	
	public String importDldFile() throws Exception {
		
		logger.debug("entering importDldFile ...");
		
		// 1. get list of strings from file upload.
		List<String> rows = null;
		String retMsg = "Success";
		
		// 2. decrypt the compressed & encrypted csv file upload
		String folderName = getText("sftp.tmpDir") + csvFileFileName.replaceAll(getText("sftp.compressedExtension"), "");	// returns something like /tmp/veteransComplete20120618
		Encryption encryption = new Encryption(csvFile, new File(folderName), getText("sftp.key"));
		encryption.decrypt();	// result decrypted file is located in folderName (/tmp/veteransComplete20120618).
		
		rows = FileUtils.readLines(new File(folderName + "/" + csvFileFileName.replaceAll(".zip", "") + getText("sftp.fileNameExtension")));
					
		// 3. insert records to db from uploaded file
		if ("Success".equals(retMsg)) {
			try {
				dldDbInserts(rows);
			} catch (Exception e) {
	        	retMsg = "File upload data import error: " + e.getMessage().substring(0, 65);
			}			
		}
        	
		// 4. insert recorded record for this import operation.
		dataImport = new DataImport();
		dataImport.setFileName(getCsvFileFileName());	
		
		if (rows != null) {
			dataImport.setRecordCount(new Integer(rows.size()));
		} else {
			dataImport.setRecordCount(new Integer(0));
		}
		dataImport.setBadRec(new Integer(badRec));
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DL")));	// DLD record type (source)
		dataImport.setRecordType(recordType);
		
		if (retMsg.length() < 100) {
			dataImport.setStatus(retMsg);
		} else {
			dataImport.setStatus(retMsg.substring(0, 99));
		}
				
		dataImport.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		dataImport.setActive(new Integer(1));
		dataImport.setInsertTimestamp(new Date());
		dataImport.setUpdateTimestamp(new Date());
		
		service.saveDataImport(dataImport);
        
		// 5. return page and clean up
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DL"))));
		dataImport = null;
		FileUtils.forceDelete(new File(folderName));	// clean up decrypted folder and its file
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.dldImportSuccess"));
        	return SUCCESS;
        } else {
        	dataImport = null;
        	addActionError(retMsg);
        	return INPUT;
        }
	}
	
	public String viewDld() throws Exception {
		super.viewRecord();
		return SUCCESS;
	}
	
	public String viewDldFile() throws Exception {
		super.viewRecord();
		return SUCCESS;
	}
	
	public String deleteDld() throws Exception {
		super.deleteRecord(new Long(getText("SOURCE_DL")));
		return SUCCESS;
	}
	
	public String deleteDldFile() throws Exception {
		super.deleteRecord(new Long(getText("SOURCE_DL")));
		return SUCCESS;
	}

	public void validate() {
		
		if (request.getParameter("import") != null) {	// validate insertTimestamp only when Import button is clicked in driverLicense.jsp 
			if (dataImport.getInsertTimestamp() == null) {
				if (request.getParameter("dataImport.insertTimestamp") == "") 
					addActionError(getText("error.required", Util.getStringArray(getText("data.importDate"))));
				else
					addActionError(request.getParameter("dataImport.insertTimestamp") + " is not a valid date of form MM/dd/yyyy");
				
				dataImport = null;
			}
		}
	}
	
	private List<String> getSftpInputStream(String filePathName) throws Exception {
		
		Session session = null;
		ChannelSftp sftpChannel = null;
		try {
			JSch jsch = new JSch();
			
			session = jsch.getSession(getText("sftp.userName"), getText("sftp.server"), Integer.parseInt(getText("sftp.port")));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(getText("sftp.password"));
			session.connect();
			
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp)channel;
				
			InputStream is = sftpChannel.get(filePathName);
			
			List<String> rows;
			String zipFileName = filePathName.replaceAll(getText("sftp.filePath"), "");	// returns veteransComplete20120618.zip
			String folderName = getText("sftp.tmpDir") + zipFileName.replaceAll(getText("sftp.compressedExtension"), "");		// returns /tmp/veteransComplete20120618
			File zipFile = null;
			try {
				// convert input stream to file
				zipFile = new File(getText("sftp.tmpDir") + zipFileName);
				Util.copyInputStreamToFile(is, zipFile);
				
				// decrypt compressed && encrypted file.
				Encryption encryption = new Encryption(zipFile, new File(folderName), getText("sftp.key"));
				encryption.decrypt();	// result decrypted file is located in folderName (/tmp/veteransComplete20120618).
				
				// retrieve list of records
				rows = FileUtils.readLines(new File(folderName + "/" + zipFileName.replaceAll(getText("sftp.compressedExtension"), "") + getText("sftp.fileNameExtension")));
				
				//rows = IOUtils.readLines(is, null); // for uncompressed and unencrypted SFTP file.
			} finally {
				IOUtils.closeQuietly(is);
				
				FileUtils.forceDelete(new File(folderName));	// clean up decrypted folder and its file
				FileUtils.forceDelete(zipFile);		// clean up zip file
			}
			
			return rows;
		} finally {
			sftpChannel.exit();
			session.disconnect();
		}
		

	}
	
	private void dldDbInserts(List<String> rows) throws Exception {
		
		for (String row : rows) {
			String[] cols = row.split("\\|");

			try {
				dldDbInsert(cols);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				setBadRec(getBadRec() + 1);
				logger.error(e.getMessage());
			}
		}			
	}
	
	private void dldDbInsert(String[] cols) throws Exception {
		
		Veteran veteran = new Veteran();
		veteran.setLastName(Util.toUpperCaseNull(cols[2]));	// skip 0, 1 - license and id card numbers
		veteran.setFirstName(Util.toUpperCaseNull(cols[3]));
		veteran.setMiddleName(Util.toUpperCaseNull(cols[4]));
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		veteran.setDateOfBirth(sdf.parse(cols[5]));
		
		if (cols[6].length() == 9) {
			veteran.setSsn(Util.formatSsn(cols[6]));
		} else {	// default to WW2-##-#### - Redmine 12421
			veteran.setSsn("WW2-" + getRandomNumber(2) + "-" + getRandomNumber(4));
		}
		
		veteran.setAddress1(cols[7]);
		veteran.setCity(cols[8]);
		veteran.setState(cols[9]);
		veteran.setZip(Util.trimNull(cols[10]));
		setRuralCrosswalk(veteran);
		
		veteran.setMailingAddr1(cols[11]);
		veteran.setMailingCity(cols[12]);
		veteran.setMailingState(cols[13]);
		veteran.setMailingZip(Util.trimNull(cols[14]));
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DL")));	// DLD record type (source)
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setReviewed(new Integer(0));	// mark record as un-reviewed
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(false));
		veteran.setActive(new Integer(1));
		
		if ((User)request.getSession().getAttribute(getText("USER")) == null) {	// default system user
			User defaultUser = new User();
			defaultUser.setId(new Long(getText("SYSTEM_USER")));
			veteran.setCreatedBy(defaultUser);
			veteran.setUpdatedBy(defaultUser);
		} else {
			veteran.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));			
		}
		
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		
		// check if there is no other records for this ssn, mark it review and create current record
		List<Veteran> veterans = service.findVeteransBySsn(veteran.getSsn());
		if (veterans.size() == 0) {
			veteran.setReviewed(new Integer(1));
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran			
			service.saveVeteran(currentVeteran);	// id = 0 will merge to create current veteran
		} else {	// mark as reviewed when all data match current record
			for (Veteran v : veterans) {
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
					break;
				}
			}
		}
		
		service.saveVeteran(veteran);	// id = null will persist to create veteran
	}

	public int getBadRec() {
		return badRec;
	}

	public void setBadRec(int badRec) {
		this.badRec = badRec;
	}
	
	private void setRuralCrosswalk(Veteran v) {
		List<NameValue> rurals = service.findUtahRurals();
		for (NameValue rural : rurals) {
			if (v.getZip() != null && v.getZip().length() >= 5 && rural.getName().equals(v.getZip().substring(0, 5))) {
				v.setRural(rural.getValue());
				break;
			}
		}
	}
	
}
