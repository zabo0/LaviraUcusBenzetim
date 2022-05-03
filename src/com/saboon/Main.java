package com.saboon;

import com.saboon.Atmosphere.Atmosphere;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {


    static double g = 9.801;
    static double time0 = 0;
    static double time_iteration = 0.01;

    static double theta0 = 85;

    static double mass0_total = 28.032;    //28.032     25
    static double mass0_engine = 4.349;   //4.349  4.659
    static double altitude0 = 980;
    static double isp = 197.6; //209.5       197.6
    static double A_area = Math.PI * Math.pow(0.126,2); //0.126    0.14
    static double L_length = 2;

    static double V0 = 2;
    static double v0_x = Math.cos(Math.toRadians(theta0))*V0;
    static double v0_z = Math.sin(Math.toRadians(theta0))*V0;

    static double x0_position = 0;
    static double z0_position = 0;
    static ArrayList<Double> x_positions = new ArrayList<>();
    static ArrayList<Double> z_positions = new ArrayList<>();

    static double time_apogee;
    static double time_flight = 2 * time_apogee;


    static Atmosphere atm = new Atmosphere();
    static Aerodynamic aero = new Aerodynamic();



    public static void main(String[] args) {
        double mass = mass0_total;
        double altitude = altitude0;
        double previousTheta = theta0;
        double previousVelocity = V0;
        double previousV_x = v0_x;
        double previousV_z = v0_z;
        double previousXposition = x0_position;
        double previousZposition = z0_position;

        System.out.println("\ttime\t|\tx position\t|\tz position\t|\tvelocity\t|\tvelocity_z\t|\tvelocity_x\t|\ttheta\t\t|\taltitude\t|\tF thrust\t|\td mass\t\t|\tkt\t\t\t|\tkd\t\t\t|\tcd");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


        for( int i = 0; i<7000; i ++) {


            double dynamicPressure = q_dynamicPressure(atm.Density(atm.Temperature(altitude), atm.Pressure(altitude)), previousVelocity);
            double speedOfSound = atm.SpeedOfSound(atm.Temperature(altitude));

            double density = atm.Density(atm.Temperature(altitude),atm.Pressure(altitude));

            double cd = Cd(i, altitude, previousVelocity, speedOfSound);
            double kd = k_drag(density, cd, A_area, mass);
            double kt = k_thrust(F_thrust(i),mass);



/////////////////////////The Parallel-Perpendicular Coordinate Frame////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            double velocity = V(kt,kd,previousTheta,previousVelocity);
            double theta = theta(previousTheta, velocity);
            double vx = (velocity * Math.cos(Math.toRadians(theta))); //* time_iteration + previousV_x;
            double vz = (velocity * Math.sin(Math.toRadians(theta)));//* time_iteration + previousV_z;
            double xPosition = x_position(velocity, theta, previousXposition);
            double zPosition = z_position(velocity, theta, previousZposition);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////The X-Y Coordinate Frame///////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            double Vcr = Vcr(kt, L_length);
//            double vz = V_z(kt, kd, previousVelocity, previousV_z);
//            double vx = V_x(kt, kd, previousVelocity, previousV_x);
////            if (vx < Vcr){
////                vx = vz * (previousV_z/previousV_x);
////            }
//            double velocity = V(vx, vz);
//            double theta = theta(vz, vx, previousTheta);
//            double xPosition = x_position(vx, previousXposition);
//            double zPosition = z_position(vz, previousZposition);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



            x_positions.add(xPosition);
            z_positions.add(zPosition);

            altitude = altitude + zPosition - previousZposition;
            previousTheta = theta;
            previousVelocity = velocity;
            previousV_x = vx;
            previousV_z = vz;
            double dx = xPosition - previousXposition;
            double dz = zPosition - previousZposition;
            previousXposition = xPosition;
            previousZposition = zPosition;
            mass = mass - d_mass(i);

            if(i%10 == 0){
                System.out.println();
            }
            System.out.println(
                    "\t" + String.format("%.2f",(double)i/100)
                            + "\t\t" + String.format("%.6f", x_positions.get(i))//+"-"+String.format("%.6f", dx)
                            + "\t\t" + String.format("%.6f", z_positions.get(i))//+"-"+String.format("%.6f", dz)
                            + "\t\t" + String.format("%.6f",velocity)
                            + "\t\t" + String.format("%.6f",vz)
                            + "\t\t" + String.format("%.6f",vx)
                            + "\t\t" + String.format("%.6f",theta)
                            + "\t\t" + String.format("%.6f",altitude)
                            + "\t\t" + String.format("%.6f",F_thrust(i))
                            + "\t\t" + String.format("%.6f",mass)
                            + "\t\t" + String.format("%.6f",kt)
                            + "\t\t" + String.format("%.6f",kd)
                            + "\t\t" + String.format("%.6f",cd)
            );

            if (velocity <= 0){
                break;
            }
        }
    }


/////////////////////////The Parallel-Perpendicular Coordinate Frame////////////////////////////////////////////////////
    static double V(double kt,double kd, double previousTheta, double previousVelocity) {

        double velocity = (kt - g * Math.cos(Math.toRadians(previousTheta)) - kd * Math.pow(previousVelocity, 2)) * time_iteration + previousVelocity;

        return velocity;
    }

    static double x_position(double velocity, double theta, double previousXPosition) {
        return (velocity * Math.cos(Math.toRadians(theta))) * time_iteration + previousXPosition;
    }

    static double z_position(double velocity, double theta, double previousZPosition) {
        return (velocity * Math.sin(Math.toRadians(theta))) * time_iteration + previousZPosition;
    }

    static double theta(double previousTheta, double velocity) {
        double t = ((g/velocity) * Math.cos(previousTheta)) * time_iteration + previousTheta;
        return t;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////The X-Y Coordinate Frame///////////////////////////////////////////////////////////////////////
//    static double V(double vx, double vz) {
//
//        double velocity = Math.sqrt(vx*vx + vz*vz);
//
//        return velocity;
//    }
//
//    static double V_x(double kt, double kd, double previousVelocity, double previousV_x){
//        double V_x = (kt * (previousV_x/previousVelocity) - kd *  previousVelocity * previousV_x) * time_iteration + previousV_x;
//        return V_x;
//    }
//
//    static double V_z(double kt, double kd, double previousVelocity, double previousV_z){
//        double V_z = (kt * (previousV_z/previousVelocity) - kd * previousVelocity * previousV_z - g) * time_iteration + previousV_z;
//        return V_z;
//    }
//
//    static double Vcr(double kt, double L){
//        double vcr= Math.sqrt(2 * kt * L);
//        return vcr;
//    }
//
//    static double x_position(double vx, double previousXPosition) {
//        return vx * time_iteration + previousXPosition;
//    }
//
//    static double z_position(double vz,  double previousZPosition) {
//        return vz * time_iteration + previousZPosition;
//    }
//
//    static double theta(double vx, double vz, double previousTheta) {
//        double t = (Math.atan(vz/vx)) * time_iteration + previousTheta;
//        return t;
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    static double k_drag(double density, double Cd, double area, double mass) {
        double kd = (density*Cd*area)/(2*mass);
        return kd;
    }

    static double k_thrust(double F_thrust, double mass) {
        return F_thrust/mass;
    }

    static double d_mass(int iteration) {
        double dMass = (F_thrust(iteration) / (isp * g)) * time_iteration;
        return dMass;
    }

    static double F_thrust(int iteration) {
        double Ft;
        try {
            Ft = aero.getF_Thrust().get(iteration);
        } catch (Exception e) {
            return 0;
        }

        return Ft;
    }

    static double Cd(int i, double altitude, double velocity, double speedOfSound) {
        double mach = Double.parseDouble(new DecimalFormat("#,#").format(velocity/speedOfSound));
        double cd;
        if(mach == 0) {
            mach = 0.1;
        }

        if(altitude < 3000) {
            cd = aero.getCd_0().get((int) (mach*10-1));
            return cd;
        }else if(altitude>=3000 && altitude<6000) {
            cd = aero.getCd_3().get((int) (mach*10-1));
            return cd;
        }else if(altitude>=6000 && altitude<12000) {
            cd = aero.getCd_6().get((int) (mach*10-1));
            return cd;
        }
        cd = aero.getCd_12().get((int) (mach*10-1));
        return cd;

//        return aero.getCd().get(i);
    }

    static double q_dynamicPressure(double density, double velocity) {
        return (density * velocity * velocity)/2;
    }


}
