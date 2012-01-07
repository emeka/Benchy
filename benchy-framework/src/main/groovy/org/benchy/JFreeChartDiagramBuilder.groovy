package org.benchy

import java.awt.Color
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.plot.XYPlot
import org.jfree.data.category.CategoryDataset
import org.jfree.data.xy.XYDataset
import static org.jfree.chart.ChartUtilities.saveChartAsPNG
import static org.jfree.chart.axis.NumberAxis.createIntegerTickUnits

class JGraphGraphBuilder {

    static def writeLineChartAsPng(XYDataset dataset, String title,
                          String xAxisLabel = "x", String yAxisLabel = "y",
                          int xSize = 800, int ySize = 500, File output) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        )
        chart.antiAlias = true
        def plot = (XYPlot) chart.plot
        plot.rangeAxis.standardTickUnits = createIntegerTickUnits()
        plot.domainAxis.standardTickUnits = createIntegerTickUnits()

        def renderer = plot.renderer
        renderer.shapesVisible = true
        renderer.drawOutlines = true
        renderer.useFillPaint = true
        renderer.fillPaint = Color.white

        saveChartAsPNG(output, chart, xSize, ySize);
    }

    static def writeBarChartAsPng(CategoryDataset dataset, String title,
                          String xAxisLabel = "x", String yAxisLabel = "y",
                          int xSize = 800, int ySize = 500, File output) {
        final JFreeChart chart = ChartFactory.createBarChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        )
        chart.antiAlias = true
        def plot = (CategoryPlot) chart.plot
        plot.rangeAxis.standardTickUnits = createIntegerTickUnits()
        //plot.domainAxis.standardTickUnits = createIntegerTickUnits()

        def renderer = plot.renderer
        //renderer.shapesVisible = true
        //renderer.drawOutlines = true
        //renderer.useFillPaint = true
        //renderer.fillPaint = Color.white

        saveChartAsPNG(output, chart, xSize, ySize);
    }
}