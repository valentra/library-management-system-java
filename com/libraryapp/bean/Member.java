package com.libraryapp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    private int memberId;
    private String name;
    private int maxBooks = 3;
    private List<String> activeLoanIds = new ArrayList<>();

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getMaxBooks() { return maxBooks; }
    public List<String> getActiveLoanIds() { return activeLoanIds; }

    @Override
    public String toString() {
        return "Member{id=" + memberId + ", name='" + name + "', activeLoans=" + activeLoanIds.size() + "}";
    }
}
