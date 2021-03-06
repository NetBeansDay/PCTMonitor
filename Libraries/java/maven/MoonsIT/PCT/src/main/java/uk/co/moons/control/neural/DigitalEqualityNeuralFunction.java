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
package uk.co.moons.control.neural;

import uk.co.moons.control.functions.BaseControlFunction;
import java.util.List;
import pct.moons.co.uk.schema.layers.Parameters;

public class DigitalEqualityNeuralFunction extends NeuralFunction {

    public DigitalEqualityNeuralFunction() {
        super();
    }

    public DigitalEqualityNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            //String pname = param.getName();
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        double a = controls.get(0).getValue();
        double b = controls.get(1).getValue();

        if (a == 0) {
            output = 0;
        } else if (a == b || b == 0) {
            output = 1;
        } else {
            output = -1;
        }

        return output;
    }
}
