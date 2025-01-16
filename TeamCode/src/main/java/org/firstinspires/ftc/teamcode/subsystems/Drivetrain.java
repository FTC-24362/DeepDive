package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {

    private final DcMotorEx leftMotor;
    private final DcMotorEx rightMotor;

    public Drivetrain(HardwareMap hardwareMap) {

        leftMotor = hardwareMap.get(DcMotorEx.class, "CM0");
        rightMotor = hardwareMap.get(DcMotorEx.class, "CM1");

        leftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        leftMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(Gamepad gamepad) {

        double y = -gamepad.left_stick_y;
        double rx = gamepad.right_stick_x;

        // Drive train calculations
        double leftPower = y + rx;
        double rightPower = y - rx;

        // Complete drive train calculations
        double denominator = Math.max(Math.max(Math.abs(leftPower), Math.abs(rightPower)), 1);
        leftPower /= denominator;
        rightPower /= denominator;

        // Set power directly to motors
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }
}
