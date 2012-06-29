package junitx.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

/** @noinspection ErrorNotRethrown,OverlyBroadCatchBlock,UnusedCatchParameter*/
public class EqualAssert {
    protected static final Logger LOG = LogUtil.getLogger();

    public static final MemberAccessStrategy PROPERTIES = new PropertyAccessStrategy();
    public static final MemberAccessStrategy FIELDS = new FieldAccessStrategy();

    private List equalsAsserts = new ArrayList();
    private MemberAccessStrategy accessStrategy;

    public EqualAssert() {
        this(new MemberEqualAssert[0]);
    }

   public EqualAssert(MemberAccessStrategy accessStrategy) {
      this(new MemberEqualAssert[0], accessStrategy);
   }

    public EqualAssert(MemberEqualAssert[] equalAsserts) {
       this(equalAsserts, new PropertyAccessStrategy());
    }

   public EqualAssert(MemberEqualAssert[] equalAsserts, MemberAccessStrategy accessStrategy) {
      this.accessStrategy = accessStrategy;
      this.equalsAsserts.addAll(Arrays.asList(equalAsserts));
      this.equalsAsserts.add(new LoggingMemberEqualAssert());
      this.equalsAsserts.add(new ExcludeClassPropertyMemberEqualAssert());
      this.equalsAsserts.add(new CalendarDateMemberEqualAssert());
      this.equalsAsserts.add(new ArrayMemberEqualAssert());
      this.equalsAsserts.add(new CollectionMemberEqualAssert(this));
      this.equalsAsserts.add(new DoubleMemberEqualAssert());
      this.equalsAsserts.add(new SimplePropertyMemberEqualAssert());
   }

   public void assertEquals(Object expected, Object actual, String[] properties) {
       try {
           for (int i = 0; i < properties.length; i++) {
               String propertyName = properties[i];
               for (int j = 0; j < equalsAsserts.size(); j++) {
                   MemberEqualAssert memberEqualAssert = (MemberEqualAssert) equalsAsserts.get(j);
                   if (memberEqualAssert.assertEquals(propertyName, expected, actual, accessStrategy)) break;
               }
           }
       } catch (junit.framework.AssertionFailedError e) {
           throw new AssertionFailedError(
               e.getMessage() +
               "\nexpected <" + objectToString(expected, properties) + ">" +
               "\nactual <" + objectToString(actual, properties) + ">", e);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   public void assertEquals(Object expected, Object actual) {
       String[] properties = accessStrategy.getCommonMembers(expected, actual);
       LOG.debug("asserting equality of properties " + listToString(Arrays.asList(properties), null));
       assertEquals(expected, actual, properties);
   }

    public void assertEquals(Object[] expectedObjects, Object[] actualObjects, final String[] properties)
            throws Exception {
        assertEquals(Arrays.asList(expectedObjects), Arrays.asList(actualObjects), properties);
    }

    public void assertEquals(List expectedObjects, List actualObjects) {
       if (expectedObjects.size() == 0 || actualObjects.size() == 0) {
          Assert.assertEquals("collection size ",expectedObjects.size(), actualObjects.size());
          return;
       }
       String[] properties = accessStrategy.getCommonMembers(expectedObjects.get(0), actualObjects.get(0));
       assertEquals(expectedObjects, actualObjects, properties);
    }

    public void assertEquals(List expectedObjects, List actualObjects, final String[] properties) {
        Assert.assertEquals("wrong number of items", expectedObjects.size(), actualObjects.size());
        for (int i = 0; i < expectedObjects.size(); i++) {
            final Object expectedObject = expectedObjects.get(i);
            Object actualObject = CollectionUtils.find(actualObjects, new Predicate() {
                public boolean evaluate(Object o) {
                    return isEqual(o, expectedObject, properties);
                }
            });
            Assert.assertNotNull("Could not find " + objectToString(expectedObject, properties) + " in \n" +
                                 listToString(actualObjects, properties), actualObject);
        }
    }

    private String listToString(List objects, String[] properties) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < objects.size(); i++) {
            buf.append("[" + i + "]={\n");
            buf.append(objectToString(objects.get(i), properties));
            buf.append("\n}\n");
        }
        return buf.toString();
    }

    public boolean isEqual(Object o1, Object o2, final String[] properties) {
        try {
            assertEquals(o1, o2, properties);
        } catch (AssertionFailedError e) {
            return false;
        }
        return true;
    }


   public String objectToString(Object object, String[] properties) {
      return accessStrategy.objectToString(object, properties);
   }

   public class LoggingMemberEqualAssert extends ValueMemberEqualAssert {
      Class classOfMember;
      public boolean assertEquals(String memberName,
                                  Object expectedObject,
                                  Object actualObject,
                                  MemberAccessStrategy accessStrategy) {
         classOfMember = expectedObject.getClass();
         return super.assertEquals(memberName, expectedObject, actualObject, accessStrategy);
      }

      public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
         LOG.debug("asserting equality of property '" +
                   classOfMember.getName() + "." + memberName +
                   "' expected value (" +
                   expectedValue +
                   ") and actual value (" +
                   actualValue +
                   ")");
         return false;
      }
   }
    public class ExcludeClassPropertyMemberEqualAssert extends ValueMemberEqualAssert {
        public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
            return "class".equals(memberName);
        }
    }

    public class SimplePropertyMemberEqualAssert extends ValueMemberEqualAssert {
        public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
            Assert.assertEquals(memberName, expectedValue, actualValue);
            return true;
        }
    }
}