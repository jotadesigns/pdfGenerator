package es.secaro.thymeleafdemo.dto;

import java.util.List;

public class TemplateManager {

	public String template;
	private List<String> templates;

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public List<String> getTemplates() {
		return templates;
	}

	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}

}
