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
public class Mandelbrot {
    
    private static double offsetX = 500, offsetY = 300;
    private static double height = 300, width = 300;

    public static void main(String[] args) {

        SVG yolo = new SVG();
        Elem root = yolo.getRoot();

        bifur(root);


        yolo.write("mandel.svg");
    }

    private static String getColor(double iteration) {
        return iteration > .99 ? "#000000" : "#" + SVG_Test.toHexString(
                (int) (190 - 180 * iteration),
                (int) (255 - 180 * iteration),
                (int) (255 - 255 * iteration));
    }

    private static double mapX(double x) {
        return x * width + offsetX;
    }

    private static double mapY(double y) {
        return y * height + offsetY;
    }
    
    public static void bifur(Elem root) {
        
        int maxIteration = 500;
        int start = 100;
        int end = 600;
        double res = (double)(end - start) / maxIteration;
        
        double last = .5;
        double r = 1;
        StringBuilder path = new StringBuilder("M " + start + " " + 300);
        for(int i = 0; i < maxIteration; i++) {
            last = last*(1-last)*r;
            path.append(" L ").append(i * res + start).append(" ").append(300 + last * 100);
        }
       root.append("path")
               .attr("d", path.toString());
    }

    public static void mandelGroupIteration(Elem root) {
        
        int numPaths = 40;
        double[][][] initialPoints = new double[numPaths][numPaths][2];
        double[][][] lastPoints = new double[numPaths][numPaths][2];

        for (int i = 0; i < numPaths; i++) {
            for (int j = 0; j < numPaths; j++) {

                initialPoints[i][j][0] = lastPoints[i][j][0] = (double) i / numPaths * -2.25 + 1;
                initialPoints[i][j][1] = lastPoints[i][j][1] = (double) j / numPaths * -1.25 + .625;

            }
        }

        int maxIteration = 60;

        Elem[][][] elems = new Elem[numPaths][numPaths][maxIteration];
        String[][] colors = new String[numPaths][numPaths];

        for (int h = 0; h < maxIteration; h++) {
            for (int i = 0; i < numPaths; i++) {
                for (int j = 0; j < numPaths; j++) {
                    double x = lastPoints[i][j][0];
                    double y = lastPoints[i][j][1];
                    if (x * x + y * y < 4) {
                        double xm = lastPoints[i][j][0] = x * x - y * y + initialPoints[i][j][0];
                        double ym = lastPoints[i][j][1] = 2 * x * y + initialPoints[i][j][1];
                        elems[i][j][h] = root.append("path")
                                .attr("d", "M " + mapX(x) + " " + mapY(y) + " L " + mapX(xm) + " " + mapY(ym));
                        if (h == maxIteration - 1) {
                            colors[i][j] = getColor((double) (h + 1) / maxIteration);
                        }
                    } else if (colors[i][j] == null) {
                        colors[i][j] = getColor((double) (h + 1) / maxIteration);
                    }
                }
            }
        }

        for (int h = 0; h < maxIteration; h++) {
            for (int i = 0; i < numPaths; i++) {
                for (int j = 0; j < numPaths; j++) {
                    if (elems[i][j][h] != null) {
                        elems[i][j][h]
                                .attr("fill", "none")
                                .attr("stroke", colors[i][j])
                                .attr("opacity", colors[i][j].equals("#000000") ? "0.04" : ".1");
                    }
                }
            }
        }
        
    }

    public static void mandelGroupPath(Elem root) {

        int numPaths = 40;
        int maxIteration = 60;
        
        for (int i = 0; i < numPaths; i++) {
            for (int j = 0; j < numPaths;
                    j++) {

                double x0 = (double) i / numPaths * -2.25 + 1;
                double y0 = (double) j
                        / numPaths * -1.25 + .625;
                double x = 0;
                double y = 0;

                int iteration = 0;

                StringBuilder sb = new StringBuilder();
                sb.append("M ").append(mapX(x0)).append(" ").append(mapY(y0));

                while (x * x + y * y < 4 && iteration < maxIteration) {

                    double temp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = temp;

                    iteration++;

                    sb.append("L ").append(mapX(x)).append(" ").append(mapY(y)).append(" ");

                }

                root.append("path").attr("d", sb.toString())
                        .attr("fill", "none")
                        .attr("opacity", ".05")
                        .attr("stroke", getColor((double) iteration / maxIteration));

            }
        }

    }
}