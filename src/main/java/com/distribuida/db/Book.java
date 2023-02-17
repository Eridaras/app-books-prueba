package com.distribuida.db;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

@Data
@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@Access(AccessType.FIELD)
@NamedQueries(value = {
        @NamedQuery(name = "Book.findAll",
                query = "SELECT b FROM Book b"),
        @NamedQuery(name = "Book.findById",
                query = "SELECT b FROM Book b WHERE b.id = :id")
})
@Schema(name = "Books", description = "Esquema de Books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "isbn")
    @Schema(required = true)
    private String isbn;
    @Column(name = "title")
    @Schema(required = true)
    private String title;
    @Column(name = "author")
    @Schema(required = true)
    private String author;
    @Column(name = "price")
    @Schema(required = true)
    private Double price;

}
