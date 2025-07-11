package raisetech.StudentManagement.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public void registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);
  }

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

}
