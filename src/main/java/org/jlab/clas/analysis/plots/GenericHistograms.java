/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.freehep.math.minuit.MnUserParameters;
import org.jlab.clas.analysis.plots.fit.FitFunction;
import org.jlab.clas.analysis.plots.fit.FitLine;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.math.F1D;
import org.jlab.groot.ui.LatexText;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author veronique
 */
public class GenericHistograms {
    private String reaction="";
    private String cut1="";
    private String cut2="";
    private String cut3="";
    private double varStep1 = 1; 
    private double varStep2 = 1; 
    private double varStep3 = 1;  
    private int nVarSteps1 = 1;
    private int nVarSteps2 = 1;
    private int nVarSteps3 = 1;
    private Map<IntArrayWrapper, H1F> histosMap = new HashMap<>();
    private int nBins = 150;
    private double hLowEdge  = 1.0;
    private double hHighEdge = 1.75;
    public double fitLowEdge = 1.09;
    public double fitHighEdge = 1.8;
    private static final DecimalFormat decfor = new DecimalFormat("0.0");  
    private static final DecimalFormat decfor2 = new DecimalFormat("0.00");  
    GraphErrors ge2 = new GraphErrors();
    GraphErrors ge3 = new GraphErrors();
    public void init() {
        for(int i = 0; i<getnVarSteps1(); i++) {
            for(int j = 0; j<getnVarSteps2(); j++) {
                for(int k = 0; k<getnVarSteps3(); k++) {
                    double c1=getVarStep1()*i;
                    double c2=getVarStep2()*j;
                    double c3=getVarStep3()*k;
                    int[] v = new int[]{i,j,k};
                    
                    String XTitle = getReaction()+" (GeV)";
                    float binning = (float) (1000.0*(gethHighEdge()-gethLowEdge())/(float) getnBins());
                    String YTitle = "Counts/"+binning+" MeV";
                    
                    H1F h = new H1F("h",XTitle, YTitle, getnBins(), gethLowEdge(), gethHighEdge());
                    h.setTitle("Mass, "+getCut1()+" "+(float)c1+", "+getCut2()+" "+(float)c2+" "+getCut3()+" "+(float)c3); 
                    
                    this.histosMap.put(new IntArrayWrapper(v), h);
                }
            }
        }
    }

    /**
     * @return the reaction
     */
    public String getReaction() {
        return reaction;
    }

    /**
     * @param reaction the reaction to set
     */
    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    /**
     * @return the cut1
     */
    public String getCut1() {
        return cut1;
    }

    /**
     * @param cut1 the cut1 to set
     */
    public void setCut1(String cut1) {
        this.cut1 = cut1;
    }

    /**
     * @return the cut2
     */
    public String getCut2() {
        return cut2;
    }

    /**
     * @param cut2 the cut2 to set
     */
    public void setCut2(String cut2) {
        this.cut2 = cut2;
    }

    /**
     * @return the cut3
     */
    public String getCut3() {
        return cut3;
    }

    /**
     * @param cut3 the cut3 to set
     */
    public void setCut3(String cut3) {
        this.cut3 = cut3;
    }

    /**
     * @return the varStep1
     */
    public double getVarStep1() {
        return varStep1;
    }

    /**
     * @param varStep1 the varStep1 to set
     */
    public void setVarStep1(double varStep1) {
        this.varStep1 = varStep1;
    }

    /**
     * @return the varStep2
     */
    public double getVarStep2() {
        return varStep2;
    }

    /**
     * @param varStep2 the varStep2 to set
     */
    public void setVarStep2(double varStep2) {
        this.varStep2 = varStep2;
    }

    /**
     * @return the varStep3
     */
    public double getVarStep3() {
        return varStep3;
    }

    /**
     * @param varStep3 the varStep3 to set
     */
    public void setVarStep3(double varStep3) {
        this.varStep3 = varStep3;
    }

    /**
     * @return the nVarSteps1
     */
    public int getnVarSteps1() {
        return nVarSteps1;
    }

    /**
     * @param nVarSteps1 the nVarSteps1 to set
     */
    public void setnVarSteps1(int nVarSteps1) {
        this.nVarSteps1 = nVarSteps1;
    }

    /**
     * @return the nVarSteps2
     */
    public int getnVarSteps2() {
        return nVarSteps2;
    }

    /**
     * @param nVarSteps2 the nVarSteps2 to set
     */
    public void setnVarSteps2(int nVarSteps2) {
        this.nVarSteps2 = nVarSteps2;
    }

    /**
     * @return the nVarSteps3
     */
    public int getnVarSteps3() {
        return nVarSteps3;
    }

    /**
     * @param nVarSteps3 the nVarSteps3 to set
     */
    public void setnVarSteps3(int nVarSteps3) {
        this.nVarSteps3 = nVarSteps3;
    }

    /**
     * @return the histosMap
     */
    public Map<IntArrayWrapper, H1F> getHistosMap() {
        return histosMap;
    }

    /**
     * @param histosMap the histosMap to set
     */
    public void setHistosMap(Map<IntArrayWrapper, H1F> histosMap) {
        this.histosMap = histosMap;
    }

