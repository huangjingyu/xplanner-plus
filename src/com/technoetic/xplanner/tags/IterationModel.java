package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class IterationModel {
    private final Iteration iteration;

    public IterationModel(Iteration iteration) {
        this.iteration = iteration;
    }

    public String getName() {
        return getProject().getName() + " :: " + iteration.getName();
    }

    public int getId() {
        return iteration.getId();
    }


    protected Project getProject() {
        try {
            return (Project)ThreadSession.get().load((Project.class), new Integer(iteration.getProject().getId()));
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IterationModel)) return false;

        final IterationModel option = (IterationModel)o;

        if (!iteration.equals(option.iteration)) return false;

        return true;
    }

    @Override
	public int hashCode() {
        return iteration.hashCode();
    }

    @Override
	public String toString() {
        return "Option{" +
                "iteration=" +
                iteration +
                "}";
    }
}
