package poc.ITextDemo;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.itextpdf.text.pdf.AcroFields;

public class XMLCreator {
	
		/**
		 * 
		 * @param form
		 * @param outputFile
		 * @throws Exception
		 */
	 	public static void createXMLForPDFFields(AcroFields form, String outputFile) throws Exception{
	    	
	    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = builder.newDocument();
			
			// create the root element node
			Element element = doc.createElement("nameSpaceData");
			Element fieldsElement = doc.createElement("fields");
			for(String fieldName: form.getFields().keySet()){
				Element fieldElement = doc.createElement("field");
				
				Element nameElement = doc.createElement("name");
				nameElement.setTextContent(fieldName);
				fieldElement.appendChild(nameElement);
				
				Element valueElement = doc.createElement("value");
				valueElement.setTextContent("yes");
				fieldElement.appendChild(valueElement);
				
				fieldsElement.appendChild(fieldElement);
				
			}
			element.appendChild(fieldsElement);
			
			doc.appendChild(element);
			prettyPrint(doc.getDocumentElement(), outputFile);
	    }
	    
	 	/**
	 	 * 
	 	 * @param xml
	 	 * @param outputFile
	 	 * @throws Exception
	 	 */
	    public static  void prettyPrint(Node xml, String outputFile) throws Exception {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			Writer out = new StringWriter();
			tf.transform(new DOMSource(xml), new StreamResult(new File(outputFile)));
			System.out.println(out.toString());
		}

}
