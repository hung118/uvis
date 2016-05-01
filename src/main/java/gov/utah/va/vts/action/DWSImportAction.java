package gov.utah.va.vts.action;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.Note;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Handles DWS SFTP connection and file upload data import.
 * 
 * @author HNGUYEN
 *
 */
public class DWSImportAction extends DataImportAction {

	private static final long serialVersionUID = 1L;

	private int badRec = 0;
	
	public String displayDws() throws Exception {
		
		logger.debug("entering displayDws ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DWS"))));
		return INPUT;
	}
	
	public String displayDwsFile() throws Exception {
		
		logger.debug("entering displayDwsFile ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DWS"))));
		return INPUT;		
	}
		
	public String importDws() throws Exception {
		
		logger.debug("entering importDws ...");
		
		// 1. get list of strings from sftp server via input stream.
		String filePath = getText("sftp_dws.filePath");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = getText("sftp_dws.fileNamePrefix") + sdf.format(dataImport.getInsertTimestamp()) + getText("sftp_dws.fileNameExtension");
		
		List<String[]> rows = null;
		String retMsg = "Success";
		try {
			rows = getSftpInputStream(filePath + fileName);
		} catch (Exception e) {
			if (e.getMessage().length() > 75) {
				retMsg = "SFTP server error: " + e.getMessage().substring(0, 75);
			} else {
				retMsg = "SFTP server error: " + e.getMessage();
			}
		}
		
		// 2. insert records to db from sftp input stream.
		if ("Success".equals(retMsg)) {
			try {
				dwsDbInserts(rows);
			} catch (Exception e) {
				if (e.getMessage().length() > 75) {
					retMsg = "SFTP data import error: " + e.getMessage().substring(0, 65);
				} else {
					retMsg = "SFTP data import error: " + e.getMessage();
				}
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
		recordType.setId(new Long(getText("SOURCE_DWS")));	// DLD record type (source)
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
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DWS"))));
		dataImport = null;
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.dwsImportSuccess"));
        	return SUCCESS;
        } else {
        	dataImport = null;
        	addActionError(retMsg);
        	return INPUT;
        }
	}
	
	public String importDwsFile() throws Exception {
		
		logger.debug("entering importDwsFile ...");
		
		// 1. get list of strings from file upload.
		List<String[]> rows = null;
		String retMsg = "Success";
					
		CSVReader reader = new CSVReader(new FileReader(this.csvFile));
		rows = reader.readAll();
		reader.close();		// to prevent WARNING: Resource Leaking:  Could not remove uploaded file 'C:\tmp\...'
		
		// 2. insert records to db from uploaded file
		if ("Success".equals(retMsg)) {
			try {
				dwsDbInserts(rows);
			} catch (Exception e) {
				if (e.getMessage().length() > 65) {
					retMsg = "File upload data import error: " + e.getMessage().substring(0, 65);
				} else {
					retMsg = "File upload data import error: " + e.getMessage();
				}
			}			
		}
        	
		// 3. insert recorded record for this import operation.
		dataImport = new DataImport();
		dataImport.setFileName(getCsvFileFileName());
		
		if (rows != null) {
			dataImport.setRecordCount(new Integer(rows.size()));
		} else {
			dataImport.setRecordCount(new Integer(0));
		}
		dataImport.setBadRec(new Integer(badRec));
				
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DWS")));	// DWS record type (source)
		dataImport.setRecordType(recordType);
		
		dataImport.setStatus(retMsg);		
		dataImport.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		dataImport.setActive(new Integer(1));
		dataImport.setInsertTimestamp(new Date());
		dataImport.setUpdateTimestamp(new Date());
		
		service.saveDataImport(dataImport);
        
		// 4. return page
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DWS"))));
		dataImport = null;
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.dwsImportSuccess"));
        	return SUCCESS;
        } else {
        	dataImport = null;
        	addActionError(retMsg);
        	return INPUT;
        }
	}
	
	public String viewDws() throws Exception {
		super.viewRecord();
		return SUCCESS;
	}
	
	public String viewDwsFile() throws Exception {
		super.viewRecord();
		return SUCCESS;
	}
	
	public String deleteDws() throws Exception {
		super.deleteRecord(new Long(getText("SOURCE_DWS")));
		return SUCCESS;
	}
	
	public String deleteDwsFile() throws Exception {
		super.deleteRecord(new Long(getText("SOURCE_DWS")));
		return SUCCESS;
	}

	public void validate() {
		
		if (request.getParameter("import") != null) {	// validate insertTimestamp only when Import button is clicked
			if (dataImport.getInsertTimestamp() == null) {
				if (request.getParameter("dataImport.insertTimestamp") == "") 
					addActionError(getText("error.required", Util.getStringArray(getText("data.importDate"))));
				else
					addActionError(request.getParameter("dataImport.insertTimestamp") + " is not a valid date of form MM/dd/yyyy");
				
				dataImport = null;
			}
		}
	}
	
	private List<String[]> getSftpInputStream(String filePathName) throws Exception {
		
		JSch jsch = new JSch();
		Session session = null;
		
		session = jsch.getSession(getText("sftp_dws.userName"), getText("sftp_dws.server"), Integer.parseInt(getText("sftp_dws.port")));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(getText("sftp_dws.password"));
		session.connect();
		
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftpChannel = (ChannelSftp)channel;
			
		InputStream is = sftpChannel.get(filePathName);
		
		List<String[]> rows;
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(is));
			rows = reader.readAll();
		} finally {
			IOUtils.closeQuietly(is);
			reader.close();
		}
		
		sftpChannel.exit();
		session.disconnect();
		
		return rows;
	}
	
	private void dwsDbInserts(List<String[]> rows) throws Exception {
		
		// skip the first blank row
		rows.remove(0);
		
		for (String[] row : rows) {
			try {
				dwsDbInsert(row);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				setBadRec(getBadRec() + 1);
				logger.error(e.getMessage());
			}
		}			
	}
	
	private void dwsDbInsert(String[] cols) throws Exception {
		
		Veteran veteran = new Veteran();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		if (cols[0].length() == 9) {
			veteran.setSsn(Util.formatSsn(cols[0]));
		} else {	// default to WW2-##-#### - Redmine 12421
			veteran.setSsn("WW2-" + getRandomNumber(2) + "-" + getRandomNumber(4));
		}
		
		veteran.setLastName(Util.toUpperCaseNull(cols[1]));	
		veteran.setFirstName(Util.toUpperCaseNull(cols[2]));
		veteran.setMiddleName(Util.toUpperCaseNull(cols[3]));		
		veteran.setDateOfBirth(sdf.parse(cols[4]));		
		veteran.setAddress1(cols[5]);
		veteran.setAddress2(cols[6]);
		veteran.setCity(cols[7]);
		veteran.setState(cols[8]);
		veteran.setZip((cols[9].length() == 9) ? Util.formatZipCode(cols[9]) : cols[9]);
		setRuralCrosswalk(veteran);
		
		veteran.setMailingAddr1(cols[10]);
		veteran.setMailingAddr1(cols[11]);
		veteran.setMailingCity(cols[12]);
		veteran.setMailingState(cols[13]);
		veteran.setMailingZip((cols[14].length() == 9) ? Util.formatZipCode(cols[14]) : cols[14]);
		
		if ("V".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(0));	// non-diabled veteran
		} else if ("D".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(25));	// disabled veteran
		} else if ("S".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(50));	// severely disabled veteran
		}
		
		veteran.setVerified(new Integer(0));
		if ("Y".equals(cols[16])) {	// redmine 14461 - create a note with text "DWS indicates verified."
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			Note note = new Note();
			note.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText(sdf2.format(note.getInsertTimestamp()) + " -- Created by manual data import " + 
					"<br>DWS indicates verified.<br>----------");
			service.saveNote(note);
			
			ArrayList<Note> noteList = new ArrayList<Note>();
			noteList.add(note);
			veteran.setNoteList(noteList);
		}

		veteran.setEmail(cols[17]);
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DWS")));	// DLD record type (source)
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
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
			currentVeteran.setId(null);		// make sure it's null, not 0.
			
			// insert orig note to current record. This is needed or else 2 records will point to the same note id.
			if (veteran.getNoteList() != null) {
				List<Note> newNotes = new ArrayList<Note>();
				for (Note oldNote : veteran.getNoteList()) {
					Note newNote = new Note();
					BeanUtils.copyProperties(newNote, oldNote);
					newNote.setId(null);
					service.saveNote(newNote);
					newNotes.add(newNote);
				}
				currentVeteran.setNoteList(newNotes);
			}
			
			service.saveVeteran(currentVeteran);
		} else {	// mark as reviewed when all data match current record
			for (Veteran v : veterans) {
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
					break;
				}
			}
		}
		
		service.saveVeteran(veteran);
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
