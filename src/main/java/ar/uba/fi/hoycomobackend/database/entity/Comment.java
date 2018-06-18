package ar.uba.fi.hoycomobackend.database.entity;

import javax.persistence.*;
import java.sql.Date;

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
    private Date userCommentDate;
    private Long pedidoId;
    private Long comercioId;
    @Column(columnDefinition = "varchar(300)")
    private String commerceReply;
    private Date commerceReplyDate;

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

    public Date getUserCommentDate() {
        return userCommentDate;
    }

    public void setUserCommentDate(Date userCommentDate) {
        this.userCommentDate = userCommentDate;
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

    public Date getCommerceReplyDate() {
        return commerceReplyDate;
    }

    public void setCommerceReplyDate(Date commerceReplyDate) {
        this.commerceReplyDate = commerceReplyDate;
    }

    public Long getMobileUserFacebookId() {
        return mobileUserFacebookId;
    }

    public void setMobileUserFacebookId(Long mobileUserFacebookId) {
        this.mobileUserFacebookId = mobileUserFacebookId;
    }
}
