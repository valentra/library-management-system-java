# 📚 Library Management System (Java)

A simple **console-based Library Management System** built with **Core Java**.  
It supports adding/searching/removing books, registering members, issuing & returning books with fines, and persisting data using serialization.

---

## ✨ Features
- ➕ Add, search, and remove books
- 👤 Register library members
- 📖 Issue and return books
- ⏳ Track due dates & overdue fines
- 💾 Data persistence with serialization (`.ser` file)
- ⚠ Custom exceptions for clean error handling

---

## 🛠 Tech Stack
- Java (Core)
- OOP principles (Encapsulation, Abstraction, Inheritance, Polymorphism)
- Exception Handling
- File I/O & Serialization
- Collections Framework

---

## 📂 Project Structure

com.libraryapp

 ├── bean
 
 │    ├── Book.java 
 
 │    ├── Loan.java
 
 │    └── Member.java
 
 ├── exception
 
 │    ├── BookNotAvailableException.java
 
 │    ├── BookNotFoundException.java
 
 │    └── MemberNotFoundException.java
 
 ├── service

 │    └── LibraryService.java
 
 ├── util
 
 │    └── FileUtil.java
 
 └── main
 
      └── LibraryApp.java   ← Main entry point


---

## 🚀 How to Run
1. Clone this repo:
   ```bash
   git clone https://github.com/valentra/library-management-system-java.git

📌 Future Enhancements

GUI using JavaFX or Swing

Database integration (PostgreSQL / MySQL)

REST API with Spring Boot

👨‍💻 Author
Valentra
