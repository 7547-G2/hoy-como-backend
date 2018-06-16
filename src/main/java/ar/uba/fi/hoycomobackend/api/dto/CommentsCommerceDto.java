package ar.uba.fi.hoycomobackend.api.dto;

public class CommentsCommerceDto {

    private String dateComment;
    private Integer rating;
    private String user;
    private String comment;
    private String dateReplica;
    private String replica;

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDateReplica() {
        return dateReplica;
    }

    public void setDateReplica(String dateReplica) {
        this.dateReplica = dateReplica;
    }

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }
}
