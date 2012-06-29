package com.technoetic.xplanner.charts;

import java.awt.Color;
import java.awt.Paint;
import java.util.Iterator;
import java.util.Map;

import org.jfree.chart.ChartColor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;

import de.laures.cewolf.ChartPostProcessor;
import de.laures.cewolf.taglib.util.ColorHelper;

public class PieChartPostProcessor implements ChartPostProcessor {
    private static Paint[] DEFAULT_PAINTLIST = {
        ChartColor.VERY_LIGHT_RED,
        ChartColor.VERY_LIGHT_BLUE,
        ChartColor.VERY_LIGHT_GREEN,
        ChartColor.VERY_LIGHT_YELLOW,
        ChartColor.VERY_LIGHT_MAGENTA,
        ChartColor.VERY_LIGHT_CYAN,
        ChartColor.LIGHT_RED,
        ChartColor.LIGHT_BLUE,
        ChartColor.LIGHT_GREEN,
        ChartColor.LIGHT_YELLOW,
        ChartColor.LIGHT_MAGENTA,
        ChartColor.LIGHT_CYAN,
        Color.lightGray,
        Color.red,
        Color.blue,
        Color.green,
        Color.yellow,
        Color.orange,
        Color.magenta,
        Color.cyan,
        Color.pink,
        Color.gray,
        ChartColor.DARK_RED,
        ChartColor.DARK_BLUE,
        ChartColor.DARK_GREEN,
        ChartColor.DARK_YELLOW,
        ChartColor.DARK_MAGENTA,
        ChartColor.DARK_CYAN,
        Color.darkGray,
        ChartColor.VERY_DARK_RED,
        ChartColor.VERY_DARK_BLUE,
        ChartColor.VERY_DARK_GREEN,
        ChartColor.VERY_DARK_YELLOW,
        ChartColor.VERY_DARK_MAGENTA,
        ChartColor.VERY_DARK_CYAN,
    };

    public void processChart(Object chart, Map parameters) {
        PiePlot plot = (PiePlot)((JFreeChart)chart).getPlot();
        setPlotDefaults(plot);
        for (Iterator iterator = parameters.keySet().iterator(); iterator.hasNext();) {
            String key = (String)iterator.next();
            if (key.equals("border.color")) {
                plot.setOutlinePaint(parseColor(parameters.get(key)));
            } else if (key.startsWith("section.exploded") && isTrue(parameters.get(key))) {
                //plot.setRadius(0.90);
                plot.setExplodePercent(getIndex(key), 1.0);
            } else if (key.startsWith("depth") && isTrue(parameters.get(key))) {
                if (plot instanceof PiePlot3D) {
                    ((PiePlot3D)plot).setDepthFactor(Double.parseDouble(parameters.get(key).toString()));
                }
            } else if (key.startsWith("section.color")) {
                plot.setSectionOutlinePaint(getIndex(key), parseColor(parameters.get(key)));
            }
        }
    }

    private void setPlotDefaults(PiePlot plot) {
        for (int i = 0; i < DEFAULT_PAINTLIST.length; i++) {
            plot.setSectionOutlinePaint(i, DEFAULT_PAINTLIST[i]);
        }
        plot.setOutlinePaint(Color.WHITE);
        if (plot instanceof PiePlot3D) {
            ((PiePlot3D)plot).setDepthFactor(0.2);
        }
    }

    private boolean isTrue(Object o) {
        return Boolean.valueOf(o.toString()).booleanValue();
    }

    private int getIndex(String key) {
        try {
            return Integer.parseInt(key.substring(key.lastIndexOf(".") + 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Paint parseColor(Object value) {
        return ColorHelper.getColor(value.toString());
    }
}
