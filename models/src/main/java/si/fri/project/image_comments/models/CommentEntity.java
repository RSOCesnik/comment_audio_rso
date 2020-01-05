package si.fri.project.image_comments.models;

import javax.persistence.*;
import java.util.List;

@Entity(name = "comments")
@NamedQueries(value =
        {
                @NamedQuery(name = "CommentEntity.getAll", query = "SELECT p FROM comments p"),
                @NamedQuery(name="CommentEntity.findByImage",
                        query="SELECT c FROM comments c WHERE c.imageId = :imgId"),
        }
)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "comment_data")
    private String commentData;


    public Integer getId() {
        return commentId;
    }

    public void setId(Integer id) {
        this.commentId = id;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer image_id) {
        this.imageId = image_id;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String comment_data) {
        this.commentData = comment_data;
    }


}
