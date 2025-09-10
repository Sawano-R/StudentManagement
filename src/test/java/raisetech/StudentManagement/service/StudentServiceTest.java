package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseStatus;
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

    List<CourseStatus> courseStatusList = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchCourseList()).thenReturn(studentCourseList);
    when(repository.searchStatusList()).thenReturn(courseStatusList);

    //実行
    sut.searchStudentDetailList();

    //検証
    verify(repository).search();
    verify(repository).searchCourseList();
    verify(repository).searchStatusList();
    verify(converter).convertStudentCourseStatusList(studentCourseList, courseStatusList);
    verify(converter).convertStudentDetails(any(), any());

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
    List<CourseStatus> courseStatusList = new ArrayList<>();

    when(repository.searchStudentName("nameTest")).thenReturn(student);
    when(repository.searchCourseID(1)).thenReturn(studentCourse);
    when(repository.searchStatusID(1)).thenReturn(courseStatusList);

    StudentDetail actual = sut.matchName(inputStudentDetail);

    verify(repository).searchStudentName("nameTest");
    verify(repository).searchCourseID(1);
    verify(repository).searchStatusID(1);
    verify(converter).convertStudentCourseStatusList(any(), any());
    assertThat(actual.getStudent()).isEqualTo(student);
  }

  @Test
  void 受講生詳細の名前一致での検索機能_名前が存在しないとき例外に投げること() {
    StudentDetail inputStudentDetail = new StudentDetail();
    Student inputStudent = new Student();
    inputStudent.setName("nameTest");
    inputStudentDetail.setStudent(inputStudent);

    when(repository.searchStudentName("nameTest")).thenReturn(null);

    assertThatThrownBy(() -> sut.matchName(inputStudentDetail))
        .isInstanceOf(TestException.class)
        .hasMessageContaining("存在しない名前です。");
    verify(repository, times(0)).searchCourseID(anyInt());
  }

  @Test
  void 受講生詳細のID一致での検索機能_入力した受講生IDに対応したリポジトリの処理が適切に呼び出せていること()
      throws TestException {
    Student student = new Student();

    List<StudentCourse> studentCourse = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();

    when(repository.searchStudentID(1)).thenReturn(student);
    when(repository.searchCourseID(1)).thenReturn(studentCourse);
    when(repository.searchStatusID(1)).thenReturn(courseStatusList);

    StudentDetail actual = sut.matchID(1);

    verify(repository).searchStudentID(1);
    verify(repository).searchCourseID(1);
    verify(repository).searchStatusID(1);
    verify(converter).convertStudentCourseStatusList(any(), any());
    assertThat(actual.getStudent()).isEqualTo(student);
  }

  @Test
  void 受講生詳細のID一致での検索機能_入力した受講生IDが存在しないときに例外を投げること() {
    when(repository.searchStudentID(1)).thenReturn(null);

    assertThatThrownBy(() -> sut.matchID(1))
        .isInstanceOf(TestException.class)
        .hasMessageContaining("存在しないIDです。");
    verify(repository, times(0)).searchCourseID(anyInt());
  }

  @Test
  void 受講生詳細の登録_レポジトリが適切に呼び出せれていること() {
    Student inputStudent = new Student();

    StudentCourseStatus inputSCS1 = new StudentCourseStatus();
    inputSCS1.setStudentCourse(new StudentCourse());
    inputSCS1.setCourseStatus(new CourseStatus());
    StudentCourseStatus inputSCS2 = new StudentCourseStatus();
    inputSCS2.setStudentCourse(new StudentCourse());
    inputSCS2.setCourseStatus(new CourseStatus());
    List<StudentCourseStatus> studentCourseStatusList = List.of(inputSCS1, inputSCS2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseStatusList);

    //自動採番を実施するモック
    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(123);
      return null;
    }).when(repository).registerStudent(any(Student.class));
    doAnswer(invocation -> {
      StudentCourse arg = invocation.getArgument(0);
      arg.setId(234);
      return null;
    }).when(repository).registerCourse(any(StudentCourse.class));

    StudentDetail actual = sut.registerStudent(inputStudentDetail);

    verify(repository).registerStudent(inputStudent);
    verify(repository, times(2)).registerCourse(any(StudentCourse.class));
    verify(repository, times(2)).registerStatus(any(CourseStatus.class));
    assertThat(actual.getStudent().getId()).isEqualTo(123);
    assertThat(actual.getStudentCourseStatusList().getFirst().getStudentCourse().getIdStudents()).isEqualTo(123);
    assertThat(actual.getStudentCourseStatusList().getFirst().getCourseStatus().getIdStudents()).isEqualTo(123);
    assertThat(actual.getStudentCourseStatusList().getFirst().getCourseStatus().getIdCourses()).isEqualTo(234);
  }

  @Test
  void 受講生コース状態への情報の格納_受講生詳細登録を呼び出しその中で使われるinitStudentCourseとinitCourseStatusが適切に処理されていること() {
    Student inputStudent = new Student();
    inputStudent.setId(1);

    StudentCourse inputCourse = new StudentCourse();
    inputCourse.setId(2);
    CourseStatus inputStatus = new CourseStatus();
    inputStatus.setStatus("テスト");
    StudentCourseStatus inputSCS = new StudentCourseStatus(inputCourse, inputStatus);
    List<StudentCourseStatus> studentCourseStatusList = List.of(inputSCS);

    StudentDetail studentDetail = new StudentDetail(inputStudent, studentCourseStatusList);

    Date testStartDay = Date.valueOf(LocalDate.now());
    Date testEndDay = Date.valueOf(LocalDate.now().plusMonths(3));

    StudentDetail actual = sut.registerStudent(studentDetail);

    for (StudentCourseStatus SCS : actual.getStudentCourseStatusList()) {
      assertThat(SCS.getStudentCourse().getIdStudents()).isEqualTo(1);
      assertThat(SCS.getStudentCourse().getStartDay()).isEqualTo(testStartDay);
      assertThat(SCS.getStudentCourse().getEndDay()).isEqualTo(testEndDay);
      assertThat(SCS.getCourseStatus().getIdStudents()).isEqualTo(1);
      assertThat(SCS.getCourseStatus().getIdCourses()).isEqualTo(2);
      assertThat(SCS.getCourseStatus().getStatus()).isEqualTo("テスト");
    }
  }

  @Test
  void 受講生詳細の更新_レポジトリが適切に呼び出せれること() {
    Student inputStudent = new Student();
    inputStudent.setId(1);

    Student student = new Student();
    student.setId(1);
    List<Student> students = List.of(student);

    StudentCourseStatus inputSCS1 = new StudentCourseStatus();
    inputSCS1.setStudentCourse(new StudentCourse());
    inputSCS1.setCourseStatus(new CourseStatus());
    StudentCourseStatus inputSCS2 = new StudentCourseStatus();
    inputSCS2.setStudentCourse(new StudentCourse());
    inputSCS2.setCourseStatus(new CourseStatus());
    List<StudentCourseStatus> studentCourseStatusList = List.of(inputSCS1, inputSCS2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseStatusList);

    when(repository.search()).thenReturn(students);

    sut.updateStudent(inputStudentDetail);

    verify(repository).search();
    verify(repository).updateStudent(inputStudent);
    verify(repository, times(2)).updateCourse(any(StudentCourse.class));
    verify(repository, times(2)).updateStatus(any(CourseStatus.class));
  }

  @Test
  void 受講生更新で存在しないIDの場合エラーを返すこと() {
    Student inputStudent = new Student();
    inputStudent.setId(1);

    Student student = new Student();
    student.setId(2);
    List<Student> students = List.of(student);

    StudentCourseStatus inputSCS1 = new StudentCourseStatus();
    StudentCourseStatus inputSCS2 = new StudentCourseStatus();
    List<StudentCourseStatus> studentCourseStatusList = List.of(inputSCS1, inputSCS2);

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent, studentCourseStatusList);

    when(repository.search()).thenReturn(students);

    assertThatThrownBy(() -> sut.updateStudent(inputStudentDetail))
        .isInstanceOf(TestException.class)
        .hasMessageContaining("受講生情報が存在しません。");

    verify(repository).search();
    verify(repository, times(0)).updateStudent(inputStudent);
    verify(repository, times(0)).updateCourse(any(StudentCourse.class));
    verify(repository, times(0)).updateStatus(any(CourseStatus.class));
  }
}