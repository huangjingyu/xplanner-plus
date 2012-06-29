package com.technoetic.xplanner.charts;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.classic.Session;
import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

public class PieChartQuery implements DatasetProducer {
    private final String producerId = Long.toString(System.currentTimeMillis());
    private String query;
    private boolean includeCountInLabel;

    public Object produceDataset(Map parameters) throws DatasetProduceException {
        List results = null;
        try {
            results = null;
            Session session = ThreadSession.get();
            results = session.find(query);
        } catch (Exception e) {
            throw new DatasetProduceException(e.getMessage());
        }
        DefaultPieDataset data = new DefaultPieDataset();
        for (int i = 0; i < results.size(); i++) {
            Object[] objects = (Object[])results.get(i);
            data.setValue(getLabel(objects), getCount(objects));
        }
        return data;
    }

    private int getCount(Object[] objects) {
        return ((Number)objects[1]).intValue();
    }

    private Comparable getLabel(Object[] objects) {
        return objects[0] + (includeCountInLabel ? " (" + getCount(objects) + ")" : "");
    }

    public boolean hasExpired(Map map, Date date) {
        return true;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setIncludeCountInLabel(boolean includeCountInLabel) {
        this.includeCountInLabel = includeCountInLabel;
    }
}
