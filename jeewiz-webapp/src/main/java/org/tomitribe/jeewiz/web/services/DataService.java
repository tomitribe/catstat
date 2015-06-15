/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tomitribe.jeewiz.web.services;


import org.tomitribe.jeewiz.web.model.Persistable;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author WalmartLabs
 * @author Ryan McGuinness [rmcguinness@walmartlabs.com]
 *         Created: 6/5/15
 */
@Named
@Singleton
public class DataService {
    @PersistenceContext(unitName = "exampleUnit")
    private EntityManager em;

    @PostConstruct
    public void init() {
        System.out.println("########### Data Service Initialized ##########");
    }

    public <E> E saveOrUpdate(E e) {
        if (!(e instanceof Persistable)) {
            throw new RuntimeException(
                "Invalid object type, unable to save or update");
        }
        if (((Persistable) e).getId()==null) {
            return create(e);
        } else {
            return update(e);
        }
    }

    public <E> E create(E e) {
        em.persist(e);
        return e;
    }

    public <E> E update(E e) {
        return em.merge(e);
    }

    public <E> void delete(Class<E> clazz, String id) {
        em.remove(em.find(clazz, id));
    }

    public <E> E find(Class<E> clazz, String id) {
        return em.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> find(Class<E> clazz, String query, int min, int max) {
        return queryRange(em.createQuery(query, clazz), min, max)
            .getResultList();
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> namedFind(Class<E> clazz, String query, int min, int max) {
        return queryRange(em.createNamedQuery(query, clazz), min, max)
            .getResultList();
    }

    public <E> List<E> findByQuery(Class<E> clazz, String queryName) {
        List<E> r = em.createNamedQuery(queryName, clazz).getResultList();
        return r;
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> findByQueryWithParams(Class<E> clazz, String queryName,
                                             Object... parms) {
        Query q = em.createNamedQuery(queryName, clazz);
        for (int i = 0; i < parms.length; i++) {
            q.setParameter(i+1, parms[i]);
        }
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <E> E findUniqueByQueryWithParams(Class<E> clazz, String queryName,
                                             Object... parms) {
        Query q = em.createNamedQuery(queryName, clazz);
        for (int i = 0; i < parms.length; i++) {
            q.setParameter(i + 1, parms[i]);
        }
        try {
            return (E) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    private static Query queryRange(Query query, int min, int max) {

        if (max >= 0) {
            query.setMaxResults(max);
        }
        if (min >= 0) {
            query.setFirstResult(min);
        }
        return query;
    }
}
