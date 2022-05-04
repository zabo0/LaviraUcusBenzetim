package com.saboon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Graph extends JPanel {

    private ArrayList<Double> vls = new ArrayList<>();
    int marg = 30;

    public Graph(ArrayList<Double> values){
        vls = values;
    }

    protected void paintComponent(Graphics grf){
        //create instance of the Graphics to use its methods
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D)grf;

        //Sets the value of a single preference for the rendering algorithms.
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // get width and height
        int width = getWidth();
        int height = getHeight();

        // draw graph
        graph.draw(new Line2D.Double(marg, marg, marg, height-marg));
        graph.draw(new Line2D.Double(marg, height-marg, width-marg, height-marg));

        //find value of x and scale to plot points
        double x = (double)(width-2*marg)/(vls.size()-1);
        double scale = (double)(height-2*marg)/getMax();

        graph.draw(new Line2D.Double(marg, marg, height - marg, marg));

        //set color for points
        graph.setPaint(Color.RED);

        // set points to the graph
        for(int i = 0; i< vls.size(); i++){
            double x1 = marg+i*x;
            double y1 = height-marg-scale* vls.get(i);
            graph.fill(new Ellipse2D.Double(x1-2, y1-2, 4, 4));
        }
    }

    //create getMax() method to find maximum value
    private double getMax(){
        double max = -Integer.MAX_VALUE;
        for(int i = 0; i< vls.size(); i++){
            if(vls.get(i)>max)
                max = vls.get(i);

        }
        return max;
    }


//    public void plot(){
//        //create an instance of JFrame class
//        JFrame frame = new JFrame();
//        // set size, layout and location for frame.
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new Graph());
//        frame.setSize(600, 600);
//        frame.setLocation(200, 200);
//        frame.setVisible(true);
//    }


}
