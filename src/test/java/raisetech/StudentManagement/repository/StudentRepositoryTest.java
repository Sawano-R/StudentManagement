package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索ができること() {
    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生のID検索ができること() {
    Student expected = new Student();
    expected.setId(2);
    expected.setName("佐藤 花子");
    expected.setNameKana("さとう　はなこ");
    expected.setNickname("はな");
    expected.setMail("sato.hanako@example.com");
    expected.setResion("北海道");
    expected.setAge(34);
    expected.setGender("女");

    Student actual = sut.searchStudentID(2);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生のID検索で存在しないIDを入力するとnullを返すこと() {
    Student actual = sut.searchStudentID(99);

    assertThat(actual).isEqualTo(null);
  }

  @Test
  void 受講生の名前検索ができること() {
    Student expected = new Student();
    expected.setId(2);
    expected.setName("佐藤 花子");
    expected.setNameKana("さとう　はなこ");
    expected.setNickname("はな");
    expected.setMail("sato.hanako@example.com");
    expected.setResion("北海道");
    expected.setAge(34);
    expected.setGender("女");

    Student actual = sut.searchStudentName("佐藤 花子");

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生の名前検索で存在しない名前で検索するとnullに返すこと() {
    Student actual = sut.searchStudentName("テスト987");

    assertThat(actual).isEqualTo(null);
  }

  @Test
  void 受講生コースの全件検索ができること() {
    List<StudentCourse> actual = sut.searchCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 受講生コースの受講生ID検索ができること() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setId(1);
    studentCourse1.setIdStudents(1);
    studentCourse1.setCourse("java");
    studentCourse1.setStartDay(Date.valueOf("2025-05-12"));
    studentCourse1.setEndDay(Date.valueOf("2025-08-12"));
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setId(2);
    studentCourse2.setIdStudents(1);
    studentCourse2.setCourse("excel");
    studentCourse2.setStartDay(Date.valueOf("2025-05-12"));
    studentCourse2.setEndDay(Date.valueOf("2025-07-12"));
    List<StudentCourse> expected = List.of(studentCourse1, studentCourse2);

    List<StudentCourse> actual = sut.searchCourseID(1);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生コースが存在しない受講生ID検索すると空のリストを返すこと() {
    List<StudentCourse> expected = new ArrayList<>();

    List<StudentCourse> actual = sut.searchCourseID(99);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 受講生の登録ができること() {
    Student student = new Student();
    student.setName("テスト");
    student.setNameKana("テスト");
    student.setNickname("テスト");
    student.setMail("test@example.com");
    student.setResion("テスト");
    student.setAge(12);
    student.setGender("その他");
    student.setRemark("特になし");

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual.get(5)).isEqualTo(student);
  }

  @Test
  void 受講生コースの登録ができること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setIdStudents(5);
    studentCourse.setCourse("テスト");
    studentCourse.setStartDay(Date.valueOf("2025-09-21"));
    studentCourse.setEndDay(Date.valueOf("2025-09-24"));

    sut.registerCourse(studentCourse);

    List<StudentCourse> actual = sut.searchCourseList();
    assertThat(actual.size()).isEqualTo(7);
    assertThat(actual.get(6)).isEqualTo(studentCourse);
  }

  @Test
  void 受講生の更新ができること() {
    Student student = new Student();
    student.setId(1);
    student.setName("テスト");
    student.setNameKana("テスト");
    student.setNickname("テスト");
    student.setMail("test@example.com");
    student.setResion("テスト");
    student.setAge(12);
    student.setGender("その他");
    student.setRemark("特になし");
    student.setDeleted(false);

    sut.updateStudent(student);

    Student actual = sut.searchStudentID(1);
    assertThat(actual).isEqualTo(student);
  }

  @Test
  void 受講生コースの更新ができること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(1);
    studentCourse.setCourse("テスト");
    studentCourse.setDeleted(false);

    sut.updateCourse(studentCourse);

    List<StudentCourse> actual = sut.searchCourseList();
    assertThat(actual.get(0).getCourse()).isEqualTo("テスト");
  }
}