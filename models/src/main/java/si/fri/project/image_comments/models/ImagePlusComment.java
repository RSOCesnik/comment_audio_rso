package si.fri.project.image_comments.models;

import java.util.List;

public class ImagePlusComment {

    private List<CommentEntity> comments;
    private ImageDto image;

    public List<CommentEntity>  getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }
}
