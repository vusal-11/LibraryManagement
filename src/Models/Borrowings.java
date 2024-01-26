package Models;
import java.util.Date;

public class Borrowings {
    private int borrowingID;
    private int bookID;
    private int memberID;
    private Date borrowDate;
    private Date returnDate;

    public int getBorrowingID() {
        return borrowingID;
    }

    public void setBorrowingID(int borrowingID) {
        this.borrowingID = borrowingID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Borrowing ID: " + borrowingID +
                ", Book ID: " + bookID +
                ", Member ID: " + memberID +
                ", Borrow Date: " + borrowDate +
                ", Return Date: " + returnDate;
    }

}
