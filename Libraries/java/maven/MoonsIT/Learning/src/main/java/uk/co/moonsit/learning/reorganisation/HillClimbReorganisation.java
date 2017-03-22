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
package uk.co.moonsit.learning.reorganisation;

/**
 *
 * @author Rupert
 */
public class HillClimbReorganisation extends BaseReorganisation {

    private double correction = 0;

    public HillClimbReorganisation(double lr, double learningRateMax, Double adaptiveSmoothUpper, Double adaptiveSmoothLower, double delta, boolean continuous) {
        learningRate = lr;
        this.adaptiveSmoothUpper = adaptiveSmoothUpper;
        this.adaptiveSmoothLower = adaptiveSmoothLower;
        this.learningRateMax = learningRateMax;
        this.delta = delta;
        this.continuous = continuous;
    }

    @Override
    public double correct(double errorResponse, int period, int counter, double parameter, double parameterMA) {

        if (counter % period == 0) {
            learningRate = adaptLearningRate(errorResponse);

            double errorResponseChange = errorResponse - previousErrorResponse;

            if (errorResponseChange > 0) {
                delta = -delta;
            }
            correction = learningRate * delta * parameterMA * Math.abs(errorResponse);
            if (!continuous) {
                parameter += correction;
            }
            previousErrorResponse = errorResponse;
        }

        if (continuous) {
            parameter += correction;
        }

        return parameter;
    }

}