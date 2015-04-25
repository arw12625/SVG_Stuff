/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svg;

import org.w3c.dom.Element;
import org.w3c.dom.Attr;

/**
 *
 * @author Andy
 */
public class Elem {
    
    Element e;
    
    protected Elem() {
        
    }
    
    public Elem append(String name) {
        Elem child = ElemFactory.makeElem(name);
        e.appendChild(child.getElement());
        return child;
    }
    
    public Elem attr(String name, String value) {
        e.setAttribute(name, value);
        return this;
    }

    public Elem text(String value) {
        e.setTextContent(value);
        return this;
    }

    protected Element getElement() {
        return e;
    }

    protected void setElement(Element e) {
        this.e = e;
    }
    
    
    
    
}
