package com.technoetic.xplanner.tags.displaytag;

/**
 * User: Mateusz Prokopowicz
 * Date: Jan 20, 2005
 * Time: 11:31:35 AM
 */

import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Project;

import org.displaytag.properties.MediaTypeEnum;
import org.displaytag.tags.TableTag;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.domain.ActionMapping;
import com.technoetic.xplanner.domain.DomainClass;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.views.ActionRenderer;

public class TestActionButtonsTag extends TestCase {
    ActionButtonsTag tag;
    protected XPlannerTestSupport support;
    Project object = new Project();

    public void setUp() throws Exception {
        super.setUp();
        object.setName("test project");
        support = new XPlannerTestSupport();
        support.pageContext.setAttribute(TableTag.PAGE_ATTRIBUTE_MEDIA, MediaTypeEnum.HTML);
        tag = new ActionButtonsTag();
        tag.setPageContext(support.pageContext);
        tag.setName("project");
        tag.setId("action");
        tag.setObject(object);
    }

   public void testIterationWithOneMapping() throws Exception
   {
      DomainMetaDataRepository repository = new DomainMetaDataRepository();
      DomainClass domainClass = new DomainClass("project", Project.class);
      repository.add(domainClass);
      ActionMapping mapping = createAndAddMapping(domainClass, 1);

      tag.setDomainMetaDataRepository(repository);

      int status = tag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, status);
      assertEquivalent(mapping, (ActionRenderer) support.pageContext.getAttribute("action"));

      status = tag.doAfterBody();
      assertEquals(BodyTag.SKIP_BODY, status);
   }

   public void testIterationWithTwoMappings() throws Exception
   {
      DomainMetaDataRepository repository = new DomainMetaDataRepository();
      DomainClass domainClass = new DomainClass("project", Project.class);
      repository.add(domainClass);
      ActionMapping mapping1 = createAndAddMapping(domainClass, 1);
      ActionMapping mapping2 = createAndAddMapping(domainClass, 2);

      tag.setDomainMetaDataRepository(repository);

      int status = tag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, status);
      assertEquivalent(mapping1, (ActionRenderer) support.pageContext.getAttribute("action"));

      status = tag.doAfterBody();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, status);
      assertEquivalent(mapping2, (ActionRenderer) support.pageContext.getAttribute("action"));

      status = tag.doAfterBody();
      assertEquals(BodyTag.SKIP_BODY, status);
   }

   //DEBT(DATADRIVEN) Should have a test to cover boundary testing (first invisible and last invisible, all invisible)
   public void testIterationWithThreeMappingsWithMiddleNotVisible() throws Exception
   {
      DomainMetaDataRepository repository = new DomainMetaDataRepository();
      DomainClass domainClass = new DomainClass("project", Project.class);
      repository.add(domainClass);
      ActionMapping mapping1 = createAndAddMapping(domainClass, 1);
      ActionMapping invisibleMapping =
            new ActionMapping("action"+2, "actionKey"+2, "permission"+2, "iconPath"+2, "targetPage"+2, "domainType"+2, false){
    	  	   @Override
               public boolean isVisible(Nameable object) {
                  return false;
               }
            };
      domainClass.addMapping(invisibleMapping);
      ActionMapping mapping3 = createAndAddMapping(domainClass, 3);

      tag.setDomainMetaDataRepository(repository);

      int status = tag.doStartTag();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, status);
      assertEquivalent(mapping1, (ActionRenderer) support.pageContext.getAttribute("action"));

      status = tag.doAfterBody();
      assertEquals(BodyTag.EVAL_BODY_BUFFERED, status);
      assertEquivalent(mapping3, (ActionRenderer) support.pageContext.getAttribute("action"));

      status = tag.doAfterBody();
      assertEquals(BodyTag.SKIP_BODY, status);
   }

   private ActionMapping createAndAddMapping(DomainClass domainClass, int id) {
      ActionMapping mapping1 =
            new ActionMapping("action"+id, "actionKey"+id, "permission"+id, "iconPath"+id, "targetPage"+id, "domainType"+id, false);
      domainClass.addMapping(mapping1);
      return mapping1;
   }

   private void assertEquivalent(ActionMapping expectedMapping, ActionRenderer equivalentRenderer) {
      assertNotNull(equivalentRenderer);
      assertEquals(expectedMapping.getName(), equivalentRenderer.getName());
      assertEquals(expectedMapping.getTitleKey(), equivalentRenderer.getTitleKey());
      assertEquals(expectedMapping.getPermission(), equivalentRenderer.getPermission());
      assertEquals(expectedMapping.getIconPath(), equivalentRenderer.getIconPath());
      assertEquals(expectedMapping.getTargetPage(), equivalentRenderer.getTargetPage());
      assertEquals(expectedMapping.getDomainType(), equivalentRenderer.getDomainType());
   }
}
