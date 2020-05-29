package com.artik.saml.web.controller;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallerFactory;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.schema.XSString;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Response;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.shibboleth.utilities.java.support.xml.XMLParserException;

@Controller
public class HelloWorldController {
	
	@GetMapping(value = "/test")
	public String test(final ModelMap model) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		Saml2Authentication saml2 = (Saml2Authentication) context.getAuthentication();
		
		String saml2Response = saml2.getSaml2Response();
		
		getAssertion(saml2Response, model);
				
		return "test";
	}
	
	
	
	@GetMapping(path = {"/", "/home"})
	public String home(final ModelMap model) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		Saml2Authentication saml2 = (Saml2Authentication) context.getAuthentication();
		
		String saml2Response = saml2.getSaml2Response();
		
		getAssertion(saml2Response, model);
		
		return "home";
	}
	
	private void getAssertion(String s, ModelMap model) {
		
		Response resp = (Response) doItInOneGo(s);
		
		
		for (Assertion assertion : resp.getAssertions()) {

			for (AttributeStatement statement : assertion.getAttributeStatements()) {

				for (Attribute attribute : statement.getAttributes()) {
					
					String name = attribute.getName();
					System.out.println(attribute.getName());
					
					XSString xsString = (XSString) attribute.getAttributeValues().get(0);
					System.out.println(xsString.getValue());
					
					model.put(name, xsString.getValue());
					
				}
			}
		}
	}
	
	private XMLObject getXMLObject(String s) {
		
		Document doc = createXMLDocument(s);
		
		Element element = doc.getDocumentElement();
		
		UnmarshallerFactory unmarshallerFactory = new UnmarshallerFactory();
		
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(element);
		
		try {
			XMLObject xmlObject = unmarshaller.unmarshall(element);
			
			return xmlObject;
			
		} catch (UnmarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Document createXMLDocument(String s) {
		
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      	
		DocumentBuilder builder;
		Document document;
		
		try {
			builder = factory.newDocumentBuilder();
			
			InputSource is = new InputSource(new StringReader(s));
			
			document = builder.parse(is);
			 
			return document;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;

	}
	
	private XMLObject doItInOneGo(String s) {
		
		// The DBF via the global ParserPool here is already namespace-aware.
		Document doc = null;
		try {
			doc = XMLObjectProviderRegistrySupport.getParserPool().parse(new StringReader(s));
		} catch (XMLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		           
		Unmarshaller unmarshaller = XMLObjectSupport.getUnmarshaller(doc.getDocumentElement());
		
		try {
			XMLObject xmlObject = unmarshaller.unmarshall(doc.getDocumentElement());
			
			return xmlObject;
			
		} catch (UnmarshallingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
