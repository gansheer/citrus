package com.consol.citrus.functions;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.consol.citrus.testng.AbstractIntegrationTest;

/**
 * 
 * @author deppisch Christoph Deppisch Consol* Software GmbH
 * @since 31.10.2008
 */
public class XPathFunctionTest extends AbstractIntegrationTest {
    @Test
    public void xPathFunctionTest(ITestContext testContext) {
        executeTest(testContext);
    }
}