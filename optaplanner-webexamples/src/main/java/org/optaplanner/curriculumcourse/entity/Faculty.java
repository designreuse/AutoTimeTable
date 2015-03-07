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
//@Entity(name = "Faculty")
//@Table(name = "Faculty")
//public class Faculty {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//    
//    @Column(name = "facultyName", unique = true)
//    private String facultyName;
//    
//    @OneToMany
//    @JoinTable(name = "facultiesDepartment",
//            joinColumns = @JoinColumn(name = "faculty"),
//            inverseJoinColumns = @JoinColumn(name = "department"))
//    private List<Department> departments;
//    
//    public Faculty(String facultyName) {
//        this.facultyName = facultyName;
//        this.departments = new ArrayList<Department>();
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getFacultyName() {
//        return facultyName;
//    }
//
//    public void addDepartment(Department department) {
//        departments.add(department);
//    }
//    
//    
//    
//}
