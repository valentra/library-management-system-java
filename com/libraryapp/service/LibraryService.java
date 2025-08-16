package com.libraryapp.service;

import com.libraryapp.bean.Book;
import com.libraryapp.bean.Loan;
import com.libraryapp.bean.Member;
import com.libraryapp.exception.BookNotAvailableException;
import com.libraryapp.exception.BookNotFoundException;
import com.libraryapp.exception.MemberNotFoundException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds in-memory data + operations. Serializable so we can persist everything.
 */
public class LibraryService implements Serializable {

    // Core storage
    private Map<String, Book> books = new HashMap<>();         // isbn -> Book
    private Map<Integer, Member> members = new HashMap<>();    // id -> Member
    private Map<String, Loan> loans = new HashMap<>();         // loanId -> Loan

    // Business config
    private int loanDays = 14;
    private double finePerDay = 5.0; // currency units per overdue day

    // ---- Book ops ----
    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book getBook(String isbn) throws BookNotFoundException {
        Book b = books.get(isbn);
        if (b == null) throw new BookNotFoundException("Book with ISBN " + isbn + " not found.");
        return b;
    }

    public List<Book> searchBooksByTitle(String keyword) {
        String k = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(k))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public boolean removeBook(String isbn) throws BookNotFoundException {
        Book b = books.remove(isbn);
        if (b == null) throw new BookNotFoundException("Cannot remove. Book with ISBN " + isbn + " not found.");
        return true;
    }

    // ---- Member ops ----
    public void registerMember(Member m) {
        members.put(m.getMemberId(), m);
    }

    public Member getMember(int memberId) throws MemberNotFoundException {
        Member m = members.get(memberId);
        if (m == null) throw new MemberNotFoundException("Member " + memberId + " not found.");
        return m;
    }

    // ---- Loan ops ----
    public Loan issueBook(String isbn, int memberId)
            throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {

        Book book = getBook(isbn);
        Member member = getMember(memberId);

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No copies available for ISBN " + isbn);
        }
        if (member.getActiveLoanIds().size() >= member.getMaxBooks()) {
            throw new BookNotAvailableException("Member " + memberId + " has reached max active loans.");
        }

        Loan loan = new Loan(isbn, memberId, loanDays);
        loans.put(loan.getLoanId(), loan);
        member.getActiveLoanIds().add(loan.getLoanId());
        book.decrementAvailable();
        return loan;
    }

    public double returnBook(String loanId)
            throws IllegalArgumentException {

        Loan loan = loans.get(loanId);
        if (loan == null) throw new IllegalArgumentException("Invalid loanId.");

        if (!loan.isActive()) return 0.0; // already returned, no fine double-charge

        loan.markReturned(LocalDate.now());

        // Fine calculation
        long overdueDays = Math.max(0, ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate()));
        double fine = overdueDays * finePerDay;

        // Update book & member
        Book book = books.get(loan.getIsbn());
        if (book != null) book.incrementAvailable();

        Member member = members.get(loan.getMemberId());
        if (member != null) member.getActiveLoanIds().remove(loanId);

        return fine;
    }

    // ---- Reporting helpers ----
    public List<Loan> getActiveLoansByMember(int memberId) {
        return loans.values().stream()
                .filter(Loan::isActive)
                .filter(l -> l.getMemberId() == memberId)
                .sorted(Comparator.comparing(Loan::getDueDate))
                .collect(Collectors.toList());
    }

    public List<Loan> getAllActiveLoans() {
        return loans.values().stream()
                .filter(Loan::isActive)
                .sorted(Comparator.comparing(Loan::getDueDate))
                .collect(Collectors.toList());
    }

    public Collection<Book> getAllBooks() { return books.values(); }
    public Collection<Member> getAllMembers() { return members.values(); }

    // Getters for config (optional)
    public int getLoanDays() { return loanDays; }
    public double getFinePerDay() { return finePerDay; }
    public void setLoanDays(int loanDays) { this.loanDays = loanDays; }
    public void setFinePerDay(double finePerDay) { this.finePerDay = finePerDay; }
}
