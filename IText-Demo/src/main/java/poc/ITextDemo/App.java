package poc.ITextDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * This APP demos 
 * 1) PDF form filling through IText
 * 2) Bundle Filled PDF as single PDF
 * @author i696921
 *
 */
public class App 
{
	public static final String FORM_1 			= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\aif.pdf";
	public static final String FORM_1_XML 		= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\aif.xml";
	public static final String FORM_1_RESULT 	= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\aif_filled.pdf";
	
	public static final String FORM_2 			= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\control-file.pdf";
	public static final String FORM_2_XML 		= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\control-file.xml";
	public static final String FORM_2_RESULT 	= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\control-file_filled.pdf";
	
	public static final String FORM_BUNDLE 		= "C:\\srinivas\\DEEP\\POC\\PDF-Form -Filling\\IText-POC\\bundle.pdf";
	
    
	public static void main( String[] args ) throws Exception{
		
		//Generating XML file for PDF Fields of FORM 1
		//generateXMLMappingForPDF(FORM_1, FORM_1_XML);
		
		//Generating XML File for PDF Fields of FOrm 2
		//generateXMLMappingForPDF(FORM_2, FORM_2_XML);
		
		//Generate PDF Filled for Form 1		
		fillPdfFields(FORM_1, FORM_1_XML, FORM_1_RESULT);
		
		//Gnerate PDF Filled for Form 2
		fillPdfFields(FORM_2, FORM_2_XML, FORM_2_RESULT);
		
		//Create PDF bundle 
    	mergePDF(FORM_1_RESULT, FORM_2_RESULT, FORM_BUNDLE);
    }
    
	/**
	 * 
	 * @param pdfFile
	 * @param xmlFile
	 * @throws Exception
	 */
	private static void generateXMLMappingForPDF(String pdfFile, String xmlFile) throws Exception{
		 
		PdfReader reader = new PdfReader(pdfFile);
		AcroFields form = reader.getAcroFields();
		XMLCreator.createXMLForPDFFields(form, xmlFile);
	}
    /**
     * Iterante over the fields and Pre Fill the Info
     *   
     * @param src
     * @param dest
     * @param list
     * @throws Exception 
     */
    public static void fillPdfFields(String srcPDFFile ,String srcXMLMAppingFile, String destPDFFile) throws Exception {
    	
    	SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser = parserFactor.newSAXParser();
        StaxHandler handler = new StaxHandler();
        InputStream in = new FileInputStream(new File(srcXMLMAppingFile));
        parser.parse(in ,handler);
        List<FormFields> list = handler.getFormFieldList();
    	File file = new File(destPDFFile);
        file.getParentFile().mkdirs();
        
        PdfReader reader = new PdfReader(srcPDFFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destPDFFile));
        AcroFields form = stamper.getAcroFields();
        if(list != null && !list.isEmpty()){
        	for(FormFields formField: list){
        		form.setField(formField.getName(), formField.getValue());
        	}
        }
        stamper.setFormFlattening(true);
        stamper.close();
        
    }
    
    /**
     * 
     * @param srcPdfFile1
     * @param srcPdfFile2
     * @param resultPdf
     * @throws DocumentException
     * @throws IOException
     */
    private static void mergePDF(String srcPdfFile1, String srcPdfFile2, String resultPdf) throws DocumentException, IOException {
    	 
    	List<File> list = new ArrayList<File>();
         // Source pdfs
         list.add(new File(srcPdfFile1));
         list.add(new File(srcPdfFile2));

         // Resulting pdf
         File out = new File(resultPdf);

         concatenatePdfs(list, out);
    }
    
    /**
     * 
     * @param listOfPdfFiles
     * @param outputFile
     * @throws DocumentException
     * @throws IOException
     */
    public static void concatenatePdfs(List<File> listOfPdfFiles, File outputFile) throws DocumentException, IOException {
        
    	Document document = new Document();
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        PdfCopy copy = new PdfSmartCopy(document, outputStream);
        document.open();
        for (File inFile : listOfPdfFiles) {
            PdfReader reader = new PdfReader(inFile.getAbsolutePath());
            copy.addDocument(reader);
            
            reader.close();
        }
        document.close();
    }
    
  
}
