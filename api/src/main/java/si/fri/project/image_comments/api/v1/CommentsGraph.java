package si.fri.project.image_comments.api.v1;

        import com.kumuluz.ee.graphql.annotations.GraphQLClass;
        import io.leangen.graphql.annotations.GraphQLQuery;
        import si.fri.project.image_comments.models.CommentEntity;
        import si.fri.project.image_comments.services.CommentBean;

        import javax.enterprise.context.RequestScoped;
        import javax.inject.Inject;
        import java.util.List;

@RequestScoped
@GraphQLClass
public class CommentsGraph {

    @Inject
    private CommentBean commentBean;

    @GraphQLQuery
    public List<CommentEntity> getAllComments() {
        return commentBean.getComments();
    }

    @GraphQLQuery
    public String hello() {
        return "world";
    }
}