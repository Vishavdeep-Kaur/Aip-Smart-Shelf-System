package com.example.library_management.service;

import com.example.library_management.model.Book;
import com.example.library_management.model.Loan;
import com.example.library_management.model.Student;
import com.example.library_management.repository.BookRepository;
import com.example.library_management.repository.LoanRepository;
import com.example.library_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    public void issueBook(Long bookId, String studentIdNumber, int days) {
        Book book = bookRepository.findById(bookId).orElse(null);
        Student student = studentRepository.findByStudentIdNumber(studentIdNumber).orElse(null);

        if (book == null || student == null || book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Cannot issue book! Check stock or Student ID.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setStudent(student);
        loan.setIssueDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(days));

        loanRepository.save(loan);
    }

    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null || loan.getReturnDate() != null) return;

        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findByReturnDateIsNull();
    }

    public List<Loan> getOverdueReport() {
        List<Loan> activeLoans = loanRepository.findByReturnDateIsNull();
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan loan : activeLoans) {
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }
}
