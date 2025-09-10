package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生と受講生コース情報を受講生詳細に変換するコンバーターです。
 */
@Component
public class StudentConverter {


  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentCourseStatus> studentCourseStatusList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourseStatus> convertStudentCourseStatusList = studentCourseStatusList.stream()
          .filter(studentCourseStatus -> student.getId()
              .equals(studentCourseStatus.getStudentCourse().getIdStudents()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseStatusList(convertStudentCourseStatusList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  public List<StudentCourseStatus> convertStudentCourseStatusList(
      List<StudentCourse> studentCourseList, List<CourseStatus> courseStatusList) {
    List<StudentCourseStatus> studentCourseStatuses = new ArrayList<>();
    studentCourseList.forEach(studentCourse -> {
      StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
      studentCourseStatus.setStudentCourse(studentCourse);

      CourseStatus convertCourseStatus = courseStatusList.stream()
          .filter(courseStatus -> studentCourse.getId().equals(courseStatus.getIdCourses()))
          .findFirst().orElse(null);

      studentCourseStatus.setCourseStatus(convertCourseStatus);
      studentCourseStatuses.add(studentCourseStatus);
    });
    for (StudentCourseStatus status : studentCourseStatuses) {
      boolean hasCourseStatus = status.getCourseStatus() != null;
      boolean hasStudentCourse = status.getStudentCourse() != null;

      if (hasCourseStatus ^ hasStudentCourse) {
        throw new IllegalStateException(
            "CourseStatus と StudentCourse は両方存在する必要があります。"
        );
      }
    }
    return studentCourseStatuses;
  }
}
