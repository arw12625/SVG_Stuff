/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svg;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Andy
 */
public class SVG {
    protected Document document;
    protected ElemFactory elemFac;
    protected Elem root;

    public SVG() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            document = docBuilder.newDocument();
            ElemFactory.setSVG(this);
            
            root = elemFac.makeElem("svg")
                    .attr("xmlns", "http://www.w3.org/2000/svg")
                    .attr("version", "1.1");
            
            document.appendChild(root.getElement());
                    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Elem getRoot() {
        return root;
    }
    
    public void write(String path) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File(path));

                // Output to console for testing
                // StreamResult result = new StreamResult(System.out);

                transformer.transform(source, result);
                System.out.println("File saved!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
