/*
 * Copyright 2015 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.curriculumcourse.dao;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Parameter;
import org.eclipse.persistence.internal.jpa.querydef.ParameterExpressionImpl;
import org.optaplanner.examples.common.app.LoggingMain;

/**
 *
 * @author gurhan
 */
public class GenericDaoImp<T> extends LoggingMain implements GenericDao<T> {

    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected EntityTransaction tx;
    
    
    
    /*
    Parametre olarak sessiondan gelen entity manageri alsın ve onun üzerinden
    işlem yapsın.Session entity manager ise context listener kullanılarak yazılsın
    ve context destroy edilirken silinsin
    */
    public GenericDaoImp(EntityManager em) {
        this.em = em;
        this.tx = em.getTransaction();
    }

    @Override
    public T save(T t) {
        tx.begin();
        em.persist(t);
        tx.commit();
        return t;
    }

    @Override
    public T update(T t) {
        tx.begin();
        em.merge(t);
        tx.commit();
        return t;
    }

    @Override
    public T find(Class type, Object id) {
        return (T) this.em.find(type, id);
    }

    @Override
    public T createOrUpdate(T t) {
        tx.begin();
        em.merge(t);
        tx.commit();
        return t;
    }
    
    

    @Override
    public void delete(Class type, Object id) {
        Object ref = this.em.getReference(type, id);
        tx.begin();
        em.remove(ref);
        tx.commit();
    }

    @Override
    public boolean isObjectManaged(T t) {
        return em.contains(t);

    }

   

}
