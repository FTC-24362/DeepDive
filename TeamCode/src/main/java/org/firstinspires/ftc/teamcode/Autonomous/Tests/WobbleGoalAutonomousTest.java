/**
 * This is the program for testing the wobble goal arm
 * @author Sid
 * @version 1.0
 * @since 2020-11-01
 */
package org.firstinspires.ftc.teamcode.Autonomous.Tests;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Accesories.WobbleGoal;
import org.firstinspires.ftc.teamcode.Components.BasicChassis;
import org.firstinspires.ftc.teamcode.Robot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

@Autonomous (name = "WobbleGoalAutonomous ", group="Tests: ")

public class WobbleGoalAutonomousTest extends LinearOpMode {

    protected DcMotor wobbleGoalMotor = null;

    public void runOpMode(){
        wobbleGoalMotor = (DcMotor) opMode.hardwareMap.get("wobbleGoalMotor");
        wobbleGoalMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        wobbleGoalMotor.setDirection(DcMotor.Direction.FORWARD);
        wobbleGoalMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobbleGoalMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();

    }
}
