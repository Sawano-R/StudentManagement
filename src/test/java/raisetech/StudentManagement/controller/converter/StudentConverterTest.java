package raisetech.StudentManagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
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

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setIdStudents(1);
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setIdStudents(2);
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setIdStudents(1);
    StudentCourse studentCourse4 = new StudentCourse();
    studentCourse4.setIdStudents(1);

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3,
        studentCourse4);
    List<StudentCourse> studentCourseList1 = List.of(studentCourse1, studentCourse3,
        studentCourse4);
    List<StudentCourse> studentCourseList2 = List.of(studentCourse2);
    List<StudentDetail> expected = List.of(new StudentDetail(student1, studentCourseList1),
        new StudentDetail(student2, studentCourseList2));

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }
}