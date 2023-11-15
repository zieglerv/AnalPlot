/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots.fit;

import org.freehep.math.minuit.MnUserParameters;
import org.jlab.groot.math.Func1D;

/**
 *
 * @author veronique
 */
public class FitLine extends Func1D{
    public double min;
    public double max;
    public FitFunction fc ;
    
    private double[] par ;
    public FitLine(String name, double min, double max, MnUserParameters pars, FitFunction fc) {
        super(name, min, max);
        this.fc=fc;
        this.initParameters(pars);
    }
    public FitLine(double min, double max, MnUserParameters pars, FitFunction fc) {
        super("GausnthOrderPoly", min, max);
        this.fc=fc;
        this.initParameters(pars);
    }

    private void initParameters(MnUserParameters pars) {
         par = new double[pars.variableParameters()];
        for(int p = 0; p< pars.variableParameters(); p++) {
            par[p] = pars.value(p);
        }
    }
    @Override
    public double evaluate(double x) { 
        double y = fc.eval(x, par);
        return y;
    }

    
   
}
