package org.firstinspires.ftc.teamcode.Systems.arm

import com.acmerobotics.dashboard.config.Config
import org.firstinspires.ftc.teamcode.Algorithms.chain_actioner
import org.firstinspires.ftc.teamcode.Algorithms.quality_of_life_funcs.posdiff
import org.firstinspires.ftc.teamcode.TeleOp.chainactioner
import org.firstinspires.ftc.teamcode.Variables.system_funcs.arm
import org.firstinspires.ftc.teamcode.Variables.system_funcs.claws
import org.firstinspires.ftc.teamcode.Variables.system_funcs.intake
import org.firstinspires.ftc.teamcode.Variables.system_vars.larmInit

@Config
object arm_vars {
    @JvmField
    var larmUp: Double = 0.11
    @JvmField
    var larmDown: Double = 0.395
    @JvmField
    var rarmUp : Double = 0.65
    @JvmField
    var rarmDown: Double = 0.36
    @JvmField
    var fourbarinit: Double = 0.35
    @JvmField
    var fourbarfinalpos: Double = 0.75
    @JvmField
    var rarmPreload: Double = 0.72
    @JvmField
    var larmPreload: Double = 0.07
    @JvmField
    var fourbarPreload: Double = 0.95
    @JvmField
    ///aici creez pozitii intermediare pt transfer, astfel incat sa nu isi mai dea smash in pixeli si sa mi se arunce
    var rarmPozitieIntermediara: Double = 0.41
    @JvmField
    ///aici creez pozitii intermediare pt transfer, astfel incat sa nu isi mai dea smash in pixeli si sa mi se arunce
    var larmPozitieIntermediara: Double = 0.35

    private val lclawclosepos: Double = 0.63
    private val lidopenpos: Double = 0.75
    private val lidclosepos: Double = 1.0

    val emptystack = chain_actioner().addAction({}, {true})

    val armstack = chain_actioner().addAction({ intake.intakeServo.position = 0.75}, { posdiff(intake.intakeServo.position, 0.75) })
        .addSleep(100.0)
        .addAction({arm.goInit()}, {posdiff(arm.larm.position, larmInit)})
        .addSleep(100.0)
        .addAction({ intake.lidServo.position = lidopenpos}, {posdiff(intake.lidServo.position, lidopenpos)})
        .addSleep(300.0)
        .addAction({arm.goDown()}, {posdiff(arm.larm.position, larmDown)})
        .addSleep(300.0)
        .addAction({ claws.grab()}, { posdiff(claws.lclaw.position, lclawclosepos) })
        .addSleep(200.0)
        .addAction({arm.goInit()}, { posdiff(arm.larm.position, larmInit) })
        .addSleep(100.0)
        .addAction({intake.lidServo.position = lidclosepos}, { posdiff(intake.lidServo.position, lidclosepos) })
}