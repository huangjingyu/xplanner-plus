package com.technoetic.xplanner.domain.repository;

public interface MetaRepository {
    ObjectRepository  getRepository(Class objectClass);

    void setRepository(Class objectClass, ObjectRepository repository);
}
