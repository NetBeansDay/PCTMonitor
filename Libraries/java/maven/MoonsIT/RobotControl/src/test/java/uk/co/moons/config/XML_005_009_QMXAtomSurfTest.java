/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.moons.config;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ReStart
 */
public class XML_005_009_QMXAtomSurfTest {
    
    public XML_005_009_QMXAtomSurfTest() {
    }

    /**
     * Test of main method, of class TestMain.
     */
    @Test
    public void testMain()  {
        String[] args = {"..\\..\\..\\..\\..\\Controllers\\Models\\QuantumMoves\\", "005-009-QMXAtomSurf"};
        System.out.println(args[0]+args[1]);

        try {
            ODGConfig.main(args);
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.out.println(ex.getCause());
            fail(ex.toString());
        }
    }
    
}
