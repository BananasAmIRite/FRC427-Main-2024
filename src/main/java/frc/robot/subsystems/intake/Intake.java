package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.IOUtils;

 /*
  * Notes about how Intake works
  - theres 2 main parts, The SHOOTER (intakMoterShootTOP/BOTTOM) and the SUCKER (outtakeMotorSuck)
  - the shooter has 2 motors Top and Bottom (teh bottom one follows the top) and the sucker has only one 
  - (the naming conventions are a little swapped thats my fault yo...)
  - so the sucker, SUCKS the rings from the ground and puts them into the shooter.
  - Then the shooter takes the rings from the sucker and shoots them into whatever it needs
  */

public class Intake extends SubsystemBase {
    // establishes the motors for shooter and sucker. Also establishes the beambreak.
    CANSparkMax m_intakeMotorShootTop = new CANSparkMax(Constants.IntakeConstants.kIntakeMotorShootTopId, MotorType.kBrushless);
    RelativeEncoder m_intakeEncoderShootTop = m_intakeMotorShootTop.getEncoder();

    CANSparkMax m_intakeMotorShootBottom = new CANSparkMax(Constants.IntakeConstants.kIntakeMotorShootBottomId, MotorType.kBrushless);
    RelativeEncoder m_intakeEncoderShootBottom = m_intakeMotorShootBottom.getEncoder();

    CANSparkMax m_outtakeMotorSuck = new CANSparkMax(Constants.IntakeConstants.kOuttakeMotorSuckId, MotorType.kBrushless);
    RelativeEncoder m_outtakeEncoderSuck = m_outtakeMotorSuck.getEncoder();

    // beambreak checks for if theres a note in the intake
    DigitalInput m_BeamBreak = new DigitalInput(Constants.IntakeConstants.kBeamBreakId); 

    
    public Intake() {
        setupMotors();
    }
    public void setupMotors() {
        // sets limits for all the motors and has the bottom shoot motor, follow the top
        m_intakeMotorShootTop.setInverted(Constants.IntakeConstants.kShootTopIntakeInverted);
        m_intakeMotorShootTop.setSmartCurrentLimit(Constants.IntakeConstants.kShootTopMotorlimit);
        m_intakeEncoderShootTop.setVelocityConversionFactor(0);

        m_intakeMotorShootBottom.setInverted(Constants.IntakeConstants.kShootBottomIntakeInverted);
        m_intakeMotorShootBottom.setSmartCurrentLimit(Constants.IntakeConstants.kShootBottomMotorlimit);
        m_intakeMotorShootBottom.follow(m_intakeMotorShootTop);
        m_intakeEncoderShootBottom.setVelocityConversionFactor(0);

        m_outtakeMotorSuck.setInverted(Constants.IntakeConstants.kSuckOuttakeInverted);
        m_outtakeMotorSuck.setSmartCurrentLimit(Constants.IntakeConstants.kSuckOuttakeMotorLimit);
        m_outtakeEncoderSuck.setVelocityConversionFactor(0);


    }

    public void periodic() {
        // code inside here will run repeatedly while the robot is on
        doSendables();
       
    }
    //so intaking the ring is sucking it
    public void intakeRing(double speed){
        m_outtakeMotorSuck.set(-speed);
    }
    //and outtaking the ring is shooting it
    public void outtakeRing(double speed){
        m_intakeMotorShootTop.set(speed);
    }
    //beambreak is a scanner that checks if a ring is inside the whole intake
    public boolean beamBreakState(){ 
        return m_BeamBreak.get(); 
    }
    public void StopSuckMotor() {
        m_outtakeMotorSuck.set(0); 
     }
    public void stopMotor(){
        m_intakeMotorShootTop.set(0);
    }
     public void doSendables(){ 
         SmartDashboard.putNumber("SpeedOfSuck m/s", m_outtakeEncoderSuck.getVelocity());
         SmartDashboard.putNumber("SpeedOfShoot m/s", m_intakeEncoderShootTop.getVelocity());
         SmartDashboard.putBoolean("BeanBreakHit t/f", m_BeamBreak.get());
    }
}