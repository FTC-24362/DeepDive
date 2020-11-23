/**
 * Aamod, i appoint you the owner of this file.
 *
 * @author: Aamod
 * @version: 1.0
 * @status: work in progress
 */

package org.firstinspires.ftc.teamcode.Qualifier_1;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Accesories.RingDepositor;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Accesories.WobbleGoal;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Accesories.Shooter;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Accesories.Intake;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Chassis;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.EncoderChassis;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Navigations.VuforiaWebcam;
import org.firstinspires.ftc.teamcode.Qualifier_1.Components.ObjectDetection.TensorFlow;
//import org.firstinspires.ftc.teamcode.Qualifier_1.Components.Navigations.VuforiaWebcam;


public class Robot {
//    Shooter shooter = new Shooter();
//    WobbleGoal wobbleGoal = new WobbleGoal();

    private LinearOpMode op = null;
    private HardwareMap hardwareMap = null;
    private ElapsedTime runtime = null;
    final boolean isCorgi = true;
    private TensorFlow tensorFlow = null;

    // Hardware Objects
    private Chassis drivetrain = null;
    private Intake intake = null;
    private WobbleGoal wobbleGoal = null;
    private RingDepositor ringDepositor = null;
    private VuforiaWebcam vuforiaWebcam = null;
    private Shooter shooter=null;
    private EncoderChassis encoder = null;


    private double vuforiaX = 0;
    private double vuforiaY = 0;
    private double vuforiaAngle = 0;
    private double robotAngle = 0;
    public Robot(LinearOpMode opMode) {
        op = opMode;
        hardwareMap = op.hardwareMap;

        runtime = new ElapsedTime();
        drivetrain = new Chassis(op);
        if(!isCorgi){
            tensorFlow = new TensorFlow(op);
        }
        intake = new Intake(op);
        wobbleGoal = new WobbleGoal(op);
        ringDepositor = new RingDepositor(op);
        intake = new Intake(op);
        shooter=new Shooter(op);
        encoder = new EncoderChassis(op);

        // comment by victor
        // drivetrain.init(opMode);
        if(!isCorgi) {
            vuforiaWebcam.init(opMode);
        }
    }

    /*
    public Robot() {

    }
     */

    public void initChassis_no_long_in_use (LinearOpMode opMode) {
        op = opMode;
        hardwareMap = op.hardwareMap;

        //Initialize Motors
        DcMotorEx motorLeftFront;
        DcMotorEx motorRightFront;
        DcMotorEx motorLeftBack;
        DcMotorEx motorRightBack;
        DcMotorEx ShooterMotor;
        DcMotorEx wobbleGoalMotor;
        Servo shooter_Servo;


        motorLeftFront = (DcMotorEx) hardwareMap.dcMotor.get("motorLeftFront");
        motorRightFront = (DcMotorEx) hardwareMap.dcMotor.get("motorRightFront");
        motorLeftBack = (DcMotorEx) hardwareMap.dcMotor.get("motorLeftBack");
        motorRightBack = (DcMotorEx) hardwareMap.dcMotor.get("motorRightBack");
        ShooterMotor = (DcMotorEx) hardwareMap.dcMotor.get("ShooterMotor");
        wobbleGoalMotor = (DcMotorEx) hardwareMap.dcMotor.get("wobbleGoalMotor");
        shooter_Servo = (Servo) hardwareMap.servo.get("ShooterServo");
        if(!isCorgi) {
            vuforiaWebcam = new VuforiaWebcam(op, VuforiaLocalizer.CameraDirection.BACK);
        }
        // comment by Victor
        // drivetrain.init(opMode);
        if(!isCorgi) {
            vuforiaWebcam.init(opMode);
        }

        this.wobbleGoal = new WobbleGoal(op);
        intake = new Intake(op);
        if(!isCorgi) {
            vuforiaWebcam.start();
        }
        shooter=new Shooter(op);
        if(!isCorgi) {
        vuforiaWebcam.start();
        getVuforiaPosition();
        }
        if(!isCorgi) {
            op.telemetry.addData("Position","%.2f %.2f %.2f %.2f", vuforiaX, vuforiaY, vuforiaAngle, robotAngle);
            op.telemetry.update();
            op.sleep(1000);
            op.telemetry.addData("Position","%.2f %.2f %.2f %.2f", vuforiaX, vuforiaY, vuforiaAngle, robotAngle);
            op.telemetry.update();
            op.sleep(1000);
        }

    }

