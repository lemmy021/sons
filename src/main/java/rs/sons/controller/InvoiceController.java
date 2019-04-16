package rs.sons.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rs.sons.entity.Client;
import rs.sons.entity.Invoice;
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
	public void newPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("jwtId") String jwtId) throws java.io.IOException {

		Integer documentId = JwtHelper.decodeJWT(jwtId);
		
		System.out.println(documentId);
		if(documentId == 0) {
			response.sendRedirect(request.getHeader("Referer"));
		}
		
		Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(documentId.longValue()));
		
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
		contentStream.showText("Profaktura br. 3001/2019");
		contentStream.endText();

		height -= 20;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Kupac: CREATIVEFACTORY DOO");
		contentStream.endText();

		contentStream.moveTo(41, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Adresa sedišta kupca: Turgenjeva 9");
		contentStream.endText();

		contentStream.moveTo(109, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(109, height);
		contentStream.showText("21000 Novi Sad");
		contentStream.endText();

		contentStream.moveTo(10, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Matični broj: 32450954");
		contentStream.endText();

		contentStream.moveTo(66, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("PIB: 324509540");
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

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Datum izdavanja predračuna: 06.10.1978.");
		contentStream.endText();

		contentStream.moveTo(144, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

		height -= 15;

		contentStream.beginText();
		contentStream.setFont(verdanaFont, 9);
		contentStream.newLineAtOffset(10, height);
		contentStream.showText("Datum prometa: 29.03.2019.");
		contentStream.endText();

		contentStream.moveTo(85, height - 2);
		contentStream.setLineWidth(0.3f);
		contentStream.lineTo(285, height - 2);
		contentStream.stroke();

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
		
		String ime = "DRAGON LINE DOO WWWW JJKLSKDFJ OWEI";
		
		Long addressKorekcija = (long) 0;
		
		if(ime.length() < 35) {
			contentStream.beginText();
			contentStream.setFont(verdanaFont, 12);
			contentStream.newLineAtOffset(315, postAddressHeight);
			contentStream.showText(ime);
			contentStream.endText();
		} else {
			String[] lines = WordWrap.wordWrapping(ime, 34);
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
		contentStream.showText("Bulevar Petra Petrovica Njegosa 1124");
		contentStream.endText();
		
		postAddressHeight -= 20;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 12);
		contentStream.newLineAtOffset(315, postAddressHeight);
		contentStream.showText("21000 NOVI SAD");
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
		String[] usluge = {"danas ka da postajem ko jksdfjlksfjuioruto vjkfdgjldkgj. fkjsdlkjdglk j! FSDFS flskjdfjskgdfs.  wwwwwwwwwwwww pre oi po po poopopopokjjkllk popopopopopop pofdpsofspdfodspfods pfospdfosdp pofsdpfosdpfosdp sfposdfposdp psfosdpfo pofsdpfosp pfosspd pdfospdfojflks fshdjfhsdkjfhsdkjfhsdk hfdsufhksdjfhksd", "usluga broj dvaaaaaaa"};
		String[] cena = {"100.120.000,00", "20.000,02"};
		
        for(int i = 0; i < usluge.length; i++) {
        	contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(12, height);
    		contentStream.showText(i+1 + ".");
    		contentStream.endText();
    		
    		Long korekcija = (long) 0;
    		
    		if(usluge[i].length() < 66) {
	    		contentStream.beginText();
	    		contentStream.setFont(verdanaFont, 8);
	    		contentStream.newLineAtOffset(30, height);
	    		contentStream.showText(usluge[i]);
	    		contentStream.endText();
    		} else {
    			String[] lines = WordWrap.wordWrapping(usluge[i], 60);
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
    		contentStream.newLineAtOffset(this.alignTextToCenter(375, 405, "2", verdanaFont, 8), height);
    		contentStream.showText("2");
    		contentStream.endText();
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.alignTextToCenter(405, 435, "kom", verdanaFont, 8), height);
    		contentStream.showText("kom");
    		contentStream.endText();
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		//contentStream.newLineAtOffset(this.moneyPosition("20.000,02", verdanaFont, 8, 500-1), height);
    		contentStream.newLineAtOffset(this.alignTextToCenter(435, 500, cena[i], verdanaFont, 8), height);
    		contentStream.showText(cena[i]);
    		contentStream.endText();
    		
    		contentStream.beginText();
    		contentStream.setFont(verdanaFont, 8);
    		contentStream.newLineAtOffset(this.moneyPosition("20.000,02", verdanaFont, 8, 585-1), height);
    		contentStream.showText("20.000,02");
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
		contentStream.newLineAtOffset(this.moneyPosition("40.000,04", verdanaFont, 8, 585-1), height);
		contentStream.showText("40.000,04");
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("OSNOVICA:");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(this.moneyPosition("40.000,04", verdanaFont, 8, 585-1), height);
		contentStream.showText("40.000,04");
		contentStream.endText();
		
		height -= 14;
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(12, height);
		contentStream.showText("PDV (0%):");
		contentStream.endText();
		
		contentStream.beginText();
		contentStream.setFont(verdanaFont, 8);
		contentStream.newLineAtOffset(this.moneyPosition("0,00", verdanaFont, 8, 585-1), height);
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
		contentStream.newLineAtOffset(this.moneyPosition("40.000,04", verdanaFontBold, 8, 585-1), height);
		contentStream.showText("40.000,04");
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
		contentStream.showText("Rok za plaćanje: 12.03.2019.");
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
		contentStream.showText("Predračun je validan bez potpisa i pečata");
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

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"pdfbox.pdf\"");
		OutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}
	
	private String moneyFormat(Double amount) {
		
		//double amount = 2000000;
		//System.out.println(String.format("%,.2f", amount));
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        
		return format.format(amount);
	}
	
	private float moneyPosition(String money, PDFont font, int fontSize, long rightBorder) throws IOException {
		
		float textWidth = font.getStringWidth(money) / 1000 * fontSize;
		
		return (rightBorder - textWidth);
	}
	
	private float alignTextToCenter(long leftBorder, long rightBorder, String text, PDFont font, int fontSize) throws IOException {
		
		float textWidth = font.getStringWidth(text) / 1000 * fontSize;
		
		return (leftBorder + ((rightBorder - leftBorder - textWidth) / 2));
	}
	
}
