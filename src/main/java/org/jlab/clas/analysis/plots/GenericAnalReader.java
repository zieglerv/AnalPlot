/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.jlab.clas.analysis.plots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.clas.analysis.Constants;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

/**
 *
 * @author veronique
 */
public class GenericAnalReader {
    public Electron electron;
    private int parentPID;
    public int evParticlePID;
    public double evParticlePIDMassCut = 0.035;
    public List<Particle> evParticle = new ArrayList<>();
    
    public List<Map<String, Particle>> particleMaps = new ArrayList<>(); 
    private Map<Integer, String> _particlesPID = new HashMap<>(); //Follows REC::ANAL convention
    //e.g. Omega --> Lambda K-; Lambda --> p pi- 
    /*
        _particlesPID.put(0, "Omega");
        _particlesPID.put(1, "Lambda");
        _particlesPID.put(2, "p");
        _particlesPID.put(3, "pi");
        _particlesPID.put(4, "K");
    */

    /**
     * @return the parentPID
     */
    public int getParentPID() {
        return parentPID;
    }

    /**
     * @param parentPID the parentPID to set
     */
    public void setParentPID(int parentPID) {
        this.parentPID = parentPID;
    }
    
    public GenericAnalReader(int parentPID, Map<Integer, String> particlePIDs) {
        _particlesPID = particlePIDs; 
        this.setParentPID(parentPID); 
    }
    
    public enum Target {
        PROTON(0.93827),
        DEUTERON(1.875612);

        private final double mass;

        Target(double mass) {
            this.mass = mass;
        }

        public double getMass() {
            return mass;
        }
    }
    public void readBanks(DataEvent event, int parentIdx, double beamE, Target tar, 
            int idref) { //idx of particle wrt which vertex displacement is calculated
        particleMaps.clear();
        electron = new Electron();
        evParticle.clear();
        
        double e0 = 0;
        double p0x = 0;
        double p0y = 0;
        double p0z = 0;
        double v0x = 0;
        double v0y = 0;
        double v0z = 0;
        
        DataBank bank = null;
        if(event.hasBank("ANAL::Particle")) {
            bank = event.getBank("ANAL::Particle");
        }
        DataBank bankb = null;
        if(event.hasBank("REC::Particle")) {
            bankb = event.getBank("REC::Particle");
            if(bankb.getInt("pid", 0) ==11) {
                p0x =  bankb.getFloat("px",0);
                p0y =  bankb.getFloat("py",0);
                p0z =  bankb.getFloat("pz",0);
                v0x =  bankb.getFloat("vx",0);
                v0y =  bankb.getFloat("vy",0);
                v0z =  bankb.getFloat("vz",0);
                e0 = Math.sqrt(0.0005*0.0005+p0x*p0x+p0y*p0y+p0z*p0z);
                electron.e=e0;
                electron.px=p0x;
                electron.py=p0y;
                electron.pz=p0z;
            }
            for(int i = 0; i<bankb.rows(); i++) {
                boolean ismatched = false; 
                if(event.hasBank("RECFT::Particle") && event.getBank("RECFT::Particle").getInt("pid", i) == this.evParticlePID )
                    ismatched=true;
                if(!event.hasBank("RECFT::Particle") && bankb.getInt("pid", i) == this.evParticlePID ) 
                    ismatched=true;
                if(ismatched==true) { 
                    double pex =  bankb.getFloat("px",i);
                    double pey =  bankb.getFloat("py",i);
                    double pez =  bankb.getFloat("pz",i);
                    double vex =  bankb.getFloat("vx",i);
                    double vey =  bankb.getFloat("vy",i);
                    double vez =  bankb.getFloat("vz",i);
                    double beta =  bankb.getFloat("beta",i);
                    double mass = Constants.particleMap.get(this.evParticlePID).mass();
                    double p = Math.sqrt(pex*pex+pey*pex*pey+pez*pez);
                    double pt = Math.sqrt(pex*pex+pey*pex*pey);
                    double umass = Math.sqrt((p/beta)*(p/beta)-p*p);
                    double erec = p/beta;
                    double e = Math.sqrt(mass*mass+p*p);
                    int charge = (int)Math.signum(this.evParticlePID);
                    double chi2pid = Math.abs(bankb.getFloat("chi2pid", i));
                    //add the particle to the list
                    Particle pe = new Particle();
                    
                    pe.beta=beta;
                    pe.px = pex;
                    pe.py = pey;
                    pe.pz = pez;
                    pe.vx = vex;
                    pe.vy = vey;
                    pe.vz = vez;
                    pe.erec = erec;
                    pe.e = e;
                    pe.mass = mass;
                    pe.pt = pt;
                    pe.p = p;
                    pe.umass = umass;
                    pe.charge = charge;
                    pe.pid = this.evParticlePID;
                    pe.chi2pid = chi2pid;
                    if(Math.abs(mass-umass)<this.evParticlePIDMassCut)
                        this.evParticle.add(pe);
                }
            }
        }
        DataBank bankf = null;
        if(event.hasBank("RECFT::Particle")) {
            bankf = event.getBank("RECFT::Particle");
            for(int i =0; i<bankf.rows(); i++) {
                if(bankf.getInt("pid", i) ==11 && Math.abs(bankb.getInt("status", i))/1000==1
                        ) {
                    p0x =  bankf.getFloat("px",i);
                    p0y =  bankf.getFloat("py",i);
                    p0z =  bankf.getFloat("pz",i);
                    v0x =  bankb.getFloat("vx",i);
                    v0y =  bankb.getFloat("vy",i);
                    v0z =  bankb.getFloat("vz",i);
                    e0 = Math.sqrt(0.0005*0.0005+p0x*p0x+p0y*p0y+p0z*p0z);
                    electron.e=e0;
                    electron.px=p0x;
                    electron.py=p0y;
                    electron.pz=p0z;
                    electron.isFT = true;
            }
            }
        }

        if(bank!=null) { 
            for(int il=0; il<bank.rows(); il++) {
                if(bank.getInt("pid", il) == getParentPID()) { 
                    Map<String, Particle> particleMap = new HashMap<>();
                    Particle parent = this.setParticle(bank, il);
                    parent.name = _particlesPID.get(0);
                    particleMap.put(parent.name, parent); 
                    for(int id = 1; id<_particlesPID.size(); id++) {
                        int i = il+id;
                        double beta=0;
                        double chi2pid=0;
                        double upx = 0;
                        double upy = 0;
                        double upz = 0;
                        if(bank.getByte("ndau", i)==0) {
                            int recidx =  (int) bank.getShort("idx", i)-1; 
                            upx = bankb.getFloat("px", recidx);
                            upy = bankb.getFloat("py", recidx);
                            upz = bankb.getFloat("pz", recidx);
                            chi2pid = Math.abs(bankb.getFloat("chi2pid", recidx));
                            if(bankf!=null) {
                                beta = bankf.getFloat("beta", recidx);
                                chi2pid = Math.abs(bankf.getFloat("chi2pid", recidx));
                            }
                        }
                        Particle p = this.setParticle(bank,i);
                        p.name = _particlesPID.get(id);
                        p.beta = beta;
                        p.chi2pid = chi2pid;
                        p.upx = upx;
                        p.upy = upy;
                        p.upz = upz;
                        if(id == idref) {
                            v0x = p.vx;
                            v0y = p.vy;
                            v0z = p.vz;
                        }
                        particleMap.put(p.name, p);
                        for(String nm : particleMap.keySet()) {
                            particleMap.get(nm).v0x = v0x;
                            particleMap.get(nm).v0y = v0y;
                            particleMap.get(nm).v0z = v0z;
                        }
                    }
                    this.particleMaps.add(particleMap);
                }
            }
        }
    }

