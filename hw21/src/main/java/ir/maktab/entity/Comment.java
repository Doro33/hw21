package ir.maktab.entity;

import ir.maktab.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Comment extends BaseEntity<Integer> {
    @ManyToOne
    @JoinColumn(name = "student_course_rating_id")
    private StudentCourseRating studentCourseRating;

    private Timestamp timestamp;
    private String commentText;
}
