package com.stonekeep.congo.util;

import java.io.BufferedReader;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.io.File;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.stonekeep.congo.data.Badge;
import com.stonekeep.congo.data.BadgeRow;
import com.stonekeep.congo.data.Convention;
import com.stonekeep.congo.data.Registrant;

public class SystemBadge {

	private Logger logger = Logger.getLogger(SystemBadge.class);

	public String tmpdir = "/tmp/";
	public String imagedir = "/tmp/imagedir/";
	public String badgeIdentifier = "";
	public String printCommand="";

	public boolean preview = false;

	public Registrant registrant;
	public Convention convention;

	public Badge badge;

	public SystemBadge() throws Exception {}

	public SystemBadge(Registrant r) throws Exception {
		this.registrant = r;
	}

	public void setRegistrant(Registrant r) {
		this.registrant = r;
	}

	public void setConvention(Convention c) {
		this.convention=c;
	}

	public void printIt() throws Exception {
		logger.debug("printIt starting...");

		// Lowagie Document measurements are in points.  A CR80 badge is 3.375 x 2.125.  So that's
		// 243 points by 153 points.
		Document document = new Document(new com.lowagie.text.Rectangle(0,0,243,153),0,0,0,0);

		// we create a writer that listens to the document
		// and directs a PDF-stream to a file
		String outputFile = tmpdir + "/" + registrant.rid + ".pdf" ;
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
		document.addCreationDate();
		document.open();

		PdfContentByte cb = writer.getDirectContent();

		// Populate the badge image based on the rows in the Badge object...
		String printElement;
		String fname;

		

		logger.info("Generating " + outputFile + " using " + badge.getBadgeRows().size() + " fields.");
		for (BadgeRow br : badge.getBadgeRows() ) {
			printElement = "";
			fname = br.getFieldname();

			// Figure out what we need to print here based on the fieldname 
			// from the badge layout...
			if (fname.equalsIgnoreCase("badgename")) 	printElement = registrant.getBadgeName();
			if (fname.equalsIgnoreCase("company")) 	printElement = registrant.getCompany();


			if (fname.equalsIgnoreCase("id"))			printElement = "#" + registrant.getRid();
			if (fname.equalsIgnoreCase("fname")) 		printElement = registrant.getFirstName();
			if (fname.equalsIgnoreCase("lname")) 		printElement = registrant.getLastName();
			if (fname.equalsIgnoreCase("fnamelname")) 	printElement = registrant.getFirstName() + " " + registrant.getLastName();
			if (fname.equalsIgnoreCase("lnamefname")) 	printElement = registrant.getLastName() + ", " + registrant.getFirstName();
			if (fname.equalsIgnoreCase("regtype"))		printElement = registrant.currentState.printAs ;
			if (fname.equalsIgnoreCase("banner"))		printElement = registrant.currentState.banner ;
			if (fname.equalsIgnoreCase("fnbnln")) {
				printElement=registrant.getFirstName();
				if (registrant.getBadgeName().length() > 0) {
					printElement = printElement + " \"" + registrant.getBadgeName() + "\""; 
				}
				printElement = printElement + " " + registrant.getLastName();
			}
			
			// Badge or First Last - Prints badge if exists, else First Last
			if (fname.equalsIgnoreCase("badgeorfnln")){
				if (registrant.getBadgeName().length() > 0) {
					printElement = registrant.getBadgeName();
				} else {
					printElement = registrant.getFirstName() + " " + registrant.getLastName();
				}
			}

			// Badge or Last, First - Prints Badgename if exists, else Last, First
			if (fname.equalsIgnoreCase("badgeorlnfn")){
				if (registrant.getBadgeName().length() > 0) {
					printElement = registrant.getBadgeName();
				} else {
					printElement = registrant.getLastName() + ", " + registrant.getFirstName();
				}
			}

			// First Last if Badge - Prints First Last if Badgename exists, else blank
			if (fname.equalsIgnoreCase("fnlnifbadge")){
				if (registrant.getBadgeName().length() > 0) {
					printElement = registrant.getFirstName() + " " + registrant.getLastName();
				} else {
					printElement = "";
				}
			}
		
			// Last, First if Badge - Prints Last, First if Badgename exists, else blank
			if (fname.equalsIgnoreCase("lnfnifbadge")){
				if (registrant.getBadgeName().length() > 0) {
					printElement = registrant.getLastName() + ", " + registrant.getFirstName();
				} else {
					printElement = "";
				}
			}
			
			


			// Background graphic images (note schema doesn't support this at the moment)
			if (fname.equalsIgnoreCase("image"))	{
				logger.debug("Adding image " + br.getBackgroundImage() +" ...");
				Image img = Image.getInstance(br.getBackgroundImage());
				logger.debug("Image loaded: " + img.width() + " x " + img.height() + " pixels?");
				img.setAlignment(Image.LEFT | Image.UNDERLYING);
				cb.addImage(img, br.getWidth(), 0, 0, br.getHeight(), br.getPosx(),br.getPosy());
				continue;
			}
			
			// Slap it onto the badge.  Use the appropriate routine depending on alignment.

			logger.debug("Locating field " + fname + " which has value " + 
					printElement + " at position " + br.getPosx() + "," + br.getPosy() + ", aligned: " + br.getAlignment());
			
			BaseFont bf;
			
			// Pick a font for the element
			if (br.getFont() == null || br.getFont().isEmpty()){ // default to Helvetica
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else if (br.getFont().equalsIgnoreCase("helvetica")){
				bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else if (br.getFont().equalsIgnoreCase("courier")){
				bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else if (br.getFont().equalsIgnoreCase("times")){
				bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			} else {
				File f = new File(br.getFont());
				if(f.exists() && !f.isDirectory()){ 
					bf = BaseFont.createFont(br.getFont(), BaseFont.CP1252, BaseFont.EMBEDDED);
				} else {
					logger.info("Using the default font, as the user-specified one can not be located");
					bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				}
			}
			logger.debug("Using font: " + (bf.getFullFontName())[0][3]);
			
			Font btFont;
			Phrase blockText = null;
			
			// Pick a style for the element
			if (br.getStyle().equalsIgnoreCase("bold")){
				btFont = new Font(bf,br.getFontSize(),Font.BOLD);
				logger.debug("Using bold style");
				
			} else if (br.getStyle().equalsIgnoreCase("italic")){
				btFont = new Font(bf,br.getFontSize(),Font.ITALIC);
				logger.debug("Using italic style");
				
			} else if (br.getStyle().equalsIgnoreCase("bolditalic")){
				btFont = new Font(bf,br.getFontSize(),Font.BOLDITALIC);
				logger.debug("Using bolditalic style");
				
			} else if (br.getStyle().equalsIgnoreCase("strikethru")){
				btFont = new Font(bf,br.getFontSize(),Font.STRIKETHRU);
				logger.debug("Using strikethrough style");
				
			} else if (br.getStyle().equalsIgnoreCase("underline")){
				btFont = new Font(bf,br.getFontSize(),Font.UNDERLINE);
				logger.debug("Using underline style");
			
			} else {
				btFont = new Font(bf,br.getFontSize(),Font.NORMAL);
				logger.debug("Using default style");
			}
			
			if (fname.equalsIgnoreCase("banner")) { // Banner text is a little more showy...
				btFont = new Font(bf,br.getFontSize(),Font.BOLD);
				// this is a little ugly.
				if (printElement != null) 
					blockText = new Phrase(15,printElement + " " +
							printElement + " " +
							printElement + " " +
							printElement + " " +
							printElement,btFont);
			} else if (br.getAlignment().equalsIgnoreCase("block")) {  // For block elements, use size as specified
				blockText = new Phrase(15,printElement,btFont);
				
			} else { // for all other elements, shrink to fit them in the space available
				float maxWidth;
				if(br.getMaxWidth() == 0 || br.getMaxWidth() > 233){
					maxWidth = 233; // 5pt minimum margins
				} else {
					maxWidth = br.getMaxWidth();
				}
				do {
					blockText = new Phrase(15,printElement,btFont);
					btFont.setSize(btFont.getCalculatedSize() - (float)0.25);
				} while (ColumnText.getWidth(blockText) > maxWidth); // 12pt padding on each side, just in case
				logger.debug("Using a fontsize of " + (btFont.getCalculatedSize() + 0.25));
			}
			int alignment = 0;

			if (br.getAlignment().equalsIgnoreCase("left"))	alignment=PdfContentByte.ALIGN_LEFT;
			if (br.getAlignment().equalsIgnoreCase("center")) alignment=PdfContentByte.ALIGN_CENTER;
			if (br.getAlignment().equalsIgnoreCase("right")) alignment=PdfContentByte.ALIGN_RIGHT;
			if (br.getAlignment().equalsIgnoreCase("block")) {
				logger.debug("Printing field " + fname + " using block layouts.");
				if (preview) {
					cb.setLineDash(2, 0); 
					cb.setLineWidth(1);
					cb.rectangle(br.getPosx()-(br.getWidth() / 2),br.getPosy() - (br.getHeight() / 2), br.getWidth(),br.getHeight());
					cb.stroke();
				}
				ColumnText ct = new ColumnText(cb);
				ct.setSimpleColumn(br.getPosx()-(br.getWidth() / 2),
						br.getPosy()-(br.getHeight() / 2),
						br.getPosx()+(br.getWidth() / 2),
						br.getPosy()+(br.getHeight() / 2),
						19,
						Element.ALIGN_CENTER);
				ct.addText(blockText);
				ct.go();
			} else {
				ColumnText.showTextAligned(cb,alignment,blockText,br.getPosx(),br.getPosy(),0);
			}
		}
		document.close();

		printFile(outputFile);
	}

	/**
	 * Generate a simple badge layout
	 * @return
	 */
	public Badge generateBadge() throws Exception {
		logger.debug("generateBadge: Interpreting event badgeLayout XML to build new badge schema...");
		Badge b = new Badge();

		b.cid = convention.getConCID();
		b.setBadgeIdentifier("system");
		ArrayList<BadgeRow> rows = new ArrayList<BadgeRow>();

		// Read the event's badge layout (in XML) and build a badge object...
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(convention.getConBadgelayout()));

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				StartElement el = event.asStartElement();
				if (el.getName().getLocalPart().equalsIgnoreCase("field")) {
					BadgeRow br = new BadgeRow();
					br.setCid(b.cid);
					br.setFieldname(el.getAttributeByName(new javax.xml.namespace.QName("name")).getValue());
					br.setPosx(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("posx")).getValue()));
					br.setPosy(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("posy")).getValue()));
					br.setFontSize(10);	// an arbitrary default font size.
					br.setAlignment("center"); // A working default.
					br.setStyle("normal"); // default;

