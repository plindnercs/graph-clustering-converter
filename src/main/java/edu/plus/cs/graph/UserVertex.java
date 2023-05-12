package edu.plus.cs.graph;

public class UserVertex {
    long key;

    Gender gender;

    public UserVertex(long key, Gender gender) {
        this.key = key;
        this.gender = gender;
    }

    public long getKey() {
        return key;
    }

    public Gender getGender() {
        return gender;
    }
}
