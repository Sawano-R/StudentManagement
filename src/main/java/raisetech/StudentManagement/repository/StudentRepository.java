package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   */
  List<Student> search();

  /**
   * 受講生検索を行います。
   */
  Student searchStudentID(Integer id);

  /**
   * 受講生検索を行います。
   */
  Student searchStudentName(String name);

  /**
   * 受講生コース情報の全件検索です。
   */
  List<StudentCourse> searchCourseList();

  /**
   * 受講生コース情報を受講生IDと合致するものを検索します。
   */
  List<StudentCourse> searchCourseID(Integer idStudents);

  /**
   * コース状態情報の全件検索です。
   */
  List<CourseStatus> searchStatusList();

  List<CourseStatus> searchStatusID(Integer idStudents);

  /**
   * 受講生情報を登録します。deletedは常にfalseです。
   */
  void registerStudent(Student student);

  /**
   * 受講生コースを登録します。
   */
  void registerCourse(StudentCourse inputCourse);


  void registerStatus(CourseStatus courseStatus);

  /**
   * 受講生情報の更新をします。更新するのはID以外です。
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報の更新をします。更新するのはコース名と削除フラグです。
   */
  void updateCourse(StudentCourse updateCourse);

  void updateStatus(CourseStatus courseStatus);
}
