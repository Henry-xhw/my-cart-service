package com.active.services.cart.domain.discount.condition;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

@RequiredArgsConstructor
public class XmlRestrictionSpec implements DiscountSpecification {
    private final String expression;
    private final String formDataXml;

    @Override
    public boolean satisfy() {
        if (StringUtils.isBlank(expression)) {
            return true;
        }

        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDoc =
                    xmlDocumentBuilder.parse(new ByteArrayInputStream(formDataXml.getBytes(Charset.forName("UTF-8"))));
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setXPathVariableResolver(qname -> {
                // set the transaction/purchase date to now; this is used to validate a min or max age where
                // the 'age by date' has been set to 'purchase date'
                if (qname.getLocalPart().equals("transaction-date")) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                }
                return null;
            });

            return (Boolean) xpath.evaluate(expression, xmlDoc, XPathConstants.BOOLEAN);
        } catch (Exception ex) {
            throw new IllegalArgumentException("OrderLine formDataXml value is invalid: " + ex.getLocalizedMessage(), ex);
        }
    }
}