    private Particle setParticle(DataBank bank, int i) {
        Particle p = new Particle();
        p.erec = bank.getFloat("erec", i);
        p.e = bank.getFloat("e", i);
        p.dau1Idx = bank.getShort("dau1idx", i);
        p.dau2Idx = bank.getShort("dau2idx", i);
        p.charge = bank.getByte("charge", i);
        p.det = bank.getByte("det", i);
        p.pid = bank.getInt("pid", i);
        p.px =  bank.getFloat("px",i);
        p.py =  bank.getFloat("py",i);
        p.pz =  bank.getFloat("pz",i);
        p.vx =  bank.getFloat("vx",i);
        p.vy =  bank.getFloat("vy",i);
        p.vz =  bank.getFloat("vz",i);
        p.r =  bank.getFloat("r",i);
        p.mass =  bank.getFloat("mass",i);
        p.pt = Math.sqrt(p.px*p.px+p.py*p.py);
        p.p = Math.sqrt(p.px*p.px+p.py*p.py+p.pz*p.pz);
        p.umass = Math.sqrt(p.erec*p.erec-p.p*p.p);
        
        return p;
    }
    public class Electron {
        public double e;
        public double px;
        public double py;
        public double pz;
        public boolean isFT;
    }
    public class Particle {
        public int idx;
        public int charge;
        public double e;
        public double erec;
        public double p;
        public double pt;
        public double px;
        public double py;
        public double pz;
        public double upx;
        public double upy;
        public double upz;
        public double vx;
        public double vy;
        public double vz;
        public double v0x;
        public double v0y;
        public double v0z;
        public double mass;
        public double umass;
        public double beta;
        public int det;
        public double r;
        public int dau1Idx;
        public int dau2Idx;
        public int pid;
        public double chi2pid;
        public String name;

        double getMissingMass(double beamE, Target target) { 
            double mmE = beamE+target.getMass()-e-electron.e; 
            double mmPx = -px-electron.px;
            double mmPy = -py-electron.py;
            double mmPz = beamE-pz-electron.pz;
            double mass2 = mmE*mmE-mmPx*mmPx-mmPy*mmPy-mmPz*mmPz;  
            if(mass2<0) return -999;
            
            return Math.sqrt(mass2);
        }

        double combine(Particle p) {
            int pid1 = this.pid; 
            double m1 = Constants.particleMap.get(pid1).mass();
            double e1 = Math.sqrt(m1*m1+this.p*this.p);
            int pid2 = p.pid;
            double m2 = Constants.particleMap.get(pid2).mass();
            double e2 = Math.sqrt(m2*m2+p.p*p.p);
            double E = e1+e2;
            double Px = this.px+p.px;
            double Py = this.py+p.py;
            double Pz = this.pz+p.pz;
            
            return Math.sqrt(E*E-Px*Px-Py*Py-Pz*Pz);
        }
    }
}
