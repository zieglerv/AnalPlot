/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.groot.data.H1F;

/**
 *
 * @author veronique
 */
public class Test {
    public String vtxReaction;
    public double vtxyStep = 0.05; //0.5 mm increments
    public double vtzStep = 0.5;   //5 mm increments
    public int nVtxySteps = 6;
    public int nVtzSteps = 6;
    public Map<int[], H1F> vtxHistosMap = new HashMap<>();
    public int nBins = 150;
    public double vtxLowEdge  = 1.0;
    public double vtxHighEdge = 1.75;
    
    public void init() {
        for(int i = 0; i<nVtxySteps; i++) {
            for(int j = 0; j<nVtzSteps; j++) {
		double cvtxy=vtxyStep*i;
		double cvtz=vtzStep*j;
                int[] v = new int[2];
                v[0] = i;
                v[1] = j;
                vtxHistosMap.put(v, new H1F("Mass, vtxy="+cvtxy+", vtz="+cvtz, vtxReaction+" (GeV)", 
                        "Counts/"+1000.0*(vtxHighEdge-vtxLowEdge)/(float) nBins, 
                        nBins, vtxLowEdge, vtxHighEdge));
            }
        }
    }
    
    public void fillVtxHistos(double vtxy, double vtz, double mass) {
        int i = (int)Math.floor(vtxy/(double)vtxyStep);
        int j = (int)Math.floor(vtz/(double)vtzStep);
        int[] v = new int[] {i,j};
        
        this.vtxHistosMap.get(v).fill(mass);
    }
}
