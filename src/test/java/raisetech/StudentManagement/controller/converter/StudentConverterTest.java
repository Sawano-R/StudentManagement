package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生詳細のコンバーターが適切に動作すること() {
    Student student1 = new Student();
    student1.setId(1);
    Student student2 = new Student();
    student2.setId(2);

    StudentCourse studentCourse = new StudentCourse();

    StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus();
    studentCourse.setId(1);
    studentCourseStatus1.setStudentCourse(studentCourse);
    StudentCourseStatus studentCourseStatus2 = new StudentCourseStatus();
    studentCourse.setId(2);
    studentCourseStatus2.setStudentCourse(studentCourse);
    StudentCourseStatus studentCourseStatus3 = new StudentCourseStatus();
    studentCourse.setId(1);
    studentCourseStatus3.setStudentCourse(studentCourse);
    StudentCourseStatus studentCourseStatus4 = new StudentCourseStatus();
    studentCourse.setId(1);
    studentCourseStatus4.setStudentCourse(studentCourse);

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourseStatus> studentCourseStatusList = List.of(studentCourseStatus1,
        studentCourseStatus2, studentCourseStatus3,
        studentCourseStatus4);
    List<StudentCourseStatus> studentCourseStatusList1 = List.of(studentCourseStatus1,
        studentCourseStatus3,
        studentCourseStatus4);
    List<StudentCourseStatus> studentCourseStatusList2 = List.of(studentCourseStatus2);
    List<StudentDetail> expected = List.of(new StudentDetail(student1, studentCourseStatusList1),
        new StudentDetail(student2, studentCourseStatusList2));

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseStatusList);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生コース状態のコンバーターが適切に動作すること() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId(1);
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId(2);
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setId(3);

    CourseStatus courseStatus1 = new CourseStatus();
    courseStatus1.setIdCourses(3);
    CourseStatus courseStatus2 = new CourseStatus();
    courseStatus2.setIdCourses(2);
    CourseStatus courseStatus3 = new CourseStatus();
    courseStatus3.setIdCourses(1);

    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);
    List<CourseStatus> courseStatusList = List.of(courseStatus1, courseStatus2, courseStatus3);

    List<StudentCourseStatus> expected = List.of(
        new StudentCourseStatus(studentCourse1, courseStatus3),
        new StudentCourseStatus(studentCourse2, courseStatus2),
        new StudentCourseStatus(studentCourse3, courseStatus1));

    List<StudentCourseStatus> actual = sut.convertStudentCourseStatusList(studentCourseList,
        courseStatusList);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void 受講生コース状態が１対１で存在しない場合エラーを返すこと_コース状態が余る時() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId(1);
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId(2);

    CourseStatus courseStatus1 = new CourseStatus();
    courseStatus1.setIdCourses(3);
    CourseStatus courseStatus2 = new CourseStatus();
    courseStatus2.setIdCourses(2);
    CourseStatus courseStatus3 = new CourseStatus();
    courseStatus3.setIdCourses(1);

    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2);
    List<CourseStatus> courseStatusList = List.of(courseStatus1, courseStatus2, courseStatus3);

    assertThatThrownBy(
        () -> sut.convertStudentCourseStatusList(studentCourseList, courseStatusList))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("CourseStatus と StudentCourse は両方存在する必要があります。");
  }

  @Test
  void 受講生コース状態が１対１で存在しない場合エラーを返すこと_受講生コースが余る時() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId(1);
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId(2);
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setId(3);

    CourseStatus courseStatus1 = new CourseStatus();
    courseStatus1.setIdCourses(3);
    CourseStatus courseStatus2 = new CourseStatus();
    courseStatus2.setIdCourses(2);

    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);
    List<CourseStatus> courseStatusList = List.of(courseStatus1, courseStatus2);

    assertThatThrownBy(
        () -> sut.convertStudentCourseStatusList(studentCourseList, courseStatusList))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("CourseStatus と StudentCourse は両方存在する必要があります。");
  }
}