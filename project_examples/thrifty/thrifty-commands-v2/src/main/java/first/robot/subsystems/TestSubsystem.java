// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.subsystems;

import org.wpilib.command2.*;
import com.thrifty.nova.*;
import com.thrifty.core.MotorConfig.ConfigOptions;
import com.thrifty.core.MotorControl;

public class TestSubsystem extends SubsystemBase {

  // setup an motor
  private final Nova motor = new Nova(33, 0); // uses can_S1
  private final double CLOCKWISE = 0.3;
  private final double COUNTERCLOCKWISE = -0.3;

  public TestSubsystem() {
    configureMotors();
  }

  private void configureMotors() {
    // This creates a config action. Nothing has been sent to the device yet.
    ConfigOptions limit = NovaConfig.statorCurrent(40);
    motor.configure(NovaConfig.factoryReset(),limit);
  }

  public Command runClockwise() {
    return Commands.run(() -> {
      motor.control(MotorControl.percent(CLOCKWISE));
    });
  }

  public Command runCounterClockwise() {
    return Commands.run(() -> {
      motor.control(MotorControl.percent(COUNTERCLOCKWISE));
    });
  }

  public Command stopMotors() {
    return Commands.run(() -> {
      stop();
    });
  }

  public void stop() {
    motor.stop();
  }

}
