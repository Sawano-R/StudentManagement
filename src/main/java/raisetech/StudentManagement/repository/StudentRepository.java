package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students ")
  List<Student> search();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student searchStudentID(Integer id);

  @Select("SELECT * FROM students WHERE name = #{name}")
  Student searchStudentName(String name);

  @Select("SELECT * FROM students_courses")
  List<StudentCourses> searchCoursesList();

  @Select("SELECT * FROM students_courses WHERE id_students = #{idStudents}")
  List<StudentCourses> searchCourses(Integer idStudents);

  @Insert("INSERT students (name, name_kana, nickname, mail, resion, age, gender, remark) values(#{name}, #{nameKana}, #{nickname}, #{mail}, #{resion}, #{age}, #{gender}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT students_courses(id_students,course,start_day,end_day) values(#{idStudents}, #{course}, #{startDay}, #{endDay})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerCourse(StudentCourses inputCourse);

  @Update("UPDATE students SET name=#{name}, name_kana=#{nameKana}, nickname=#{nickname}, mail=#{mail}, resion=#{resion}, age=#{age}, gender=#{gender}, remark=#{remark}, deleted=#{deleted} WHERE id=#{id}")
  void updateStudent(Student student);

  @Insert("UPDATE students_courses SET course = #{course}, deleted=#{deleted} WHERE id = #{id}")
  void updateCourse(StudentCourses updateCourse);
}
