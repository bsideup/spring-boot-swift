package com.example.calculator;

import com.example.calculator.protocol.TCalculatorService;
import com.facebook.nifty.processor.NiftyProcessorAdapters;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServiceProcessor;
import org.apache.thrift.protocol.*;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

import javax.servlet.Servlet;
import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class CalculatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }

    @Bean
    TProtocolFactory tProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
    
    @Bean
    ThriftCodecManager thriftCodecManager() {
        return new ThriftCodecManager();
    }

    @Bean
    Servlet thrift(ThriftCodecManager thriftCodecManager, TProtocolFactory protocolFactory, TCalculatorService exampleService) {
        ThriftServiceProcessor processor = new ThriftServiceProcessor(thriftCodecManager, Arrays.<ThriftEventHandler>asList(), exampleService);

        return new TServlet(
                NiftyProcessorAdapters.processorToTProcessor(processor),
                protocolFactory,
                protocolFactory
        );
    }
}