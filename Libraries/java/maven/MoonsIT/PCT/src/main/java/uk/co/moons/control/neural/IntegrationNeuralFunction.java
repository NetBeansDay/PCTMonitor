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

//import uk.co.moons.math.RMath;
import java.util.List;
import java.util.logging.Logger;
import pct.moons.co.uk.schema.layers.Parameters;
import uk.co.moons.control.functions.BaseControlFunction;

/**
 * <b>Integration function.</b>
 *
 * <p>
 * Leaky integrator function.
 *
 * <p>
 * The amplified input is integrated and slowed by a slowing factor.
 *
 * If the input is infinity then zero is output. However, if IgnoreInfinity is
 * true, then the previous output is returned. However, if EnableInfinity is
 * true, then infinity is output.
 *
 * If EnableInfinity is true, output is infinity, and the input returns to a
 * real value then the output is set to the reset value, which comes from a
 * link, if configured.
 *
 * </p>
 * The configuration parameters to the function are as follows:
 * <p>
 * <b>Gain</b> - the amplification factor (type Double). Mandatory. <br>
 * <b>Slow</b> - the slowing factor (type Double). <br>
 * <b>EnableInfinity</b> - true or false (type Boolean).<br>
 * </p>
 *
 * @author Rupert Young <rupert@moonsit.co.uk>
 * @version 1.0
 */
public class IntegrationNeuralFunction extends NeuralFunction {

    private static final Logger LOG = Logger.getLogger(IntegrationNeuralFunction.class.getName());

    public Double gain = null;
    public Double slow = null;
    public Double min = null;
    public Double max = null;
    public Double tolerance = null;
    public Boolean integrate = true;
    public Boolean enableinfinity = false;
    public Boolean ignoreinfinity = false;

    private Integer resetIndex = null;
    private Integer rateIndex = null;
    private Integer resetValueIndex = null;
    private Integer dataIndex = null;

    public IntegrationNeuralFunction(int g) {
        super();
        gain = new Double(g);
    }

    public IntegrationNeuralFunction(List<Parameters> ps) throws Exception {
        super(ps);

        for (Parameters param : ps) {
            if (param.getName().equals("Gain")) {
                gain = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Slow")) {
                slow = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Min")) {
                min = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Max")) {
                max = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Tolerance")) {
                tolerance = Double.valueOf(param.getValue());
            }
            if (param.getName().equals("Integrate")) {
                integrate = Boolean.valueOf(param.getValue());
            }

            if (param.getName().equals("EnableInfinity")) {
                enableinfinity = Boolean.valueOf(param.getValue());
            }
            if (param.getName().equals("IgnoreInfinity")) {
                ignoreinfinity = Boolean.valueOf(param.getValue());
            }

        }
        if (enableinfinity && ignoreinfinity) {
            throw new Exception("EnableInfinity and IgnoreInfinity can not both be true");
        }
        if (gain == null) {
            throw new Exception("Gain null for IntegrationNeuralFunction");
        }
        if (slow == null) {
            throw new Exception("Slow null for IntegrationNeuralFunction");
        }

    }

    @Override
    public void verifyConfiguration() throws Exception {
        if (links.getControlList().isEmpty()) {
            throw new Exception("IntegrationNeuralFunction requires at least one link");
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<BaseControlFunction> controls = links.getControlList();

        for (int i = 0; i < controls.size(); i++) {
            String linkType = links.getType(i);// controls.get(i).getLinkType();
            if (linkType == null) {
                dataIndex = i;
                continue;
            }
            //LOG.log(Level.INFO, "LinkType {0}", linkType);
            if (linkType.equals("Reset")) {
                resetIndex = i;
                continue;
            }
            if (linkType.equals("ResetValue")) {
                resetValueIndex = i;
                continue;
            }
            if (linkType.equals("Rate")) {
                rateIndex = i;
            }

        }

        reset();
    }

    private void reset() {

        //if (Math.abs(input) == Double.POSITIVE_INFINITY) {
        //output = 0;
        //}
        List<BaseControlFunction> controls = links.getControlList();
        Double resetLink = null;
        if (resetIndex != null) {
            resetLink = controls.get(resetIndex).getValue();
        }

        if (resetLink != null && resetLink == 1) {
            setResetValue(controls);
        }
    }

    private void setResetValue(List<BaseControlFunction> controls) {
        Double resetValue = null;
        if (resetValueIndex != null) {
            resetValue = controls.get(resetValueIndex).getNeural().getOutput();
        }
        if (resetValue != null) {
            output = resetValue;
        } else {
            output = 0;
        }
    }

    @Override
    public double compute() {
        List<BaseControlFunction> controls = links.getControlList();
        Double input = controls.get(dataIndex).getValue();
        reset();
        if (Math.abs(input) != Double.POSITIVE_INFINITY) {
            if (output == Double.POSITIVE_INFINITY) {
                setResetValue(controls);
            }
            double timeRate = 1.0f;

            if (rateIndex != null) {
                timeRate = controls.get(rateIndex).getValue() / 1000;
            }

            double adjustment = tolerance(timeRate * (gain * input - output) / slow);
            if (integrate) {
                output += adjustment;
            } else {
                output = adjustment;
            }
            output = limits(output);

        } else if (enableinfinity) {
            output = Double.POSITIVE_INFINITY;
        } else if (!ignoreinfinity) {
            output = 0;
        }
        return output;
    }

    private double tolerance(double val) {

        if (tolerance != null && Math.abs(val) < tolerance) {
            return 0;
        }
        return val;
    }

    private double limits(double val) {

        if (min != null && val < min) {
            val = min;
        }

        if (max != null && val > max) {
            val = max;
        }
        return val;
    }

    @Override
    public double getParameter() {
        return gain;
    }

    @Override
    public void setParameter(double par) {
        gain = par;
    }

    @Override
    public String getParametersString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Gain").append(":");
        sb.append(gain).append("_");

        sb.append("Slow").append(":");
        sb.append(slow);

        return sb.toString();
    }

}
