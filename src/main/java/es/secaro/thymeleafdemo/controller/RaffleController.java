package es.secaro.thymeleafdemo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.lowagie.text.DocumentException;

import es.secaro.thymeleafdemo.dto.Raffle;
import es.secaro.thymeleafdemo.dto.TemplateManager;
import es.secaro.thymeleafdemo.service.PdfGenarator;

@Controller
public class RaffleController {

	@Autowired
	private PdfGenarator pdfGenarator;

	private String templateName = "templatePDF.html";

	private String fileName = "PDF_Demo.pdf";
	
	 private static final long serialVersionUID=1L;
	    public static Logger logger=Logger.getLogger("global");

	@GetMapping("/")
	public String raffleForm(final Model model) {
		
		File folder = new File("./src/main/resources/templates");
		TemplateManager templateManager = new TemplateManager();

		File[] listOfFiles = folder.listFiles();
		List<String> listTemplates = new ArrayList<String>();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			  listTemplates.add(listOfFiles[i].getName());
		  }
		}
		templateManager.setTemplates(listTemplates);
		model.addAttribute("templateManager", templateManager);
		return "web_pages/raffle";
	}

	@PostMapping("/download")
	public String raffleSubmit(@ModelAttribute final TemplateManager templateManager) {
		return "web_pages/result";
	}

	@GetMapping("/download/pdf")
	public ResponseEntity<ByteArrayResource> rafflePDF(@ModelAttribute final TemplateManager templateManager, final HttpServletRequest request,
			final HttpServletResponse response) throws DocumentException, IOException {
		
		String selected_template = templateManager.getTemplate();
		logger.info("generando pdf: "+selected_template);

		Map<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put("name", "HBS PDF");

		ByteArrayOutputStream byteArrayOutputStreamPDF = pdfGenarator.createPdf(selected_template, mapParameter, request, response);
		ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + selected_template.split("\\.")[0].concat(".pdf")).contentType(MediaType.APPLICATION_PDF)
				.contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);

	}
	
	@GetMapping("/raffle")
	public String raffleOld() {
		return "web_pages/welcome";
	}

	private List<String> doRaffle(final List<String> candidates) {

		Random random = new Random();
		List<String> winners = new ArrayList<String>();

		int winnerSize = new HashSet<String>(candidates).size(); // HashSet elimina duplicados
		while (winnerSize > 0) {
			String nextWinner = candidates.get(random.nextInt(candidates.size()));
			while (winners.contains(nextWinner)) {
				nextWinner = candidates.get(random.nextInt(candidates.size()));
			}
			winners.add(nextWinner);
			winnerSize--;
		}
		return winners;
	}

	private List<String> asList(final String candidates) {

		List<String> candidatesAsList = new ArrayList<String>();
		for (String line : candidates.split("\\n")) {

			String[] split = line.split(",");
			String name = split[0].trim();
			int tickets = Integer.valueOf(split[1].trim());

			while (tickets > 0) {
				candidatesAsList.add(name);
				tickets--;
			}
		}
		return candidatesAsList;
	}

}
