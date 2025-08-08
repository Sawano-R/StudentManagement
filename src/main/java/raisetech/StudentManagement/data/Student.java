package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
  
  private Integer id;

  @NotBlank
  private String name;

  @NotBlank
  private String nameKana;

  @NotBlank
  private String nickname;

  @NotBlank
  @Email
  private String mail;

  @NotBlank
  private String resion;

  private int age;

  private String gender;

  private String remark;


  private boolean deleted;
}
