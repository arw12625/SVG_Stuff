/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svg;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Andy
 */
public class ElemFactory {
    
    static SVG svg;
    
    public static void setSVG(SVG s) {
        svg = s;
    }
    
    protected static Elem cloneElem(Elem e, boolean deep) {
        Element clonedInner = (Element) e.getElement().cloneNode(deep);
        Elem clonedWrapper = makeElem();
        clonedWrapper.setElement(clonedInner);
        return clonedWrapper;
    }

    public static Elem makeElem() {
        return new Elem();
    }
    
    public static Elem makeElem(String name) {
        Elem e = new Elem();
        e.setElement(svg.document.createElement(name));
        return e;
    }
    
}
