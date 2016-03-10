package poc.ITextDemo;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StaxHandler extends DefaultHandler {

	List<FormFields> formFieldList = new ArrayList<FormFields>();

	FormFields formField = null;
	String content = null;

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new Employee object when the start tag is found
		case "field":
			formField = new FormFields();
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch (qName) {
		// Add the employee to list once end tag is found
		case "field":
			formFieldList.add(formField);
			break;
		// For all other end tags the employee has to be updated.
		case "name":
			formField.setName(content);
			break;
		case "value":
			formField.setValue(content);
			break;

		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}

	public List<FormFields> getFormFieldList() {
		return formFieldList;
	}

	public void setFormFieldList(List<FormFields> formFieldList) {
		this.formFieldList = formFieldList;
	}

}
