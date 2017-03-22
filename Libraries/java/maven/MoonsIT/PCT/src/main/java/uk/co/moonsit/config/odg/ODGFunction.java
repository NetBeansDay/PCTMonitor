/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.config.odg;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.w3c.dom.Element;
import uk.co.moonsit.config.functions.Utils;

/**
 *
 * @author ReStart
 */
public class ODGFunction {

    private Point2D location = null;
    private String name = null;
    private Integer configID = null;
    private final List<String[]> linkList;
    private final List<Integer> transferList;

    public ODGFunction(String name) {
        linkList = new ArrayList<>();
        transferList = new ArrayList<>();
        this.name = name;
    }

    public ODGFunction(Element elem) {
        linkList = new ArrayList<>();
        transferList = new ArrayList<>();
        DrawCustomShapeElement custom = (DrawCustomShapeElement) elem;

        double x = Utils.convertCmCoordinate(custom.getSvgXAttribute()) + ODGProcessing.FUNCTION_SIZE/2;
        double y = Utils.convertCmCoordinate(custom.getSvgYAttribute())+ ODGProcessing.FUNCTION_SIZE/2;
        location = new Point2D.Double(x, y);
    }

    public ODGFunction(String name, Element elem) {
        linkList = new ArrayList<>();
        transferList = new ArrayList<>();
        this.name = name;
        DrawCustomShapeElement custom = (DrawCustomShapeElement) elem;
        //LOG.info("Nname " + custom.getLocalName());

        double x = Utils.convertCmCoordinate(custom.getSvgXAttribute())+ ODGProcessing.FUNCTION_SIZE/2;
        double y = Utils.convertCmCoordinate(custom.getSvgYAttribute())+ ODGProcessing.FUNCTION_SIZE/2;
        location = new Point2D.Double(x, y);
    }

    public void addLink(String link) {
        if (link == null) {
            link = "TMP";
        }
        String[] llink = new String[1];
        llink[0] = link;
        linkList.add(llink);
    }

    public void addLink(String link, String type) {
        if(type==null || type.length()==0){
            addLink(link);
            return;
        }
        String[] llink = new String[2];
        llink[0] = link;
        llink[1] = type;
        linkList.add(llink);
    }
    
    public void addTransfer(Integer id) {
        transferList.add(id);
    }

    public List<Integer> getTransferList() {
        return transferList;
    }

    public String[][] getLinks() {
        String[][] list = new String[linkList.size()][];
        int index = 0;
        for (String[] link : linkList) {
            list[index++] = link;
        }
        return list;
    }

    @Override
    public String toString() {
        return name + " " + location + " " + configID;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getConfigID() {
        return configID;
    }

    public void setConfigID(Integer configID) {
        this.configID = configID;
    }

    public boolean isIn(Point2D p) {
        if (p.distance(location) < ODGProcessing.FUNCTION_SIZE/1.9) { // bit more than half
            return true;
        }
        return false;
    }

    public boolean isNear(ODGFunction other) {
        if (other.getLocation().distance(location) < ODGProcessing.FUNCTION_PROXIMITY) {
            return true;
        }

        return false;
    }

     public double distance(ODGFunction other) {
        return other.getLocation().distance(location);
    }
}