					Attribute a = el.getAttributeByName(new javax.xml.namespace.QName("fontsize"));
					if (a != null) 	br.setFontSize(Integer.parseInt(a.getValue()));

					a = el.getAttributeByName(new javax.xml.namespace.QName("width"));
					if (a != null) 	br.setWidth(Integer.parseInt(a.getValue()));

					a = el.getAttributeByName(new javax.xml.namespace.QName("height"));
					if (a != null) 	br.setHeight(Integer.parseInt(a.getValue()));

					a = el.getAttributeByName(new javax.xml.namespace.QName("align"));
					if (a != null) 	br.setAlignment(el.getAttributeByName(new javax.xml.namespace.QName("align")).getValue());
					
					a = el.getAttributeByName(new javax.xml.namespace.QName("style"));
					if (a != null) br.setStyle(el.getAttributeByName(new javax.xml.namespace.QName("style")).getValue());
					
					a = el.getAttributeByName(new javax.xml.namespace.QName("font"));
					if (a != null) br.setFont(el.getAttributeByName(new javax.xml.namespace.QName("font")).getValue());
					
					a = el.getAttributeByName(new javax.xml.namespace.QName("fontencoding"));
					if (a != null) br.setFontEncoding(el.getAttributeByName(new javax.xml.namespace.QName("fontencoding")).getValue());
					
