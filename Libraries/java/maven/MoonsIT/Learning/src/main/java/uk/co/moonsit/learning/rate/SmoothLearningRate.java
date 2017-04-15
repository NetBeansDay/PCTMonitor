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
package uk.co.moonsit.learning.rate;

import uk.co.moons.math.RMath;

/**
 *
 * @author Rupert Young
 */
public class SmoothLearningRate extends BaseLearningRate {

    protected Double adaptiveSmoothUpper = 0.95;
    protected Double adaptiveSmoothLower = 0.9;
    private double shortMA = 0;
    private double longMA = 0;
    protected double learningRateMax = 0.5;

    public SmoothLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    @Override
    public double update(double weight, double error) {
        if (adaptiveSmoothUpper != 0) {
            shortMA = RMath.smooth(error, shortMA, adaptiveSmoothLower);
            longMA = RMath.smooth(error, longMA, adaptiveSmoothUpper);
            if (longMA != 0) {
                learningRate = Math.min(learningRateMax, /*10 * */ Math.abs((shortMA - longMA) / shortMA));
            }
        }
        return learningRate;
    }

    @Override
    public void reset() {
        shortMA = 0;
        longMA = 0;
    }

     @Override
    public void setLearningRateParameters(String rateparameters) {
        String[] arr = rateparameters.split("\\^");
        adaptiveSmoothLower = Double.parseDouble(arr[0]);
        adaptiveSmoothUpper = Double.parseDouble(arr[1]);
    }
}
