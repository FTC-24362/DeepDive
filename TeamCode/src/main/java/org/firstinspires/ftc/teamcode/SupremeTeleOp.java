package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@TeleOp
public class SupremeTeleOp extends LinearOpMode {

    private Drivetrain drivetrain;
    private Arm arm;
    private Intake intake;

    enum State {
        INTAKE,
        OUTTAKE,
        TRANSFER
    }

    State state = State.TRANSFER;

    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain = new Drivetrain(hardwareMap);
        arm = new Arm(hardwareMap);
        intake = new Intake(hardwareMap);

        waitForStart();
        while(opModeIsActive()) {
            drivetrain.drive(gamepad1);

            switch (state) {
                case INTAKE:
                    if (gamepad1.right_bumper) {
                        arm.setArmState(Arm.ArmState.INTAKE_DOWN);
                        intake.setIntakeState(Intake.IntakeState.INTAKE);
                    }
                    else {
                        arm.setArmState(Arm.ArmState.INTAKE_UP);
                        intake.setIntakeState(Intake.IntakeState.STOP);
                    }

                    if (gamepad1.y) {
                        state = State.TRANSFER;
                    }
                    break;
                case OUTTAKE:
                    arm.setArmState(Arm.ArmState.OUTTAKE);

                    // TODO: ADD CLAW

                    if (gamepad1.a) {
                        state = State.INTAKE;
                    }
                    break;
                case TRANSFER:
                    arm.setArmState(Arm.ArmState.TRANSFER);
                    intake.setIntakeState(Intake.IntakeState.STOP);

                    if (gamepad1.x) {
                        state = State.OUTTAKE;
                    }
                    break;

            }

            arm.loop();
            intake.loop();
        }
    }
}
