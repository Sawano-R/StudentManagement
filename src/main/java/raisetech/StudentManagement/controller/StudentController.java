package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.groups.Default;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.StudentValidationGroup.UpdateGroup;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }


  @Operation(summary = "一覧検索", description = "受講生詳細を一覧表示します")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentDetailList();
  }

  /**
   * 受講生詳細登録です。 StudentDetailのうち、StudentのID、Deleted以外の項目及びStudentCoursesのcourseを入力します。
   *
   * @param studentDetail
   * @return　登録した受講生詳細情報(ID付き)
   */
  @Operation(summary = "受講生登録", description = "受講生詳細を登録します",
      responses = {@ApiResponse(responseCode = "200", description = "受講生登録完了"),
          @ApiResponse(responseCode = "400", description = "バリデーションエラー発生", content = @Content())})
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Validated(Default.class) StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @Operation(summary = "受講生検索", description = "{id}に一致する受講生詳細を表示します", parameters = @Parameter(name = "id", description = "受講生ID", required = true),
      responses = {@ApiResponse(responseCode = "200"),
          @ApiResponse(responseCode = "400", description = "バリデーションエラー", content = @Content())})
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Max(99) Integer id) {
    return service.matchID(id);
  }


  @Operation(summary = "受講生条件検索", description = "コース名、受講状態、出身地で受講生一覧をソートします。入力するstudentDetailには上記3つのうち、1つ以上の情報を入れてください。")
  @PostMapping("retrievalStudent")
  public ResponseEntity<List<StudentDetail>> retrievalStudent(
      @RequestBody StudentDetail studentDetail) {
    List<StudentDetail> responseStudentDetailList = service.retrievalStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetailList);
  }

  @Operation(summary = "受講生更新", description = "受講生詳細を更新します")
  @PutMapping("/updateResult")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Validated({Default.class, UpdateGroup.class}) StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました");
  }

}
