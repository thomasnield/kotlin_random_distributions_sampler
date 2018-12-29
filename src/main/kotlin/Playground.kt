import javafx.application.Application
import javafx.scene.chart.NumberAxis
import org.ojalgo.random.Normal
import tornadofx.*


fun main(args: Array<String>) {
    Application.launch(QuickScatterPlotApp::class.java)
}


class QuickScatterPlotApp: App(DistributionSampler::class)

class DistributionSampler: View() {

    // Distribution Sampler goes here
    val distribution = Normal()

    val points = (0..100).asSequence()
            .map { distribution.get() }
            .map { it to distribution.getDistribution(it) }
            .toList()

    // -------------------------------

    val minX = points.asSequence().map { it.first }.min()!!.toDouble()
    val maxX = points.asSequence().map { it.first }.max()!!.toDouble()
    val minY = points.asSequence().map { it.second }.min()!!.toDouble()
    val maxY = points.asSequence().map { it.second }.max()!!.toDouble()


    override val root = scatterchart("Quick Scatter Plot", NumberAxis(minX, maxX, 1.0), NumberAxis(minY, maxY, 1.0)) {
        series("") {
            points.forEach {
                data(it.first, it.second)
            }
        }
    }
}