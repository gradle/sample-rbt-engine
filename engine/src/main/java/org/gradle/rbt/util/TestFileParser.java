package org.gradle.rbt.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TestFileParser {
    public boolean isValidTestSpecificationFile(File file) {
        return file.exists() && file.isFile() && file.canRead() && file.getName().toLowerCase().endsWith(".xml");
    }

    public List<String> parseTestNames(File testDefinitions) {
        if (!isValidTestSpecificationFile(testDefinitions)) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(testDefinitions);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("test");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element testElement = (Element) nodeList.item(i);
                String name = testElement.getAttribute("name");
                names.add(name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return names;
    }
}