					a = el.getAttributeByName(new javax.xml.namespace.QName("maxwidth"));
					if (a != null) br.setMaxWidth(Float.parseFloat(a.getValue()));


					logger.debug("Parsed field '" + br.getFieldname() + "' to position " + br.getPosx() + "," + br.getPosy());
					rows.add(br);
				}
				
				if (el.getName().getLocalPart().equalsIgnoreCase("image")) {
					BadgeRow br = new BadgeRow();
					br.setCid(b.cid);
					br.setFieldname("image");
					br.setBackgroundImage(el.getAttributeByName(new javax.xml.namespace.QName("filename")).getValue());
					br.setPosx(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("posx")).getValue()));
					br.setPosy(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("posy")).getValue()));
					br.setWidth(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("width")).getValue()));
					br.setHeight(Integer.parseInt(el.getAttributeByName(new javax.xml.namespace.QName("height")).getValue()));
					logger.debug("Parsed '" + br.getFieldname() + "' from " + br.getBackgroundImage() + " to position " + br.getPosx() + "," + br.getPosy());
					rows.add(br);
				}

			}
		}
		b.setBadgeRows(rows);
		return b;
	}

	void printFile(String fileName) throws Exception {
		// Display the print dialog with default printer selected

		logger.info("Printing badge via " + this.printCommand + " " + fileName);
        long lBefore = System.currentTimeMillis();  
		Process p = Runtime.getRuntime().exec(this.printCommand + " " + fileName);
        InputStream stderr = p.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ( (line = br.readLine()) != null)
        	logger.error(line);
        int exitVal = p.waitFor();  
        long lElapsed = System.currentTimeMillis() - lBefore;  
        logger.info("Print command exited with value " + exitVal + " after " + lElapsed + " milliseconds.");
        
        if (exitVal > 0) {
        	throw new Exception("Error printing badge - command returned value " + exitVal);
        }

	}

}
