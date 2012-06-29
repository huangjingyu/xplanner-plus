package com.technoetic.xplanner.tags;

import java.awt.Color;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.math.NumberUtils;

import com.technoetic.xplanner.format.DecimalFormat;

import de.laures.cewolf.CewolfException;
import de.laures.cewolf.ChartImage;
import de.laures.cewolf.Configuration;
import de.laures.cewolf.Storage;
import de.laures.cewolf.storage.SerializableChartImage;
import de.laures.cewolf.taglib.tags.ChartImgTag;


public class ProgressBarImageTag extends ChartImgTag implements ProgressBarTag  {
    private float quality = 0.75F;
    private double actual;
    private double estimate;
    private boolean complete;

    public static final Color UNCOMPLETED_COLOR = new Color(0x80, 0x80, 0xFF);
    public static final Color COMPLETED_COLOR = new Color(0xA0, 0xFF, 0xA0);
    public static final Color EXCEEDED_COLOR = new Color(0xFF, 0xA0, 0xA0);
    public static final Color UNWORKED_COLOR = new Color(0xC0, 0xC0, 0xC0);

    public int doStartTag() throws JspException {
        setRenderer("/cewolf");
        setChartid(getId());
        Storage storage = Configuration.getInstance(pageContext.getServletContext()).getStorage();
        try {
            this.sessionKey = storage.storeChartImage(getChartImage(), pageContext);
        } catch (CewolfException cwex) {
            throw new JspException(cwex.getMessage());
        }
        return SKIP_BODY;
    }

    private ChartImage getChartImage() throws CewolfException {
        ProgressBarImage image = new ProgressBarImage(createModel());
        return new SerializableChartImage(new CewolfProgressBarChartImage(image));
    }

    public void setActual(double actual) {
        this.actual = actual;
    }

    public void setEstimate(double estimate) {
        this.estimate = estimate;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setHeight(int height) {
        super.setHeight(13);
    }
    
    @Override
    public void setWidth(String width) {
    	super.setWidth(NumberUtils.toInt(width));
    }

    public void setQuality(String quality) {
        this.quality = Float.parseFloat(quality);
        if ((this.quality < 0.0F) || (this.quality > 1.0F)) {
            throw new IllegalArgumentException("quality \"" + quality + "\" out of range");
        }
    }

    public String getQuality() { return Float.toString(quality); }


    private double getCaptionValue() {
        return Math.min(actual, estimate);
    }

    private boolean isComplete() {
        return complete;
    }

    public Color getForegroundColor() {
        if (isComplete()) {
            return PrintLinkTag.isInPrintMode(pageContext) ? Color.GRAY : COMPLETED_COLOR;
        } else {
            return PrintLinkTag.isInPrintMode(pageContext) ? Color.DARK_GRAY : UNCOMPLETED_COLOR;
        }
    }

    public Color getBackgroundColor() {

        if (actual > estimate) {
            return PrintLinkTag.isInPrintMode(pageContext) ? Color.BLACK : EXCEEDED_COLOR;
        } else {
            return UNWORKED_COLOR;
        }
    }

    public double getBarValue() {
        if (actual == 0 && estimate == 0 && complete) {
            return width;
        }
        return Math.min(actual, estimate);
    }

    public double getMaxValue() {
        if (actual == 0 && estimate == 0 && complete) {
            return width;
        }
        return Math.max(actual, estimate);
    }

    public ProgressBarImage.Model createModel() {
        return new ProgressBarImage.Model(width,
                                          height,
                                          getBarValue(),
                                          getForegroundColor(),
                                          getMaxValue(),
                                          getBackgroundColor(),
                                          new DecimalFormat((HttpServletRequest) pageContext.getRequest()),
                                          getCaptionValue());
    }


}
