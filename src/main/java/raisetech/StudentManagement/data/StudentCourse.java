package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import lombok.Data;
import raisetech.StudentManagement.data.StudentValidationGroup.UpdateGroup;

@Schema(description = "受講生コース情報")
@Data
public class StudentCourse {

  @NotNull(groups = UpdateGroup.class, message = "更新時はidが必須です。")
  private Integer id;

  private Integer idStudents;

  @NotBlank
  private String course;

  private Date startDay;
  private Date endDay;
  private boolean deleted;

}
