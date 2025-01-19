package com.personalfinancetracker.personal_finance_tracker.mapper;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B a);
}