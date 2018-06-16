package ar.uba.fi.hoycomobackend.api.service;

import ar.uba.fi.hoycomobackend.api.dto.ErrorMessage;
import ar.uba.fi.hoycomobackend.api.dto.ReplicaDto;
import ar.uba.fi.hoycomobackend.api.service.pushnotification.FirebaseApplication;
import ar.uba.fi.hoycomobackend.api.service.pushnotification.PushNotificationMessage;
import ar.uba.fi.hoycomobackend.database.entity.Comercio;
import ar.uba.fi.hoycomobackend.database.entity.Comment;
import ar.uba.fi.hoycomobackend.database.entity.MobileUser;
import ar.uba.fi.hoycomobackend.database.queries.ComercioQuery;
import ar.uba.fi.hoycomobackend.database.repository.CommentRepository;
import ar.uba.fi.hoycomobackend.database.repository.MobileUserRepository;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private MobileUserRepository mobileUserRepository;
    private PushNotificationMessage pushNotificationMessage;
    private ComercioQuery comercioQuery;

    @Autowired
    public CommentService(CommentRepository commentRepository, MobileUserRepository mobileUserRepository, PushNotificationMessage pushNotificationMessage, ComercioQuery comercioQuery) {
        this.commentRepository = commentRepository;
        this.mobileUserRepository = mobileUserRepository;
        this.pushNotificationMessage = pushNotificationMessage;
        this.comercioQuery = comercioQuery;
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

    public ResponseEntity replyToComment(Long comercioId, Long comentarioId, ReplicaDto replicaDto) {
        Optional<Comment> commentOptional = commentRepository.findCommentById(comentarioId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setCommerceReply(replicaDto.getReplica());
            java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
            comment.setCommerceReplyDate(sqlDate);

            commentRepository.saveAndFlush(comment);
            try{
                Comercio comercio = comercioQuery.getComercioById(comercioId).get();
                sendMessageToAndroidDevice(comercio.getNombre());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("No se ha encontrado comercio bajo ese id"));
            }

            return ResponseEntity.ok("Comentario añadido con éxito");

        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No se ha encontrado comentario bajo ese id"));
    }

    private void sendMessageToAndroidDevice(String storeName) {
        Message message = Message.builder()
                .putData("title", "Hoy Como")
                .putData("origen", "replica")
                .putData("detail", storeName + ": ha respondido tu comentario")
                .setTopic("/topics/allDevices")
                .build();

        pushNotificationMessage.sendMessage(message);
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
