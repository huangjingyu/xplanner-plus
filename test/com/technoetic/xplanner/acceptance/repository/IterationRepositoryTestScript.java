package com.technoetic.xplanner.acceptance.repository;

import java.util.Date;
import java.util.List;

import junitx.framework.ArrayAssert;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;

import com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript;
import com.technoetic.xplanner.domain.ObjectMother;
import com.technoetic.xplanner.domain.repository.IterationRepository;
import com.technoetic.xplanner.security.auth.Authorizer;

public class IterationRepositoryTestScript extends AbstractDatabaseTestScript{
    public void test() throws Exception {
        final Project writableProject = newProject();
        final Project readableProject = newProject();
        Project invisibleProject = newProject();
        final Person person = newPerson();

        commitCloseAndOpenSession();

        setUpPersonRole(writableProject, person, Role.EDITOR);
        setUpPersonRole(readableProject, person, Role.VIEWER);

        Iteration secondWritableIteration = newIteration(writableProject);
        secondWritableIteration.setStartDate(new Date(ObjectMother.DAY*3));
        Iteration firstReadableIteration = newIteration(readableProject);
        Iteration firstWritableIteration = newIteration(writableProject);
        firstWritableIteration.setStartDate(new Date(ObjectMother.DAY));
        Iteration secondNonWritableIteration = newIteration(readableProject);

        commitCloseAndOpenSession();

        Authorizer authorizer = createAuthorizer();

        IterationRepository iterationRepository = new IterationRepository(getSession(), authorizer, person.getId());
        List list = iterationRepository.fetchEditableIterations();

        ArrayAssert.assertEquals("iterations returned", new Iteration[] {firstWritableIteration, secondWritableIteration}, list.toArray());

        int iterationId = firstWritableIteration.getId();
        Iteration foundIteration = iterationRepository.getIteration(iterationId);
        assertEquals("iteration", firstWritableIteration, foundIteration);
        assertEquals("iteration id", firstWritableIteration.getId(), foundIteration.getId());
        assertEquals("iteration's project id", writableProject.getId(), foundIteration.getProject().getId());
    }
}