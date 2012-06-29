package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

public class RelationshipMappingRegistry {
    private static RelationshipMappingRegistry instance;

   //DEBT(DATADRIVEN) Move the relationship converters to the Domain meta data repository (relationships should become part of the DomainClass)
    Map dataToDomainRelationshipMappingMap = new HashMap();
    Map domainToActionMappingMap = new HashMap();

    public static void initDefaultRegistry() {
        instance = new RelationshipMappingRegistry();
        //Relations mapping
        instance.addMapping(UserStory.class, new RelationshipConvertor("customerId", "customer"));
        instance.addMapping(Task.class, new RelationshipConvertor("userStoryId", "userStory"));
        instance.addMapping(Note.class, new RelationshipConvertor("attachmentId", "file"));
    }

    private void addMapping(Class domainClass, ActionMapping mapping) {
        Map classMappings = (Map)domainToActionMappingMap.get(domainClass);
        if (classMappings == null){
            classMappings = new LinkedHashMap();
            domainToActionMappingMap.put(domainClass, classMappings);
        }
        classMappings.put(mapping.getName(), mapping);
    }

    public void addMapping(Class domainClass, RelationshipConvertor convertor) {
        HashMap classMappings = (HashMap) dataToDomainRelationshipMappingMap.get(domainClass);
        if (classMappings == null) {
            classMappings = new HashMap();
            dataToDomainRelationshipMappingMap.put(domainClass, classMappings);
        }
        classMappings.put(convertor.getAdapterProperty(), convertor);
    }

    public RelationshipConvertor getRelationshipMapping(net.sf.xplanner.domain.DomainObject domainObject, String propertyName) {
        Map classMappings = (Map) dataToDomainRelationshipMappingMap.get(domainObject.getClass());
        if (classMappings == null) return null;
        return (RelationshipConvertor) classMappings.get(propertyName);
    }

    public Collection getRelationshipMappings(DomainObject domainObject) {
        Map classMappings = (Map) dataToDomainRelationshipMappingMap.get(domainObject.getClass());
        if (classMappings == null) return Collections.EMPTY_LIST;
        return classMappings.values();
    }

    public ActionMapping getActionMapping(DomainObject domainObject, String action) {
        Map classMappings = (Map) domainToActionMappingMap.get(domainObject.getClass());
        if (classMappings == null) return null;
        return (ActionMapping) classMappings.get(action);
    }

   //FIXME Should sort the actions => add an order to the definition
    public Collection getActionsMappings(DomainObject domainObject) {
        Map classMappings = (Map) domainToActionMappingMap.get(domainObject.getClass());
        if (classMappings == null) return Collections.EMPTY_LIST;
        return classMappings.values();
    }

   /**
    * @deprecated DEBT(SPRING) Should be injected
    */
    public static RelationshipMappingRegistry getInstance() {
        if (instance == null) initDefaultRegistry();
        return instance;
    }

    public static void setInstanceForTest(RelationshipMappingRegistry instance) {
        RelationshipMappingRegistry.instance = instance;
    }
}