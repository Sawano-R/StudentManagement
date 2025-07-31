package raisetech.StudentManagement.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;
import raisetech.StudentManagement.domain.StudentDetail;
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
    List<StudentCourses> studentJavaCoursesList = repository.searchCoursesList();
    return converter.convertStudentDetails(studentList, studentJavaCoursesList);
  }

  /**
   * 受講生検索です。入力した名前と合致する受講生情報を検索します。
   *
   * @param studentDetail 　受講生の名前
   * @return　受講生(名前一致)
   */
  public StudentDetail matchName(StudentDetail studentDetail) {
    Student student = repository.searchStudentName(studentDetail.getStudent().getName());
    List<StudentCourses> studentCourses = repository.searchCourses(student.getId());
    return new StudentDetail(student, studentCourses);
  }

  /**
   * 受講生検索です。入力したIDと合致する受講生情報を検索します。
   *
   * @param id 　受講生のID
   * @return　受講生(ID一致)
   */
  public StudentDetail matchID(Integer id) {
    Student student = repository.searchStudentID(id);
    List<StudentCourses> studentCourses = repository.searchCourses(id);
    return new StudentDetail(student, studentCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setIdStudents(studentDetail.getStudent().getId());
      LocalDate localdate = LocalDate.now();
      studentCourses.setStartDay(Date.valueOf(localdate));
      studentCourses.setEndDay(Date.valueOf(localdate.plusMonths(3)));
      repository.registerCourse(studentCourses);
    }
    return studentDetail;
  }

  /**
   * studentCourseだけをstudentの名前と一致するものに対して登録する。 今は使わない。
   *
   * @Transactional public void registerCourse(StudentDetail studentDetail) { List<Student> students
   * = repository.search(); for (Student student : students) { if
   * (student.getName().equals(studentDetail.getStudent().getName())) { StudentCourses inputCourse =
   * new StudentCourses(); inputCourse.setIdStudents(student.getId());
   * inputCourse.setCourse(studentDetail.getStudentCourses().getFirst().getCourse()); LocalDate
   * localdate = LocalDate.now(); inputCourse.setStartDay(Date.valueOf(localdate));
   * inputCourse.setEndDay(Date.valueOf(localdate.plusMonths(3)));
   * repository.registerCourse(inputCourse); break; } } }
   */

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourses studentCourse : studentDetail.getStudentCourses()) {
      repository.updateCourse(studentCourse);
    }
  }
}
