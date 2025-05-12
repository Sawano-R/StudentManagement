package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface Student2Repository {

  @Select("SELECT * FROM student2 WHERE name = #{name}")
  Student2 searchByName(String name);

  @Select("SELECT * FROM student2")
  List<Student2> select();

  @Insert("INSERT INTO student2 (name, age, howLong) VALUES (#{name}, #{age}, #{howLong})")
  void registerStudent(String name, int age, int howLong);

  @Update(("UPDATE student2 SET age =#{age} WHERE name = #{name}"))
  void updateStudentAge(String name, int age);

  @Update(("UPDATE student2 SET howLong =#{howLong} WHERE name = #{name}"))
  void updateStudentHowLong(String name, int howLong);

  @Delete("DELETE FROM student2 WHERE name=#{name}")
  void deleteStudent(String name);

  @Update("UPDATE student2 SET howLong = howLong + 1")
  void passYear();

}
