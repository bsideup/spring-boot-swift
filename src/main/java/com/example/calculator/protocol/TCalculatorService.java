package com.example.calculator.protocol;

import com.facebook.swift.service.ThriftMethod;
import com.facebook.swift.service.ThriftService;
import com.google.common.util.concurrent.ListenableFuture;

@ThriftService
public interface TCalculatorService {

    @ThriftMethod
    ListenableFuture<Integer> calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException;
}
