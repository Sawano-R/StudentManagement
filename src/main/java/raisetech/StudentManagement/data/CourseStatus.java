package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import raisetech.StudentManagement.data.StudentValidationGroup.UpdateGroup;

@Data
@Schema(description = "コース申し込み状況")
public class CourseStatus {

  @NotNull(groups = UpdateGroup.class, message = "更新時はidが必須です。")
  private Integer id;

  private int idCourses;

  private int idStudents;

  @NotBlank
  private String status;
}
