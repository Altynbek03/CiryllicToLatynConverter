package com.example.CiryllicToLatin.dto;

import lombok.Data;

@Data
public class ClientDto {
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "ClientDto{" + "fullName='" + fullName + '\'' + '}';
    }
}
