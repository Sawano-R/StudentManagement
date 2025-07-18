package raisetech.StudentManagement.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchName();
  }

  public List<StudentCourses> searchStudentJavaCoursesList() {
    return repository.searchCourses();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourses studentCourses : studentDetail.getStudentCourses()) {
      studentCourses.setIdStudents(studentDetail.getStudent().getId());
      LocalDate localdate = LocalDate.now();
      studentCourses.setStartDay(Date.valueOf(localdate));
      studentCourses.setEndDay(Date.valueOf(localdate.plusMonths(3)));
      repository.registerCourse(studentCourses);
    }
  }

  @Transactional
  public void registerCourse(StudentDetail studentDetail) {
    List<Student> students = repository.searchName();
    for (Student student : students) {
      if (student.getName().equals(studentDetail.getStudent().getName())) {
        StudentCourses inputCourse = new StudentCourses();
        inputCourse.setIdStudents(student.getId());
        inputCourse.setCourse(studentDetail.getStudentCourses().getFirst().getCourse());
        LocalDate localdate = LocalDate.now();
        inputCourse.setStartDay(Date.valueOf(localdate));
        inputCourse.setEndDay(Date.valueOf(localdate.plusMonths(3)));
        repository.registerCourse(inputCourse);
        break;
      }
    }
  }

  public StudentDetail matchName(StudentDetail studentDetail) {
    List<Student> students = repository.searchName();
    StudentDetail studentDetail1MatchName = new StudentDetail();
    for (Student student : students) {
      if (student.getName().equals(studentDetail.getStudent().getName())) {
        studentDetail1MatchName.setStudent(student);
        break;
      }
    }
    return studentDetail1MatchName;
  }

  public StudentDetail matchID(Integer id) {
    List<Student> students = repository.searchName();
    StudentDetail studentDetail1MatchID = new StudentDetail();
    for (Student student : students) {
      if (id.equals(student.getId())) {
        studentDetail1MatchID.setStudent(student);
        break;
      }
    }
    return studentDetail1MatchID;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }
}
