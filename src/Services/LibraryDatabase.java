package Services;

import Models.Book;
import Models.Borrowings;
import Models.Members;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDatabase {


    public void insertBookPrepared(Connection connection, String title, String author, String genre, int yearPublished) {
        String sql = "INSERT INTO Books (Title, Author, Genre, YearPublished) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, genre);
            preparedStatement.setInt(4, yearPublished);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBookPrepared(Connection connection, int bookID) {
        String sql = "DELETE FROM Books WHERE BookID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book findBookByIdPrepared(Connection connection, int bookID) {
        String sql = "SELECT * FROM Books WHERE BookID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookID);
            ResultSet resultSet = preparedStatement.executeQuery();

            Book book = null;
            while (resultSet.next()) {
                book = new Book();
                book.setBookID(resultSet.getInt("BookID"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setGenre(resultSet.getString("Genre"));
                book.setYearPublished(resultSet.getInt("YearPublished"));
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBookByIdPrepared(Connection connection, int bookID, String newTitle, String newAuthor, String newGenre, int newYearPublished) {
        String sql = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, YearPublished = ? WHERE BookID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setString(2, newAuthor);
            preparedStatement.setString(3, newGenre);
            preparedStatement.setInt(4, newYearPublished);
            preparedStatement.setInt(5, bookID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No book with BookID " + bookID + " found for update.");
            } else {
                System.out.println("Book with BookID " + bookID + " updated successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertMemberPrepared(Connection connection, String name, String membershipType, Date joinDate) {
        String sql = "INSERT INTO Members (Name, MembershipType, JoinDate) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, membershipType);
            preparedStatement.setDate(3, joinDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMemberPrepared(Connection connection, int memberID) {
        String sql = "DELETE FROM Members WHERE MemberID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, memberID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMemberByIdPrepared(Connection connection, int memberID, String newName, String newMembershipType, Date newJoinDate) {
        String sql = "UPDATE Members SET Name = ?, MembershipType = ?, JoinDate = ? WHERE MemberID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newMembershipType);
            preparedStatement.setDate(3, newJoinDate);
            preparedStatement.setInt(4, memberID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No member with MemberID " + memberID + " found for update.");
            } else {
                System.out.println("Member with MemberID " + memberID + " updated successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertBorrowingPrepared(Connection connection, int bookID, int memberID, Date borrowDate, Date returnDate) {
        String sql = "INSERT INTO Borrowings (BookID, MemberID, BorrowDate, ReturnDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookID);
            preparedStatement.setInt(2, memberID);
            preparedStatement.setDate(3, borrowDate);
            preparedStatement.setDate(4, returnDate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteBorrowingPrepared(Connection connection, int borrowingID) {
        String sql = "DELETE FROM Borrowings WHERE BorrowingID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrowingID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBorrowingByIdPrepared(Connection connection, int borrowingID, int bookID, int memberID, Date borrowDate, Date returnDate) {
        String sql = "UPDATE Borrowings SET BookID = ?, MemberID = ?, BorrowDate = ?, ReturnDate = ? WHERE BorrowingID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookID);
            preparedStatement.setInt(2, memberID);
            preparedStatement.setDate(3, borrowDate);
            preparedStatement.setDate(4, returnDate);
            preparedStatement.setInt(5, borrowingID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No borrowing with BorrowingID " + borrowingID + " found for update.");
            } else {
                System.out.println("Borrowing with BorrowingID " + borrowingID + " updated successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findAllBooks(Connection connection) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Book book = new Book();
                book.setBookID(resultSet.getInt("BookID"));
                book.setTitle(resultSet.getString("Title"));
                book.setAuthor(resultSet.getString("Author"));
                book.setGenre(resultSet.getString("Genre"));
                book.setYearPublished(resultSet.getInt("YearPublished"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    public List<Members> findAllMembers(Connection connection) {
        List<Members> members = new ArrayList<>();
        String sql = "SELECT * FROM Members";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Members member = new Members();
                member.setMemberID(resultSet.getInt("MemberID"));
                member.setName(resultSet.getString("Name"));
                member.setMembershipType(resultSet.getString("MembershipType"));
                member.setJoinDate(resultSet.getDate("JoinDate"));
                members.add(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return members;
    }

    public List<Borrowings> findAllBorrowings(Connection connection) {
        List<Borrowings> borrowings = new ArrayList<>();
        String sql = "SELECT * FROM Borrowings";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Borrowings borrowing = new Borrowings();
                borrowing.setBorrowingID(resultSet.getInt("BorrowingID"));
                borrowing.setBookID(resultSet.getInt("BookID"));
                borrowing.setMemberID(resultSet.getInt("MemberID"));
                borrowing.setBorrowDate(resultSet.getDate("BorrowDate"));
                borrowing.setReturnDate(resultSet.getDate("ReturnDate"));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return borrowings;
    }

    public Members findMemberById(Connection connection, int memberID) {
        String sql = "SELECT * FROM Members WHERE MemberID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, memberID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Members member = new Members();
                member.setMemberID(resultSet.getInt("MemberID"));
                member.setName(resultSet.getString("Name"));
                member.setMembershipType(resultSet.getString("MembershipType"));
                member.setJoinDate(resultSet.getDate("JoinDate"));
                return member;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Borrowings findBorrowingById(Connection connection, int borrowingID) {
        String sql = "SELECT * FROM Borrowings WHERE BorrowingID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrowingID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Borrowings borrowing = new Borrowings();
                borrowing.setBorrowingID(resultSet.getInt("BorrowingID"));
                borrowing.setBookID(resultSet.getInt("BookID"));
                borrowing.setMemberID(resultSet.getInt("MemberID"));
                borrowing.setBorrowDate(resultSet.getDate("BorrowDate"));
                borrowing.setReturnDate(resultSet.getDate("ReturnDate"));
                return borrowing;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
