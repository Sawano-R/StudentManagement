package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private StudentService service;

  @Captor
  ArgumentCaptor<StudentDetail> captorStudentDetail;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索を実行でき空のリストがかえってくること() throws Exception {
    mockMVC.perform(get("/studentList"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).searchStudentDetailList();
  }

  @Test
  void 受講生詳細の登録が正しい形式で入力すると成功すること() throws Exception {
    Student student = new Student();
    student.setName("テストN");
    student.setNameKana("テストNK");
    student.setNickname("テストNN");
    student.setMail("test@example.com");
    student.setResion("テストR");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テストC");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMVC.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentDetailJson))
        .andExpect(status().isOk());

    verify(service).registerStudent(captorStudentDetail.capture());
    StudentDetail actual = captorStudentDetail.getValue();

    assertThat(actual.getStudent().getName()).isEqualTo("テストN");
    assertThat(actual.getStudentCourseList().getFirst().getCourse()).isEqualTo("テストC");
  }

  @Test
  void 受講生詳細の登録が必要事項に空欄があるとエラーとなること() throws Exception {
    Student student = new Student();
    student.setNameKana("テストNK");
    student.setNickname("テストNN");
    student.setMail("test@example.com");
    student.setResion("テストR");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テストC");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMVC.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentDetailJson))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).registerStudent(captorStudentDetail.capture());
  }

  @Test
  void 受講生検索が実行できること() throws Exception {
    Student student = new Student();
    student.setName("名前");
    student.setNameKana("なまえ");
    student.setNickname("名");
    student.setMail("test@example.com");
    student.setResion("県");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テスト");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    when(service.matchID(1)).thenReturn(studentDetail);

    MvcResult result = mockMVC.perform(get("/student/{id}", 1))
        .andExpect(status().isOk()).andReturn();

    String jsonResponse = result.getResponse().getContentAsString();
    StudentDetail actual = objectMapper.readValue(jsonResponse, StudentDetail.class);

    assertThat(actual.getStudent().getName()).isEqualTo("名前");
    assertThat(actual.getStudentCourseList().getFirst().getCourse()).isEqualTo("テスト");
    verify(service).matchID(1);
  }

  @Test
  void 受講生検索で100以上のidを入力したときにエラーとなること() throws Exception {
    mockMVC.perform(get("/student/{id}", 100))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).matchID(any());
  }

  @Test
  void 受講生検索で数字以外のidを入力したときにエラーとなること() throws Exception {
    mockMVC.perform(get("/student/{id}", "あ"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).matchID(any());
  }

  @Test
  void 受講生更新が実行できること() throws Exception {
    Student student = new Student();
    student.setId(1);
    student.setName("テストN");
    student.setNameKana("テストNK");
    student.setNickname("テストNN");
    student.setMail("test@example.com");
    student.setResion("テストR");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テストC");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMVC.perform(put("/updateResult").contentType(MediaType.APPLICATION_JSON)
        .content(studentDetailJson)).andExpect(status().isOk());

    verify(service).updateStudent(captorStudentDetail.capture());
  }

  @Test
  void 受講生更新でidを入力していないときにバリデーションエラーが出ること() throws Exception {
    Student student = new Student();
    student.setName("テストN");
    student.setNameKana("テストNK");
    student.setNickname("テストNN");
    student.setMail("test@example.com");
    student.setResion("テストR");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テストC");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMVC.perform(put("/updateResult").contentType(MediaType.APPLICATION_JSON)
        .content(studentDetailJson)).andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudent(captorStudentDetail.capture());
  }

  @Test
  void 受講生情報で形式通りに入力したときに問題が発生しないこと() {
    Student student = new Student();
    student.setName("テスト");
    student.setNameKana("テスト");
    student.setNickname("テスト");
    student.setMail("test@example.com");
    student.setResion("テスト");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生情報で必須項目が空欄の時入力チェックにかかること() {
    Student student = new Student();

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(5);
  }

  @Test
  void 受講生情報でメールにアドレスの形式以外を用いたときに入力チャックにかかること() {
    Student student = new Student();
    student.setMail("テストです。");
    student.setName("テスト");
    student.setNameKana("テスト");
    student.setNickname("テスト");
    student.setResion("テスト");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("電子メールアドレスとして正しい形式にしてください");
  }

  @Test
  void 受講生コース情報で形式通りに入力して問題が発生しないこと() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse("テスト");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 受講生コース情報でテストが空欄の時入力チェックにかかること() {
    StudentCourse studentCourse = new StudentCourse();

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    assertThat(violations.size()).isEqualTo(1);
  }
}