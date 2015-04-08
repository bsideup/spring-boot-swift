package com.example.calculator.handler;

import com.example.calculator.protocol.TCalculatorService;
import com.example.calculator.protocol.TDivisionByZeroException;
import com.example.calculator.protocol.TOperation;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.stereotype.Component;

import com.example.calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

@Component
public class CalculatorServiceHandler implements TCalculatorService {

    @Autowired
    CalculatorService calculatorService;

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    @Override
    public ListenableFuture<Integer> calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException {
        return JdkFutureAdapters.listenInPoolThread(
                scheduledExecutorService.schedule(() -> {
                    switch(op) {
                        case ADD:
                            return calculatorService.add(num1, num2);
                        case SUBTRACT:
                            return calculatorService.subtract(num1, num2);
                        case MULTIPLY:
                            return calculatorService.multiply(num1, num2);
                        case DIVIDE:
                            try {
                                return calculatorService.divide(num1, num2);
                            } catch(IllegalArgumentException e) {
                                throw new TDivisionByZeroException();
                            }
                        default:
                            throw new IllegalArgumentException("Unknown operation " + op);
                    }
                }, 2, TimeUnit.SECONDS)
        );
    }
}