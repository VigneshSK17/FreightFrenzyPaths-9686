//NOTE BLUE LINE MEANS BACKWARDS
package trajs

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints

object RedLoadedShippingHubTrajectoryGen {
    // Remember to set these constraints to the same values as your DriveConstants.java file in the quickstart
    private val driveConstraints = DriveConstraints(60.0, 60.0, 0.0, 270.0.toRadians, 270.0.toRadians, 0.0)

    // Remember to set your track width to an estimate of your actual bot to get accurate trajectory profile duration!
    private const val trackWidth = 16.0

    private val combinedConstraints = MecanumConstraints(driveConstraints, trackWidth)

    private val startPose = Pose2d(41.0, -62.0, 180.0.toRadians)
    private val homePose = Pose2d(9.0, -62.0, 180.0.toRadians) // The pose where the bot starts at the start of Auton is called HomePose
    private val pose2 = Pose2d(-12.0, -56.0, 180.0.toRadians)
    private val pose3 = Pose2d(-12.0, -56.0, 90.0.toRadians)

    fun createTrajectory(): ArrayList<Trajectory> {
        val list = ArrayList<Trajectory>()

        val builder1 = TrajectoryBuilder(startPose, startPose.heading, combinedConstraints)
        val builder2 = TrajectoryBuilder(homePose, homePose.heading, combinedConstraints)
        val builder3 = TrajectoryBuilder(pose2, pose2.heading, combinedConstraints)
        val builder4 = TrajectoryBuilder(pose3, pose3.heading, combinedConstraints)

        builder1
            .lineTo(Vector2d(9.0, -62.0));

        builder2
            .lineTo(Vector2d(-12.0, -56.0));

        /*builder3
            .turn(Math.toRadians(90.0));
         */

        builder4
            .lineTo(Vector2d(-12.0, -44.0));

        list.add(builder1.build())
        list.add(builder2.build())
        //list.add(builder3.build())
        list.add(builder4.build())

        return list
    }

    fun drawOffbounds() {
        GraphicsUtil.fillRect(Vector2d(0.0, -63.0), 18.0, 18.0) // robot against the wall
    }
}

val Double.toRadians5 get() = (Math.toRadians(this))
