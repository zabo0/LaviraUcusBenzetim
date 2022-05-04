package com.saboon;

import java.util.ArrayList;

public class Atmosphere {

    static ArrayList<Double> table_Hb = new ArrayList<>();
    static ArrayList<Double> table_Lmb = new ArrayList<>();
    static ArrayList<Double> table_Tmb = new ArrayList<>();
    static ArrayList<Double> table_Pb = new ArrayList<>();

    public Atmosphere() {
        table();
    }

    static void table() {
        table_Hb.add(00000.0);
        table_Hb.add(11000.0);
        table_Hb.add(20000.0);
        table_Hb.add(32000.0);
        table_Hb.add(47000.0);
        table_Hb.add(51000.0);
        table_Hb.add(71000.0);
        table_Hb.add(84852.0);

        table_Lmb.add(-0.0065);
        table_Lmb.add(0.0000);
        table_Lmb.add(0.0010);
        table_Lmb.add(0.0028);
        table_Lmb.add(0.0000);
        table_Lmb.add(0.0028);
        table_Lmb.add(0.0020);
        table_Lmb.add(0.0000);

        table_Tmb.add(288.150);
        table_Tmb.add(216.650);
        table_Tmb.add(216.650);
        table_Tmb.add(228.650);
        table_Tmb.add(270.650);
        table_Tmb.add(270.650);
        table_Tmb.add(214.650);
        table_Tmb.add(186.946);

        table_Pb.add(1.01325000000000E+5);
        table_Pb.add(2.26320639734629E+4);
        table_Pb.add(5.47488866967777E+3);
        table_Pb.add(8.68018684755228E+2);
        table_Pb.add(1.10906305554966E+2);
        table_Pb.add(6.69388731186873E+1);
        table_Pb.add(3.95642042804073E+0);
        table_Pb.add(3.73383589976215E-1);
    }

    public static double Temperature(double altitude) {
        double H = altitude*6356766/(altitude+6356766);
        int i;
        for (i=0;i<table_Hb.size();i++) {
            if(H<table_Hb.get(i+2)) {
                break;
            }
        }
        return table_Tmb.get(i)+table_Lmb.get(i)*(H-table_Hb.get(i));
    }

    public static double Pressure(double altitude) {
        double H = altitude*6356766/(altitude+6356766);
        int i;
        for (i=0;i<table_Hb.size();i++) {
            double table = table_Hb.get(i+2);
            if(H<table) {
                break;
            }
        }
        double C = -0.0341631947363104;
        double Hb = table_Hb.get(i);
        double Lb = table_Lmb.get(i);
        double Tb = table_Tmb.get(i);
        double Pb = table_Pb.get(i);

        return Pb*(Math.abs(Lb)>1E-12?Math.pow(1+Lb/Tb*(H-Hb), C/Lb):Math.exp(C*(H-Hb)/Tb));
    }

    public static double Density(double temperature, double pressure) {
        return pressure*0.00348367635597379/temperature;
    }

    public static double SpeedOfSound(double temperature){
        return Math.sqrt(401.87430086589*temperature);
    }
}
