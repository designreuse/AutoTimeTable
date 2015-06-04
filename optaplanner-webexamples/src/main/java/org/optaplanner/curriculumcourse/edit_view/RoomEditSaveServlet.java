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
package org.optaplanner.curriculumcourse.edit_view;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.examples.curriculumcourse.domain.Room;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/RoomEditSaveServlet")
public class RoomEditSaveServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        RoomDao tDao = new RoomDao(em);
        Room room;
        String id = req.getParameter("roomId");
        if (id == null || id.trim().equals("")) {
            room = new Room();

        } else {
            room = tDao.find(Room.class, Long.parseLong(id));
        }

        try {
            room.setCode(req.getParameter("roomCode"));
            room.setCapacity(Integer.parseInt(req.getParameter("roomCapacity")));
            tDao.createOrUpdate(room);
        } catch (Exception e) {
            System.out.println("Eklenemedi");
            e.printStackTrace();
        }
        resp.sendRedirect("RoomsViewServlet");
    }
    
}
