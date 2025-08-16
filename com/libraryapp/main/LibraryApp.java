package com.libraryapp.main;

import com.libraryapp.bean.Book;
import com.libraryapp.bean.Loan;
import com.libraryapp.bean.Member;
import com.libraryapp.exception.BookNotAvailableException;
import com.libraryapp.exception.BookNotFoundException;
import com.libraryapp.exception.MemberNotFoundException;
import com.libraryapp.service.LibraryService;
import com.libraryapp.util.FileUtil;

import java.util.List;
import java.util.Scanner;

public class LibraryApp {

    private static final String DATA_FILE = "library-data.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LibraryService lib = FileUtil.load(DATA_FILE);
        if (lib == null) lib = new LibraryService();

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add Book");
            System.out.println("2. Search Books by Title");
            System.out.println("3. Remove Book");
            System.out.println("4. Register Member");
            System.out.println("5. Issue Book");
            System.out.println("6. Return Book");
            System.out.println("7. List All Books");
            System.out.println("8. List Members & Active Loans");
            System.out.println("9. Save & Exit");
            System.out.print("Enter choice: ");

            int choice = readIntSafely(sc);

            try {
                switch (choice) {
                    case 1 -> addBookUI(sc, lib);
                    case 2 -> searchBooksUI(sc, lib);
                    case 3 -> removeBookUI(sc, lib);
                    case 4 -> registerMemberUI(sc, lib);
                    case 5 -> issueBookUI(sc, lib);
                    case 6 -> returnBookUI(sc, lib);
                    case 7 -> listAllBooksUI(lib);
                    case 8 -> listMembersUI(lib);
                    case 9 -> {
                        FileUtil.save(DATA_FILE, lib);
                        System.out.println("👋 Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("❗ " + e.getMessage());
            }
        }
    }

    private static int readIntSafely(Scanner sc) {
        while (!sc.hasNextInt()) { System.out.print("Enter a number: "); sc.next(); }
        int n = sc.nextInt(); sc.nextLine();
        return n;
    }

    // ---- UI Helpers ----
    private static void addBookUI(Scanner sc, LibraryService lib) {
        System.out.print("ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Total copies: ");
        int copies = readIntSafely(sc);

        lib.addBook(new Book(isbn, title, author, copies));
        System.out.println("✅ Book added.");
    }

    private static void searchBooksUI(Scanner sc, LibraryService lib) {
        System.out.print("Search title keyword: ");
        String q = sc.nextLine().trim();
        List<Book> results = lib.searchBooksByTitle(q);
        if (results.isEmpty()) System.out.println("No books found.");
        else results.forEach(System.out::println);
    }

    private static void removeBookUI(Scanner sc, LibraryService lib) throws BookNotFoundException {
        System.out.print("ISBN to remove: ");
        String isbn = sc.nextLine().trim();
        lib.removeBook(isbn);
        System.out.println("✅ Book removed.");
    }

    private static void registerMemberUI(Scanner sc, LibraryService lib) {
        System.out.print("Member ID (number): ");
        int id = readIntSafely(sc);
        System.out.print("Member name: ");
        String name = sc.nextLine().trim();
        lib.registerMember(new Member(id, name));
        System.out.println("✅ Member registered.");
    }

    private static void issueBookUI(Scanner sc, LibraryService lib)
            throws BookNotFoundException, MemberNotFoundException, BookNotAvailableException {
        System.out.print("ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Member ID: ");
        int id = readIntSafely(sc);
        Loan loan = lib.issueBook(isbn, id);
        System.out.println("✅ Issued. Loan ID: " + loan.getLoanId() + " | Due: " + loan.getDueDate());
    }

    private static void returnBookUI(Scanner sc, LibraryService lib) {
        System.out.print("Loan ID: ");
        String loanId = sc.nextLine().trim();
        double fine = lib.returnBook(loanId);
        if (fine > 0) System.out.println("⚠ Overdue fine: " + fine);
        else System.out.println("✅ Returned. No fine.");
    }

    private static void listAllBooksUI(LibraryService lib) {
        var all = lib.getAllBooks();
        if (all.isEmpty()) System.out.println("No books in catalog.");
        else all.forEach(System.out::println);
    }

    private static void listMembersUI(LibraryService lib) {
        var members = lib.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        members.forEach(m -> {
            System.out.println(m);
            var loans = lib.getActiveLoansByMember(m.getMemberId());
            if (loans.isEmpty()) System.out.println("  (No active loans)");
            else loans.forEach(l -> System.out.println("  -> " + l));
        });
    }
}
