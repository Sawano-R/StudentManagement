package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return　受講生(全件)
   */
  List<Student> search();

  /**
   * 受講生検索を行います。
   *
   * @param id 　受講生ID
   * @return　受講生(ID合致)
   */
  Student searchStudentID(Integer id);

  /**
   * 受講生検索を行います。
   *
   * @param name 　受講生の名前
   * @return　受講生(名前合致)
   */
  @Select("SELECT * FROM students WHERE name = #{name}")
  Student searchStudentName(String name);

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchCourseList();

  @Select("SELECT * FROM students_courses WHERE id_students = #{idStudents}")
  List<StudentCourse> searchCourseID(Integer idStudents);

  @Insert("INSERT students (name, name_kana, nickname, mail, resion, age, gender, remark, deleted) values(#{name}, #{nameKana}, #{nickname}, #{mail}, #{resion}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT students_courses(id_students,course,start_day,end_day) values(#{idStudents}, #{course}, #{startDay}, #{endDay})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerCourse(StudentCourse inputCourse);

  @Update("UPDATE students SET name=#{name}, name_kana=#{nameKana}, nickname=#{nickname}, mail=#{mail}, resion=#{resion}, age=#{age}, gender=#{gender}, remark=#{remark}, deleted=#{deleted} WHERE id=#{id}")
  void updateStudent(Student student);

  @Insert("UPDATE students_courses SET course = #{course}, deleted=#{deleted} WHERE id = #{id}")
  void updateCourse(StudentCourse updateCourse);
}
