/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jlab.io.base.DataEvent;
import org.jlab.io.hipo.HipoDataSource;
import org.jlab.jnp.hipo4.data.SchemaFactory;
import org.jlab.utils.system.ClasUtilsFile;
import org.jlab.clas.analysis.Constants;
import org.jlab.geom.prim.Vector3D;

/**
 *
 * @author veronique
 */
public class LambdaPiAnalyzer {
    public static double beamE = 10.1998;
    public static void main(String[] args) throws FileNotFoundException {
        //double beamE = 10.6;
        System.setProperty("CLAS12DIR", "/Users/veronique/Work/HighLumi/coatjava/coatjava/");
        //String mapDir = CLASResources.getResourcePath("etc")+"/data/magfield";
        //System.out.println(mapDir);
        //try {
        //    MagneticFields.getInstance().initializeMagneticFields(mapDir,
        //            "Symm_torus_r2501_phi16_z251_24Apr2018.dat","Symm_solenoid_r601_phi1_z1201_13June2018.dat");
        //}
        //catch (Exception e) {
        //    e.printStackTrace();
        //}
        //String var = "fall2018";
        String dir = ClasUtilsFile.getResourceDir("CLAS12DIR", "etc/bankdefs/hipo4");
        SchemaFactory schemaFactory = new SchemaFactory();
        schemaFactory.initFromDirectory(dir);
        
        // Show a popup to enter the input file name
        String inputFile = JOptionPane.showInputDialog("Enter the input file name:");
     
        // Check if the user canceled the input
        if (inputFile == null) {
            System.out.println("No input file provided. Exiting.");
            return;
        }

        //String inputFile = GenericAnalyzer.getInputFile();
        
        // Show a popup to enter the beam energy
        String numberStr = JOptionPane.showInputDialog("Enter the beam energy:");
        
        // Check if the user canceled the input or entered an invalid number
        if (numberStr == null) {
            System.out.println("No number provided. Exiting.");
            return;
        }

        try {
            // Parse the number as a double
            beamE = Double.parseDouble(numberStr);

            // Now you can use the inputFile and number in your method
            // Do something with inputFile and number
            System.out.println("Input File: " + inputFile);
            System.out.println("Beam energy: " + beamE +" GeV");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Exiting.");
        }
    
        
        Map<Integer, String> particlesPID = new HashMap<>();
        int parentPID = 997;
        particlesPID.put(0, "LambdaPi");
        particlesPID.put(1, "Lambda");
        particlesPID.put(2, "p");
        particlesPID.put(3, "pi");
        particlesPID.put(4, "Pi");
        
        GenericAnalyzer gan = new GenericAnalyzer(parentPID, particlesPID, 4);
        gan.gar.evParticlePID = 321;
        gan.gar.evParticlePIDMassCut=0.035;
        GenericHistograms vtxH = new GenericHistograms();
        vtxH.setReaction("m(p #pi^-)");
        vtxH.setCut1("VtXY");
        vtxH.setCut2("VtZ");
        vtxH.setCut3("pt");
        vtxH.setVarStep1(0.05);
        vtxH.setVarStep2(0.5);
        vtxH.setVarStep3(0.25);
        vtxH.setnVarSteps1(6);
        vtxH.setnVarSteps2(6);
        vtxH.setnVarSteps3(2);
        vtxH.setnBins(150);
        vtxH.sethLowEdge(1.05);
        vtxH.sethHighEdge(1.8);
        vtxH.init();
        
//        GenericHistograms vtxHu = new GenericHistograms();
//        vtxHu.setReaction("m(p #pi^-)");
//        vtxHu.setCut1("VtXY");
//        vtxHu.setCut2("VtZ");
//        vtxHu.setCut3("pt");
//        vtxHu.setVarStep1(0.05);
//        vtxHu.setVarStep2(0.5);
//        vtxHu.setVarStep3(0.25);
//        vtxHu.setnVarSteps1(6);
//        vtxHu.setnVarSteps2(6);
//        vtxHu.setnVarSteps3(2);
//        vtxHu.setnBins(150);
//        vtxHu.sethLowEdge(1.05);
//        vtxHu.sethHighEdge(1.8);
//        vtxHu.init();
        
        GenericHistograms vtxH2 = new GenericHistograms();
        vtxH2.setReaction("m(p #pi^- #pi^-)");
        vtxH2.setCut1("VtXY");
        vtxH2.setCut2("VtZ");
        vtxH2.setCut3("pt");
        vtxH2.setVarStep1(0.05);
        vtxH2.setVarStep2(0.5);
        vtxH2.setVarStep3(0.25);
        vtxH2.setnVarSteps1(6);
        vtxH2.setnVarSteps2(6);
        vtxH2.setnVarSteps3(2);
        vtxH2.setnBins(140);
        vtxH2.sethLowEdge(1.1);
        vtxH2.sethHighEdge(2.5);
        vtxH2.init();
        
        int counter = 0;

        HipoDataSource reader = new HipoDataSource();
        reader.open(inputFile);

        while (reader.hasEvent()) {
            counter++;
            DataEvent event = reader.getNextEvent();
            double hmass=0;      
            gan.processEvent(event, beamE, GenericAnalReader.Target.PROTON);
            List<Map<String, GenericAnalReader.Particle>> particlesMap = gan.gar.particleMaps;
            //if(particlesMap.isEmpty()) continue;
            for(Map<String, GenericAnalReader.Particle> m : particlesMap) {
                if(!m.containsKey("Lambda")) continue;
                //if(gan.gar.evParticle.isEmpty()) continue;
                if(m.get("Lambda").r<5 && m.get("p").chi2pid<15 && m.get("pi").chi2pid<15 && m.get("Pi").chi2pid<15 
                       // && Math.abs(m.get("K").mass-m.get("Lambda").getMissingMass(beamE, GenericAnalReader.Target.PROTON))<0.035
                        //&& m.get("Pi").det== 0
                        //&& m.get("p").det==1
                        ) {
                    double dvx = m.get("Lambda").vx - m.get("Lambda").v0x;
                    double dvy = m.get("Lambda").vy - m.get("Lambda").v0y;
                    double dvz = m.get("Lambda").vz - m.get("Lambda").v0z;
                    double dvxy = Math.sqrt(dvx*dvx + dvy*dvy);
                    
                    if(hmass!=m.get("Lambda").mass)
                        vtxH.fillVtxHistos(m.get("Lambda").mass, dvxy, dvz,m.get("p").pt);
                    hmass=m.get("Lambda").mass;
//                    double EL = m.get("p").e+m.get("pi").e;
//                    double uPxL = m.get("p").upx+m.get("pi").upx;        
//                    double uPyL = m.get("p").upy+m.get("pi").upy;  
//                    double uPzL = m.get("p").upz+m.get("pi").upz;  
//                    double uLMass = Math.sqrt(EL*EL-uPxL*uPxL-uPyL*uPyL-uPzL*uPzL);
//                    
//                    vtxHu.fillVtxHistos(uLMass, dvxy, dvz,m.get("p").pt);
//                            
                    double cos2Pi = new Vector3D(m.get("pi").px,m.get("pi").py,m.get("pi").pz).asUnit().
                            dot(new Vector3D(m.get("Pi").px,m.get("Pi").py,m.get("Pi").pz).asUnit());
                    if(Math.abs(m.get("Lambda").mass-Constants.particleMap.get(m.get("Lambda").pid).mass())<0.015 
                            //&& m.get("Lambda").getMissingMass(beamE, GenericAnalReader.Target.PROTON)>0.15 
                            //&& m.get("Lambda").vz - m.get("LambdaPi").vz>2.5
                            //&& cos2Pi>0.95
                            ) {
                        vtxH2.fillVtxHistos(m.get("LambdaPi").mass, dvxy, dvz,m.get("p").pt);
                    }
                }
            }
            
            counter++;
            if(counter%100000 == 0) System.out.println("Analyzed " + counter + " events");
        }
        //vtxH.plotHistograms(48);
        //vtxHu.plotHistograms(46);
        vtxH.fitHistograms(1.115, 0.007, 0.008, 1.09, 1.8); 
        vtxH2.plotHistograms(45);
    }

    
}
