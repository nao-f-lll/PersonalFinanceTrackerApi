package com.personalfinancetracker.mapper;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B a);
}