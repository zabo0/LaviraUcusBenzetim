package com.saboon;

import com.saboon.Atmosphere.Atmosphere;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Main {


    static double g = 9.801;
    static double time0 = 0;
    static double time_iteration = 0.01;

    static double theta0 = 85;

    static double mass0_total = 25;
    static double mass0_engine = 4.659;
    static double altitude0 = 980;
    static double isp = 197.6; //209.5; //197.6
    static double A_area = Math.PI * Math.pow(0.14,2);

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


        double altitude = altitude0;
        double previousTheta = theta0;
        double previousVelocity = V0;
        double previousXposition = x0_position;
        double previousZposition = z0_position;

        System.out.println("\ttime\t\t\t|\t\tx position\t\t\t|\t\t\tz position\t\t\t|\t\tvelocity\t\t\t|\t\t\ttheta\t\t\t|\t\taltitude");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");


        for( int i = 0; i<2000; i ++) {


            double velocity = V(i,altitude,previousTheta,previousVelocity);
            double theta = theta(previousTheta, velocity);

            double xPosition = x_position(velocity, theta, previousXposition);
            double zPosition = z_position(velocity, theta, previousZposition);

            x_positions.add(xPosition);
            z_positions.add(zPosition);

            //iteration += time_iteration;
            altitude = altitude + zPosition - previousZposition;
            previousTheta = theta;
            previousVelocity = velocity;
            previousXposition = xPosition;
            previousZposition = zPosition;

            System.out.println((double)i/100 + "  \t\t\t\t\t" + x_positions.get(i) + "  \t\t  " + z_positions.get(i) + "  \t\t      " + velocity + "   \t\t  " + theta + "  \t\t" + altitude);
        }




    }

    static double V(int iteration, double altitude, double previousTheta, double previousVelocity) {

        double dynamicPressure = q_dynamicPressure(atm.Density(atm.Temperature(altitude), atm.Pressure(altitude)), previousVelocity);
        double speedOfSound = atm.SpeedOfSound(atm.Temperature(altitude));

        double kd = k_drag(dynamicPressure, Cd(altitude, previousVelocity, speedOfSound), A_area, d_mass(iteration));
        double kt = k_thrust(F_thrust(iteration),d_mass(iteration));

        double velocity = (kt - g * Math.cos(Math.toRadians(previousTheta)) - kd * Math.pow(previousVelocity, 2)) * time_iteration + previousVelocity;

        return velocity;
    }

    static double x_position(double velocity, double theta, double previousXPosition) {
        return (velocity * Math.cos(Math.toRadians(theta))) * time_iteration + previousXPosition;
    }

    static double z_position(double velocity, double theta, double previousZPosition) {
        return (velocity * Math.sin(Math.toRadians(theta))) * time_iteration + previousZPosition;
    }

    static double k_drag(double q_dynPressure, double Cd, double area, double mass) {
        return (q_dynPressure*Cd*area)/(mass);
    }

    static double k_thrust(double F_thrust, double mass) {
        return F_thrust/mass;
    }

    static double d_mass(int iteration) {
        double dMass = F_thrust(iteration)*time_iteration/(isp*g);
        return mass0_total - dMass;
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

    static double theta(double previousTheta, double velocity) {
        return ((g/velocity) * Math.sin(previousTheta))* time_iteration + previousTheta;
    }

    static double Cd(double altitude, double velocity, double speedOfSound) {
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
    }

    static double q_dynamicPressure(double density, double velocity) {
        return (density * velocity * velocity)/2;
    }








}