    public void moveVuforiaWebcam(double x, double y, double endAngle) {
        if(!isCorgi) {
            getVuforiaPosition();

            double xdifference = x - vuforiaX;
            double ydifference = y - vuforiaY;

            double turn = endAngle - robotAngle;

            double magnitude = Math.sqrt((xdifference * xdifference) + (ydifference * ydifference));

            double mAngle = robotAngle - Math.toDegrees(Math.acos(ydifference / magnitude)); //move Angle

            op.telemetry.addData("VuforiaX", "%.2f %.2f %.2f %.3f %.3f %.3f", vuforiaX, x, xdifference, robotAngle, vuforiaAngle, endAngle);
            op.telemetry.addData("VuforiaY", "%.2f %.2f %.2f %.2f %.3f %.3f", vuforiaY, y, ydifference, magnitude, turn, mAngle);
            op.telemetry.update();
            op.sleep(5000);
            drivetrain.moveAngle2(magnitude, mAngle, turn);

            getVuforiaPosition();

            op.telemetry.addData("VuforiaX", "%.2f %.2f %.2f %.3f %.3f %.3f", vuforiaX, x, xdifference, robotAngle, vuforiaAngle, endAngle);
            op.telemetry.addData("VuforiaY", "%.2f %.2f %.2f %.2f %.3f %.3f", vuforiaY, y, ydifference, magnitude, turn, mAngle);
            op.telemetry.update();
            op.sleep(5000);
        }
    }


    public void stopAllMotors() {
        drivetrain.stopAllMotors();
    }



    /******** Left Front Motor **********/
    public void moveMotorLeftFront(double distance) {
        drivetrain.moveMotorLeftFront(distance);
    }

    /******** Right Front Motor **********/
    public void moveMotorRightFront(double distance) {
        drivetrain.moveMotorRightFront(distance);
    }

    /******** Left Back Motor **********/
    public void moveMotorLeftBack(double distance) {
        drivetrain.moveMotorLeftBack(distance);
    }

    /******** Right Back Motor **********/
    public void moveMotorRightBack(double distance) {
        drivetrain.moveMotorRightBack(distance);
    }



    public double getAngle() {
        return drivetrain.getAngle();
    }


    /**
     * Directional Movement
     **/
    public void moveForward(double distance, double power) {
        drivetrain.moveForward(distance, power);
    }

    public void moveForwardIMU(double distance, double power) {
        drivetrain.moveForwardIMU(distance, power);
    }

    public void moveBackward(double distance, double power) {
        drivetrain.moveBackward(distance, power);
    }

    public void moveBackwardIMU(double distance, double power) {
        drivetrain.moveBackwardIMU(distance, power);
    }

    public void moveRight(double distance, double power) {
        drivetrain.moveRight(distance, power);
    }
    public void moveAngle(double x, double y, double power){
        encoder.moveAngle(x,y,power);
    }

    public void moveRightIMU(double distance, double power, double startingAngle, double gain, double maxCorrection) {
        drivetrain.moveRightIMU(distance, power, startingAngle, gain, maxCorrection);
    }

    public void moveLeft(double distance, double power) {
        drivetrain.moveLeft(distance, power);
    }

    public void multidirectionalMove(double power, double angle, float rightStick) {
        drivetrain.multidirectionalMove(power, angle, rightStick);
    }

    public void moveLeftIMU(double distance, double power, double startingAngle, double gain, double maxCorrection) {
        drivetrain.moveLeftIMU(distance, power, startingAngle, gain, maxCorrection);
    }

    public void moveAngle2(double distance, double angle, double turn) {
        drivetrain.moveAngle2(distance, angle, turn);
    }



    /**Vuforia**/

    public double getVuforiaAngle() {
        if(!isCorgi) {
            return vuforiaWebcam.getVuforiaAngle();
        }
        return 0;
    }

