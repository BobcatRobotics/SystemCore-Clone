// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.commands;

import static org.wpilib.units.Units.Seconds;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.Time;

import first.robot.subsystems.TestSubsystem;

/** Container for auto command factories. */
public final class Autos {
  public static Command simpleClockwiseAuto(TestSubsystem testSubsystem) {
    return testSubsystem.runClockwise();
  }

  public static Command simpleCounterClockwiseAuto(TestSubsystem testSubsystem) {
    return testSubsystem.runCounterClockwise();
  }

  public static Command stopMotorInAuto(TestSubsystem testSubsystem) {
    return testSubsystem.stopMotors();
  }

  public static Command fullMotorAuto(TestSubsystem testSubsystem) {
    Time timeout = Seconds.of(180);
    return Command.sequence(
      simpleClockwiseAuto(testSubsystem).withTimeout(timeout),
      stopMotorInAuto(testSubsystem).withTimeout(timeout),
      simpleCounterClockwiseAuto(testSubsystem).withTimeout(timeout),
      stopMotorInAuto(testSubsystem)
    ).named("Full Motor Auto (Forward/Stop/Reverse)");
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
