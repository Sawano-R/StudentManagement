package raisetech.StudentManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;
  @Autowired
  private Student2Repository repository2;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }


  @GetMapping("/student")
  public List<Student> getStudentList() {
    return repository.searchName();
  }

  @GetMapping("/studentcourses")
  public List<StudentCourses> getStudentCoursesList() {
    return repository.searchCourses();

  }

  @GetMapping("/student2")
  public String getStudent2(@RequestParam String name) {
    Student2 student2 = repository2.searchByName(name);
    return student2.getName() + " " + student2.getAge() + "歳" + student2.getHowLong() + "年所属";
  }

  @GetMapping("/student2All")
  public String select() {
    List<Student2> student2All = repository2.select();
    StringBuilder studentNames = new StringBuilder();
    for (Student2 s : student2All) {
      studentNames.append(String.format("%s %d歳 %d年所属\n",
          s.getName(), s.getAge(), s.getHowLong()));
    }
    return studentNames.toString();
  }

  @PostMapping("/student2")
  public void registerStudent2(String name, int age, int howLong) {
    repository2.registerStudent(name, age, howLong);
  }

  @PatchMapping("/student2age")
  public void updateStudent2Age(String name, int age) {
    repository2.updateStudentAge(name, age);
  }

  @PatchMapping("/student2howlong")
  public void updateStudent2HowLong(String name, int howLong) {
    repository2.updateStudentHowLong(name, howLong);
  }

  @DeleteMapping("/student2")
  public void deleteStudent2(String name) {
    repository2.deleteStudent(name);
  }

  @PatchMapping("/student2nextyear")
  public void passOneYaer() {
    repository2.passYear();
  }
}

