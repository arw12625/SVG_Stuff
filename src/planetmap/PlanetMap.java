/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package planetmap;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PlanetMap {
    
    static Document doc;
    static String[] planets = {"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};
    //static double[] orbitalRadii = {5.7900, 1.0750 * 10, 1.5 * 10, 2.2790 * 10, 7.7860 * 10, 1.4335 * 100, 2.8725 * 100, 4.4951 * 100};
    static double[] orbitalRadiiTest = {150, 200, 250, 300, 400, 500, 600, 700};
    static double[] phases = {-251.75, -55.38, 0, 44.33, 97.16, 106.14, 111.32, 113.16};
    static double[] ejections = {176.56,178.13,0,178.53,157.98,159.62,164.48,163.46};//{121.22,156.12,0,152.27,116.12,111.33,108.81,107.96};
    //static int[][] colors = {{100, 90, 70}, {220, 200, 100}, {180, 40, 40}, {180, 150, 100}, {180, 150, 100}, {190, 100, 190}, {70, 100, 190}};
    static String[] colors = {"a4620c", "fed312", "00adee", "f06a4d", "db8828", "e4ca1f", "7accc8", "0054a5"};
    static double[] planetRadii = {8, 20, 18, 16, 32, 28, 26, 28};
    static double[] deltav = {-1.71,-.532,0,.559,1.4,1.57,1.59,1.57};    
    
    
    
    
    public static void main(String argv[]) {
        
        try {
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            doc = docBuilder.newDocument();
            //<svg xmlns="http://www.w3.org/2000/svg" version="1.1">
            Element svg = makeEle("svg");
            svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            svg.setAttribute("version", "1.1");
            svg.setAttribute("width", ""+2000);
            svg.setAttribute("height", ""+1200);
            doc.appendChild(svg);
            
            Element background = makeRectangle(0, 0, 2000, 1200, 40, 40, 40);
            svg.appendChild(background);
            
            Element starWrap = makeEle("g");
            svg.appendChild(starWrap);
            for (int i = 0; i < 1000; i++) {
                double x = 2000 * Math.random();
                double y = 1200 * Math.random();
                Element star = makeCircle(3 * Math.random());
                star.setAttributeNode(translate(x, y));
                star.setAttributeNode(makeColor(238, 217, 190));
                starWrap.appendChild(star);
            }
            
            
            Element sunOrigin = makeEle("g");
            sunOrigin.setAttributeNode(translate(800, 600));
            svg.appendChild(sunOrigin);
            
            Element sun = makePlanet(0, 0, 100, 240, 200, 90);
            sunOrigin.appendChild(sun);
            
            int ticks = 16;
            for (int i = 0; i < ticks; i++) {
                double angle = 360f * i / ticks;
                Element tick = makeRectangle(0, -2.5, 20, 5, 240, 190, 30);
                tick.setAttributeNode(rotTrans(100, 0, angle, 0, 0));
                sun.appendChild(tick);
            }
            
            Element[] planetElements = new Element[planets.length];
            
            for (int i = 0; i < planets.length; i++) {
                
                Element wrappattack = makeEle("g");
                Element orbit = makeOrbit(mapOrbit(orbitalRadiiTest[i]), 8, colors[i]);
                sun.appendChild(wrappattack);
                wrappattack.appendChild(orbit);
                
                planetElements[i] = makePlanet(mapOrbit(orbitalRadiiTest[i]), phases[i], planetRadii[i], colors[i]);
                
                wrappattack.appendChild(planetElements[i]);
            }
            int earthIndex = 2;
            double earthx = mapOrbit(orbitalRadiiTest[earthIndex]);
            double earthy = 0;
            
            for (int i = 0; i < planets.length; i++) {
                
                Element wrappattack = makeEle("g");
                
                if (i != earthIndex) {
                    double orbitRadius = mapOrbit(orbitalRadiiTest[i]);
                    double destx = orbitRadius * Math.cos(Math.toRadians(-phases[i]));
                    double desty = orbitRadius * Math.sin(Math.toRadians(-phases[i]));
                    double c1 = 200;
                    double c2 =orbitRadius*orbitRadius/500 + 50;
                    //ejections[i] = 0;
                    double c1x = c1 * Math.cos(Math.toRadians(-ejections[i] - 90)) + earthx;
                    double c1y = c1 * Math.sin(Math.toRadians(-ejections[i] - 90)) + earthy;
                    double c2x = c2 * Math.cos(Math.toRadians(-phases[i] + 90)) + destx;
                    double c2y = c2 * Math.sin(Math.toRadians(-phases[i] + 90)) + desty;
                    Element arc = makeEle("path");
                    arc.setAttribute("d", "M " + earthx + " " + earthy + " C " + c1x + " " + c1y + " " + c2x + " " + c2y + " " + destx + " " + desty);
                    arc.setAttribute("fill", "none");
                    arc.setAttribute("stroke", "#" + fade(colors[i],.2,.95));
                    arc.setAttribute("stroke-dasharray", "10, 10");
                    arc.setAttribute("stroke-width", "" + 4);
                    wrappattack.appendChild(arc);
                    
                    Element textWrapper = makeEle("g");
                    textWrapper.setAttributeNode((Attr)planetElements[i].getAttributeNode("transform").cloneNode(false));
                    Element deltavE = makeEle("text");
                    deltavE.setTextContent(""+ deltav[i]);
                    deltavE.setAttributeNode(rotTrans(-17, 0, (((phases[i]+360)%360) > 180)?-90:90, 0, 0));
                    deltavE.setAttributeNode(makeColor(255, 255, 255));
                    deltavE.setAttribute("font-size", ""+36);
                    deltavE.setAttribute("font-size", ""+36);
                    Element deltavEBack = (Element)deltavE.cloneNode(true);
                    deltavEBack.setAttributeNode(makeColor(0, 0, 0));
                    deltavE.setAttributeNode(rotTrans(-18, 0, (((phases[i]+360)%360) > 180)?-90:90, 0, 0));
                    textWrapper.appendChild(deltavEBack);
                    textWrapper.appendChild(deltavE);
                    wrappattack.appendChild(textWrapper);
                    
                }
                
                    
                    Element planetName = makeEle("text");
                    planetName.setTextContent(planets[i]);
                    planetName.setAttributeNode(translate(-40, mapOrbit(orbitalRadiiTest[i])));
                    planetName.setAttribute("font-size", ""+36);
                    planetName.setAttribute("fill", "#"+colors[i]);
                    wrappattack.appendChild(planetName);
                
                sun.appendChild(wrappattack);
                
            }
            

            //Element mars = makePlanet(300, 55, 15, 180, 40, 40);
            //sun.appendChild(mars);

            // salary elements
            //Element salary = makeEle("salary");
            //salary.appendChild(doc.createTextNode("100000"));
            //staff.appendChild(salary);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/res/test.svg"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
            
            System.out.println("File saved!");
            
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
    
    public static Element makeEle(String name) {
        return doc.createElement(name);
    }
    
    public static Element makeCircle(double radius) {
        Element c = makeEle("circle");
        c.setAttribute("r", "" + radius);
        return c;
    }
    
    public static Element makeOrbit(double radius, double width, String color) {
        Element c = makeEle("circle");
        c.setAttribute("r", "" + radius);
        c.setAttribute("stroke", "#" + fade(color, .5, .9));
        c.setAttribute("stroke-width", "" + width);
        c.setAttribute("stroke-opacity", "0.6");
        c.setAttribute("fill", "none");
        return c;
    }
    
    public static Attr rotTrans(double x, double y, double a, double rx, double ry) {
        return combineTransform(rotate(a, rx, ry), translate(x, y));
    }
    
    public static Attr translate(double x, double y) {
        Attr transform = doc.createAttribute("transform");
        transform.setValue("translate(" + x + " " + y + ")");
        return transform;
    }
    
    public static Attr rotate(double a, double x, double y) {
        Attr transform = doc.createAttribute("transform");
        transform.setValue("rotate(" + a + " " + x + " " + y + ")");
        return transform;
    }
    
    public static Attr combineTransform(Attr t1, Attr t2) {
        Attr combined = doc.createAttribute("transform");
        combined.setValue(t1.getValue() + t2.getValue());
        return combined;
    }
    
    public static Attr makeColor(int r, int g, int b) {
        return makeColor(toHexString(r, g, b));
    }
    
    public static Attr makeColor(String color) {
        Attr style = doc.createAttribute("style");
        style.setValue("fill: #" + color);
        return style;
    }
    
    public static String toHexString(int r, int g, int b) {
        return toHex2Char(r) + toHex2Char(g) + toHex2Char(b);
    }
    
    public static String toHex2Char(int a) {
        String tmp = "0" + Integer.toHexString(a);
        return tmp.substring(tmp.length() - 2);
    }
    
    public static Element makePlanet(double orbitalRadius, double phaseAngle, double planetRadius, int r, int g, int b) {
        return makePlanet(orbitalRadius, phaseAngle, planetRadius, toHexString(r, g, b));
    }
    
    public static Element makePlanet(double orbitalRadius, double phaseAngle, double planetRadius, String color) {
        Attr planetTransform = rotTrans(orbitalRadius, 0, -phaseAngle, 0, 0);
        Element planetFrame = makeEle("g");
        planetFrame.setAttributeNode(planetTransform);
        Element planet = makeCircle(planetRadius);
        planet.setAttributeNode(makeColor(color));
        planet.setAttribute("stroke", "#" + fade(color, 0.3, 0.8));
        planet.setAttribute("stroke-width", "" + 1.51f);
        planetFrame.appendChild(planet);
        return planetFrame;
    }
    
    public static Element makeRectangle(double x, double y, double width, double height, int r, int g, int b) {
        Element rect = makeEle("rect");
        rect.setAttribute("x", "" + x);
        rect.setAttribute("y", "" + y);
        rect.setAttribute("width", "" + width);
        rect.setAttribute("height", "" + height);
        Attr color = makeColor(r, g, b);
        rect.setAttributeNode(color);
        return rect;
    }
    
    public static double mapOrbit(double r) {
        return r / 1.5 + 50;
    }
    
    public static String fade(String color, double factor, double darken) {
        int[] rgb = new int[3];
        String faded = "";
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = Integer.parseInt(color.substring(2 * i, 2 * (i + 1)), 16);
        }
        int sum = 0;
        for (int i = 0; i < rgb.length; i++) {
            sum += rgb[i];
        }
        int[] diff = new int[3];
        for (int i = 0; i < diff.length; i++) {
            diff[i] = sum / rgb.length - rgb[i];
            rgb[i] += factor * diff[i];
            rgb[i] *= darken;
        }
        
        return toHexString(rgb[0], rgb[1], rgb[2]);
    }
}
