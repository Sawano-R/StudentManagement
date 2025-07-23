package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentJavaCoursesList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentListAll")
  public String getStudentListAll(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentJavaCoursesList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentListAll";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(new Student());
    studentDetail.setStudentCourses(Arrays.asList(new StudentCourses()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    System.out.println(
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    return "redirect:/studentList";
  }

  @GetMapping("/newCourse")
  public String newCourse(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerCourse";
  }

  @PostMapping("/registerCourse")
  public String registerCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerCourse";
    }
    service.registerCourse(studentDetail);
    return "redirect:/studentList";
  }

  @GetMapping("/updateStudent")
  public String updateStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "preUpdateStudent";
  }

  @PostMapping("/updateStudentHere")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      return "preupdateStudent";
    }
    StudentDetail studentMatchName = service.matchName(studentDetail);
    model.addAttribute("studentDetail", studentMatchName);
    return "updateStudent";
  }

  @GetMapping("/student/{id}")
  public String updateStudentHyper(@PathVariable Integer id, Model model) {
    StudentDetail studentMatchID = service.matchID(id);
    model.addAttribute("studentDetail", studentMatchID);
    return "updateStudent";
  }

  @PostMapping("/updateResult")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "preupdateStudent";
    }
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }
}
