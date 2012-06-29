/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 20, 2006
 * Time: 3:38:29 AM
 */
package com.technoetic.xplanner.domain.io;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.domain.CharacterEnum;
import com.technoetic.xplanner.domain.DomainClass;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.IterationStatusPersistent;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.ClassUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;

public class IOStream {

  public class Document {
    public DomainObject domainObject;
    public Person[] persons;
  }


  public String toXML(Object object) {
    XStream stream = newXStream();
    return stream.toXML(object);
  }

  public Object fromXML(String xml) {
    XStream stream = newXStream();
    return stream.fromXML(xml);
  }

  private XStream newXStream() {
    XStream stream = new XStream();
    stream.setMode(XStream.ID_REFERENCES);
    Map metadataByTypeName = DomainMetaDataRepository.getInstance().getMetadataByTypeName();
    Iterator iterator = metadataByTypeName.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry e = (Entry) iterator.next();
      DomainClass domainClass = (DomainClass) e.getValue();
      stream.alias((String) e.getKey(), domainClass.getJavaClass());
    }
    stream.registerConverter(new IterationStatusConverter());
    stream.registerConverter(new CharacterEnumConverter(StoryStatus.class));
    stream.registerConverter(new CharacterEnumConverter(TaskDisposition.class));
    stream.registerConverter(new CharacterEnumConverter(StoryDisposition.class));
    stream.alias("status", IterationStatus.class, IterationStatusPersistent.class);
    stream.addImmutableType(IterationStatusPersistent.class);
    stream.addImmutableType(StoryStatus.class);
    stream.addImmutableType(TaskDisposition.class);
    stream.addImmutableType(StoryDisposition.class);
    stream.addImmutableType(Date.class);
    return stream;
  }

  class IterationStatusConverter extends AbstractBasicConverter {

    protected Object fromString(String str) {
      return IterationStatus.fromKey(str);
    }

    protected String toString(Object obj) {
      return ((IterationStatus) obj).getKey();
    }

    public boolean canConvert(Class type) {
      return IterationStatus.class.isAssignableFrom(type);
    }

  }

  class CharacterEnumConverter extends AbstractBasicConverter {
    Class enumClass;
    Method fromName;

    protected CharacterEnumConverter(Class enumClass) {
      this.enumClass = enumClass;
      try {
        fromName = enumClass.getMethod("fromName", new Class[]{String.class});
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException(enumClass.getName() + " does not have a method fromName()");
      }
    }

    protected String toString(Object obj) {
      return ((CharacterEnum) obj).getName();
    }

    protected Object fromString(String str) {
      try {
        return fromName.invoke(null, new Object[]{str});
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e.getCause());
      }
    }

    public boolean canConvert(Class type) {
      return enumClass.isAssignableFrom(type);
    }

  }

  class ReferencedPersonList extends ArrayList {
    public ReferencedPersonList(DomainObject object) {
      init(object);
    }

    private void init(DomainObject object) {
      try {
        List fields = ClassUtil.getAllFields(object);
        for (int i = 0; i < fields.size(); i++) {
          Field field = (Field) fields.get(i);
          if (Person.class.isAssignableFrom(field.getType())) {
            add(field.get(object));
          }

        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

}