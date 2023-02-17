package com.distribuida.rest;

import com.distribuida.clientes.authors.AuthorRestProxy;
import com.distribuida.clientes.authors.AuthorsCliente;
import com.distribuida.db.Book;

import com.distribuida.dtos.BookDto;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.rest.client.inject.RestClient;


import java.util.List;
import java.util.stream.Collectors;


@Path("/books")
@Consumes(MediaType.APPLICATION_JSON)

public class BookRest {

    @PersistenceContext(unitName = "BookUP")
    private EntityManager entityManager;

    @RestClient
    @Inject
    AuthorRestProxy proxyAuthor;
    @GET
    @Operation(
            operationId = "OBTENER TODOS",
            summary = "Lista de authors",
            description = "obtenemos todas los authors"

    )
    @APIResponse(
            responseCode = "200",
            description = "Obtención completada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Método no valido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> findAll() {
        return entityManager.createNamedQuery("Book.findAll", Book.class).getResultList();
    }

    @POST
    @Operation(
            operationId = "CREAR Book",
            summary = "Se crea un Book",
            description = "Ingreso de un nuevo author"

    )
    @APIResponse(
            responseCode = "204",
            description = "Book Creado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Metodo no valido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void create(
            @RequestBody(
                    description = "Books a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class))
            )
            Book book) {
        System.out.println("Entramos a Crear Book " + book.getAuthor());
        try {
            entityManager.persist(book);
        } catch (Exception e) {
            System.out.println(e);;
        }
    }
    @GET
    @Path("/{id}")
    @Operation(
            operationId = "OBTENER UN BOOK",
            summary = "Se obtiene un book",
            description = "Book"

    )
    @APIResponse(
            responseCode = "200",
            description = "Obtención completada",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Método no valido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Book find(@PathParam("id") Integer id){
        try {
            Book book = entityManager.find(Book.class, id);
            return book;
        }catch (Exception e){
            return null;
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(
            operationId = "BORRAR BOOKS",
            summary = "borrar un book",
            description = "Se a borrado un book existente"

    )
    @APIResponse(
            responseCode = "204",
            description = "Book Borrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Metodo no valido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Transactional
    public void delete(@PathParam("id") Integer id){
        System.out.println("Entramor a Borrar");
        try {
            Book book = entityManager.find(Book.class, id);
            entityManager.remove(book);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @PUT
    @Path("{id}")
    @Operation(
            operationId = "ACTUALIZAR BOOK",
            summary = "actualiza un Book",
            description = "Se actualiza un book existente"

    )
    @APIResponse(
            responseCode = "204",
            description = "Book Actualizado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Metodo no valido",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Book update(
            @RequestBody(
            description = "Books a crear",
            required = true,
            content = @Content(schema = @Schema(implementation = Book.class))
            ) Book book, @PathParam("id") Integer id ){
        try {
            Book bookActualizado = entityManager.find(Book.class,id);

            bookActualizado.setIsbn(book.getIsbn());
            bookActualizado.setAuthor(book.getAuthor());
            bookActualizado.setTitle(book.getTitle());
            bookActualizado.setPrice(book.getPrice());
            entityManager.persist(bookActualizado);
            return bookActualizado;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @GET
    @Path("/authors")
    public List<BookDto> getAll(){
        var books = entityManager.createNamedQuery("Book.findAll", Book.class).getResultList();

        List<BookDto> ret = books.stream()
                .map(s -> {
                    System.out.println("*********buscando " + s.getId() );

                    AuthorsCliente author = proxyAuthor.findById(s.getId().longValue());
                    return new BookDto(
                            s.getId(),
                            s.getIsbn(),
                            s.getTitle(),
                            s.getAuthor(),
                            s.getPrice(),
                            String.format("%s, %s", author.getLastName(), author.getFirtName())
                    );
                })
                .collect(Collectors.toList());
        return ret;
    }

}
