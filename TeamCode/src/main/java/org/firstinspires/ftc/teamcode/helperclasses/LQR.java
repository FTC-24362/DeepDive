package org.firstinspires.ftc.teamcode.helperclasses;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.competition.Hardware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.Scanner;

public class LQR extends Application
{

    //GyroSensor gyro;
    BNO055IMU imu;
    ColorSensor colorSensor;
    Servo backServo;
    double mass = 100;
    double momentOfInertia = 1250;
    double radius = 5;
    double wheelRadius = 1;
    double angle = 0;
    double lastAngle = 0;
    double lastTime = 0;
    double lastX = 0;
    double lastY = 0;

    public static double[][][] path;

    Hardware robot;

    public LQR(Hardware robot)
    {

        this.robot=robot;

    }

    /**
     * Tests if the robot is within a circle of given radius and center
     * @param x the robot's x position
     * @param y the robot's y postition
     * @param r radius of circle
     * @return boolean indicating if the robot is within a circle of radius r
     * */
    public boolean robotInCircle(double x, double y, double r)
    {

        return Math.pow(y-Hardware.y,2)+Math.pow(x-Hardware.x,2)<Math.pow(r,2);

    }

    /**
     * Runs the robot to a goal position using LQR (linear quadratic regulator)
     * @param path LQR path data generated by our LQR python script this data should be loaded with the load path method
     * @param xGoal the x position (in inches) for the robot to move to on the field
     * @param yGoal the y position (in inches) for the robot to move to on the field
     * @param thetaGoal the angular position (in degrees) for the robot to move to on the field
     * */
    public void runLqrDrive(double[][][] path, double xGoal, double yGoal, double thetaGoal)
    {

        //get matrix for current angle
        int fileLocation=(int) Math.abs(Math.round((Math.PI*2-Hardware.theta) * 1800/Math.PI));
        if(fileLocation==3600)
            fileLocation=0;
        double[][] k = path[fileLocation];
        double[] x;

        //flip the angle based on if the robot should be turning clockwise or counterclockwise
        byte sign = 1;
        double diff = thetaGoal-Hardware.theta;
        if(diff<0)
            diff+=2*Math.PI;
        if(diff>Math.PI)
            sign=-1;
        if(diff>Math.PI)
            diff=Math.PI*2-diff;

        //define state matrix
        x = new double[]{-xGoal + Hardware.x,  yGoal-Hardware.y, -sign*(diff), -robot.xVelocity/8,  -robot.yVelocity/8, -robot.thetaVelocity/8};

        //multiply x by the gain matrix k
        double[] d = new double[k.length];
        for (int i = 0; i < k.length; i++) {

            d[i] = 0;

            for (int j = 0; j < k[0].length; j++) {

                d[i] += k[i][j] * x[j];

            }

        }

        //scale the motor power bellow 1
        double scale = 1;
        if (Math.abs(d[0]) > 1 || Math.abs(d[1]) > 1 || Math.abs(d[2]) > 1 || Math.abs(d[3]) > 1)
            scale = Math.max(Math.max(Math.abs(d[0]), Math.abs(d[1])), Math.max(Math.abs(d[2]), Math.abs(d[3])));

        //run the motors
        robot.leftFront.setPower(d[0]/scale);
        robot.rightFront.setPower(d[1]/scale);
        robot.leftRear.setPower(d[2]/scale);
        robot.rightRear.setPower(d[3]/scale);

    }

    /**
     * @param file name of file where LQR data is located
     * @return matrix of LQR gain matrices
     * */
    public double[][][] loadPath(String file) throws IOException, ClassNotFoundException
    {

        //Gets LQR matrices file
        String content = new Scanner(new File(Environment.getExternalStorageDirectory() + file)).useDelimiter("\\Z").next();

        //split the file into individual matrices
        String[] data = content.split("\r\n\r\n");

        double[][][] path = new double[3600][4][6];
        int matrix=0;
        //store these matrices in an array
        for(String d:data)
        {

            String[] rows = d.split("\r\n");
            for(int j = 0; j<4; j++)
            {

                String[] vals=rows[j].split(" ");
                for(int n = 0; n<6; n++)
                {

                    path[matrix][j][n]=Double.parseDouble(vals[n]);

                }


            }

            matrix++;

        }

        return path;

    }
}
