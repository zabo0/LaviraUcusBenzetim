package com.saboon;

import com.saboon.Atmosphere.Atmosphere;

import java.util.ArrayList;

public class Main {

    static double g = 9.801;
    static double time0 = 0;
    static double time_iteration = 0.01;

    static double theta0 = 85;

    static double mass0_total = 25;
    static double mass0_engine = 4.659;
    static double altitude0 = 980;
    static double isp = 209.5; //197.6
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

    public static void main(String[] args) {

        double iteration = time0;
        double altitude = altitude0;
        double previousTheta = theta0;
        double previousVelocity = V0;
        double previousXposition = x0_position;
        double previousZposition = z0_position;

        for( int i = 0; i<500; i ++) {

            double velocity = V(iteration,altitude,previousTheta,previousVelocity);
            double theta = theta(previousTheta);

            double xPosition = x_position(velocity, theta, previousXposition);
            double zPosition = z_position(velocity, theta, previousZposition);

            x_positions.add(xPosition);
            z_positions.add(zPosition);

            iteration += time_iteration;
            altitude = altitude0 + xPosition;
            previousTheta = theta;
            previousVelocity = velocity;
            previousXposition = xPosition;
            previousZposition = zPosition;

            System.out.print(x_positions.get(i));
            System.out.println("\t"+z_positions.get(i));
        }

        //System.out.println(atm.Pressure(altitude));


    }

    static double V(double iteration, double altitude, double previousTheta, double previousVelocity) {

        double kd = k_drag(atm.Pressure(altitude), Cd(iteration), A_area, d_mass(iteration));
        double kt = k_thrust(F_thrust(),d_mass(iteration));

        double velocity = (kt - g * Math.cos(previousTheta) - kd * Math.pow(previousVelocity, 2)) * 0.01 + previousVelocity;

        return velocity;
    }

    static double x_position(double velocity, double theta, double previousXPosition) {
        return velocity * Math.sin(theta) + previousXPosition;
    }

    static double z_position(double velocity, double theta, double previousZPosition) {
        return velocity * Math.cos(theta) + previousZPosition;
    }
    static double Pressure(double altitude) {
        double pressure = atm.Pressure(altitude);
        return pressure;
    }

    static double k_drag(double pressure, double Cd, double area, double mass) {
        double kd = (pressure*Cd*area)/(2*mass);
        return kd;
    }

    static double k_thrust(double F_thrust, double mass) {
        return F_thrust/mass;
    }

    static double d_mass(double iteration) {
        double dMass = F_thrust()*iteration/isp*g;
        return mass0_total - dMass;
    }

    static double F_thrust() {
        return 450.02;
    }

    static double theta(double previousTheta) {
        double theta = (g * Math.sin(previousTheta))* 0.01 + previousTheta;
        return theta;
    }

    static double Cd(double iteration) {
        return 0.015971;
    }
}
