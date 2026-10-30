package com.example.library_management.controller;

import com.example.library_management.model.Book;
import com.example.library_management.model.Student;
import com.example.library_management.service.BookService;
import com.example.library_management.service.LoanService;
import com.example.library_management.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LoanService loanService;

    // 1. Show the main dashboard page with all books
    @GetMapping("/")
    public String showMainDashboard(@RequestParam(value = "search", required = false) String search, Model model) {
        model.addAttribute("listBooks", bookService.searchBooks(search));
        model.addAttribute("searchKeyword", search);
        return "index";
    }

    // 2. Show the form to add a new book
    @GetMapping("/showNewBookForm")
    public String showNewBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "new_book";
    }

    // 3. Save a book record to the database
    @PostMapping("/saveBook")
    public String saveBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/";
    }

    // 4. Show the form to update an existing book
    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "update_book";
    }

    // 5. Delete a book record from the library database
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable(value = "id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/";
    }

    // 6. Show the students directory page
    @GetMapping("/students")
    public String showStudentsPage(Model model) {
        model.addAttribute("listStudents", studentService.getAllStudents());
        model.addAttribute("student", new Student());
        return "students";
    }

    // 7. Save a newly registered student record
    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // 8. Show the main book issuing and return page
    @GetMapping("/loans")
    public String showLoansPage(Model model) {
        model.addAttribute("activeLoans", loanService.getActiveLoans());
        model.addAttribute("books", bookService.getAllBooks());
        return "loans";
    }

    // 9. Process issuing a book to a student
    @PostMapping("/issueBook")
    public String processIssueBook(@RequestParam("bookId") Long bookId, 
                                    @RequestParam("studentIdNumber") String studentIdNumber,
                                    @RequestParam("duration") int duration, Model model) {
        try {
            loanService.issueBook(bookId, studentIdNumber, duration);
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("activeLoans", loanService.getActiveLoans());
            model.addAttribute("books", bookService.getAllBooks());
            return "loans";
        }
        return "redirect:/loans";
    }

    // 10. Process returning a borrowed book back to library shelves
    @GetMapping("/returnBook/{id}")
    public String processReturnBook(@PathVariable("id") Long id) {
        loanService.returnBook(id);
        return "redirect:/loans";
    }

    // 11. Show the overdue tracking report screen page
    @GetMapping("/reports/overdue")
    public String showOverdueReport(Model model) {
        model.addAttribute("overdueLoans", loanService.getOverdueReport());
        return "overdue_report";
    }
}