    /**
     * @return the nBins
     */
    public int getnBins() {
        return nBins;
    }

    /**
     * @param nBins the nBins to set
     */
    public void setnBins(int nBins) {
        this.nBins = nBins;
    }

    /**
     * @return the hLowEdge
     */
    public double gethLowEdge() {
        return hLowEdge;
    }

    /**
     * @param hLowEdge the hLowEdge to set
     */
    public void sethLowEdge(double hLowEdge) {
        this.hLowEdge = hLowEdge;
    }

    /**
     * @return the hHighEdge
     */
    public double gethHighEdge() {
        return hHighEdge;
    }

    /**
     * @param hHighEdge the hHighEdge to set
     */
    public void sethHighEdge(double hHighEdge) {
        this.hHighEdge = hHighEdge;
    }
    
    public void fillVtxHistos(double mass, double ... vars) {
        int n = vars.length;
        
        for(int i = 0; i<getnVarSteps1(); i++) {
            for(int j = 0; j<getnVarSteps2(); j++) {
                for(int k = 0; k<getnVarSteps3(); k++) {
                    double c1=getVarStep1()*i;
                    double c2=getVarStep2()*j;
                    double c3=getVarStep3()*k;
                    int[] v = new int[]{i,j,k};
                    boolean pass = false;
                    if(vars[0]>c1 && vars[1]>c2) 
                        pass = true;
                    if(n>2) {
                        if(vars[2]<=c3) 
                            pass = false;
                    }
                    if(pass) {
                        IntArrayWrapper keyToRetrieve = new IntArrayWrapper(v);
                        if(this.getHistosMap().containsKey(keyToRetrieve))
                            this.getHistosMap().get(keyToRetrieve).fill(mass);
                    }
                }
            }
        }
    }
    
