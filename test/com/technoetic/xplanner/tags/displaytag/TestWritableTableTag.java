package com.technoetic.xplanner.tags.displaytag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.tags.TableTag;
import org.easymock.MockControl;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * User: Mateusz Prokopowicz
 * Date: Feb 14, 2005
 * Time: 2:22:17 PM
 */
public class TestWritableTableTag extends TestCase {
    WritableTableTag writableTableTag;
    protected XPlannerTestSupport support;
    MockControl authorizerControl;
    Authorizer authorizer;
    Project project;

    public void setUp() throws Exception {
        super.setUp();
        writableTableTag = new WritableTableTag();
        support = new XPlannerTestSupport();
        support.pageContext.setAttribute(TableTag.PAGE_ATTRIBUTE_MEDIA, MediaTypeEnum.HTML);
        writableTableTag.setPageContext(support.pageContext);
        authorizerControl = MockControl.createControl(Authorizer.class);
        authorizer = (Authorizer) authorizerControl.getMock();
        Collection projectCol = new ArrayList();
        writableTableTag.setWholeCollection(projectCol);
        project = new Project();
        projectCol.add(project);
        Set principalSet = new HashSet();
        Person person = new Person();
        principalSet.add(new PersonPrincipal(person));
        Subject subject = new Subject(false, principalSet, new HashSet(), new HashSet());
        SecurityHelper.setSubject(support.request, subject);
        SystemAuthorizer.set(authorizer);
    }

    public void testIsWritable_emptyPermissionList() throws Exception {
        writableTableTag.setPermissions(null);
        boolean isWritable = writableTableTag.isWritable();
        assertEquals("table should be writable", true, isWritable);
    }

    public void testIsWritable() throws Exception {
        authorizerControl.expectAndReturn(authorizer.hasPermission(0, 0, project, "edit"), true);
        writableTableTag.setPermissions("edit,delete");
        authorizerControl.replay();
        boolean isWritable = writableTableTag.isWritable();
        assertEquals("table should be writable", true, isWritable);
        authorizerControl.verify();
    }

    public void testIsNotWritable() throws Exception {
        authorizerControl.expectAndReturn(authorizer.hasPermission(0, 0, project, "edit"), false);
        authorizerControl.expectAndReturn(authorizer.hasPermission(0, 0, project, "delete"), false);
        writableTableTag.setPermissions("edit,delete");
        authorizerControl.replay();
        boolean isWritable = writableTableTag.isWritable();
        assertEquals("table should be writable", false, isWritable);
        authorizerControl.verify();
    }
}
