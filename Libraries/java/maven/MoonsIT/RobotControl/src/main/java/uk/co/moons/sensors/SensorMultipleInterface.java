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
package uk.co.moons.sensors;

/**
 *
 * @author ReStart
 */
public interface SensorMultipleInterface {

    public double getValue();

    public float[] getData();

    public double getValue(int i);
    
    public void close();
}
