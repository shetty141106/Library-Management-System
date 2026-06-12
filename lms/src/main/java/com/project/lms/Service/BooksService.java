package com.project.lms.Service;

import com.project.lms.Entity.Books;
import com.project.lms.Repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Books getBookByIsbn(String isbn) {
        return booksRepository.findById(isbn).orElse(null);
    }

    public Books addBook(Books book) {
        return booksRepository.save(book);
    }

    public Books updateBook(String isbn, Books book) {

        Books existingBook = booksRepository.findById(isbn).orElse(null);
        return null;
    }

    public void deleteBook(String isbn) {
        booksRepository.deleteById(isbn);
    }
}