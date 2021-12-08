package trajs

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints

object AutonBlueGen {
    // Remember to set these constraints to the same values as your DriveConstants.java file in the quickstart
    private val driveConstraints = DriveConstraints(60.0, 60.0, 0.0, 270.0.toRadians, 270.0.toRadians, 0.0)

    // Remember to set your track width to an estimate of your actual bot to get accurate trajectory profile duration!
    private const val trackWidth = 16.0

    private val combinedConstraints = MecanumConstraints(driveConstraints, trackWidth)

    private val startPose = Pose2d(-36.0, 63.0, 180.0.toRadians)
    private val startPose2 = Pose2d(-60.0, 63.0, 180.0.toRadians)
    private val startPose3 = Pose2d(-60.0, 21.0, 0.0.toRadians)
    private val startPose4 = Pose2d(-30.0, 21.0, 0.0.toRadians)
    private val startPose5 = Pose2d(-62.0, 21.0, 0.0.toRadians)


    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()

        val builder1 = TrajectoryBuilder(startPose, startPose.heading, combinedConstraints)
        val builder2 = TrajectoryBuilder(startPose2, startPose2.heading, combinedConstraints)
        val builder3 = TrajectoryBuilder(startPose3, startPose3.heading, combinedConstraints)
        val builder4 = TrajectoryBuilder(startPose4, startPose4.heading, combinedConstraints)
        val builder5 = TrajectoryBuilder(startPose5, startPose5.heading, combinedConstraints)

        builder1.forward(24.0)
        builder2.lineToLinearHeading(Pose2d(-60.0, 21.0))
        builder3.forward(30.0)
        builder4.back(32.0)
        builder5.strafeLeft(15.0)

        // Small Example Routine
//        builder1
//            .splineTo(Vector2d(10.0, 10.0), 0.0)
//            .splineTo(Vector2d(15.0, 15.0), 90.0);

        list.add(builder1.build())
        list.add(builder2.build())
        list.add(builder3.build())
        list.add(builder4.build())
        list.add(builder5.build())

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(Vector2d(0.0, -63.0), 18.0, 18.0) // robot against the wall
    }


}

val Double.toRadians2 get() = (Math.toRadians(this))
