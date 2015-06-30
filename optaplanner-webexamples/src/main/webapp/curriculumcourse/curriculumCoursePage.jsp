<%@page import="org.optaplanner.examples.curriculumcourse.domain.Lecture"%>
<%@page import="java.util.List"%>
<%@page import="org.optaplanner.examples.curriculumcourse.domain.Room"%>
<%@page import="org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore"%>
<%@page import="org.optaplanner.examples.curriculumcourse.domain.CourseSchedule"%>
<%@page import="org.optaplanner.curriculumcourse.CurriculumCourseSessionAttributeName"%>
<%@page import="org.optaplanner.curriculumcourse.CurriculumCourseWebAction"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>




<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="../twitterbootstrap/js/jquery.js"></script>
        <link href='../website/css/optaplannerWebexamples.css' rel='styleSheet' type='text/css' />

        <title>JSP Page</title>
    </head>

    <%

        CourseSchedule solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);
        HardSoftScore score = solution.getScore();

        List<Lecture> lectures = solution.getLectureList();
        session.setAttribute("score", score);
        session.setAttribute("lectures", lectures);


    %>
    <script>

        $(document).ready(function () {

            var changeList = {};
            row = 1;
            col = 0;
            var cell = $('table tbody tr:eq(' + row + ') td:eq(' + col + ')');
            var tdnumb = $('table').children('thead').children('tr').children('th').length;
            var thList = [];
            for (i = 0; i < tdnumb; i++) {
                thList[i] = $('table thead tr:eq(0) th:eq(' + i + ')').html().trim();
            }
            perSize = parseInt("${timeSlots.size()}");

        <c:forEach var = "lecture" items = "${lectures}" >
            roomLabel = "${lecture.room.label}";
            col = thList.indexOf(roomLabel) - 2;
            dayIndex = ${lecture.day.dayIndex};
            timeSlotIndex = ${lecture.period.timeslot.timeslotIndex};
            row = (perSize * dayIndex) + timeSlotIndex;
            cell = $('table tbody tr:eq(' + row + ') td:eq(' + col + ')');
            s = "${lecture.course.name} <br /> ${lecture.course.teacher.name}  ${lecture.course.teacher.surname} <br /> ${lecture.course.lectureSize} : ${lecture.lectureIndexInCourse} <br />";
            <c:forEach var = "curriculum" items = "${lecture.course.curriculumList}" >
            s += "${curriculum.toString()}, ";
            </c:forEach >
            s = s.substr(0, s.length - 2);
            cell.css("background-color", "rgb(${colorOfCourses[lecture.course.code].red},${colorOfCourses[lecture.course.code].green},${colorOfCourses[lecture.course.code].blue})");
            cell.find("label").html(s);
            cell.find("label").attr("id", "${lecture.course.code}:${lecture.lectureIndexInCourse}");
        </c:forEach>

        <c:if test="${solver.solving == false}">

                    $('.event').on("dragstart", function (event) {

                        var dt = event.originalEvent.dataTransfer;
                        dt.setData('bg-color', $(this).parent().css("background-color"));
                        dt.setData('lectureId', $(this).attr('id'));
                        dt.setData('text', $(this).html());
                        dt.setData('comeIndex', $(this).parent().attr("id"));
                        $(this).attr('id', '');
                        $(this).html('');
                        $(this).parent().css("background-color", "rgb(255,255,255)");

                    });


                    $('table td').on("dragenter dragover drop", function (event) {
                        event.preventDefault();
                        if (event.type === 'drop') {

                            var lectureId = event.originalEvent.dataTransfer.getData('lectureId');
                            var text = event.originalEvent.dataTransfer.getData('text', $(this).html());
                            var bgColor = event.originalEvent.dataTransfer.getData('bg-color', $(this).parent().css("background-color"));


                            changeList[lectureId] = $(this).attr("id");


                            if ($(this).children("label").text().length !== 0) {
                                var thisLectureId = $(this).children("label").attr("id");
                                var thisBgColor = $(this).css("background-color");
                                var thisHtml = $(this).children("label").html();
                                var comeIndex = event.originalEvent.dataTransfer.getData("comeIndex");

                                $("#" + comeIndex).css("background-color", thisBgColor);
                                $("#" + comeIndex).children("label").attr("id", thisLectureId);
                                $("#" + comeIndex).children("label").html(thisHtml);
                                changeList[thisLectureId] = comeIndex;
                                //$("#"+data).parent().css("background-color", "rgb(255,255,255)");
                                //$("#"+data).text("");

                            }
                            $(this).children("label").html(text);
                            $(this).children("label").attr("id", lectureId);
                            $(this).css("background-color", bgColor);
                        }




                    });
        </c:if>
                    $("#exportButton").click(function () {

                        $("#changeList").val(JSON.stringify(changeList));
                        console.log($("#changeList").val());
                        window.location.href = '../CurriculumCourseSaveServlet';

                    });

                });


    </script>

    
    <body>
        <form action="CurriculumCourseSaveServlet" method="post">
            <input type="hidden" id="changeList" name="changeList"/>
            <c:if test="${solver.solving == false}">
                <input type="submit" value="Dışarı Aktar" id="exportButton"/>
            </c:if>
            
        </form>
        ${size}
        <p style="margin-top: 10px;">Cost: <%=score == null ? "" : score.isFeasible() ? -score.getSoftScore() + " $" : "Infeasible"%></p>
        <c:out value="${lectures.size()}" />
        <c:out value="${timeSlots.size()}" />
        <c:out value="${days.size()}" />
        <table border="1" id="myt" class="table table-bordered">
            <thead>
                <tr>
                    <th>Day</th>
                    <th>Period</th>
                        <c:forEach items="${rooms}" var="room">
                        <th scope="col">
                            <c:out value="${room.label}" />
                        </th>
                    </c:forEach>

                </tr>

            </thead>
            <tbody>

                <c:forEach items="${days}" var="day" varStatus="j">
                    <c:forEach items="${timeSlots}" var="timeslot" varStatus="i">
                        <tr >
                            <c:if test="${i.index == 0}">
                                <th rowspan="${timeSlots.size()}"><c:out value="${day.label}" /></th>
                                </c:if>

                            <th><c:out value="${timeslot.label}" /></th>
                                <c:forEach var="room" items="${rooms}" varStatus="k">
                                <td id='${j.index}x${i.index}x${k.index}' style='text-align: center; cursor: pointer;'>
                                    <label id="" class="event" draggable="true"></label>
                                </td>
                            </c:forEach>

                        </tr>
                    </c:forEach>
                </c:forEach>

            </tbody>
        </table>
    </body>
</html>
