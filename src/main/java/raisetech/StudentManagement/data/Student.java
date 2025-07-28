package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer id;
  private String name;
  private String nameKana;
  private String nickname;
  private String mail;
  private String resion;
  private int age;
  private String gender;
  private String remark;
  private boolean deleted;
}
