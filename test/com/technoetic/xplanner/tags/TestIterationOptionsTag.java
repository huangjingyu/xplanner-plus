package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junitx.framework.ArrayAssert;

import com.technoetic.mocks.hibernate.MockQuery;
import com.technoetic.xplanner.XPlannerTestSupport;

public class TestIterationOptionsTag extends AbstractOptionsTagTestCase {

  protected void setUp() throws Exception {
      tag = new IterationOptionsTag();
      super.setUp();
  }

    public void testGetOptionsSimpleCase() throws Exception {
        setUpMockQuery();

        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_1_1, "edit");
        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_0_1, "edit");

        Collection actualIterations = tag.getOptions();

        IterationModel[] expectedOptions = new IterationModel[]{
            new IterationModel(ITERATION_0_1),
            new IterationModel(ITERATION_1_1)
            };

        ArrayAssert.assertEquals("options",expectedOptions,actualIterations.toArray());
    }

    public void testGetOptionsForOnlyCurrentProject() throws Exception {
        setUpMockQueryForTwoProjects();
        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_0_1, "edit");
        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_1_1, "edit");
        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_2_1, "edit");
        authorizer.givePermission(XPlannerTestSupport.DEFAULT_PERSON_ID, ITERATION_0_2, "edit");

        IterationOptionsTag iterationOptionsTag = (IterationOptionsTag) tag;
        int projectId = ITERATION_0_1.getProject().getId();
        iterationOptionsTag.setProjectId(projectId);
        iterationOptionsTag.setStartDate(ITERATION_0_1.getStartDate());
        iterationOptionsTag.setOnlyCurrentProject(true);
        Collection actualIterations = iterationOptionsTag.getOptions();

        IterationModel[] expectedOptions = new IterationModel[]{
            new IterationModel(ITERATION_1_1),
            new IterationModel(ITERATION_2_1)
            };

        ArrayAssert.assertEquals("options",expectedOptions,actualIterations.toArray());
    }

    private void setUpMockQueryForTwoProjects()
    {
        List results = new ArrayList(4);
        results.add(ITERATION_0_1);
        results.add(ITERATION_1_1);
        results.add(ITERATION_2_1);
        results.add(ITERATION_0_2);
        setUpQuery(results);
    }

    private void setUpMockQuery() {
        List results = new ArrayList(2);
        results.add(ITERATION_0_1);
        results.add(ITERATION_1_1);
        setUpQuery(results);
    }

  private void setUpQuery(List results) {
    MockQuery query = new MockQuery();
    support.hibernateSession.getNamedQueryReturn = query;
    query.listReturn  = results;
  }

}
