// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.subsystems;

import org.wpilib.command3.*;

import com.thrifty.nova.*;
import com.thrifty.core.MotorConfig.ConfigOptions;
import com.thrifty.core.MotorControl;

public class TestSubsystem extends Mechanism {

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
    motor.configure(NovaConfig.factoryReset(), limit);
  }

  public Command runClockwise() {
    // implicitly require `this`
    return this.run(coro -> {
      motor.control(MotorControl.percent(CLOCKWISE));

    }).named("MotorsSpinClockwise");
  }

  public Command runCounterClockwise() {
    // implicitly require `this`
    return this.run(coro -> {
      motor.control(MotorControl.percent(COUNTERCLOCKWISE));

    }).named("MotorsSpinClockwise");
  }

  public Command stopMotors() {
    return this.run(coro -> {
      stop();
    }).named("StopMotors");
  }

  public void stop() {
    motor.stop();
  }

}
