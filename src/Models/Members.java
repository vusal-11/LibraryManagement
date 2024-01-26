package Models;

import java.util.Date;

public class Members {
    private int memberID;
    private String name;
    private String membershipType;
    private Date joinDate;


    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public String toString() {
        return "Member ID: " + memberID +
                ", Name: " + name +
                ", Membership Type: " + membershipType +
                ", Join Date: " + joinDate;
    }
}
