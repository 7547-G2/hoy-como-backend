package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.database.entity.Comment;
import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.repository.CommentRepository;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private MobileUserRepository mobileUserRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, MobileUserRepository mobileUserRepository) {
        this.commentRepository = commentRepository;
        this.mobileUserRepository = mobileUserRepository;
    }

    public ResponseEntity getComentariosOfComercio(Long comercioId) {
        List<Comment> commentList = commentRepository.findCommentsByComercioId(comercioId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        commentList.forEach(comment -> {
            CommentDto commentDto = new CommentDto();
            commentDto.id = comment.getId();
            MobileUser mobileUser = mobileUserRepository.getMobileUserByFacebookId(comment.getMobileUserFacebookId()).get();
            commentDto.usuario = mobileUser.getFirstName() + " " + mobileUser.getLastName();
            commentDto.puntaje = comment.getStars();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            commentDto.fecha = formatter.format(comment.getUserCommentDate());
            commentDto.comentario = comment.getUserComment();
            commentDto.replica = comment.getCommerceReply();

            commentDtoList.add(commentDto);
        });

        return ResponseEntity.ok(commentDtoList);
    }

    private class CommentDto {
        public Long id;
        public String usuario;
        public Integer puntaje;
        public String fecha;
        public String comentario;
        public String replica;
    }
}
