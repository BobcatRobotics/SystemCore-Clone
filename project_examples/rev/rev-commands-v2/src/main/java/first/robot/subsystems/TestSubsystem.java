// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.subsystems;

import org.wpilib.command2.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class TestSubsystem extends SubsystemBase {

  // setup an motor
  private final SparkMax motor = new SparkMax(0, 33, MotorType.kBrushless); // uses can_S1
  private SparkMaxConfig motorConfig;
  private final double CLOCKWISE = 0.3;
  private final double COUNTERCLOCKWISE = -0.3;

  public TestSubsystem() {
    configureMotors();
  }

  private void configureMotors() {
    /*
     * Create a new SPARK MAX configuration object. This will store the
     * configuration parameters for the SPARK MAX that we will set below.
     */
    motorConfig = new SparkMaxConfig();
    motorConfig.smartCurrentLimit(20);
    /*
     * Apply the configuration to the SPARK MAX.
     *
     * kResetSafeParameters is used to get the SPARK MAX to a known state. This
     * is useful in case the SPARK MAX is replaced.
     *
     * kPersistParameters is used to ensure the configuration is not lost when
     * the SPARK MAX loses power. This is useful for power cycles that may occur
     * mid-operation.
     */
    motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

  }

  public Command runClockwise() {
    return Commands.run(() -> {
      motor.setThrottle(CLOCKWISE);
    });
  }

  public Command runCounterClockwise() {
    return Commands.run(() -> {
      motor.setThrottle(COUNTERCLOCKWISE);
    });
  }

  public Command stopMotors() {
    return Commands.run(() -> {
      stop();
    });
  }

  public void stop() {
    motor.stopMotor();
  }

}
