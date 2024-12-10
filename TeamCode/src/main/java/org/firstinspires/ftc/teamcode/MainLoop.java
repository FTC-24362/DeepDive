package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MainLoop extends LinearOpMode {

    private Drivetrain basicDrivetrain;
    // INSERT_DECLARE

    @Override
    public void runOpMode() throws InterruptedException {

        basicDrivetrain = new Drivetrain(hardwareMap);
        // INSERT_INIT

        waitForStart();
        while (opModeIsActive()) {

            basicDrivetrain.drive(gamepad1);
            // INSERT_METHODS
            
        }
    }
}
