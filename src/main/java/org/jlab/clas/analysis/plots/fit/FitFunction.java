/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots.fit;

import org.freehep.math.minuit.FCNBase;
import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnScan;
import org.freehep.math.minuit.MnUserParameters;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;

/**
 *
 * @author veronique
 */
public class FitFunction implements FCNBase{
    private  GraphErrors _graph;
    private  GraphErrors _signalgraph;
    private double _binning;
    public MnUserParameters pars ;
    public double fitRangeMin = 0.0;
    public double fitRangeMax = 1.8;
    public double threshold = 1.09;
    public double signalRangeMin = 1.1;
    public double signalRangeMax = 1.2;
    public boolean fitOnlyBg = true;
    private int polyDeg = 8;
    
    public FitFunction() {
    }
    
    public void setGraph(H1F histo) {
        double binWidth =  histo.getDataX(1)- histo.getDataX(0);
        int nbins = histo.getData().length;
        _graph =  new GraphErrors();
        if(fitOnlyBg) {
            _graph.addPoint(threshold, 0, 0, 1.41);
            for (int i =0; i<nbins; i++) {
                double le = histo.getDataX(i)-0.5*binWidth;
                double he = histo.getDataX(i)+0.5*binWidth;
                double ey = histo.getDataEY(i);
                if(ey==0) continue;
                if((le>threshold && he<signalRangeMin) || (le>signalRangeMax && le<fitRangeMax)) {
                    _graph.addPoint(histo.getDataX(i), histo.getDataY(i), histo.getDataEX(i), histo.getDataEY(i));
                   // System.out.println("adding "+histo.getDataX(i)+",  "+histo.getDataY(i));
                }
            } 
        } else {
            for (int i =0; i<nbins; i++) {
                double le = histo.getDataX(i)-0.5*binWidth;
                double he = histo.getDataX(i)+0.5*binWidth;
                double ey = histo.getDataEY(i);
                if(ey==0) continue;
                _graph.addPoint(histo.getDataX(i), histo.getDataY(i), histo.getDataEX(i), histo.getDataEY(i));
                if(le>signalRangeMin && he<signalRangeMax)
                    _signalgraph.addPoint(histo.getDataX(i), histo.getDataY(i), histo.getDataEX(i), histo.getDataEY(i));        
            } 
        }
        this.setParsBg();
    }
    
    public FitFunction(H1F histo) {
        this.setGraph(histo);
        double binWidth =  histo.getDataX(1)- histo.getDataX(0);
        double le = histo.getDataX(0)-0.5*binWidth;
        int nbins = histo.getData().length;
        double he = histo.getDataX(nbins-1)+0.5*binWidth;
        _binning = (double) nbins/(he-le); 
    }
    public void setParsSig(double mean, double sigma1, double sigma2) {
        double x0  = this._signalgraph.getDataX(0);
        int refBin = this._signalgraph.getDataSize(0)-1;
        double xf  = this._signalgraph.getDataX(refBin);
        double y0  = this._signalgraph.getDataY(0);
        double yf  = this._signalgraph.getDataY(refBin);
        double slope = (yf-y0)/(xf-x0); 
        
        double yieldEst =0;
        for(int i =0; i<refBin+1; i++) {
            double x = this._signalgraph.getDataX(i);
            double y = this._signalgraph.getDataY(i);
            yieldEst+=y-(((yf-y0)/(xf-x0))*(x-x0)+y0);
        }
        double[] values = new double[4];
        double[] errors = new double[4];
        String[] names  = new String[4];
        
        names[0] = "yield";
        names[1] = "mean";
        names[2] = "sigmaLeft";
        names[3] = "sigmaRight";
        
        values[0] = yieldEst;
        values[1] = mean;
        values[2] = sigma1;
        values[2] = sigma2;
        
        errors[0] = Math.sqrt(yieldEst);
        errors[1] = 0.1;
        errors[2] = 0.01;
        errors[3] = 0.01;
        
        for(int i = 0; i<4; i++) {
            pars.add(names[i], values[i], errors[i]);
        }
        
    }
    public void setParsBg() {
        double x0  = this._graph.getDataX(0); 
        int refBin = this._graph.getDataSize(0)-2;
        if(refBin<0) return;
        double xf  = this._graph.getDataX(refBin);
        double y0  = this._graph.getDataY(0);
        double yf  = this._graph.getDataY(refBin);
        double slope = (yf-y0)/(xf-x0);
        
        if(this.fitOnlyBg) {
            double[] values = new double[polyDeg];
            double[] errors = new double[polyDeg];
            String[] names  = new String[polyDeg];
            values[0] = slope;
            errors[0] = 1;
            names[0]  = "p1";
            for(int i = 1; i<polyDeg; i++) {
                names[i]  = "p"+(i+1);
                values[i] = 0.0;
                errors[i] = 1.0;
            }
            pars = new MnUserParameters();
            for(int i = 0; i<polyDeg; i++) {
                pars.add(names[i], values[i], errors[i]);
            }
        } else {
            int shift=4;
            double[] values = new double[shift+polyDeg];
            double[] errors = new double[shift+polyDeg];
            String[] names  = new String[shift+polyDeg];
            values[shift] = slope;
            errors[shift] = 1;
            names[shift]  = "p1";
            for(int i = 1; i<polyDeg; i++) {
                names[i+shift]  = "p"+(i+1);
                values[i+shift] = 0.0;
                errors[i+shift] = 1.0;
            }
            for(int i = 0; i<4; i++) {
                names[i]  = "";
                values[i] = 0;
                errors[i] = 0.1;
            }
            pars = new MnUserParameters();
            for(int i = 0; i<4+polyDeg; i++) {
                pars.add(names[i], values[i], errors[i]);
            }
        }
    }
    
    
    public double evalBg(double x, double[] par, int parShift) { //force to go to 0 at threshold
        double[] p = new double[polyDeg];
        double t = this.threshold;
        double p0 = 0;
        for(int i =0; i<polyDeg; i++) {
            p[i] = par[i+parShift];
            p0+=par[i]*Math.pow(t, i+1);
        }
        double value = -p0;
        for(int i =0; i<polyDeg; i++) {
            value+=p[i]*Math.pow(x, i+1);
        }
       
        return value;
    }
    
