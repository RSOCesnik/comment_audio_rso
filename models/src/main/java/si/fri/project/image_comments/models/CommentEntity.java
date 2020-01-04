package si.fri.project.image_comments.models;

import javax.persistence.*;
import java.util.List;

@Entity(name = "comments")
@NamedQueries(value =
        {
                @NamedQuery(name = "CommentEntity.getAll", query = "SELECT p FROM comments p")
        }
)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer comment_id;

    @Column(name = "image_id")
    private String image_id;

    @Column(name = "comment_data")
    private Integer comment_data;


    public Integer getId() {
        return comment_id;
    }

    public void setId(Integer id) {
        this.comment_id = id;
    }

    public String getImageId() {
        return image_id;
    }

    public void setImageId(String image_id) {
        this.image_id = image_id;
    }

    public Integer getCommentData() {
        return comment_data;
    }

    public void setCommentData(Integer comment_data) {
        this.comment_data = comment_data;
    }


}