    public void plotHistograms(int col) {
        List<TCanvas> cans = new ArrayList<>();
        int[] ctr = new int[nVarSteps3];
        for(int i = 0; i<nVarSteps3; i++) {
            cans.add(new TCanvas("Vtx plots", 1200, 800));
            cans.get(cans.size()-1).divide(nVarSteps1, nVarSteps2);
        }
        for(int k = 0; k<nVarSteps3; k++) {
            ctr[k]=0;
            for(int i = 0; i<nVarSteps1; i++) {
                for(int j = 0; j<nVarSteps2; j++) {
                    cans.get(k).cd(ctr[k]);
                    this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})).setFillColor(col);
                    this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})).setLineColor(col);
                    cans.get(k).draw(this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})), "");
                    cans.get(k).draw(this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})).histClone("h"), "Esame");
                    ctr[k]++;
                }
            }
        }
    }
    double SigIntegralLow = 1.09;
    double SigIntegralHigh = 1.15;
    
    public void fitHistograms(double mean, double w1, double w2, double min, double max) {
        List<TCanvas> cans = new ArrayList<>();
        int[] ctr = new int[nVarSteps3];
        for(int i = 0; i<nVarSteps3; i++) {
            cans.add(new TCanvas("Vtx plots", 1200, 800));
            cans.get(cans.size()-1).divide(nVarSteps1, nVarSteps2);
        }
        for(int k = 0; k<nVarSteps3; k++) {
            ctr[k]=0;
            for(int i = 0; i<nVarSteps1; i++) {
                for(int j = 0; j<nVarSteps2; j++) {
                    cans.get(k).cd(ctr[k]);
                    H1F h = this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})).histClone("h");
                    //h.setTitle(this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})).getTitle());
                    h.setFillColor(49);
                    cans.get(k).draw(h);
                    cans.get(k).draw(this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})), "Esame");
                    FitFunction f = new FitFunction(this.getHistosMap().get(new IntArrayWrapper(new int[]{i,j,k})));
                    
                    f.runFit();
                    MnUserParameters parsb = f.pars;
                    FitLine l = new FitLine("sb", fitLowEdge, fitHighEdge, parsb, f); 
                    l.setLineColor(8);
                    l.setLineWidth(3);
                    l.setLineStyle(5);
                    
                    double bkg =0;
                    double N =0;
                    for(int ib =0; ib<h.getDataSize(0); ib++) {
                        double x = h.getDataX(ib);
                        //if(x<1.09) continue;
                        double yfit = l.evaluate(x);
                        double y = h.getDataY(ib);
                        double ey = h.getDataEY(ib);
                        if(yfit>0 && x>1.09) {
                            if(yfit>y) yfit=y;
                            bkg+=yfit;
                        } 
                        N+=y; //System.out.println("x "+x+" y "+y+" N "+N);
                        if(x>1.15) {
                            SigIntegralHigh=h.getDataX(ib)+(h.getDataX(ib+1)-h.getDataX(ib))*0.5;
                            break;
                        }
                    }
                      double sig = N-bkg;
                    double sigOvBgErr = 0;
                    LatexText lt = new LatexText("S="+decfor.format(sig)+" +/- "+decfor.format(Math.sqrt(N)));
                    
                    lt.setLocation(104, 15);
                    lt.setFontSize(14);
                    lt.setFont("Helvetica");
                    lt.setColor(2);
                    LatexText lt2 = new LatexText("S/B="+decfor2.format(sig/bkg)+" +/- "+decfor2.format((1.0/bkg)*Math.sqrt(N+sig*sig/(bkg*bkg))));
                    lt2.setLocation(104, 35);
                    lt2.setFontSize(14);
                    lt2.setFont("Helvetica");
                    lt2.setColor(2);
                    cans.get(k).draw(lt);
                    cans.get(k).draw(lt2);
                    cans.get(k).draw(l, "same");
                    
                    ctr[k]++;
                }
            }
        }
    }
    
    public void fitFcn(H1F histo, double mean, double sigma, double sigma2, 
            double min, double max, TCanvas can) {
        
        int   maxBin = histo.getMaximumBin();
        double   amp = histo.getBinContent(maxBin);
        double r = 0.3;
        double x0 = 1.09; //fcn must go to 0 at 1.09
        
        F1D f = new F1D("f", "-([a]*1.09+[b]*1.09*1.09+[c]*1.09*1.09*1.09)+[a]*x+[b]*x*x+[c]*x*x*x", 0, 50.0);
        f.setRange(1.13, 1.2);
        f.setParameter(0, 1);
        f.setParameter(1, 1);
        f.setParameter(2, 1);
        DataFitter.fit(f, histo, "Q");
        f.setLineWidth(0);
        F1D fg = new F1D("fg", "[amp]*gaus(x,[mean],[sigma])+[amp]*[r]*gaus(x,[mean2],[sigma2])", 0, 50.0);
        fg.setParameter(0, amp);
        fg.setParameter(1, mean);
        fg.setParameter(4, mean);
        fg.setParameter(2, sigma);
        fg.setParameter(5, sigma2);
        fg.setParameter(3, r);
        fg.setParLimits(3, 0.0, 0.5);
        fg.setRange(1.095, 1.18);
        DataFitter.fit(fg, histo, "Q");
        fg.setLineWidth(0);
        F1D f1 = new F1D("f1", "[amp]*gaus(x,[mean],[sigma])+[amp]*[r]*gaus(x,[mean2],[sigma2])+(-([a]*1.09+[b]*1.09*1.09+[c]*1.09*1.09*1.09)+[a]*x+[b]*x*x+[c]*x*x*x)", 0, 50.0);
        f1.setParameter(0, fg.getParameter(0));
        f1.setParameter(1, fg.getParameter(1));
        f1.setParameter(2, fg.getParameter(2));
        f1.setParameter(3, fg.getParameter(3));
        f1.setParameter(4, fg.getParameter(4));
        f1.setParameter(5, fg.getParameter(5));
        
        f1.setParLimits(3, 0.0, 0.3);
        f1.setRange(1.09, 1.25);
        f1.setParameter(6, f.getParameter(0));
        f1.setParameter(7, f.getParameter(1));
        f1.setParameter(8, f.getParameter(2));
        f1.setLineWidth(0);
        DataFitter.fit(f1, histo, "Q");
        F1D f2 = new F1D("f2", "-([a]*1.09+[b]*1.09*1.09+[c]*1.09*1.09*1.09)+[a]*x+[b]*x*x+[c]*x*x*x", 0, 50.0);
        
        f2.setParameter(0, f1.getParameter(6));
        f2.setParameter(1, f1.getParameter(7));
        f2.setParameter(2, f1.getParameter(8));
        f2.setLineColor(3);
        f2.setLineStyle(4);
        f2.setLineWidth(2);
        
        double histoWidth = histo.getDataX(1)-histo.getDataX(0);
        
        double S = 0;
        for(int i = 0; i<50; i++) {
            if(histo.getDataX(i)<1.15) {
                S+=histo.getBinContent(i);
            } else {
                break;
            }
        }
        f2.setRange(1.09, 1.15);
        double B = Math.ceil(f2.getIntegral()/histoWidth);
        S-=B;
        //String st = "Yield = "+S+ "; Signif = "+S/Math.sqrt(S+B);
        double signif = (float)(S/B);
        
        F1D t = new F1D("t", "([Yield]+[Bg]*x+[SoverB]*x*x)*[c]", 0, 50.0);
        t.setRange(1.05, 1.80);
        t.setParameter(0, S);
        t.setParameter(1, B);
        t.setParameter(2, signif);
        t.setParameter(3, 0);
        t.setOptStat(1110);
        
        can.draw(t, "same");
        histo.setFillColor(49);
        can.draw(histo, "same");
        can.draw(f2, "same");
        
    }  
    
    public static class IntArrayWrapper {
        private int[] intArray;

        public IntArrayWrapper(int[] intArray) {
            this.intArray = intArray;
        }

        public int[] getIntArray() {
            return intArray;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntArrayWrapper that = (IntArrayWrapper) o;
            return Arrays.equals(intArray, that.intArray);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(intArray);
        }
    }

}
