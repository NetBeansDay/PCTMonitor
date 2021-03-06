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

    public SmoothLearningRate(Double learningRate, String rateparameters) {
        this.learningRate = learningRate;
        setLearningRateParametersPrivate(rateparameters);
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
        setLearningRateParametersPrivate(rateparameters);
    }

    private void setLearningRateParametersPrivate(String rateparameters) {
        String[] arr = rateparameters.split("\\^");
        adaptiveSmoothLower = Double.parseDouble(arr[0]);
        adaptiveSmoothUpper = Double.parseDouble(arr[1]);
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SmoothUpper").append(":");
        sb.append(adaptiveSmoothUpper).append("_");

        sb.append("SmoothLower").append(":");
        sb.append(adaptiveSmoothLower);

        return sb.toString();
    }
}
