package com.technoetic.xplanner.tags;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;

import junit.framework.TestCase;

import org.apache.struts.Globals;

import com.technoetic.xplanner.XPlannerTestSupport;

public class TestLinkTag extends TestCase {
    private XPlannerTestSupport support;
    private DummyLinkTag tag;

    public TestLinkTag(String name) {
        super(name);
    }

    public class TestObject {
        private int value;

        public TestObject(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    protected void setUp() throws Exception {
        support = new XPlannerTestSupport();
        tag = new DummyLinkTag();
        tag.setPageContext(support.pageContext);
        support.mapping.setPath("/page.jsp");
        support.request.setAttribute(Globals.MAPPING_KEY, support.mapping);
    }

    public void testDoStartTag() throws Exception {
        tag.setHref("somepage.jsp");

        executeTag();

        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp") != -1);
    }

    public void testDoStartTagWithOid() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");

        executeTag();

        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=222") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    private void executeTag() throws JspException {
        int result = tag.doStartTag();
        assertEquals("wrong result", BodyTag.EVAL_BODY_BUFFERED, result);

        result = tag.doEndTag();
        assertEquals("wrong result", BodyTag.EVAL_PAGE, result);
    }

    public void testDoStartTagWithOidAndFkey() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);

        executeTag();

        assertTrue("missing fkey", support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    public void testDoStartTagWithParams() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        Object object = new Object();
        support.pageContext.setAttribute("bar", object);
        tag.setHref("somepage.jsp");
        tag.setParamId("foo");
        tag.setParamName("bar");
        tag.setParamProperty("class");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=class+java.lang.Object") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    public void testDoStartTagWithMap() throws Exception {
        support.request.setParameterValue("oid", new String[]{"222"});
        Object object = new Object();
        HashMap params = new HashMap();
        params.put("foo", object.getClass());
        support.pageContext.setAttribute("bar", params);
        tag.setHref("somepage.jsp");
        tag.setName("bar");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=class+java.lang.Object") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    public void testDoTagTwice() throws Exception {
        support.pageContext.setAttribute("object", new TestObject(100));
        tag.setPage("foo.jsp");
        tag.setParamId("foo");
        tag.setParamName("object");
        tag.setParamProperty("value");

        executeTag();

        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=100") != -1);

        int result = tag.doStartTag();

        assertEquals("wrong result", BodyTag.EVAL_BODY_BUFFERED, result);
        assertTrue("missing param",
                support.jspWriter.printValue.indexOf("foo=100") != -1);
    }

    public void testDoStartTagWithOidAndFkeyAndProjectId() throws Exception {
        DomainContext context = new DomainContext();
        context.setProjectId(44);
        context.save(support.request);
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);

        executeTag();

        assertOutputContains("projectId=44");
        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    public void testDoStartTagWithOidAndFkeyAndInhibitedProjectId() throws Exception {
        DomainContext context = new DomainContext();
        context.setProjectId(44);
        context.save(support.request);
        support.request.setParameterValue("oid", new String[]{"222"});
        tag.setHref("somepage.jsp");
        tag.setFkey(111);
        tag.setIncludeProjectId("false");

        executeTag();

        assertTrue("missing projectId",
                support.jspWriter.printValue.indexOf("projectId=44") == -1);
        assertTrue("missing fkey",
                support.jspWriter.printValue.indexOf("fkey=111") != -1);
        assertTrue("missing or incorrect returnto",
                support.jspWriter.printValue.indexOf("returnto=%2Fdo%2Fpage.jsp%3Foid%3D222") != -1);
    }

    public void testDoEndTagMnemonicInFirstCharacter() throws Exception {
        tag.setText("&All");
        tag.doEndTag();
        assertOutputContains("accesskey=\"A\"");
        assertOutputContains("<span class=\"mnemonic\">A</span>ll");
    }

    public void testDoEndTagMnemonicInLastCharacter() throws Exception {
        tag.setText("All&");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("All&");
    }

    public void testDoEndTagMnemonicInMiddleCharacter() throws Exception {
        tag.setText("A&ll");
        tag.doEndTag();
        assertOutputContains("accesskey=\"L\"");
        assertOutputContains("A<span class=\"mnemonic\">l</span>l");
    }

    public void testDoEndTagMnemonicBeforeSpace() throws Exception {
        tag.setText("A& ll");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("A& ll");
    }

    public void testDoEndTagEscapedAmpersand() throws Exception {
        tag.setText("A&&ll");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("A&ll");
    }

    public void testDoEndTagNoMnemonic() throws Exception {
        tag.setText("All");
        tag.doEndTag();
        assertOutputDoesNotContain("accesskey");
        assertOutputContains("All");
    }

    private void assertOutputContains(String expectedOutputPart) {
        assertTrue("missing "+expectedOutputPart + " in '" + support.jspWriter.printValue + "'",
                support.jspWriter.printValue.indexOf(expectedOutputPart) != -1);
    }

    private void assertOutputDoesNotContain(String unexpectedOutputPart) {
        assertTrue("contains "+unexpectedOutputPart + " in '" + support.jspWriter.printValue + "'",
                   support.jspWriter.printValue.indexOf(unexpectedOutputPart) == -1);
    }

    private static class DummyLinkTag extends LinkTag {
        public void setText(String text) {
            this.text = text;
        }
    }

}