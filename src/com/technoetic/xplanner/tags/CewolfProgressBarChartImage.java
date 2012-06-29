package com.technoetic.xplanner.tags;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.laures.cewolf.CewolfException;
import de.laures.cewolf.ChartImage;
import de.laures.cewolf.taglib.tags.ChartImgTag;
import de.laures.cewolf.util.RenderingException;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Jun 11, 2004
 * Time: 4:50:56 PM
 */
public class CewolfProgressBarChartImage implements ChartImage {
    private ProgressBarImage image;
    private byte[] bytes;

    public CewolfProgressBarChartImage(ProgressBarImage image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public int getType() {
        return IMG_TYPE_CHART;
    }

    public byte[] getBytes() throws CewolfException {
        if (bytes == null)
            bytes = render();
        return bytes;
    }

    private byte[] render() throws RenderingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream( baos );
        try {
            image.encodeJPEG( bos, 0.75f );
        } catch (IOException e) {
            throw new RenderingException(e);
        }
        return baos.toByteArray();
    }

    public String getMimeType() {
        return ChartImgTag.MIME_PNG;
    }

    public int getSize() throws CewolfException {
        return getBytes().length;
    }

	@Override
	public Date getTimeoutTime() {
		Calendar cal = new GregorianCalendar();
	    cal.add(Calendar.SECOND,30);
		return cal.getTime();
	}
}
