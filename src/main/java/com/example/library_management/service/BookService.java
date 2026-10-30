package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Get a list of all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Save or update a book
    public void saveBook(Book book) {
        // If it is a new book, set available copies to equal total copies
        if (book.getId() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        bookRepository.save(book);
    }

    // Find one book by its ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // Delete a book by its ID
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    // Simple search by title or author
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return bookRepository.findAll();
        }
        
        List<Book> allBooks = bookRepository.findAll();
        List<Book> filteredBooks = new ArrayList<>();
        
        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                book.getAuthor().toLowerCase().contains(keyword.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }
}

