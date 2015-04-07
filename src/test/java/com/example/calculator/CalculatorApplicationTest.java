package com.example.calculator;

import com.example.calculator.protocol.TCalculatorService;
import com.example.calculator.protocol.TDivisionByZeroException;
import com.example.calculator.protocol.TOperation;
import com.facebook.nifty.client.HttpClientConnector;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftClientManager;
import org.apache.thrift.protocol.TProtocolFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CalculatorApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class CalculatorApplicationTest {

    @Autowired
    protected TProtocolFactory protocolFactory;

    @Autowired
    ThriftCodecManager thriftCodecManager;

    @Value("${local.server.port}")
    protected int port;

    protected TCalculatorService client;

    @Before
    public void setUp() throws Exception {
        HttpClientConnector connector = new HttpClientConnector(URI.create("http://localhost:" + port + "/thrift/"));

        ThriftClientManager clientManager = new ThriftClientManager(thriftCodecManager);
        client = clientManager.createClient(connector, TCalculatorService.class).get();
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(5, client.calculate(2, 3, TOperation.ADD));
    }

    @Test
    public void testSubtract() throws Exception {
        assertEquals(3, client.calculate(5, 2, TOperation.SUBTRACT));
    }

    @Test
    public void testMultiply() throws Exception {
        assertEquals(10, client.calculate(5, 2, TOperation.MULTIPLY));
    }

    @Test
    public void testDivide() throws Exception {
        assertEquals(2, client.calculate(10, 5, TOperation.DIVIDE));
    }

    @Test(expected = TDivisionByZeroException.class)
    public void testDivisionByZero() throws Exception {
        client.calculate(10, 0, TOperation.DIVIDE);
    }
}