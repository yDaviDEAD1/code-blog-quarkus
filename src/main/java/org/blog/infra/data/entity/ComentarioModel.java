package org.blog.infra.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comentario_model")
public class ComentarioModel extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String texto;
    private LocalDateTime dataComentario = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "noticia_id")
    private NoticiaModel noticia; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private UsuarioModel autor;
}