package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;

@Schema(description = "受講生コース状態")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseStatus {

  @Valid
  private StudentCourse studentCourse;

  @Valid
  private CourseStatus courseStatus;
}
