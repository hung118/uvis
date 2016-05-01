package gov.utah.va.vts.quartz;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.Note;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * DWS SFTP download scheduler task.
 * 
 * @author HNGUYEN
 *
 */
public class DWSDownload extends BaseSchedulerTask {
	
	private int badRec = 0;
	
	public DWSDownload(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {

		logger.info("Entering Quartz scheduler task run() ...");
		em.getTransaction().begin();
		
		ResourceBundle prop = null;
		String fileName = null;
		List<String[]> rows = null;
		String retMsg = "Success";
		try {
			prop = ResourceBundle.getBundle("vts");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filePath = prop.getString("sftp_dws.filePath");
			fileName = prop.getString("sftp_dws.fileNamePrefix") + sdf.format(new Date()) + prop.getString("sftp_dws.fileNameExtension");
			
			try {
				rows = getSftpInputStream(filePath + fileName);
			} catch (Exception e) {
				logger.error(e.getMessage());
				if (e.getMessage().length() > 77) {
					retMsg = "SFTP server error: " + e.getMessage().substring(0, 77);
				} else {
					retMsg = "SFTP server error: " + e.getMessage();
				}
			}
			
			if ("Success".equals(retMsg)) {
				try {
					dwsDbInserts(rows);
				} catch (Exception e) {
					logger.error(e.getMessage());
					if (e.getMessage().length() > 70) {
						retMsg = "SFTP data import error: " + e.getMessage().substring(0, 70);
					} else {
						retMsg = "SFTP data import error: " + e.getMessage();
					}
				}			
			}
		} catch (Exception e) {
			// log the error and continue
			logger.error(e.getMessage());
		} finally {
			try {
				// insert recorded record for this import operation.
				prop = ResourceBundle.getBundle("global-messages");
				DataImport dataImport = new DataImport();
				
				dataImport.setFileName(fileName);	
				if (rows != null) {
					dataImport.setRecordCount(new Integer(rows.size()));
				} else {
					dataImport.setRecordCount(new Integer(0));
				}
				dataImport.setBadRec(new Integer(badRec));
				RecordType recordType = new RecordType();
				recordType.setId(new Long(prop.getString("SOURCE_DWS")));	// DLD record type (source)
				dataImport.setRecordType(recordType);
				
				dataImport.setStatus(retMsg);
				
				User defaultUser = new User();
				defaultUser.setId(new Long(prop.getString("SYSTEM_USER")));
				dataImport.setCreatedBy(defaultUser);
				
				dataImport.setActive(new Integer(1));
				dataImport.setInsertTimestamp(new Date());
				dataImport.setUpdateTimestamp(new Date());
				
				em.persist(dataImport);				
			} catch (Exception e) {
				// log the error and continue
				logger.error(e.getMessage());
			}
			
			// commit transaction of whatever being done.
			em.getTransaction().commit();
		}
	}

	private List<String[]> getSftpInputStream(String filePathName) throws Exception {
		
		JSch jsch = new JSch();
		Session session = null;
		
		ResourceBundle prop = ResourceBundle.getBundle("vts");
		
		session = jsch.getSession(prop.getString("sftp_dws.userName"), prop.getString("sftp_dws.server"), Integer.parseInt(prop.getString("sftp_dws.port")));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(prop.getString("sftp_dws.password"));
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
	
	private void dwsDbInserts(List<String[]> rows) {
		
		// skip the first blank row
		rows.remove(0);

		for (String[] row : rows) {
			try {
				dwsDbInsert(row);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				badRec++;
				logger.error(e.getMessage());
			}
		}		
	}
	
	private void dwsDbInsert(String[] cols) throws Exception {
		
		ResourceBundle prop = ResourceBundle.getBundle("global-messages");
		User defaultUser = new User();
		defaultUser.setId(new Long(prop.getString("SYSTEM_USER")));
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
		if (cols[4] != null) {
			veteran.setDateOfBirth(sdf.parse(cols[4]));
		}
		veteran.setAddress1(cols[5]);
		veteran.setAddress2(cols[6]);
		veteran.setCity(cols[7]);
		veteran.setState(cols[8]);
		if (cols[9] != null) {
			veteran.setZip((cols[9].length() == 9) ? Util.formatZipCode(cols[9]) : cols[9]);
		}
		setRuralCrosswalk(veteran);
		
		veteran.setMailingAddr1(cols[10]);
		veteran.setMailingAddr1(cols[11]);
		veteran.setMailingCity(cols[12]);
		veteran.setMailingState(cols[13]);
		if (cols[14] != null) {
			veteran.setMailingZip((cols[14].length() == 9) ? Util.formatZipCode(cols[14]) : cols[14]);
		}
		
		if ("V".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(0));	// non-disabled veteran
		} else if ("D".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(25));	// disabled veteran
		} else if ("S".equals(cols[15])) {
			veteran.setPercentDisability(new Integer(50));	// severely disabled veteran
		}
		
		veteran.setVerified(new Integer(0));
		if ("Y".equals(cols[16])) {	// redmine 14461 - create a note with text "DWS indicates verified."
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText(sdf2.format(note.getInsertTimestamp()) + " -- Created by automatic data import " + 
					"<br>DWS indicates verified.<br>----------");
			em.persist(note);
			
			ArrayList<Note> noteList = new ArrayList<Note>();
			noteList.add(note);
			veteran.setNoteList(noteList);
		}

		veteran.setEmail(cols[17]);
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(prop.getString("SOURCE_DWS")));	// DWS record type (source)
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setReviewed(new Integer(0));	// mark record as un-reviewed
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(false));
		veteran.setActive(new Integer(1));
		
		veteran.setCreatedBy(defaultUser);
		veteran.setUpdatedBy(defaultUser);
		
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		
		// check if there is no other records for this ssn, create current record
		String hql = "from Veteran where ssn = :ssn order by source ";
		Query q = em.createQuery(hql);
		q.setParameter("ssn", veteran.getSsn());
		@SuppressWarnings("unchecked")
		List<Veteran> veterans = q.getResultList();
		
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
					em.persist(newNote);
					newNotes.add(newNote);
				}
				currentVeteran.setNoteList(newNotes);
			}
			
			em.persist(currentVeteran);
		} else {	// mark as reviewed when all data match current record
			for (Veteran v : veterans) {
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
					break;
				}
			}
		}
		
		em.persist(veteran);
	}

	public long getBadRec() {
		return badRec;
	}

	public void setBadRec(int badRec) {
		this.badRec = badRec;
	}

	private void setRuralCrosswalk(Veteran v) {
		List<NameValue> rurals = findUtahRurals();
		for (NameValue rural : rurals) {
			if (v.getZip() != null && v.getZip().length() >= 5 && rural.getName().equals(v.getZip().substring(0, 5))) {
				v.setRural(rural.getValue());
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<NameValue> findUtahRurals() {
		String sql = "select zip_code, designation from rural_crosswalk where zip_code like '84%' order by zip_code";
		Query q = em.createNativeQuery(sql);
		List<Object[]> rows = q.getResultList();
		List<NameValue> results = new ArrayList<NameValue>();
		for (Object[] row : rows) {
			NameValue nameValue = new NameValue();
			nameValue.setName(((String)row[0]).toString());
			nameValue.setValue(((Character)row[1]).toString());
			
			results.add(nameValue);
		}
		
		return results;	
	}
	
}
