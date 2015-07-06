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
package org.optaplanner.curriculumcourse.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import static javafx.scene.input.KeyCode.T;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.GenericDao;
import org.optaplanner.curriculumcourse.dao.GenericDaoImp;
import org.optaplanner.examples.common.domain.AbstractPersistable;

/**
 *
 * @author gurhan
 */
public class GenericServiceImpl<T> implements GenericService<T> {

    protected GenericDao genericDao;
    protected EntityManager em;
    protected Class<T> type;
    protected HttpServletRequest req;
    protected HttpServletResponse resp;
    protected Message message;

    public GenericServiceImpl(HttpServletRequest req, HttpServletResponse resp, Class<T> cls) throws UnsupportedEncodingException {
        message = new Message();
        type = cls;
        this.req = req;
        req.setCharacterEncoding("utf-8");
        this.resp = resp;
        em = (EntityManager) req.getServletContext().getAttribute("enttiyManager");
        genericDao = new GenericDaoImp(em, cls);
    }

    @Override
    public T find(String id) {
        return (T) genericDao.find(Long.parseLong(id));
    }

    @Override
    public T find(Long id) {
        return (T) genericDao.find(id);
    }

    @Override
    public T createOrUpdate(T t) {
        return (T) genericDao.createOrUpdate(t);
    }

    @Override
    public void delete(T t) {
        genericDao.delete(((AbstractPersistable) t).getId());

    }

    @Override
    public void delete(Long id) {
        genericDao.delete(id);
    }

    @Override
    public void editSave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public HttpServletRequest getReq() {
        return req;
    }

    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public HttpServletResponse getResp() {
        return resp;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    
}
