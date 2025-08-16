package com.libraryapp.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Loan implements Serializable {
    private String loanId;
    private String isbn;
    private int memberId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null if not returned

    public Loan(String isbn, int memberId, int loanDays) {
        this.loanId = UUID.randomUUID().toString();
        this.isbn = isbn;
        this.memberId = memberId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(loanDays);
    }

    public String getLoanId() { return loanId; }
    public String getIsbn() { return isbn; }
    public int getMemberId() { return memberId; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void markReturned(LocalDate date) { this.returnDate = date; }

    public boolean isActive() { return returnDate == null; }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId='" + loanId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", memberId=" + memberId +
                ", issue=" + issueDate +
                ", due=" + dueDate +
                ", return=" + (returnDate == null ? "-" : returnDate) +
                '}';
    }
}
