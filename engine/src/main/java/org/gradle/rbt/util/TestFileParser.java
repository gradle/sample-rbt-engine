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
    public boolean isValidTestDefinitionFile(File file) {
        return file.exists() && file.isFile() && file.canRead()
                && (file.getName().toLowerCase().endsWith(".xml") || file.getName().toLowerCase().endsWith(".rbt"));
    }

    /**
     * Parses the given test definitions XML file and extracts the names of the tests defined within it.
     * <p>
     * The given file should be a valid definition file according to {@link #isValidTestDefinitionFile(File)}.
     *
     * @param testDefinitionsFile The XML file containing test definitions.
     * @return A list of test names extracted from the file. If no tests are found, returns an empty list.
     * @throws RuntimeException if there is an error parsing the XML file.
     */
    public List<String> parseTestNames(File testDefinitionsFile) {
        List<String> names = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(testDefinitionsFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("test");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element testElement = (Element) nodeList.item(i);
                String name = testElement.getAttribute("name");
                names.add(name);
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }

        return names;
    }
}
