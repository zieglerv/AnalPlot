/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

/**
 *
 * @author veronique
 */
public class VtxHistograms extends GenericHistograms {
    public VtxHistograms() {
        this.setReaction("m(p #pi^-)");
        this.setCut1("VtXY");
        this.setCut2("VtZ");
        this.setVarStep1(0.05);
        this.setVarStep2(0.5);
        this.setnVarSteps1(6);
        this.setnVarSteps2(6);
        this.setnBins(150);
        this.sethLowEdge(1.0);
        this.sethHighEdge(1.25);
        this.init();
    }
}
