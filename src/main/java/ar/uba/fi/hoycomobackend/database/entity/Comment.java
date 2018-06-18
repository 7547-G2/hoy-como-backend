package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comentario")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long mobileUserFacebookId;
    private Integer stars;
    @Column(columnDefinition = "varchar(300)")
    private String userComment;
    private Timestamp userCommentTimestamp;
    private Long pedidoId;
    private Long comercioId;
    @Column(columnDefinition = "varchar(300)")
    private String commerceReply;
    private Timestamp commerceReplyTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public Timestamp getUserCommentTimestamp() {
        return userCommentTimestamp;
    }

    public void setUserCommentTimestamp(Timestamp userCommentTimestamp) {
        this.userCommentTimestamp = userCommentTimestamp;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getComercioId() {
        return comercioId;
    }

    public void setComercioId(Long comercioId) {
        this.comercioId = comercioId;
    }

    public String getCommerceReply() {
        return commerceReply;
    }

    public void setCommerceReply(String commerceReply) {
        this.commerceReply = commerceReply;
    }

    public Timestamp getCommerceReplyTimestamp() {
        return commerceReplyTimestamp;
    }

    public void setCommerceReplyTimestamp(Timestamp commerceReplyTimestamp) {
        this.commerceReplyTimestamp = commerceReplyTimestamp;
    }

    public Long getMobileUserFacebookId() {
        return mobileUserFacebookId;
    }

    public void setMobileUserFacebookId(Long mobileUserFacebookId) {
        this.mobileUserFacebookId = mobileUserFacebookId;
    }
}
