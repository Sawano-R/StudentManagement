package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索機能_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    List<Student> studentList = new ArrayList<>();

    List<StudentCourse> studentCourseList = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchCourseList()).thenReturn(studentCourseList);

    //実行
    sut.searchStudentDetailList();

    //検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

  }

  @Test
  void 受講生詳細の名前一致での検索機能_入力した受講生の名前とそこから呼び出したIDに対応したリポジトリの処理が適切に呼び出せていること()
      throws TestException {
    StudentDetail inputStudentDetail = new StudentDetail();
    Student inputStudent = new Student();
    inputStudent.setName("nameTest");
    inputStudentDetail.setStudent(inputStudent);

    Student student = new Student();
    student.setId(1);

    List<StudentCourse> studentCourse = new ArrayList<>();

    when(repository.searchStudentName("nameTest")).thenReturn(student);
    when(repository.searchCourseID(1)).thenReturn(studentCourse);

    StudentDetail actual = sut.matchName(inputStudentDetail);

    verify(repository).searchStudentName("nameTest");
    verify(repository).searchCourseID(1);
    Assertions.assertEquals(new StudentDetail(student, studentCourse), actual);
  }

  @Test
  void 受講生詳細の名前一致での検索機能_名前が存在しないとき例外に投げること() {
    StudentDetail inputStudentDetail = new StudentDetail();
    Student inputStudent = new Student();
    inputStudent.setName("nameTest");
    inputStudentDetail.setStudent(inputStudent);

    when(repository.searchStudentName("nameTest")).thenReturn(null);

    TestException ex = assertThrows(TestException.class, () -> sut.matchName(inputStudentDetail));
    Assertions.assertEquals("存在しない名前です。", ex.getMessage());
    verify(repository, times(0)).searchCourseID(anyInt());
  }

  @Test
  void 受講生詳細のID一致での検索機能_入力した受講生IDに対応したリポジトリの処理が適切に呼び出せていること()
      throws TestException {
    Student student = new Student();

    List<StudentCourse> studentCourse = new ArrayList<>();

    when(repository.searchStudentID(1)).thenReturn(student);
    when(repository.searchCourseID(1)).thenReturn(studentCourse);

    StudentDetail actual = sut.matchID(1);

    verify(repository).searchStudentID(1);
    verify(repository).searchCourseID(1);
    Assertions.assertEquals(new StudentDetail(student, studentCourse), actual);
  }

  @Test
  void 受講生詳細のID一致での検索機能_入力した受講生IDが存在しないときに例外を投げること() {
    when(repository.searchStudentID(1)).thenReturn(null);

    TestException ex = assertThrows(TestException.class, () -> sut.matchID(1));
    Assertions.assertEquals("存在しないIDです。", ex.getMessage());
    verify(repository, times(0)).searchCourseID(anyInt());
  }

  @Test
  void 受講生詳細の登録_レポジトリが適切に呼び出せれていること() {
    Student inputStudent = new Student();

    StudentCourse inputCourse1 = new StudentCourse();
    StudentCourse inputCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(inputCourse1, inputCourse2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseList);

    sut.registerStudent(inputStudentDetail);

    verify(repository).registerStudent(inputStudent);
    verify(repository, times(2)).registerCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生コースへの開始日と終了日の格納_受講生詳細登録を呼び出しその中で使われるinitStudentCourseが適切に処理されていること() {
    Student inputStudent = new Student();

    StudentCourse inputCourse1 = new StudentCourse();
    StudentCourse inputCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(inputCourse1, inputCourse2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseList);

    Date testStartDay = Date.valueOf(LocalDate.now());
    Date testEndDay = Date.valueOf(LocalDate.now().plusMonths(3));

    StudentDetail actual = sut.registerStudent(inputStudentDetail);

    for (StudentCourse course : actual.getStudentCourseList()) {
      Assertions.assertEquals(testStartDay, course.getStartDay());
      Assertions.assertEquals(testEndDay, course.getEndDay());
    }
  }

  @Test
  void 受講生詳細の更新_レポジトリが適切に呼び出せれること() {
    Student inputStudent = new Student();

    StudentCourse inputCourse1 = new StudentCourse();
    StudentCourse inputCourse2 = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(inputCourse1, inputCourse2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseList);

    sut.updateStudent(inputStudentDetail);

    verify(repository).updateStudent(inputStudent);
    verify(repository, times(2)).updateCourse(any(StudentCourse.class));
  }
}