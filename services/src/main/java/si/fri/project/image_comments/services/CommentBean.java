package si.fri.project.image_comments.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.fri.project.image_comments.models.CommentEntity;
import si.fri.project.image_comments.models.ImageDto;
import si.fri.project.image_comments.models.ImagePlusComment;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@ApplicationScoped
public class CommentBean {

    @Inject
    private EntityManager em;

    private Client httpClient;
    private Logger log = Logger.getLogger(CommentBean.class.getName());
    @Inject
    @DiscoverService("image-catalog-service")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<CommentEntity> getComments(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery())
                .defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CommentEntity.class, queryParameters);

    }
    public CommentEntity getComment(Integer commentId) {
        CommentEntity photo = em.find(CommentEntity.class, commentId);

        if(photo == null) throw new NotFoundException();
        return photo;
    }

    public List<CommentEntity> getImageComment(Integer imageId) {
        Query query = em.createNamedQuery("CommentEntity.findByImage");
        query.setParameter("imgId", imageId);
        List<CommentEntity> results = query.getResultList();

        if(results == null) throw new NotFoundException();
        return results;
    }



    public ImagePlusComment getImageAndComments(Integer photoId) {
        ImagePlusComment ipc = new ImagePlusComment();

        ImageDto photo = getImage(photoId);
        if(photo == null) throw new NotFoundException();

        ipc.setImage(photo);

//        if(appProperties.isCommentsServicesEnabled()) {
        List<CommentEntity> allComments = getImageComment(photoId);
        ipc.setComments(allComments);
//        }
        return ipc;
    }


    @Timed
    @CircuitBreaker(requestVolumeThreshold = 1)
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getImageFallback")
    public ImageDto getImage(Integer imageId) {

        if (baseUrl.isPresent()) {

            log.info("Calling Images service: get image.");

            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/catalog/"+imageId)
                        .request().get(new GenericType<ImageDto>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    public ImageDto getImageFallback(Integer userId) {
        return null;
    }


    public CommentEntity makeComment(CommentEntity comment) {
        try {
            beginTx();
            em.persist(comment);
            commitTx();
            log.info("Successfully saved new comment with data " + comment.getCommentData());
        } catch (Exception e) {
            log.warning("There was a problem with saving new comment with data " + comment.getCommentData());
            log.warning(e.getMessage());
            rollbackTx();
        }
        return comment;
    }

    public boolean deleteComment(Integer commentId) {
        CommentEntity photo = em.find(CommentEntity.class, commentId);

        if(photo != null) {
            try {
                beginTx();
                em.remove(photo);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }
        return true;
    }
    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}