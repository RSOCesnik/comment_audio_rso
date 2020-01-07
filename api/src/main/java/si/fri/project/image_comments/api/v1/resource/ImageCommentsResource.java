package si.fri.project.image_comments.api.v1.resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.project.image_comments.models.ImageDto;
import si.fri.project.image_comments.models.ImagePlusComment;
import si.fri.project.image_comments.services.CommentBean;
import si.fri.project.image_comments.models.CommentEntity;

@Log
@ApplicationScoped
@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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

    @GET
    @Path("/image/{imageId}")
    public Response getImages(@PathParam("imageId") Integer imageId) {
        ImagePlusComment comments = commentBean.getImageAndComments(imageId);

        if(comments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(comments).build();
    }

    @POST
    @Path("/add/{imageId}")
    public Response addComment(@PathParam("imageId") Integer imageId,CommentEntity comment) {

        if (comment.getCommentData() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            comment.setImageId(imageId);
            comment = commentBean.makeComment(comment);
        }
        if (comment.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(comment).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(comment).build();
        }
    }

    @GET
    @Path("/{imageId}")
    public Response getImageComments(@PathParam("imageId") Integer imageId) {
        List<CommentEntity> comments = commentBean.getImageComment(imageId);

        if(comments == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comments).build();
    }
}
