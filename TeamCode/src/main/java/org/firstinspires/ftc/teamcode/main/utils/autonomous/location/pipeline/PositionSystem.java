package org.firstinspires.ftc.teamcode.main.utils.autonomous.location.pipeline;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.location.pipeline.Axis.AxisReading;
import org.firstinspires.ftc.teamcode.main.utils.autonomous.sensors.NavigationSensorCollection;
import org.firstinspires.ftc.teamcode.main.utils.interactions.items.StandardIMU;

public class PositionSystem {
    public Axis leftToRight;
    public Axis upAndDown;

    public CoordinateSystem coordinateSystem;

    public StandardIMU imu;
    public StandardIMU.DataPoint imuDirection;
    public StandardIMU.ReturnData imuData;

    public PositionSystem(NavigationSensorCollection sensors) {
        leftToRight = new Axis(sensors.east, sensors.west);
        upAndDown = new Axis(sensors.north, sensors.north);
        this.imu = sensors.imu;

        coordinateSystem = new CoordinateSystem();
    }

    private void UpdateCoordinateSystem(CoordinateSystem.FieldCoordinates coordinates) {
        coordinateSystem.update(coordinates);
    }

    public void SetAngle(double angle, AngleUnit unit) {
        switch (unit) {
            case DEGREES:
                coordinateSystem.angleDegrees = angle;
                break;
            case RADIANS:
                coordinateSystem.angleDegrees = Math.toDegrees(angle);
                break;
        }
    }

    public void getAndEvalReadings() {
        AxisReading ew = leftToRight.getReadings();
        AxisReading ns = upAndDown.getReadings();

        evalReadings(ew, ns);
    }

    private void evalReadings(AxisReading eastWest, AxisReading northSouth) {
        boolean eastWestValid = true;
        boolean northSouthValid = true;

        // Check which axes are valid
        if (eastWest.sensor1 + eastWest.interSensorDistance + eastWest.sensor2 + 10 > CoordinateSystem.maxWidthInCM)
            eastWestValid = false;

        if (northSouth.sensor1 + northSouth.interSensorDistance + northSouth.sensor2 + 10 > CoordinateSystem.maxLengthInCM)
            northSouthValid = false;

        if (!northSouth.sensor1Valid & !northSouth.sensor2Valid) northSouthValid = false;
        if (!eastWest.sensor1Valid & !eastWest.sensor2Valid) eastWestValid = false;

        double x = eastWest.sensor1;
        double y = northSouth.sensor1;

        // do some geometry-I honors level math
        int angleDegrees = (int) coordinateSystem.angleDegrees;
        double angleRadians = Math.toRadians(angleDegrees);

        x = x * Math.sin(angleRadians);
        y = y * Math.cos(angleRadians);

        if (x < 0 && eastWest.sensor2Valid) {
            x = eastWest.sensor2 * Math.sin(angleRadians);
        }
        else if (x < 0) {
            x = CoordinateSystem.maxWidthInCM - eastWest.interSensorDistance - Math.abs(x);
        }

        if (y < 0) {
            y = CoordinateSystem.maxLengthInCM - northSouth.interSensorDistance - Math.abs(y);
        }

        // Some special exceptions
        if (angleDegrees == 90) {
            x = CoordinateSystem.maxWidthInCM - northSouth.interSensorDistance - northSouth.sensor1;
            y = eastWest.sensor2;
        } else if (angleDegrees == 180) {
            x = eastWest.sensor2;
        } else if (angleDegrees == -90 || angleDegrees == 270) {
            y = eastWest.sensor1;
            x = northSouth.sensor1;
        }

        // Do our updating
        if (eastWestValid && northSouthValid) {
            coordinateSystem.update(CoordinateSystem.FieldCoordinates.make(x, y));
        } else if (eastWestValid) {
            coordinateSystem.update(CoordinateSystem.FieldCoordinates.make(x, coordinateSystem.current.y));
        } else if (northSouthValid) {
            coordinateSystem.update(CoordinateSystem.FieldCoordinates.make(coordinateSystem.current.x, y));
        }
    }
}
