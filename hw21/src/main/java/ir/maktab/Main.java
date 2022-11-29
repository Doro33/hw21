package ir.maktab;

import ir.maktab.entity.Comment;
import ir.maktab.entity.Course;
import ir.maktab.entity.Student;
import ir.maktab.entity.StudentCourseRating;
import ir.maktab.utils.HibernateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;


public class Main {
    private static final EntityManager EM = HibernateUtils.getENTITY_MANAGER_FACTORY().createEntityManager();

    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("comment.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while (bufferedReader.ready()) {
            String[] readLine = bufferedReader.readLine().split(",");
            Course course = saveCourse(readLine[0]);
            Student student = saveStudent(readLine[1]);
            StudentCourseRating scr = saveSCR(student,course,readLine[3]);
            saveComment(scr,readLine[2],readLine[4]);
        }
        bufferedReader.close();
    }

    private static Course findCourseByName(String courseName) {
        TypedQuery<Course> query = EM
                .createQuery("select c from Course c " +
                                "where c.name = :courseName "
                        , Course.class
                );
        query.setParameter("courseName", courseName);
        try {
            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    private static Student findStudentByName(String studentName) {
        TypedQuery<Student> query = EM
                .createQuery("select s from Student s " +
                                "where s.name = :studentName "
                        , Student.class
                );
        query.setParameter("studentName", studentName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private static Course saveCourse(String courseName) {
        Course course = findCourseByName(courseName);
        if (course == null) {
            course = new Course(courseName);
            EntityTransaction transaction = EM.getTransaction();
            transaction.begin();
            EM.persist(course);
            transaction.commit();
        }
        return course;
    }

    private static Student saveStudent(String studentName) {
        Student student = findStudentByName(studentName);
        if (student == null) {
            student = new Student(studentName);
            EntityTransaction transaction = EM.getTransaction();
            transaction.begin();
            EM.persist(student);
            transaction.commit();
        }
        return student;
    }
    private static StudentCourseRating saveSCR(Student student,Course course,String rating){
        StudentCourseRating scr= new StudentCourseRating(student,course,Float.parseFloat(rating));
        EntityTransaction transaction = EM.getTransaction();
        transaction.begin();
        EM.persist(scr);
        transaction.commit();
        return scr;
    }
    private static void saveComment(StudentCourseRating scr,String timeStamp, String commentText){
        Comment comment = new Comment(scr, Timestamp.valueOf(timeStamp),commentText);
        EntityTransaction transaction = EM.getTransaction();
        transaction.begin();
        EM.persist(comment);
        transaction.commit();
    }
}