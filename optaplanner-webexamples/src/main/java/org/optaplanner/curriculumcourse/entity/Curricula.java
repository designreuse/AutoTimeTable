///*
// * Copyright 2015 JBoss by Red Hat.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.optaplanner.curriculumcourse.entity;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
///**
// *
// * @author gurhan
// */
//@Entity(name = "Curricula")
//@Table(name = "Curricula")
//public class Curricula implements Serializable {
//
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//
//    @Column(name = "code")
//    private String code;
//
//    @OneToMany
//    @JoinTable(name = "CurriculasCourses",
//            joinColumns = @JoinColumn(name = "curricula"),
//            inverseJoinColumns = @JoinColumn(name = "course"))
//    private List<CourseName> courses;
//
//    public Curricula() {
//        courses = new ArrayList<CourseName>();
//    }
//
//    public Curricula(String code) {
//        this.code = code;
//        courses = new ArrayList<CourseName>();
//    }
//
//    public void addCourse(CourseName cn) {
//        courses.add(cn);
//    }
//
//    public List<CourseName> getCourses() {
//        return courses;
//    }
//
//    public void setCourses(List<CourseName> courses) {
//        this.courses = courses;
//    }
//    
//
//}
