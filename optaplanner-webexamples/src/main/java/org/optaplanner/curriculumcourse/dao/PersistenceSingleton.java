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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author gurhan
 */
public class PersistenceSingleton {
    
    /**
     * The singleton EntityManagerFactory instance that is used across the
     * application.
     */
    private static EntityManagerFactory m_entityManagerFactory;

    /**
     * A singleton EntityManager instance that is only used during application
     * initialization
     */
    private static EntityManager m_entityManager;

    /**
     * Obtains the singleton EntityManagerFactory instance.
     *
     * @return a EntityManagerFactory instance
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (m_entityManagerFactory == null) {
            try {
                m_entityManagerFactory = Persistence.createEntityManagerFactory("curriculum-pu");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
       
        return m_entityManagerFactory;
    }

    /**
     * Obtains the singleton EntityManager instance.
     *
     * @return a EntityManager instance
     */
    public static EntityManager getEntityManager() {
        if (m_entityManager == null) {
            try {
                m_entityManager = getEntityManagerFactory().createEntityManager();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return m_entityManager;
    }
    
   
    
}
