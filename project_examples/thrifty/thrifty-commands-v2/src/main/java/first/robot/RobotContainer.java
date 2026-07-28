// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot;

import org.wpilib.command2.button.GamepadButton;
import org.wpilib.driverstation.Gamepad;
import org.wpilib.driverstation.Gamepad.Button;

import first.robot.commands.Autos;
import first.robot.subsystems.TestSubsystem;

import org.wpilib.smartdashboard.SendableChooser;
import org.wpilib.smartdashboard.SmartDashboard;

import org.wpilib.command2.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final TestSubsystem testSubsystem = new TestSubsystem();

  // Retained command handles

  // The autonomous routines
  // A simple auto routine that drives forward a specified distance, and then stops.
  private final Command simpleClockwiseAuto = Autos.simpleClockwiseAuto(testSubsystem);
  // A complex auto routine that drives forward, drops a hatch, and then drives backward.
  private final Command simpleCounterClockwiseAuto = Autos.simpleCounterClockwiseAuto(testSubsystem);

  // A chooser for autonomous commands
  SendableChooser<Command> chooser = new SendableChooser<>();

  Gamepad controller = new Gamepad(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
      // Configure the button bindings
      configureButtonBindings();

      // Configure default commands
      // Set the default drive command to split-stick arcade drive
      testSubsystem.setDefaultCommand(testSubsystem.stopMotors());

      // Add commands to the autonomous command chooser
      chooser.setDefaultOption("Clockwise", simpleClockwiseAuto);
      chooser.addOption("CounterClockwise", simpleCounterClockwiseAuto);

      // Put the chooser on the dashboard
      SmartDashboard.putData("Autonomous", chooser);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link org.wpilib.driverstation.GenericHID} or one of its subclasses ({@link
   * org.wpilib.driverstation.Joystick} or {@link Gamepad}), and then passing it to a {@link
   * org.wpilib.command3.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new GamepadButton(controller,Button.RIGHT_BUMPER)
              .onTrue(
                      testSubsystem.runClockwise())
              .onFalse(
                      testSubsystem.stopMotors());

    new GamepadButton(controller,Button.LEFT_BUMPER)
              .onTrue(
                      testSubsystem.runClockwise())
              .onFalse(
                      testSubsystem.stopMotors());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return chooser.getSelected();
  }
}
