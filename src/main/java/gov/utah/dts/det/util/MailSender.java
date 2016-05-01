package gov.utah.dts.det.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;

/**
 * Handles email sending.
 * 
 * @author hnguyen
 *
 */
public class MailSender {

	/**
	 * logger for logging support 
	 */
	private static Log logger = org.apache.commons.logging.LogFactory.getLog(MailSender.class);
	
	private static String SMTP_HOST = "send.state.ut.us";
	
	public static void send(String subject, String body, String from, Collection<String> recipients) throws Exception {
		send(subject, body, from, recipients, null, null);
	}
	
	public static void send(String subject, String body, String from, Collection<String> recipients, Collection<String> ccRecipients, Collection<String> bccRecipients) throws Exception {
		
		logger.debug("Entering send ...");
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST);
		
		Session session = Session.getDefaultInstance(props,null);
		MimeMessage message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress(from));
		
        // add recipients
        addRecipients(message, recipients, RecipientType.TO);
		
		// add CC recipents
		if (ccRecipients != null) addRecipients(message, ccRecipients, RecipientType.CC);
		
		// add BCC recipients
		if (bccRecipients != null) addRecipients(message, bccRecipients, RecipientType.BCC);
		
		message.setSubject(subject);
		message.setText(body);
		
		Transport.send(message);
	}
	
	private static void addRecipients(MimeMessage message, Collection<String> recipients, RecipientType type) throws Exception {
		
		if (message == null || recipients == null || type == null) return;
		
		Iterator<String> recipientsIt = recipients.iterator();
		while (recipientsIt.hasNext()) {
			String recipient = (String)recipientsIt.next();
			message.addRecipient(type, new InternetAddress(recipient));
		}
	}
}
