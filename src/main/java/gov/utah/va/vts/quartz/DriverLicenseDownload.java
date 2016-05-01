package gov.utah.va.vts.quartz;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import gov.utah.dts.det.util.Encryption;
import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Driver licence SFTP download scheduler task.
 * 
 * @author HNGUYEN
 *
 */
public class DriverLicenseDownload extends BaseSchedulerTask {
	
	private int badRec = 0;
	
	public DriverLicenseDownload(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {

		logger.info("Entering Quartz scheduler task run() ...");
		em.getTransaction().begin();
		
		ResourceBundle prop = null;
		String fileName = null;
		List<String> rows = null;
		String retMsg = "Success";
		try {
			prop = ResourceBundle.getBundle("vts");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filePath = prop.getString("sftp.filePath");
			fileName = prop.getString("sftp.fileNamePrefix") + sdf.format(new Date()) + prop.getString("sftp.compressedExtension");
			
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
					dldDbInserts(rows);
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
				recordType.setId(new Long(prop.getString("SOURCE_DL")));	// DLD record type (source)
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

	private List<String> getSftpInputStream(String filePathName) throws Exception {
		
		ResourceBundle prop = ResourceBundle.getBundle("vts");
		Session session = null;
		ChannelSftp sftpChannel = null;
		try {
			JSch jsch = new JSch();
			
			session = jsch.getSession(prop.getString("sftp.userName"), prop.getString("sftp.server"), Integer.parseInt(prop.getString("sftp.port")));
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(prop.getString("sftp.password"));
			session.connect();
			
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp)channel;
				
			InputStream is = sftpChannel.get(filePathName);
			
			List<String> rows;
			String zipFileName = filePathName.replaceAll(prop.getString("sftp.filePath"), "");	// returns veteransComplete20120618.zip
			String folderName = prop.getString("sftp.tmpDir") + zipFileName.replaceAll(prop.getString("sftp.compressedExtension"), "");		// returns /tmp/veteransComplete20120618
			File zipFile = null;
			try {
				// convert input stream to file
				zipFile = new File(prop.getString("sftp.tmpDir") + zipFileName);
				Util.copyInputStreamToFile(is, zipFile);
				
				// decrypt compressed && encrypted file.
				Encryption encryption = new Encryption(zipFile, new File(folderName), prop.getString("sftp.key"));
				encryption.decrypt();	// result decrypted file is located in folderName (/tmp/veteransComplete20120618).
				
				// retrieve list of records
				rows = FileUtils.readLines(new File(folderName + "/" + zipFileName.replaceAll(prop.getString("sftp.compressedExtension"), "") + prop.getString("sftp.fileNameExtension")));
				
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
	
	private void dldDbInserts(List<String> rows) {
		
		for (String row : rows) {
			String[] cols = row.split("\\|");
			
			try {
				dldDbInsert(cols);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				badRec++;
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
		if (cols[5] != null) {
			veteran.setDateOfBirth(sdf.parse(cols[5]));
		}
		
		if (cols[6].length() == 9) {
			veteran.setSsn(Util.formatSsn(cols[6]));
		} else {	// default to WW2-##-#### - Redmine 12421
			veteran.setSsn("WW2-" + Util.getRandomNumber(2) + "-" + Util.getRandomNumber(4));
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
		
		ResourceBundle prop = ResourceBundle.getBundle("global-messages");
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(prop.getString("SOURCE_DL")));	// DLD record type (source)
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setReviewed(new Integer(0));	// mark record as un-reviewed
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(false));
		veteran.setActive(new Integer(1));
		
		User defaultUser = new User();
		defaultUser.setId(new Long(prop.getString("SYSTEM_USER")));
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
