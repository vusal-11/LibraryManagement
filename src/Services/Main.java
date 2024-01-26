package Services;

import Models.Book;
import Models.Borrowings;
import Models.Members;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Post7027472");
            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Scanner scanner = new Scanner(System.in);
            int choice;
            while (true) {
                System.out.println("Select an action:");
                System.out.println("1. Work with books");
                System.out.println("2. Work with library members");
                System.out.println("3. Work with borrowings");
                System.out.println("4. Show borrowing information using JOIN");
                System.out.println("5. Exit");

                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        workWithBooks(connection, libraryDatabase, scanner);
                        break;
                    case 2:
                        workWithMembers(connection, scanner);
                        break;
                    case 3:
                        workWithBorrowings(connection, libraryDatabase, scanner);
                        break;
                    case 4:
                        showBorrowingInfoWithJoin(connection);
                        break;
                    case 5:
                        System.out.println("Exiting the program.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter an existing menu item.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showBorrowingInfoWithJoin(Connection connection) {
        try {
            String sql = "SELECT Borrowings.BorrowingID, Borrowings.BorrowDate, Borrowings.ReturnDate, " +
                    "Books.Title, Members.Name " +
                    "FROM Borrowings " +
                    "JOIN Books ON Borrowings.BookID = Books.BookID " +
                    "JOIN Members ON Borrowings.MemberID = Members.MemberID";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int borrowingID = resultSet.getInt("BorrowingID");
                Date borrowDate = resultSet.getDate("BorrowDate");
                Date returnDate = resultSet.getDate("ReturnDate");
                String bookTitle = resultSet.getString("Title");
                String memberName = resultSet.getString("Name");

                System.out.println("Borrowing ID: " + borrowingID);
                System.out.println("Borrow Date: " + borrowDate);
                System.out.println("Return Date: " + returnDate);
                System.out.println("Book Title: " + bookTitle);
                System.out.println("Member Name: " + memberName);
                System.out.println();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void workWithBorrowings(Connection connection, LibraryDatabase libraryDatabase, Scanner scanner) {
        System.out.println("Select an action:");
        System.out.println("1. Show all borrowings");
        System.out.println("2. Add borrowing");
        System.out.println("3. Delete borrowing");
        System.out.println("4. Update borrowing information");
        System.out.println("5. Find borrowing by ID");
        System.out.println("6. Return to the main menu");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                showBorrowings(connection);
                break;
            case 2:
                addBorrowing(connection, scanner);
                break;
            case 3:
                deleteBorrowing(connection, scanner);
                break;
            case 4:
                updateBorrowing(connection, scanner);
                break;
            case 5:
                findBorrowingById(connection, scanner);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid choice. Please enter an existing menu item.");
        }
    }

    public static void showBorrowings(Connection connection) {
        LibraryDatabase libraryDatabase = new LibraryDatabase();
        List<Borrowings> borrowings = libraryDatabase.findAllBorrowings(connection);

        if (!borrowings.isEmpty()) {
            System.out.println("List of borrowings:");
            for (Borrowings borrowing : borrowings) {
                System.out.println("ID: " + borrowing.getBorrowingID() +
                        ", Book ID: " + borrowing.getBookID() +
                        ", Member ID: " + borrowing.getMemberID() +
                        ", Borrow Date: " + borrowing.getBorrowDate() +
                        ", Return Date: " + borrowing.getReturnDate());
            }
        } else {
            System.out.println("There are no borrowings in the library.");
        }
    }

    public static void addBorrowing(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter the book ID:");
            int bookID = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the member ID:");
            int memberID = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the borrowing date (yyyy-mm-dd):");
            String borrowDateStr = scanner.next();
            Date borrowDate = Date.valueOf(borrowDateStr);

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            libraryDatabase.insertBorrowingPrepared(connection, bookID, memberID, borrowDate, null);

            System.out.println("Borrowing successfully added.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void deleteBorrowing(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all Borrowings:");
            showBorrowings(connection);
            System.out.println("---------------------------");
            System.out.println("Enter the borrowing ID to delete:");
            int borrowingID = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Borrowings borrowing = libraryDatabase.findBorrowingById(connection, borrowingID);

            if (borrowing != null) {
                libraryDatabase.deleteBorrowingPrepared(connection, borrowingID);
                System.out.println("Borrowing successfully deleted.");
            } else {
                System.out.println("Borrowing with ID " + borrowingID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void updateBorrowing(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all Borrowings:");
            showBorrowings(connection);
            System.out.println("---------------------------");
            System.out.println("Enter the borrowing ID to update:");
            int borrowingID = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Borrowings borrowing = libraryDatabase.findBorrowingById(connection, borrowingID);

            if (borrowing != null) {
                System.out.println("Enter the new book ID:");
                int newBookID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter the new member ID:");
                int newMemberID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter the new borrowing date (yyyy-mm-dd):");
                String newBorrowDateStr = scanner.next();
                Date newBorrowDate = Date.valueOf(newBorrowDateStr);

                libraryDatabase.updateBorrowingByIdPrepared(connection, borrowingID, newBookID, newMemberID, newBorrowDate, null);
                System.out.println("Borrowing information successfully updated.");
            } else {
                System.out.println("Borrowing with ID " + borrowingID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void findBorrowingById(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter the borrowing ID:");
            int borrowingID = scanner.nextInt();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Borrowings borrowing = libraryDatabase.findBorrowingById(connection, borrowingID);

            if (borrowing != null) {
                System.out.println("Borrowing found:");
                System.out.println("ID: " + borrowing.getBorrowingID());
                System.out.println("Book ID: " + borrowing.getBookID());
                System.out.println("Member ID: " + borrowing.getMemberID());
                System.out.println("Borrow Date: " + borrowing.getBorrowDate());
                System.out.println("Return Date: " + borrowing.getReturnDate());
            } else {
                System.out.println("Borrowing with ID " + borrowingID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void workWithMembers(Connection connection, Scanner scanner) {
        LibraryDatabase libraryDatabase = new LibraryDatabase();

        System.out.println("Select an action:");
        System.out.println("1. Show all library members");
        System.out.println("2. Delete a library member");
        System.out.println("3. Update library member information");
        System.out.println("4. Add a library member");
        System.out.println("5. Find a library member by ID");
        System.out.println("6. Return to the main menu");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                showMembers(connection);
                break;
            case 2:
                deleteMember(connection, scanner);
                break;
            case 3:
                updateMember(connection, scanner);
                break;
            case 4:
                addMember(connection, scanner);
                break;
            case 5:
                findMemberById(connection, scanner);
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice. Please enter an existing menu item.");
        }
    }

    public static void showMembers(Connection connection) {
        LibraryDatabase libraryDatabase = new LibraryDatabase();
        List<Members> members = libraryDatabase.findAllMembers(connection);

        if (!members.isEmpty()) {
            System.out.println("List of members:");
            for (Members member : members) {
                System.out.println("ID: " + member.getMemberID() +
                        ", Name: " + member.getName() +
                        ", Membership Type: " + member.getMembershipType() +
                        ", Join Date: " + member.getJoinDate());

            }
        } else {
            System.out.println("There are no members in the library.");
        }
    }

    public static void addMember(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter the name of the new library member:");
            String name = scanner.nextLine();

            System.out.println("Enter the membership type:");
            String membershipType = scanner.nextLine();


            Date currentDate = new Date(System.currentTimeMillis());


            String query = "INSERT INTO members (name, membership_type, join_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, membershipType);
            preparedStatement.setDate(3, currentDate);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New library member added.");
            } else {
                System.out.println("Failed to add a new library member.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void updateMember(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all library members:");
            showMembers(connection);
            System.out.println("Enter the ID of the library member to update:");
            int memberId = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Members member = libraryDatabase.findMemberById(connection, memberId);

            if (member != null) {
                System.out.println("Enter the new name of the library member:");
                String newName = scanner.nextLine();

                System.out.println("Enter the new membership type:");
                String newMembershipType = scanner.nextLine();


                String query = "UPDATE members SET name = ?, membership_type = ? WHERE member_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newMembershipType);
                preparedStatement.setInt(3, memberId);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Library member information successfully updated.");
                } else {
                    System.out.println("Library member with ID " + memberId + " not found.");
                }
            } else {
                System.out.println("Library member with ID " + memberId + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void findMemberById(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter the ID of the library member:");
            int memberId = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Members member = libraryDatabase.findMemberById(connection, memberId);

            if (member != null) {
                System.out.println("Library member found:");
                System.out.println("ID: " + member.getMemberID());
                System.out.println("Name: " + member.getName());
                System.out.println("Membership Type: " + member.getMembershipType());
            } else {
                System.out.println("Library member with ID " + memberId + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void deleteMember(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all library members:");
            showMembers(connection);
            System.out.println("Enter the member ID to delete:");
            int memberId = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Members member = libraryDatabase.findMemberById(connection, memberId);

            if (member != null) {
                libraryDatabase.deleteMemberPrepared(connection, memberId);
                System.out.println("Member successfully deleted.");
            } else {
                System.out.println("Library member with ID " + memberId + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }


    public static void workWithBooks(Connection connection, LibraryDatabase libraryDatabase, Scanner scanner) {
        System.out.println("Select an action:");
        System.out.println("1 Show all books");
        System.out.println("2. Add a book");
        System.out.println("3. Delete a book");
        System.out.println("4. Update book information");
        System.out.println("5. Find a book by ID");
        System.out.println("6. Return to the main menu");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                showBooks(connection);
                break;
            case 2:
                addBook(connection, scanner);
                break;
            case 3:
                deleteBook(connection, scanner);
                break;
            case 4:
                updateBook(connection, scanner);
                break;
            case 5:
                findBookById(connection, scanner);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid choice. Please enter an existing menu item.");
        }
    }

    public static void showBooks(Connection connection) {
        LibraryDatabase libraryDatabase = new LibraryDatabase();
        List<Book> books = libraryDatabase.findAllBooks(connection);

        if (!books.isEmpty()) {
            System.out.println("List of books:");
            for (Book book : books) {
                System.out.println("ID: " + book.getBookID() +
                        ", Title: " + book.getTitle() +
                        ", Author: " + book.getAuthor() +
                        ", Genre: " + book.getGenre() +
                        ", Year Published: " + book.getYearPublished());
            }
        } else {
            System.out.println("There are no books in the library.");
        }
    }

    public static void addBook(Connection connection, Scanner scanner) {
        try {
            scanner.nextLine();
            System.out.println("Enter the title of the book:");
            String title = scanner.nextLine();

            System.out.println("Enter the author of the book:");
            String author = scanner.nextLine();

            System.out.println("Enter the genre of the book:");
            String genre = scanner.nextLine();

            System.out.println("Enter the year published of the book:");
            int yearPublished = scanner.nextInt();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            libraryDatabase.insertBookPrepared(connection, title, author, genre, yearPublished);

            System.out.println("Book successfully added.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }


    public static void deleteBook(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all books:");
            showBooks(connection);
            System.out.println("Enter the book ID to delete:");
            int bookID = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Book book = libraryDatabase.findBookByIdPrepared(connection, bookID);

            if (book != null) {
                libraryDatabase.deleteBookPrepared(connection, bookID);
                System.out.println("Book successfully deleted.");
            } else {
                System.out.println("Book with ID " + bookID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void updateBook(Connection connection, Scanner scanner) {
        try {
            System.out.println("List of all books:");
            showBooks(connection);
            System.out.println("Enter the book ID to update:");
            int bookID = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Book book = libraryDatabase.findBookByIdPrepared(connection, bookID);

            if (book != null) {
                System.out.println("Enter the new title of the book:");
                String newTitle = scanner.nextLine();

                System.out.println("Enter the new author of the book:");
                String newAuthor = scanner.nextLine();

                System.out.println("Enter the new genre of the book:");
                String newGenre = scanner.nextLine();

                System.out.println("Enter the new year published of the book:");
                int newYearPublished = scanner.nextInt();
                scanner.nextLine(); // Clear the scanner buffer

                libraryDatabase.updateBookByIdPrepared(connection, bookID, newTitle, newAuthor, newGenre, newYearPublished);
                System.out.println("Book information successfully updated.");
            } else {
                System.out.println("Book with ID " + bookID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }

    public static void findBookById(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter the book ID:");
            int bookID = scanner.nextInt();
            scanner.nextLine();

            LibraryDatabase libraryDatabase = new LibraryDatabase();
            Book book = libraryDatabase.findBookByIdPrepared(connection, bookID);

            if (book != null) {
                System.out.println("Book found:");
                System.out.println("ID: " + book.getBookID());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.println("Genre: " + book.getGenre());
                System.out.println("Year Published: " + book.getYearPublished());
            } else {
                System.out.println("Book with ID " + bookID + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input format.");
        }
    }


}

//
//CREATE TABLE Books (
//        BookID SERIAL PRIMARY KEY,
//        Title VARCHAR,
//        Author VARCHAR,
//        Genre VARCHAR,
//        YearPublished INT
//        );
//
//        CREATE TABLE Members (
//        MemberID SERIAL PRIMARY KEY,
//        Name VARCHAR,
//        membership_type VARCHAR,
//        JoinDate DATE
//        );
//
//
//
//        CREATE TABLE Borrowings (
//        BorrowingID SERIAL PRIMARY KEY,
//        BookID INT,
//        MemberID INT,
//        BorrowDate DATE,
//        ReturnDate DATE,
//        FOREIGN KEY (BookID) REFERENCES Books (BookID),
//        FOREIGN KEY (MemberID) REFERENCES Members (MemberID)
//        );

//