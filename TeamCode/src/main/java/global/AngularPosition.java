package global;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class AngularPosition {
    public ModernRoboticsI2cCompassSensor compassSensor;
    public BNO055IMU gyroSensor;
    public boolean calibratingCompass = true;

//    public double lastHeadingGY = 0;
//    public double headingGY = 0;

    public double addGY = 0;

    public boolean isFailing = false;


    public void init(HardwareMap hwMap){
        compassSensor = hwMap.get(ModernRoboticsI2cCompassSensor.class, "cp");
        gyroSensor = hwMap.get(BNO055IMU.class, "gyro");

        compassSensor.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);

        initGyro();
        resetGyro();

    }

    public double getHeading(double robotTheta) {
        double headingGY = getHeadingGY();
        double headingCS = getHeadingCS();
        boolean gyAccurate = Math.abs(robotTheta - headingGY) < Constants.ANGLE_ACCURACY;
        boolean csAccurate = Math.abs(robotTheta - headingCS) < Constants.ANGLE_ACCURACY;
        isFailing = !gyAccurate && !csAccurate;
        if (gyAccurate && csAccurate) {
            return 0.5 * (headingGY + headingCS);
        } else if (gyAccurate) {
            return headingGY;
        } else if (csAccurate) {
            return headingCS;
        }
        return 0;
    }

    public double getHeading() {
        return 0.5 * (getHeadingGY() + getHeadingCS());
    }

    public double getHeadingCS(){
        return compassSensor.getDirection();
    }

    public void setCompassMode () {
        if (calibratingCompass && !compassSensor.isCalibrating()) {
            compassSensor.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
            calibratingCompass = false;
        }
    }
    public void initGyro(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        gyroSensor.initialize(parameters);
    }

    public void resetGyro() {
        addGY = (int) gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
//        headingGY = 0;
    }
    public double getHeadingGY() {
//        double ca = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
//        double da = ca - lastHeading;
//        if (da < -180)
//            da += 360;
//        else if (da > 180)
//            da -= 360;
//        headingGY += da;
//        lastHeadingGY = ca;
        double ang = gyroSensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle + addGY;
        ang = ang < -180 ? (ang + 360) : (ang > 180 ? (ang - 360):ang);
        return ang;
    }

}