    public double evalSig(double x, double[] par) { //double gaussian with common width and asymmetric width
        double yield      = par[0];
        double mean       = par[1];
        double sigmaLeft  = par[2];
        double sigmaRight = par[3];
        
        double value =0;
        if(x<mean) {
            value = yield/(Math.sqrt(2.0*Math.PI)*_binning*sigmaLeft) * Math.exp(-0.5 * Math.pow((x - mean) / sigmaLeft, 2));
        } else {
            value = yield/(Math.sqrt(2.0*Math.PI)*_binning*sigmaRight) * Math.exp(-0.5 * Math.pow((x - mean) / sigmaRight, 2));
        }
        
        return value;
    }
    
    public double eval(double x, double[] par) {
        if(this.fitOnlyBg) {
            return this.evalBg(x, par, 0);
        } else {
            int shift = 4;
            return this.evalSig(x, par)+this.evalBg(x, par, shift);
        }
        
    }
    
    @Override
    public double valueOf(double[] pars) {
        double chisq = 0;
        for (int ix =0; ix< _graph.getDataSize(0); ix++) {
            double x = _graph.getDataX(ix);
            double y = _graph.getDataY(ix); 
            if(x>this.fitRangeMin && x<this.fitRangeMax) {
                double err = _graph.getDataEY(ix); 
                if(err>0) {
                    double f = this.eval(x, pars);
                    double delta = (y - f) / err; 
                    chisq += delta * delta;
                }
            }  
        }
        
        return chisq;
    }
    
    private MnScan  scanner = null;
    private MnMigrad migrad = null;
    public int maxIterFit = 10;
    public int NbRunFit = 0;
    
    public void runFit() {
       
        scanner = new MnScan(this, pars,2);

//        for (int p = 0; p < fixPar.length; p++) {
//            scanner.fix(fixPar[p]); 
//        }
        FunctionMinimum scanmin = scanner.minimize();
            pars = scanmin.userParameters();
        for(int i =0; i<maxIterFit; i++) {    
            scanner.minimize();
            pars = scanmin.userParameters();
        }
        //for (int p = 0; p < fixPar.length; p++) {
        //    scanner.release(fixPar[p]); 
        //}
        
        migrad = new MnMigrad(this, pars,2);
        migrad.setCheckAnalyticalDerivatives(true);
        
        FunctionMinimum min ;
        
        for(int it = 0; it<maxIterFit; it++) {
            min = migrad.minimize();
//            System.err.println("******************************************");
//            System.err.println("*   FIT RESULTS at iteration "+(it+1)+"  *");
//            System.err.println("******************************************");  
//            for(int i = 0; i<5; i++) {
//                System.out.println("...par"+i+" = "+min.userParameters().value(i));
//            }
            this.pars=min.userParameters();
            if(min.isValid()) {
                System.out.println("Fit OK");
                this.pars=min.userParameters();
            }
        }
    }
}
