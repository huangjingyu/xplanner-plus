package junitx.framework;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/** @noinspection ErrorNotRethrown*/
public class TestEqualAssert extends TestCase {
    EqualAssert assertObject = new EqualAssert();

    public void testAssertEqualsDefaultEqualsAssert0PropertyAndExcludeClassProperty() throws Exception {
        Test0PropertyBean bean1 = new Test0PropertyBean();
        Test0PropertyBean bean2 = new Test0PropertyBean(){};
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
    }

    public void testAssertEqualsDefaultEqualsAssert1Property() throws Exception {
        Test1PropertyBean bean1 = new Test1PropertyBean();
        Test1PropertyBean bean2 = new Test1PropertyBean();
        bean1.setId(1);
        bean2.setId(1);
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setId(2);
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    public void testAssertEqualsDefaultEqualsAssert3Property() throws Exception {
        Test3PropertyBean bean1 = new Test3PropertyBean();
        Test3PropertyBean bean2 = new Test3PropertyBean();
        bean1.setId(1);
        bean2.setId(1);
        bean1.setOid(2);
        bean2.setOid(2);
        bean1.setStr("test");
        bean2.setStr("test");
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setId(2);
        bean2.setOid(3);
        bean2.setStr("Test2");
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    public void testAssertEqualsWithCustomEqualsAssert() throws Exception {
        TestDoublePropertyBean bean1 = new TestDoublePropertyBean();
        TestDoublePropertyBean bean2 = new TestDoublePropertyBean();
        assertObject = new EqualAssert(new MemberEqualAssert[]{new DoubleMemberEqualAssert(5.0)});
        bean1.setDouble(10.0);
        bean2.setDouble(14.9);
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setDouble(15.0);
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    private void assertAssertEqualsFindObjectsNotEqual(Object bean1, Object bean2) {
        try {
            assertObject.assertEquals(bean1, bean2);
            fail("Did not throw an assertion failure");
        } catch (junit.framework.AssertionFailedError e) {
        }
    }

    private void assertAssertEqualsFindObjectsEqual(Object bean1, Object bean2) {
        assertObject.assertEquals(bean1, bean2);
    }

    public static class Test0PropertyBean {}
    public static class Test1PropertyBean {
        private int id;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
    }
    public static class Test3PropertyBean {
        private int id;
        private int oid;
        private String str;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getOid() { return oid; }
        public void setOid(int oid) { this.oid = oid; }
        public String getStr() { return str; }
        public void setStr(String str) { this.str = str; }
    }
    public static class TestDateBean {
        private Date date;

        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
    }
    public static class TestCalendarBean {
        private Calendar date;

        public Calendar getDate() { return date; }
        public void setDate(Calendar date) { this.date = date; }
    }
    public static class TestDoublePropertyBean {
        private double d;

        public double getDouble() { return d; }
        public void setDouble(double d) { this.d = d; }
    }

}