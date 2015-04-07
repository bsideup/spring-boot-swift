package com.example.calculator.handler;

import com.example.calculator.protocol.TCalculatorService;
import com.example.calculator.protocol.TDivisionByZeroException;
import com.example.calculator.protocol.TOperation;
import org.springframework.stereotype.Component;

import com.example.calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CalculatorServiceHandler implements TCalculatorService {

    @Autowired
    CalculatorService calculatorService;

    @Override
    public int calculate(int num1, int num2, TOperation op) throws TDivisionByZeroException {
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
    }
}