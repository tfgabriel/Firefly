package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import org.firstinspires.ftc.teamcode.Systems.NanoClock
import org.firstinspires.ftc.teamcode.Variables.system_funcs.firstopen
import org.firstinspires.ftc.teamcode.Variables.system_funcs.hardwareMap
import kotlin.math.roundToInt

//rr encoder
class Raikoder(val name: String, private val direction: Int) {
    private val clock = NanoClock()

    private var lastPosition = 0
    private var m = hardwareMap.get(DcMotorEx::class.java, name)

    init {
        clock.reset()
        /*if (name == "LSLIDE") {
            if (firstopen) {
                m.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                firstopen = false
            }
        } else {
            m.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        }
        m.mode = DcMotor.RunMode.RUN_USING_ENCODER*/
        m.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        m.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    private var velocityEstimateIdx: Int = 0
    private val velocityEstimates = arrayListOf(0.0, 0.0, 0.0)
    private var lastUpdateTime = 0.0

    val pos: Int
        get() {
            val currentPosition = m.currentPosition
            val currentTime = clock.seconds()
            if (currentPosition != lastPosition) {
                val dt: Double = currentTime - lastUpdateTime
                velocityEstimates[velocityEstimateIdx] = (currentPosition - lastPosition) / dt
                velocityEstimateIdx = (velocityEstimateIdx + 1) % 3
                lastPosition = currentPosition
            }
            lastUpdateTime = currentTime
            return currentPosition * direction
        }

    val vel: Double
        get() {
            val median: Double = if (velocityEstimates[0] > velocityEstimates[1])
                velocityEstimates[1].coerceAtLeast(velocityEstimates[0].coerceAtMost(velocityEstimates[2]))
            else {
                velocityEstimates[0].coerceAtLeast(velocityEstimates[1].coerceAtMost(velocityEstimates[2]))
            }
            return inverseOverflow(m.velocity, median)
        }

    private val CPS_STEP = 0x10000

    private fun inverseOverflow(input: Double, estimate: Double): Double {
        var real = input.toInt() and 0xffff
        real += real % 20 / 4 * CPS_STEP
        real += ((estimate - real) / (5 * CPS_STEP)).roundToInt() * 5 * CPS_STEP
        return real.toDouble() * direction
    }

}