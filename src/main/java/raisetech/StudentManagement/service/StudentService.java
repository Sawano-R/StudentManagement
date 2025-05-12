package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> studentList = repository.searchName();
    studentList.removeIf(n -> n.getAge() >= 40);
    studentList.removeIf(n -> n.getAge() < 30);
    return studentList;
  }

  public List<StudentCourses> searchStudentJavacoursesList() {
    List<StudentCourses> javaList = repository.searchCourses();
    return javaList.stream().filter(n -> n.getCourse().equals("java")).toList();
  }
}
