package es.secaro.thymeleafdemo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

@Service
public class PdfGenarator {

	private static final Logger logger = LoggerFactory.getLogger(PdfGenarator.class);

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ApplicationContext context;

	@Autowired
	ServletContext servletContext;

	String urlBase = "http://localhost:8080";

	public ByteArrayOutputStream createPdf(final String templateName, final Map map, final HttpServletRequest request, final HttpServletResponse response)
			throws DocumentException, IOException {

		logger.debug("Generando informe pdf");

		Assert.notNull(templateName, "The templateName can not be null");

		IWebContext ctx = new SpringWebContext(request, response, servletContext, LocaleContextHolder.getLocale(), map, context);

		String processedHtml = templateEngine.process(templateName, ctx);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(processedHtml, urlBase);
			// lato
			renderer.getFontResolver().addFont("public/fonts/Lato-Bold.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/Lato-Light.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/Lato-Regular.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/Lato-Semibold.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			// headline
			renderer.getFontResolver().addFont("public/fonts/headline/SantanderHeadlineW05-Bold.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/headline/SantanderHeadlineW05-BoldIt.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/headline/SantanderHeadlineW05-Italic.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/headline/SantanderHeadlineW05-Rg.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			// text
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-Bold.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-BoldItalic.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-Italic.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-Light.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-LightIt.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.getFontResolver().addFont("public/fonts/text/SantanderTextW05-Regular.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
			renderer.layout();
			renderer.createPDF(bos, false);
			renderer.finishPDF();
			logger.info("PDF created correctamente");

		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("Error creando pdf", e);
				}
			}
		}
		return bos;
	}
}
