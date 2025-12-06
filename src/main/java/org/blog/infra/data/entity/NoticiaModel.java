package org.blog.infra.data.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "noticia_model")
public class NoticiaModel extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String titulo;
    private String conteudo;
    private LocalDateTime dataPublicacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY) 
    private UsuarioModel autor; 

    @OneToMany(mappedBy = "noticia", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ComentarioModel> comentarios;
}