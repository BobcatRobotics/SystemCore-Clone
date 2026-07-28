// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.commands;

import org.wpilib.command2.*;
import first.robot.subsystems.TestSubsystem;

/** Container for auto command factories. */
public final class Autos {
  public static Command simpleClockwiseAuto(TestSubsystem testSubsystem) {
    return testSubsystem.runClockwise();
  }
  public static Command simpleCounterClockwiseAuto(TestSubsystem testSubsystem) {
    return testSubsystem.runCounterClockwise();
  }
  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
