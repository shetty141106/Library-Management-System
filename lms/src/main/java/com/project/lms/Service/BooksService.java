package com.project.lms.Service;

import com.project.lms.Dao.PublisherDao;
import com.project.lms.Dto.ApiResponse;
import com.project.lms.Dto.BooksRequest;
import com.project.lms.Dto.BooksResponse;
import com.project.lms.Entity.Books;
import com.project.lms.Dao.BooksDao;
import com.project.lms.Entity.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    @Autowired
    private BooksDao booksDao;

    @Autowired
    private PublisherDao pubdao;

    private BooksResponse toBookResponse(Books books) {
        return new BooksResponse(
                books.getIsbn(), books.getTitle(), books.getEdition(), books.getAuthName(), books.getPrice(), books.getCategory(),books.getQuantity(), books.getPublisher().getName(), books.getYearOfPublication()
        );
    }

    public ApiResponse<List<BooksResponse>> getAllBooks() {
        List<Books> books = booksDao.findAll();
        if (books.isEmpty())
            return ApiResponse.fail("No books available currently.");
        List<BooksResponse> res = books.stream().map(this::toBookResponse).toList();
        return ApiResponse.ok("All Books Fetched.", res);
    }

    public ApiResponse<BooksResponse> updateQuantity(BooksRequest books){
       Optional <Books> book1=booksDao.findById(books.getIsbn());
       if(book1.isEmpty())
           return ApiResponse.fail("No data found");
       Books book= book1.get();
       book.setQuantity(books.getQuantity());
       booksDao.save(book);
       return ApiResponse.ok("Quantity Updated successfully",toBookResponse(book));}

    public ApiResponse<BooksResponse> getBookByIsbn(String isbn) {
        Optional<Books> booksOptional = booksDao.findById(isbn);
        if (booksOptional.isEmpty())
            return ApiResponse.fail("Book not found.");
        Books books = booksOptional.get();
        return ApiResponse.ok("Book Fetched.", toBookResponse(books));
    }

    public ApiResponse<BooksResponse> addBook(BooksRequest bookreq) {
        if (bookreq.getAuthName().isBlank() || bookreq.getTitle().isBlank() || bookreq.getIsbn().isBlank() || bookreq.getCategory().isBlank() || bookreq.getPrice() <= 0)
            return ApiResponse.fail("All fields are required.");
        if (!bookreq.getIsbn().equals(bookreq.getConfirm_isbn()))
            return ApiResponse.fail("Isbn does not match.");
        Optional<Books> booksOptional = booksDao.findById(bookreq.getIsbn());
        if (booksOptional.isPresent())
            return ApiResponse.fail("Book with this Isbn already exists.");
        Optional<Publisher> optPub = pubdao.findByName(bookreq.getPublisherName());
        Books books = new Books();
        books.setIsbn(bookreq.getIsbn());
        books.setTitle(bookreq.getTitle());
        books.setEdition(bookreq.getEdition());
        books.setAuthName(bookreq.getAuthName());
        books.setPrice(bookreq.getPrice());
        books.setCategory(bookreq.getCategory());
        books.setQuantity(bookreq.getQuantity());
        books.setYearOfPublication(bookreq.getYearOfPublication());
        if(optPub.isPresent())
            books.setPublisher(optPub.get());
        else{
            Publisher pub = new Publisher();
            pub.setName(bookreq.getPublisherName());
            books.setPublisher(pub);
        }
        booksDao.save(books);
        return ApiResponse.ok("Book Added.", toBookResponse(books));
    }

    public ApiResponse<BooksResponse> updateBook(String isbn, BooksRequest bookreq) {
        Optional<Books> bookOptional = booksDao.findById(isbn);
        if (bookOptional.isEmpty())
            return ApiResponse.fail("Book not found.");
        Books existingBook = bookOptional.get();
        if (!bookreq.getTitle().isBlank())
            existingBook.setTitle(bookreq.getTitle());
        if (!bookreq.getAuthName().isBlank())
            existingBook.setAuthName(bookreq.getAuthName());
        if (bookreq.getEdition() != 0)
            existingBook.setEdition(bookreq.getEdition());
        if (!bookreq.getCategory().isBlank())
            existingBook.setCategory(bookreq.getCategory());
        if (bookreq.getPrice() != 0)
            existingBook.setPrice(bookreq.getPrice());
        if(bookreq.getYearOfPublication() > 0)
            existingBook.setYearOfPublication(bookreq.getYearOfPublication());
        Optional<Publisher> optionalPublisher;
        if(!bookreq.getPublisherName().isBlank()) {
            optionalPublisher = pubdao.findByName(bookreq.getPublisherName());
            Publisher publisher;
            if(optionalPublisher.isEmpty()){
                publisher = new Publisher();
                publisher.setName(bookreq.getPublisherName());
            }
            else{
                publisher = optionalPublisher.get();
            }
            existingBook.setPublisher(publisher);
        }
        booksDao.save(existingBook);
        return ApiResponse.ok("Book updated.", toBookResponse(existingBook));
    }

    public ApiResponse<Void> deleteBook(String isbn) {
        Optional<Books> booksOptional = booksDao.findById(isbn);
        if (booksOptional.isEmpty())
            return ApiResponse.fail("Book not found.");
        booksDao.deleteById(isbn);
        return ApiResponse.ok("Book Deleted", null);

    }
}