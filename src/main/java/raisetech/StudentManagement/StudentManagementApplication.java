package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;

  private String name = "Enami Kouji";
  private String age = "37";
  private Map<String, String> studentMap = new HashMap<>();


  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }


  @GetMapping("/info")
  public String getinfo() {
    return name + " " + age + "歳";
  }


  @PostMapping("/student")
  public void registerStudent(String name, int age) {
    repository.registerStudent(name, age);
  }

  @PostMapping("/updateName")
  public void updateSetName(String name) {
    this.name = name;
  }

  @GetMapping("/map")
  public Map<String, String> getStudentMap() {
    return studentMap;
  }

  @PostMapping("/map")
  public void setStudentMap(String name, String age) {
    studentMap.put(name, age);
  }

  @GetMapping("/studentInfo")
  public String getStudentInfo() {
    Student student = repository.searchByName("SawanoRyoichi");
    return student.getName() + " " + student.getAge() + "歳";
  }
}
