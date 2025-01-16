package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.PIDController;

public class Arm {
    private DcMotorEx armMotorA, armMotorB;
    private PIDController aController, bController;

    public enum ArmState {
        INTAKE_UP,
        INTAKE_DOWN,
        OUTTAKE,
        TRANSFER
    }

    ArmState armState = ArmState.TRANSFER;

    public Arm(HardwareMap hardwareMap) {
        armMotorA = hardwareMap.get(DcMotorEx.class, "armMotorA");
        armMotorB = hardwareMap.get(DcMotorEx.class, "armMotorB");

        armMotorA.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotorB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        armMotorA.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotorA.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armMotorB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotorB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        aController = new PIDController(0.002,0,0);
        bController = new PIDController(0.002,0,0);
    }

    public void setArmState(ArmState armState) {
        this.armState = armState;
    }

    public void loop() {
        switch (armState) {
            case INTAKE_DOWN:
                armMotorA.setPower(aController.calculate(0,armMotorA.getCurrentPosition()));
                armMotorB.setPower(bController.calculate(70,armMotorB.getCurrentPosition()));
                break;
            case INTAKE_UP:

                armMotorA.setPower(aController.calculate(0,armMotorA.getCurrentPosition()));
                armMotorB.setPower(bController.calculate(50,armMotorB.getCurrentPosition()));
                break;

            case TRANSFER:
                armMotorA.setPower(aController.calculate(0,armMotorA.getCurrentPosition()));
                armMotorB.setPower(bController.calculate(0,armMotorB.getCurrentPosition()));
                break;
            case OUTTAKE:

                armMotorA.setPower(aController.calculate(100,armMotorA.getCurrentPosition()));
                armMotorB.setPower(bController.calculate(70,armMotorB.getCurrentPosition()));
                break;
        }
    }


}
