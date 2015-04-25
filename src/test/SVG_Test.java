/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svg.test;

import svg.Elem;
import svg.SVG;

/**
 *
 * @author Andy
 */
public class SVG_Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SVG yolo = new SVG();
        Elem root = yolo.getRoot();
        
        root.append("circle")
                .attr("cx", "100")
                .attr("cy", "100")
                .attr("r", "20")
                .attr("fill", "#AA87F8");
        
        root.append("text")
                .text("Hello Bitches")
                .attr("font-size", ""+36)
                .attr("transform", "translate(100, 100)")
                .attr("fill", "#8962af");
        
        for(int i = 0; i < 100; i++) {
            root.append("path")
                .attr("d", getRandomPath(10))
                .attr("fill", getRandomColor())
                .attr("stroke", getRandomColor())
                .attr("opacity", ""+.5);
        }
        
        yolo.write("testing.svg");
    }

    public static String getRandomPath(int points) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("M 100 100");
        for(int i = 0 ; i < points; i++) {
            sb.append("T ").append(Math.random() * 500).append(" ").append(Math.random() * 500).append(" ");
        }
        return sb.toString();
        
    }

    public static String getRandomColor() {
        return "#" + toHexString((int)(255 * Math.random()), (int)(255 * Math.random()), (int)(255 * Math.random()));
    }
    
    public static String toHexString(int r, int g, int b) {
        return toHex2Char(r) + toHex2Char(g) + toHex2Char(b);
    }
    
    public static String toHex2Char(int a) {
        String tmp = "0" + Integer.toHexString(a);
        return tmp.substring(tmp.length() - 2);
    }
}
