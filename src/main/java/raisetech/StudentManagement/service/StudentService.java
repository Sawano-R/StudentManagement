package raisetech.StudentManagement.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseStatus;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索、登録を行います。
 */

@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生の全件検索です。それぞれ受講生自体、受講生のコースを検索します。
   *
   * @return　受講生
   */
  public List<StudentDetail> searchStudentDetailList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCoursesList = repository.searchCourseList();
    List<CourseStatus> courseStatusList = repository.searchStatusList();
    List<StudentCourseStatus> studentCourseStatusList = converter.convertStudentCourseStatusList(
        studentCoursesList, courseStatusList);
    return converter.convertStudentDetails(studentList, studentCourseStatusList);
  }

  /**
   * 受講生検索です。入力した名前と合致する受講生情報を検索します。
   *
   * @param studentDetail 　受講生の名前
   * @return　受講生(名前一致)
   */
  public StudentDetail matchName(StudentDetail studentDetail) throws TestException {
    Student student = repository.searchStudentName(studentDetail.getStudent().getName());
    if (student == null) {
      throw new TestException("存在しない名前です。");
    }
    List<StudentCourse> studentCourses = repository.searchCourseID(student.getId());
    List<CourseStatus> courseStatuses = repository.searchStatusID(student.getId());
    List<StudentCourseStatus> studentCourseStatusList = converter.convertStudentCourseStatusList(
        studentCourses, courseStatuses);
    return new StudentDetail(student, studentCourseStatusList);
  }

  /**
   * 受講生検索です。入力したIDと合致する受講生情報を検索します。
   *
   * @param id 　受講生のID
   * @return　受講生(ID一致)
   */
  public StudentDetail matchID(Integer id) throws TestException {
    Student student = repository.searchStudentID(id);
    if (student == null) {
      throw new TestException("存在しないIDです。");
    }
    List<StudentCourse> studentCourses = repository.searchCourseID(id);
    List<CourseStatus> courseStatuses = repository.searchStatusID(id);
    List<StudentCourseStatus> studentCourseStatusList = converter.convertStudentCourseStatusList(
        studentCourses, courseStatuses);
    return new StudentDetail(student, studentCourseStatusList);
  }

  /**
   * 受講生詳細の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、終了日を設定します。
   *
   * @param studentDetail 　受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());

    Integer studentID = studentDetail.getStudent().getId();
    studentDetail.getStudentCourseStatusList().forEach(studentCourseStatus -> {
      initStudentCourse(studentID, studentCourseStatus.getStudentCourse());
      repository.registerCourse(studentCourseStatus.getStudentCourse());

      initCourseStatus(studentCourseStatus, studentID);
      repository.registerStatus(studentCourseStatus.getCourseStatus());

    });
    return studentDetail;
  }

  private static void initCourseStatus(StudentCourseStatus studentCourseStatus, Integer studentID) {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setIdStudents(studentID);
    courseStatus.setIdCourses(studentCourseStatus.getStudentCourse().getId());
    courseStatus.setStatus(studentCourseStatus.getCourseStatus().getStatus());
    studentCourseStatus.setCourseStatus(courseStatus);
  }

  /**
   * コース情報に受講生ID、コース開始日、終了日を格納する。
   *
   */
  private static void initStudentCourse(Integer studentID,
      StudentCourse studentCourse) {
    LocalDate now = LocalDate.now();

    studentCourse.setIdStudents(studentID);
    studentCourse.setStartDay(Date.valueOf(now));
    studentCourse.setEndDay(Date.valueOf(now.plusMonths(3)));
  }

  /**
   * 受講生詳細の更新を行う。
   *
   * @param studentDetail
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) throws TestException {
    List<Student> students = repository.search();
    if (students.stream().noneMatch(n -> n.getId().equals(studentDetail.getStudent().getId()))) {
      throw new TestException("受講生情報が存在しません。");
    }
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseStatusList()
        .forEach(studentCourseStatus -> {repository.updateCourse(studentCourseStatus.getStudentCourse());
        repository.updateStatus(studentCourseStatus.getCourseStatus());
        });
  }
}
