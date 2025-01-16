package org.firstinspires.ftc.teamcode;

public class PIDController {
    private double kp;
    private double ki;
    private double kd;
    private double previousError;
    private double integral;

    public PIDController(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.previousError = 0;
        this.integral = 0;
    }

    public double calculate(double targetPosition, double currentPosition) {
        double error = targetPosition - currentPosition;
        integral += error;
        double derivative = error - previousError;
        double output = kp * error + ki * integral + kd * derivative;
        previousError = error;

        // Clamping the output to be between -1 and 1
        if (output > 1) {
            output = 1;
        } else if (output < -1) {
            output = -1;
        }
        return output;
    }
}