    public void getVuforiaPosition() {
        if(!isCorgi) {
            vuforiaX = vuforiaWebcam.getVuforiaX();
            vuforiaY = vuforiaWebcam.getVuforiaY();
            vuforiaAngle = vuforiaWebcam.getVuforiaAngle();
            robotAngle = vuforiaAngle + 90;
            robotAngle = (robotAngle > 180 ? robotAngle - 360 : robotAngle);
        }
    }
    public void stopVuforia() {
        if(!isCorgi) {
            vuforiaWebcam.interrupt();
        }
    }



    /**TensorFlow**/

    public void initTensorFlow() {

    }

    public void runTensorFlow () {

    }

    public void stopTensorFlow () {

    }
    public void motor_track(){
        encoder.track();
    }

    /**Odometry**/

    public void turnOdometry(double target, double power) {
        drivetrain.turnOdometry(target,power);
    }

    public void moveForwardOdometry(double distance, double power) {
        drivetrain.moveForwardOdometry(distance,power);
    }

    public void moveSideOdometry(double distance, double power) {
        //right is positive use distance to change direction
        drivetrain.moveSideOdometry(distance,power);
    }


    public void xyPath(double x, double y, double power) {
        drivetrain.xyPath(x,y,power);
    }

    public void StraightxyPath(double x, double y, double power) {
        drivetrain.StraightxyPath(x,y,power);
    }

    public void StraightPointPath(double x, double y, double power) {
        drivetrain.StraightPointPath(x,y,power);
    }

    public void DirectPointPath(double x, double y, double power) {
        drivetrain.DirectPointPath(x,y,power);
    }

    public void DirectxyPath(double x, double y, double power) {
        drivetrain.DirectxyPath(x,y,power);
    }
    public void moveAngleOdometry(double x, double y, double power){
        drivetrain.moveAngleOdometry(x,y,power);
    }


    /**
     * wobble goal methods
     */
    public WobbleGoal.Position wobbleGoalGoToPosition(WobbleGoal.Position p){
        wobbleGoal.goToPosition(p);
        return(p);
    }

    public void printCurrentWobbleGoalLocation(){
        wobbleGoal.printCurrentLocation();
    }

    public void stopWobbleGoal(){
        wobbleGoal.stop();
    }

    public void moveWobbleGoalServo(boolean direction){
        wobbleGoal.moveWobbleGoalServo(direction);
    }

    // ring depositor
    public void ringDepositorGoToPosition(RingDepositor.Position p){
        ringDepositor.goToPosition(p);
    }

    public void printCurrentRingDepositorLocation() {
        ringDepositor.printCurrentLocation();
    }

    public void stopRingDepositor(){
        ringDepositor.stop();
    }

    public void moveRingClamp(boolean direction) {
        ringDepositor.moveRingClamp(direction);
    }

    // intake
    public void startIntake(){
        intake.startIntake();
    }

    public void stopIntake(){
        intake.stopIntake();
    }

    //shooter

    public void moveShooterMotor(int distance, int power) {
        double sleepTime = (distance / 1 * 1000);

        shooter.shooterMotor.setMode(DcMotor.RunMode.RESET_ENCODERS);

        shooter.shooterMotor.setTargetPosition(distance);

        shooter.shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooter.shooterMotor.setTargetPosition(distance);
        shooter.shooterMotor.setPower(power);
        if (shooter.shooterMotor.getCurrentPosition() == distance) {
            shooter.shooterMotor.setPower(0);
        }

    }

    public void setShooterServoPosition(double position) {
        shooter.moveServoPosition(position);
    }

    public void shootHighGoal(int rings) {
        shooter.shootHighGoal(rings);
    }

    public void shootMidGoal(int distance) {
        shooter.shootMidGoal(distance);
    }

    public void shootLowGoal(int distance) {
        shooter.shootLowGoal(distance);
    }

    public void moveServo(boolean direction) {
        shooter.moveServo(direction);
    }

    public void shootGoalTeleop(int direction, int power) {
        shooter.shootGoalTeleop(direction, power);
    }



}