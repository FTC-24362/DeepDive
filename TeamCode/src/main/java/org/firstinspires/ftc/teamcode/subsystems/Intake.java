package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private CRServo intakeServo;
    public enum IntakeState {
        INTAKE,
        REVERSE,
        STOP
    }

    IntakeState intakeState = IntakeState.STOP;
    public Intake(HardwareMap hardwareMap){
        intakeServo = hardwareMap.get(CRServo.class,"intakeServer");
    }

    public void setIntakeState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }

    public void loop() {
        switch (intakeState) {
            case INTAKE:
                intakeServo.setPower(1);
                break;
            case STOP:
                intakeServo.setPower(0);
                break;
            case REVERSE:
                intakeServo.setPower(-1);
                break;
        }
    }
}
