package rs.sons.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import rs.sons.entity.Client;
import rs.sons.entity.Invoice;
import rs.sons.entity.InvoiceItem;
import rs.sons.helper.MyDateFormatter;
import rs.sons.helper.WordWrap;
import rs.sons.jwt.JwtHelper;
import rs.sons.service.ClientService;
import rs.sons.service.InvoiceService;

@Controller
public class InvoiceController {
	
	@Autowired
	ClientService clientService;

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
    public JavaMailSender emailSender;

	@GetMapping("/invoices/{jwtId:.+}")
	public String showAllInvoicesForUser(Model model, @PathVariable("jwtId") String jwtId) {

		Integer clientId = JwtHelper.decodeJWT(jwtId);

		if (clientId > 0) {
			Client clientDb = clientService.findClientById(clientId);

			if (clientDb != null) {

				List<Invoice> invoices = invoiceService.getAllInvoicesForClient(clientId);

				// invoice.setClient(clientDb);
				// invoice.setInvoice_client_id_jwt_helper(jwtId);
				model.addAttribute("client", clientDb);
				model.addAttribute("invoices", invoices);
			}
		}

		return "invoicesforuser";
	}
	
	@GetMapping("/invoicepdf/{jwtId:.+}")
	public void createInvoicePdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("jwtId") String jwtId) throws IOException, MessagingException {
		Integer documentId = JwtHelper.decodeJWT(jwtId);
		
		if(documentId == 0) {
			response.sendRedirect(request.getHeader("Referer"));
		}
		
		Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(documentId.longValue()));
		
		if(invoice == null) {
			response.sendRedirect(request.getHeader("Referer"));
		}
		
		ByteArrayOutputStream baos = _newPdf(request, invoice);
		
		//odavde
		final InputStreamSource attachment = new ByteArrayResource(baos.toByteArray());
		
		MimeMessage message = emailSender.createMimeMessage();
	      
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    
	    helper.setTo("milekosovac@yahoo.com");
	    helper.setSubject("Test subject");
	    helper.setText("Konju jedan");
	    helper.addAttachment("document.pdf", attachment);
	    
	    emailSender.send(message);
		//dovde
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"pdfbox.pdf\"");
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
		
	}
	
	@PostMapping("/sendinvoice")
	@ResponseBody
	public String sendInvoicePdfOnEmail(HttpServletRequest request, @RequestParam("jwt_invoice_id") String jwtInvoiceId) throws IOException, MessagingException {
		Integer invoiceId = JwtHelper.decodeJWT(jwtInvoiceId);
		
		if(invoiceId > 0) {
			Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(invoiceId.longValue()));
			
			if(invoice != null) {
				ByteArrayOutputStream baos = _newPdf(request, invoice);
				
				final InputStreamSource attachment = new ByteArrayResource(baos.toByteArray());
				
				MimeMessage message = emailSender.createMimeMessage();
			      
			    MimeMessageHelper helper = new MimeMessageHelper(message, true);
			    
			    helper.setTo("milekosovac@yahoo.com");
			    helper.setSubject("Test subject");
			    helper.setText("Konju jedan");
			    helper.addAttachment("document.pdf", attachment);
			    
			    emailSender.send(message);
			    
			    return "1";
			}
		}
		
		return "";
	}

	private ByteArrayOutputStream _newPdf(HttpServletRequest request, Invoice invoice) throws java.io.IOException, MessagingException {
		
		//Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(documentId.longValue()));
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);

		String netbridgeLogoPath = request.getServletContext().getRealPath("/") + "/resources/images/netbridge.jpg";
		PDImageXObject netbridgeLogoImage = PDImageXObject.createFromFile(netbridgeLogoPath, document);

		String netbridgeWatermarkPath = request.getServletContext().getRealPath("/") + "/resources/images/netbridge-watermark.jpg";
		PDImageXObject netbridgeWatermarkImage = PDImageXObject.createFromFile(netbridgeWatermarkPath, document);

		File verdanaFile = new File(request.getServletContext().getRealPath("/") + "/resources/font/verdana.ttf");
		File verdanaBoldFile = new File(request.getServletContext().getRealPath("/") + "/resources/font/verdanab.ttf");

		// Create a new font object selecting one of the PDF base fonts
		PDFont verdanaFont = PDType0Font.load(document, verdanaFile);
		PDFont verdanaFontBold = PDType0Font.load(document, verdanaBoldFile);

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		//contentStream.drawImage(netbridgeLogoImage, 0, 650, 611, 122);
		contentStream.drawImage(netbridgeLogoImage, 0, 700, 611, 122);
		contentStream.drawImage(netbridgeWatermarkImage, 0, 110, 630, 380);

		//Long height = (long) 630;
		Long height = (long) 680;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 16);
		contentStream.newLineAtOffset(10, height);
		if(invoice.isInvoice_is_invoice()) {
			contentStream.showText("Račun br. " + this.getDocumentNumber(invoice.getInvoice_year(), invoice.getInvoice_month(), invoice.getInvoice_number()));
		} else {
			contentStream.showText("Predračun br. " + this.getDocumentNumber(invoice.getInvoice_preinvoice_year(), invoice.getInvoice_preinvoice_month(), invoice.getInvoice_preinvoice_number()));
		}
		contentStream.endText();

		height -= 20;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Kupac: " + invoice.getInvoice_client_data().split(";")[0]);
		contentStream.endText();

		contentStream.moveTo(41, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Adresa sedišta kupca: " + invoice.getInvoice_client_data().split(";")[1]);
		contentStream.endText();

		contentStream.moveTo(109, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(109, height);
		contentStream.showText(invoice.getInvoice_client_data().split(";")[2]); //city
		contentStream.endText();

		contentStream.moveTo(10, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		if(invoice.getClient().isClient_type()) {
			contentStream.showText("Matični broj: " + invoice.getInvoice_client_data().split(";")[5]);
		} else {
			contentStream.showText("Matični broj: 0");
		}
		contentStream.endText();

		contentStream.moveTo(66, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		if(invoice.getClient().isClient_type()) {
			contentStream.showText("PIB: " + invoice.getInvoice_client_data().split(";")[4]);
		} else {
			contentStream.showText("PIB: 0");
		}
		contentStream.endText();

		contentStream.moveTo(29, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Mesto izdavanja predračuna: Novi Sad");
		contentStream.endText();

		contentStream.moveTo(140, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		if(!invoice.isInvoice_is_invoice()) {
			contentStream.beginText();
			contentStream.setFont(verdanaFont, 9);
			contentStream.newLineAtOffset(10, height);
			contentStream.showText("Datum izdavanja predračuna: " + MyDateFormatter.formatDate(invoice.getInvoice_creation_date()));
			contentStream.endText();
	
			contentStream.moveTo(144, height - 2);
			contentStream.setLineWidth(0.3f);
			contentStream.lineTo(285, height - 2);
			contentStream.stroke();
		} else {
			contentStream.beginText();
			contentStream.setFont(verdanaFont, 9);
			contentStream.newLineAtOffset(10, height);
			contentStream.showText("Datum izdavanja računa: " + MyDateFormatter.formatDate(invoice.getInvoice_payment_date_for_print()));
			contentStream.endText();
	
			contentStream.moveTo(124, height - 2);
			contentStream.setLineWidth(0.3f);
			contentStream.lineTo(285, height - 2);
			contentStream.stroke();
		
			height -= 15;
	
			contentStream.beginText();
			contentStream.setFont(verdanaFont, 9);
			contentStream.newLineAtOffset(10, height);
			contentStream.showText("Datum prometa: " + MyDateFormatter.formatDate(invoice.getInvoice_delivery_date()));
			contentStream.endText();
	
			contentStream.moveTo(85, height - 2);
			contentStream.setLineWidth(0.3f);
			contentStream.lineTo(285, height - 2);
			contentStream.stroke();
		}
		
		// ADDRESS WINDOW
		// top left corner
		contentStream.moveTo(305, 690);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(320, 690);
		contentStream.stroke();

		contentStream.moveTo(305, 690);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(305, 675);
		contentStream.stroke();

		// top right corner
		contentStream.moveTo(570, 690);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, 690);
		contentStream.stroke();

		contentStream.moveTo(585, 690);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, 675);
		contentStream.stroke();

		// bottom left corner
		contentStream.moveTo(305, 550);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(320, 550);
		contentStream.stroke();

		contentStream.moveTo(305, 550);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(305, 565);
		contentStream.stroke();

		// bottom right corner
		contentStream.moveTo(585, 550);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(570, 550);
		contentStream.stroke();

		contentStream.moveTo(585, 550);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, 565);
		contentStream.stroke();
		
		
		/*
		 * contentStream.beginText(); contentStream.setFont(verdanaFont, 8);
		 * contentStream.newLineAtOffset(moneyPosition("200.000,00", verdanaFont, 8,
		 * 584), 350); contentStream.showText("200.000,00"); contentStream.endText();
		 * 
		 * contentStream.beginText(); contentStream.setFont(verdanaFont, 8);
		 * contentStream.newLineAtOffset(moneyPosition("200.000.000,00", verdanaFont, 8,
		 * 584), 335); contentStream.showText("200.000.000,00");
		 * contentStream.endText();
		 */
		 
		
		
		//POST ADDRESS
		Long postAddressHeight = (long) 675;
		
		Long addressKorekcija = (long) 0;
		
		if(invoice.getInvoice_client_data().split(";")[0].length() < 35) {
			contentStream.beginText();
			contentStream.setFont(verdanaFont, 12);
			contentStream.newLineAtOffset(315, postAddressHeight);
			contentStream.showText(invoice.getInvoice_client_data().split(";")[0]);
			contentStream.endText();
		} else {
			String[] lines = WordWrap.wordWrapping(invoice.getInvoice_client_data().split(";")[0], 34);
			addressKorekcija = (long) ((lines.length - 1) * 20);
			
			for(int j = 0; j < lines.length; j++) {
				contentStream.beginText();
				contentStream.setFont(verdanaFont, 12);
				contentStream.newLineAtOffset(315, postAddressHeight - j * 20);
				contentStream.showText(lines[j]);
				contentStream.endText();
			}
		}
		
		postAddressHeight -= (addressKorekcija + 20);
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 12);
		contentStream.newLineAtOffset(315, postAddressHeight);
		contentStream.showText(invoice.getInvoice_client_data().split(";")[1]);
		contentStream.endText();
		
		postAddressHeight -= 20;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 12);
		contentStream.newLineAtOffset(315, postAddressHeight);
		contentStream.showText(invoice.getInvoice_client_data().split(";")[2]);
		contentStream.endText();
		
		postAddressHeight -= 20;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 12);
		contentStream.newLineAtOffset(315, postAddressHeight);
		contentStream.showText("Srbija");
		contentStream.endText();
		
		postAddressHeight -= 20;
		
		//items table main frame
		//top
		contentStream.moveTo(10, 525);
		contentStream.setLineWidth(1f);
		contentStream.lineTo(585, 525);
		contentStream.stroke();
		
		//bottom
		contentStream.moveTo(10, 80);
		contentStream.setLineWidth(1f);
		contentStream.lineTo(585, 80);
		contentStream.stroke();
		
		//left
		contentStream.moveTo(10, 525);
		contentStream.setLineWidth(1f);
		contentStream.lineTo(10, 80);
		contentStream.stroke();
		
		//right
		contentStream.moveTo(585, 525);
		contentStream.setLineWidth(1f);
		contentStream.lineTo(585, 80);
		contentStream.stroke();
		
		//right margins
		//rb
		contentStream.moveTo(28, 525);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(28, 140);
		contentStream.stroke();
		
		//naziv usluge
		contentStream.moveTo(375, 525);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(375, 140);
		contentStream.stroke();
		
		//kol
		contentStream.moveTo(405, 525);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(405, 140);
		contentStream.stroke();
		
		//jed mere
		contentStream.moveTo(435, 525);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(435, 140);
		contentStream.stroke();
		
		//jedinicna cena
		contentStream.moveTo(500, 525);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(500, 140);
		contentStream.stroke();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(12, 505);
		contentStream.showText("Rb");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(this.alignTextToCenter(40, 370, "Naziv(opis usluge)", verdanaFontBold, 8), 505);
		contentStream.showText("Naziv(opis usluge)");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(380, 505);
		contentStream.showText("Kol.");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(410, 512);
		contentStream.showText("Jed.");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(408, 502);
		contentStream.showText("mere");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(445, 512);
		contentStream.showText("Jedinična");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(456, 502);
		contentStream.showText("cena");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(527, 505);
		contentStream.showText("Ukupno");
		contentStream.endText();
		
		//underlining table header fields
		contentStream.moveTo(10, 500);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, 500);
		contentStream.stroke();
		
		height = (long) 487;
		
		int i = 0;
		BigDecimal totalSum = new BigDecimal(0);
		
		for(InvoiceItem item : invoice.getInvoiceItems()) {
        	contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(12, height);
    		contentStream.showText(i+1 + ".");
    		contentStream.endText();
    		
    		Long korekcija = (long) 0;
    		
    		if(item.getInvoice_item_description().length() < 66) {
    		//if(usluge[i].length() < 66) {
	    		contentStream.beginText();
	    		contentStream.setFont(verdanaFont, 8);
	    		contentStream.newLineAtOffset(30, height);
	    		contentStream.showText(item.getInvoice_item_description());
	    		contentStream.endText();
    		} else {
    			String[] lines = WordWrap.wordWrapping(item.getInvoice_item_description(), 60);
    			korekcija = (long) ((lines.length - 1) * 14);
    			
    			for(int j = 0; j < lines.length; j++) {
	    			contentStream.beginText();
		    		contentStream.setFont(verdanaFont, 8);
		    		contentStream.newLineAtOffset(30, height - (j * 14));
		    		contentStream.showText(lines[j]);
		    		contentStream.endText();
    			}
    		}
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.alignTextToCenter(375, 405, String.valueOf(item.getInvoice_item_amount()), verdanaFont, 8), height); //amount
    		contentStream.showText(String.valueOf(item.getInvoice_item_amount()));
    		contentStream.endText();
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.alignTextToCenter(405, 435, item.getInvoice_item_unit(), verdanaFont, 8), height); //unit
    		contentStream.showText(item.getInvoice_item_unit());
    		contentStream.endText();
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.alignTextToCenter(435, 500, this.moneyFormat(item.getInvoice_item_price()), verdanaFont, 8), height); //unit price
    		contentStream.showText(this.moneyFormat(item.getInvoice_item_price()));
    		contentStream.endText();
    		
    		BigDecimal finalItemPrice = this.getTotalPricePerItem(item.getInvoice_item_price(), item.getInvoice_item_discount(), item.getInvoice_item_amount());
    		totalSum = totalSum.add(finalItemPrice);
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.moneyPosition(String.valueOf(this.moneyFormat(finalItemPrice.doubleValue())), verdanaFont, 8, 585-1), height); //total price
    		contentStream.showText(this.moneyFormat(finalItemPrice.doubleValue()));
    		contentStream.endText();
    		
    		contentStream.moveTo(10, height - korekcija - 2);
    		contentStream.setLineWidth(0.3f);
    		contentStream.lineTo(585, height - korekcija - 2);
    		contentStream.stroke();
    		
    		height -= (korekcija +14);
        }
        
        //reset height
        height = (long) 140;
        
        //table footer
		contentStream.moveTo(10, height);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, height);
		contentStream.stroke();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("UKUPNO:");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(this.moneyPosition(String.valueOf(this.moneyFormat(totalSum.doubleValue())), verdanaFont, 8, 585-1), height);
		contentStream.showText(String.valueOf(this.moneyFormat(totalSum.doubleValue())));
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("OSNOVICA:");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(this.moneyPosition(String.valueOf(this.moneyFormat(totalSum.doubleValue())), verdanaFont, 8, 585-1), height);
		contentStream.showText(String.valueOf(this.moneyFormat(totalSum.doubleValue())));
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("PDV (0%):");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(this.moneyPosition("0,00", verdanaFont, 8, 585-4), height);
		contentStream.showText("0,00");
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("UKUPNO ZA NAPLATU PO RAČUNU:");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(this.moneyPosition(String.valueOf(this.moneyFormat(totalSum.doubleValue())), verdanaFontBold, 8, 585-1), height);
		contentStream.showText(String.valueOf(this.moneyFormat(totalSum.doubleValue())));
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("Napomena o poreskom oslobođenju - SONS.CTD nije obveznik PDV-a");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(419, height);
		contentStream.showText("Rok za plaćanje: " + MyDateFormatter.formatDate(invoice.getInvoice_payment_deadline()));
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFontBold, 8);
		contentStream.newLineAtOffset(419, height);
		contentStream.showText("Banka Intesa: 160-328098-41");
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(419, height);
		if(invoice.isInvoice_is_invoice()) {
			contentStream.showText("Račun je validan bez potpisa i pečata");
		} else {
			contentStream.showText("Predračun je validan bez potpisa i pečata");
		}
		contentStream.endText();
		
		//footer
		contentStream.moveTo(10, 20);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(585, 20);
		contentStream.stroke();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(this.alignTextToCenter(10, 585, "SONS.CTD - Matični broj: 20584254; PIB: 106357446; Šifra delatnosti: 4321", verdanaFont, 9), 10);
		contentStream.showText("SONS.CTD - Matični broj: 20584254; PIB: 106357446; Šifra delatnosti: 4321");
		contentStream.endText();

		//wordwrap http://www.avajava.com/tutorials/lessons/how-do-i-wrap-words-in-a-string-at-a-particular-width.html
		//alignment https://stackoverflow.com/questions/24004539/right-alignment-text-in-pdfbox
		//money format https://www.concretepage.com/java/java-bigdecimal-tutorial-with-example
		
		// Make sure that the content stream is closed:
		contentStream.close();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		document.save(baos);
		document.close();
		
		/*
		final InputStreamSource attachment = new ByteArrayResource(baos.toByteArray());
		
		MimeMessage message = emailSender.createMimeMessage();
	      
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    
	    helper.setTo("milekosovac@yahoo.com");
	    helper.setSubject("Test subject");
	    helper.setText("Konju jedan");
	    helper.addAttachment("document.pdf", attachment);
	    
	    emailSender.send(message);
		*/

	    return baos;
		/*
		 * response.setContentType("application/pdf");
		 * response.setHeader("Content-Disposition",
		 * "attachment; filename=\"pdfbox.pdf\""); OutputStream os =
		 * response.getOutputStream(); baos.writeTo(os); os.flush(); os.close();
		 */
	}
	
	private String moneyFormat(Double amount) {
		
		//double amount = 2000000;
		//System.out.println(String.format("%,.2f", amount));
		//NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
		
		//return format.format(amount);
		
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMANY);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setCurrencySymbol(""); // Don't use null.
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(amount);
	}
	
	private float moneyPosition(String money, PDFont font, int fontSize, long rightBorder) throws IOException {
		
		float textWidth = font.getStringWidth(money) / 1000 * fontSize;
		
		return (rightBorder - textWidth);
	}
	
	private float alignTextToCenter(long leftBorder, long rightBorder, String text, PDFont font, int fontSize) throws IOException {
		
		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		
		return (leftBorder + ((rightBorder - leftBorder - textWidth) / 2));
	}
	
	private String getDocumentNumber(int year, int month, int number) {
		
		/**
		 * format 
		 * month + number / year 
		 * number length = 3 i.e. 3001/2019
		 */
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(month);
		sb.append(String.format("%03d", number));
		sb.append("/");
		sb.append(year);
		
		return sb.toString();
	}
	
	private BigDecimal getTotalPricePerItem(double price, double discount, double amount) {
		
		BigDecimal bdPrice = new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal bdDiscount = new BigDecimal(discount).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal bdAmount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal bd100 = new BigDecimal(100);
		
		BigDecimal sumPerItem = new BigDecimal(0);
		
		if(amount > 0) {
			sumPerItem = bdPrice.multiply(bdAmount);
		} else {
			sumPerItem = bdPrice;
		}
		
		if(discount > 0) {
			sumPerItem = sumPerItem.multiply((bd100.subtract(bdDiscount)).divide(bd100));
		}
		
		return sumPerItem.setScale(2, RoundingMode.HALF_EVEN);
	}
	
}
