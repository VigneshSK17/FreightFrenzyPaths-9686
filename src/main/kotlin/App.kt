import com.acmerobotics.roadrunner.geometry.Vector2d
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

import javafx.stage.Stage
import javafx.util.Duration

import trajs.BlueParkingTrajectoryGen
import trajs.BlueShippingHubTrajectoryGen
import trajs.BlueWarehouseTrajectoryGen
import trajs.BlueLoadedShippingHubTrajectoryGen

import trajs.RedParkingTrajectoryGen
import trajs.RedLoadedShippingHubTrajectoryGen
import trajs.RedShippingHubTrajectoryGen
import trajs.RedWarehouseTrajectoryGen

import trajs.TestTrajectoryGen

class App : Application() {
    val robotRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val startRect = Rectangle(100.0, 100.0, 10.0, 10.0)
    val endRect = Rectangle(100.0, 100.0, 10.0, 10.0)

    var startTime = Double.NaN

    // TODO: Change this value to the name of the file
    val trajectories = BlueWarehouseTrajectoryGen.createTrajectory()

    lateinit var fieldImage: Image
    lateinit var stage: Stage


    var activeTrajectoryIndex = 0
    val trajectoryDurations = trajectories.map { it.duration() }
    val duration = trajectoryDurations.sum()
    val numberOfTrajectories = trajectories.size

    companion object {
        var WIDTH = 0.0
        var HEIGHT = 0.0
    }

    override fun start(stage: Stage?) {
        this.stage = stage!!
        fieldImage = Image("/field-2021-official.png")

        val root = Group()

        WIDTH = fieldImage.width
        HEIGHT = fieldImage.height
        GraphicsUtil.pixelsPerInch = WIDTH / GraphicsUtil.FIELD_WIDTH
        GraphicsUtil.halfFieldPixels = WIDTH / 2.0

        val canvas = Canvas(WIDTH, HEIGHT)
        val gc = canvas.graphicsContext2D
        val t1 = Timeline(KeyFrame(Duration.millis(10.0), EventHandler<ActionEvent> { run(gc) }))
        t1.cycleCount = Timeline.INDEFINITE

        stage.scene = Scene(
            StackPane(
                root
            )
        )

        root.children.addAll(canvas, startRect, endRect, robotRect)

        stage.title = "PathVisualizer"
        stage.isResizable = false

        println("duration $duration")

        stage.show()
        t1.play()
    }

    fun run(gc: GraphicsContext) {
        if (startTime.isNaN())
            startTime = Clock.seconds

        GraphicsUtil.gc = gc
        gc.drawImage(fieldImage, 0.0, 0.0)

        gc.lineWidth = GraphicsUtil.LINE_THICKNESS

        //region Edit start positions for robot
        //region Red Alliance
        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.BLUE)
        GraphicsUtil.fillRect(Vector2d(9.0, 63.0), 18.0, 18.0)
        gc.globalAlpha = 1.0

        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.BLUE)
        GraphicsUtil.fillRect(Vector2d(-33.0, 63.0), 18.0, 18.0)
        gc.globalAlpha = 1.0
        //endregion

        //region BlueAlliance
        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.RED)
        GraphicsUtil.fillRect(Vector2d(9.0, -63.0), 18.0, 18.0)
        gc.globalAlpha = 1.0

        gc.globalAlpha = 0.5
        GraphicsUtil.setColor(Color.RED)
        GraphicsUtil.fillRect(Vector2d(-33.0, -63.0), 18.0, 18.0)
        gc.globalAlpha = 1.0
        //endregion
        //endregion

        val trajectory = trajectories[activeTrajectoryIndex]

        val prevDurations: Double = {
            var x = 0.0
            for (i in 0 until activeTrajectoryIndex)
                x += trajectoryDurations[i]
            x
        }()

        val time = Clock.seconds
        val profileTime = time - startTime - prevDurations
        val duration = trajectoryDurations[activeTrajectoryIndex]

        val start = trajectories.first().start()
        val end = trajectories.last().end()
        val current = trajectory[profileTime]

        if (profileTime >= duration) {
            activeTrajectoryIndex++
            if(activeTrajectoryIndex >= numberOfTrajectories) {
                activeTrajectoryIndex = 0
                startTime = time
            }
        }

        trajectories.forEach{GraphicsUtil.drawSampledPath(it.path)}

        GraphicsUtil.updateRobotRect(startRect, start, GraphicsUtil.END_BOX_COLOR, 0.5)
        GraphicsUtil.updateRobotRect(endRect, end, GraphicsUtil.END_BOX_COLOR, 0.5)

        GraphicsUtil.updateRobotRect(robotRect, current, GraphicsUtil.ROBOT_COLOR, 0.75)
        GraphicsUtil.drawRobotVector(current)

        stage.title = "Profile duration : ${"%.2f".format(duration)} - time in profile ${"%.2f".format(profileTime)}"
    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}