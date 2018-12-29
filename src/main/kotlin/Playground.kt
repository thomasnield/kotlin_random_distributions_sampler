import javafx.application.Application
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.layout.VBox
import org.ojalgo.random.Normal
import tornadofx.*


fun main(args: Array<String>) {
    Application.launch(QuickScatterPlotApp::class.java)
}


class QuickScatterPlotApp: App(NormalView::class)

sealed class DistributionView: View() {
    val points = FXCollections.observableArrayList<Point>()
    val sampleSizeProperty = SimpleIntegerProperty(100)
    var sampleSize by sampleSizeProperty
}

typealias Point = XYChart.Data<Number,Number>

class NormalView(): DistributionView() {

    val meanProperty = SimpleDoubleProperty(0.0)
    var mean by meanProperty

    val stdDevProperty = SimpleDoubleProperty(1.0)
    var stdDev by stdDevProperty

    override val root = borderpane {

        left = form {
            fieldset {
                field("MEAN") {
                    textfield(meanProperty)
                }
                field("STANDARD DEVIATION") {
                    textfield(stdDevProperty)
                }
                field("SAMPLE SIZE") {
                    textfield(sampleSizeProperty)
                }
            }
        }

        val xAxis = NumberAxis(0.0, 0.0, 1.0)
        val yAxis = NumberAxis(0.0, 0.0, 1.0)

        center = scatterchart("Quick Scatter Plot", xAxis, yAxis) {

            series("") {
                val normal = Normal(mean, stdDev)

                repeat(sampleSize) {
                    val sampleX = normal.get()
                    Point(sampleX, normal.getProbability(sampleX)).let {
                        points += it
                        this.data.add(it)
                    }
                }

                xAxis.lowerBound = points.asSequence().map { it.xValue as Double }.min()!!
                xAxis.upperBound = points.asSequence().map { it.xValue as Double }.max()!!
                yAxis.lowerBound = points.asSequence().map { it.yValue as Double }.min()!!
                yAxis.upperBound = points.asSequence().map { it.yValue as Double }.max()!!
            }
        }
    }
}
