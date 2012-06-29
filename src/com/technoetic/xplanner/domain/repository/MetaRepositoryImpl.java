package com.technoetic.xplanner.domain.repository;

import java.util.HashMap;
import java.util.Map;

public class MetaRepositoryImpl implements MetaRepository {
    private Map repositories = new HashMap();

   public void setRepositories(Map repositories) {
      this.repositories = repositories;
   }

   public ObjectRepository getRepository(Class objectClass) {
        return (ObjectRepository)repositories.get(objectClass);
    }

    public void setRepository(Class objectClass, ObjectRepository repository) {
        repositories.put(objectClass, repository);
    }
}
