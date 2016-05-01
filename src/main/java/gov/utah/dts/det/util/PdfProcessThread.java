package gov.utah.dts.det.util;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfProcessThread extends Thread {

	private PDDocument document;
	private PipedReader pr;
	private PipedWriter pw;
	private List<String> rows = new ArrayList<String>();
	
	public PdfProcessThread(PDDocument document, PipedReader pr, PipedWriter pw) {
		this.document = document;
		this.pr = pr;
		this.pw = pw;
	}
	
	@Override
	public void run() {
		if (document != null) {	// write text from pdf to pw
			try {
				PDFTextStripper stripper = new PDFTextStripper();
				stripper.writeText(document, pw);
			} catch (IOException e) {
				System.out.println("Ignore error: " + e.getMessage());
				//e.printStackTrace();
			} finally {
				try {
					pw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {	// store text in pr to list
			try {
				int item;
				boolean name = false;
				boolean ssn = false;
				boolean dob = false;
				boolean purpleHeart = false;
				boolean address = false;
				boolean csz = false;
				StringBuffer row = new StringBuffer("");
				while ((item = pr.read()) != -1){
					if (item == 10) {	// encounter carrage return or line feed
						if (row.toString().contains("1. NAME")) {	// next line will be last first middle name
							row = new StringBuffer("");
							name = true;
							continue;
						}
						if (name) {
							rows.add((Util.removeNonAlphaNumeric(row.toString())).trim());
							row = new StringBuffer("");
							name = false;
						}
						
						if (row.toString().contains("3. SOCIAL")) {	// next line will be ssn
							row = new StringBuffer("");
							ssn = true;
							continue;
						}
						if (ssn) {
							rows.add(Util.formatSsn(Util.removeNonNumeric(row.toString())));
							row = new StringBuffer("");
							ssn = false;
						}
						
						if (row.toString().contains("5. DATE")) {	// next line will be date of birth yyyymmdd
							row = new StringBuffer("");
							dob = true;
							continue;
						}
						if (dob) {
							rows.add(Util.removeNonNumeric(row.toString()));
							row = new StringBuffer("");
							dob = false;
						}
						
						if (row.toString().contains("AWARDED OR AUTHORIZED")) {	// look for purple heart next line
							row = new StringBuffer("");
							purpleHeart = true;
							continue;
						}
						if (purpleHeart) {
							if (row.toString().contains("PURPLE HEART")) {
								rows.add("PURPLE HEART");
							} else {
								rows.add("NONE");
							}
							row = new StringBuffer("");
							purpleHeart = false;
						}
						
						if (row.toString().contains("19a. MAILING")) {	// next line will be street address
							row = new StringBuffer("");
							address = true;
							continue;
						}
						if (address) {
							rows.add(Util.removeNonAlphaNumeric(row.toString()).trim());
							row = new StringBuffer("");
							address = false;
							csz = true;		// next line will be city state zip
							continue;
						}
						
						if (csz) {
							rows.add(Util.removeNonAlphaNumeric(row.toString()).trim());	// end of needed info parsing
							break;
						}
						
						row = new StringBuffer("");
					} else {
						row.append(Character.toString((char)item));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					pr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<String> getRows() {
		return rows;
	}

	public void setRows(List<String> rows) {
		this.rows = rows;
	}
}
