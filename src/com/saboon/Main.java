package com.saboon;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    static double g = 9.801;

    static double time_iteration = 0.01;

    static double theta0 = 85;
    static ArrayList<Double> thetas = new ArrayList<>();
    static double altitude0 = 980;

    static double mass0_total = 25;

    static double isp = 209.5;
    static double A_area = Math.PI * Math.pow(0.14,2);

    static ArrayList<Double> machs = new ArrayList<>();

    static double V0 = 2;
    static double v0_x = Math.cos(Math.toRadians(theta0))*V0;
    static double v0_z = Math.sin(Math.toRadians(theta0))*V0;

    static ArrayList<Double> velocities = new ArrayList<>();
    static ArrayList<Double> xVelocities = new ArrayList<>();
    static ArrayList<Double> zVelocities = new ArrayList<>();

    static double x0_position = 0;
    static double z0_position = 0;

    static ArrayList<Double> x_positions = new ArrayList<>();
    static ArrayList<Double> z_positions = new ArrayList<>();

    static double time_apogee = 0;

    static ArrayList<Double> dynamicPressures = new ArrayList<>();

    static Atmosphere atm = new Atmosphere();
    static Aerodynamic aero = new Aerodynamic();

    public static void main(String[] args) {
        double mass = mass0_total;
        double altitude = altitude0;
        double previousVelocity = V0;
        double previousV_x = v0_x;
        double previousV_z = v0_z;
        double previousXposition = x0_position;
        double previousZposition = z0_position;

        int i = 0;
        while(true) {
            double speedOfSound = atm.SpeedOfSound(atm.Temperature(altitude));
            double density = atm.Density(atm.Temperature(altitude),atm.Pressure(altitude));

            double mach = mach(previousVelocity, speedOfSound);
            double cd = Cd(i, altitude, mach);

            double kd = k_drag(density, cd, A_area, mass);
            double kt = k_thrust(F_thrust(i),mass);

            double vz = V_z(kt, kd, previousVelocity, previousV_z);
            double vx = V_x(kt, kd, previousVelocity, previousV_x);
            double velocity = V(vx, vz);
            double theta = theta(vz, velocity);
            double xPosition = x_position(vx, previousXposition);
            double zPosition = z_position(vz, previousZposition);
            double dynamicPressure = q_dynamicPressure(density,velocity);

            x_positions.add(xPosition);
            z_positions.add(zPosition);
            velocities.add(velocity);
            xVelocities.add(vx);
            zVelocities.add(vz);
            thetas.add(theta);
            dynamicPressures.add(dynamicPressure);
            machs.add(mach);

            altitude = altitude + zPosition - previousZposition;
            previousVelocity = velocity;
            previousV_x = vx;
            previousV_z = vz;
            previousXposition = xPosition;
            previousZposition = zPosition;
            mass = mass - d_mass(i);

            if (vz <= 0){
                time_apogee = (double)i/100;
                break;
            }
            i++;
        }

        plot(x_positions, "xPos",1200,100);
        plot(z_positions, "zPos", 1510,100);
        plot(xVelocities, "vx", 1200,410);
        plot(zVelocities, "vz", 1510,410);
        plot(velocities, "velocity",1200 ,720);
        plot(dynamicPressures,"dynamicPressures",1510,720);
        plot(thetas,"theta",1200,1030);
        plot(machs,"mach",1510,1030);
    }

    static double V(double vx, double vz) {
        return Math.sqrt(vx*vx + vz*vz);
    }

    static double V_x(double kt, double kd, double previousVelocity, double previousV_x){
        return (kt * (previousV_x/previousVelocity) - kd * previousVelocity * previousV_x) * time_iteration + previousV_x;
    }

    static double V_z(double kt, double kd, double previousVelocity, double previousV_z){
        return (kt * (previousV_z/previousVelocity) - kd * previousVelocity * previousV_z - g) * time_iteration + previousV_z;
    }

    static double x_position(double vx, double previousXPosition) {
        return vx * time_iteration + previousXPosition;
    }

    static double z_position(double vz,  double previousZPosition) {
        return vz * time_iteration + previousZPosition;
    }

    static double theta(double vz, double velocity) {
        return Math.toDegrees(Math.asin(vz/velocity));
    }

    static double k_drag(double density, double Cd, double area, double mass) {
        return (density*Cd*area)/(2*mass);
    }

    static double k_thrust(double F_thrust, double mass) {
        return F_thrust/mass;
    }

    static double d_mass(int iteration) {
        return  (F_thrust(iteration) / (isp * g)) * time_iteration;
    }

    static double F_thrust(int iteration) {
        return aero.getF_Thrust().get(iteration);
    }

    static double Cd(int i, double altitude, double mach) {
        return aero.getCd().get((int) (mach*10-1));
    }

    static double mach(double velociy, double speedOfSound){
        return velociy/speedOfSound;
    }

    static double q_dynamicPressure(double density, double velocity) {
        return (density * velocity * velocity)/2;
    }

    static void plot(ArrayList<Double> values, String title, int locationX, int locationY){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Graph(values));
        frame.setSize(300, 300);
        frame.setLocation(locationX, locationY);
        frame.setTitle(title);
        frame.setVisible(true);
    }

}
