package si.fri.project.image_comments.api.v1.resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import si.fri.project.image_comments.services.CommentBean;
import si.fri.project.image_comments.models.CommentEntity;

@ApplicationScoped
@Path("/comments")
public class ImageCommentsResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private CommentBean commentBean;

    @GET
    public Response getComments() {
        List<CommentEntity> comments = commentBean.getComments(uriInfo);

        return Response.ok(comments).build();
    }
}
