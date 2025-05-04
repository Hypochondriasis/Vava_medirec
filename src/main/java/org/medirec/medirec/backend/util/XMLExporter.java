package org.medirec.medirec.backend.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.medirec.medirec.backend.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XMLExporter - A utility class for exporting model objects to XML format
 *
 * This class provides functionality to export medical records data model objects
 * to XML format, either as XML strings or directly to files.
 */
public class XMLExporter {
    private static final String ROOT_ELEMENT = "medirec-export";

    /**
     * Export a single model object to XML string
     *
     * @param object The model object to export
     * @return String containing XML representation
     */
    public static String exportToXML(Object object) throws Exception {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Creating the root element
            Element rootElement = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(rootElement);

            // Adding the object to the XML document
            addObjectToXml(doc, rootElement, object, true);

            // Converting document to string
            return documentToString(doc);

        } catch (ParserConfigurationException e) {
            throw new Exception("Error creating XML document: " + e.getMessage(), e);
        }
    }

    /**
     * Export a collection of model objects to XML string
     *
     * @param objects Collection of model objects to export
     * @return String containing XML representation
     */
    public static String exportToXML(Collection<?> objects) throws Exception {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Creating the root element
            Element rootElement = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(rootElement);

            // Add each object in the collection to the XML document
            for (Object object : objects) {
                addObjectToXml(doc, rootElement, object, true);
            }

            // Converting document to string
            return documentToString(doc);

        } catch (ParserConfigurationException e) {
            throw new Exception("Error creating XML document: " + e.getMessage(), e);
        }
    }

    /**
     * Export a single model object to XML file
     *
     * @param object The model object to export
     * @param filePath Path where the XML file should be saved
     */
    public static void exportToXMLFile(Object object, String filePath) throws Exception {
        String xmlContent = exportToXML(object);
        writeToFile(xmlContent, filePath);
    }

    /**
     * Export a collection of model objects to XML file
     *
     * @param objects Collection of model objects to export
     * @param filePath Path where the XML file should be saved
     */
    public static void exportToXMLFile(Collection<?> objects, String filePath) throws Exception{
        String xmlContent = exportToXML(objects);
        writeToFile(xmlContent, filePath);
    }

    /**
     * Write XML content to a file
     *
     * @param xmlContent The XML content as a string
     * @param filePath Path where the XML file should be saved
     */
    private static void writeToFile(String xmlContent, String filePath) throws IOException {
        File file = new File(filePath);

        // Creating parent directories if they don't exist
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(xmlContent);
        }
    }

    /**
     * Converts a Document object to a formatted XML string
     *
     * @param doc The Document object to convert
     * @return Formatted XML string
     */
    private static String documentToString(Document doc) throws Exception {
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Set output properties for formatted printing
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        }catch (TransformerException e){
            throw new Exception("Error transforming XML document to string: " + e.getMessage(), e);
        }
    }

    /**
     * Add an object to the XML document
     *
     * @param doc The XML Document
     * @param parentElement The parent element to add this object to
     * @param object The object to add
     * @param includeReferences Whether to include referenced objects
     */
    private static void addObjectToXml(Document doc, Element parentElement, Object object, boolean includeReferences) throws Exception {
        if (object == null) {
            return;
        }

        Class<?> clazz = object.getClass();
        String elementName = getElementNameForClass(clazz);

        Element objectElement = doc.createElement(elementName);
        parentElement.appendChild(objectElement);

        // Processing all of the fields for the class
        for (Field field : clazz.getDeclaredFields()) {
            // Skipping static or transient fields -> Not part of the data we need
            if(java.lang.reflect.Modifier.isStatic(field.getModifiers()) || java.lang.reflect.Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            // Skip fields marked with @IgnoreColumn if not including references
            if (!includeReferences && field.isAnnotationPresent(IgnoreColumn.class)) {
                continue;
            }

            String fieldName = field.getName();

            // Skipping table constatns if there
            if (fieldName.equals("TABLE")) {
                continue;
            }

            // Getting the getter method name
            String getterName = getGetterMethodName(fieldName, field.getType());

            try{
                Method getter = clazz.getMethod(getterName);
                Object value = getter.invoke(object);

                // Handling different field types
                if (value != null) {
                    if (value instanceof Collection) {
                        // Handle collections (one-to-many relationships)
                        Element collectionElement = doc.createElement(fieldName);
                        objectElement.appendChild(collectionElement);

                        // Only include collection items if includeReferences is true
                        if (includeReferences) {
                            for (Object item : (Collection<?>) value) {
                                addObjectToXml(doc, collectionElement, item, false);
                            }
                        }
                    } else if (isModelClass(value.getClass())) {
                        // Handle model class references (many-to-one relationships)
                        if (includeReferences) {
                            Element refElement = doc.createElement(fieldName);
                            objectElement.appendChild(refElement);
                            addObjectToXml(doc, refElement, value, false);
                        }
                    } else {
                        // Handle primitive types and strings
                        Element fieldElement = doc.createElement(fieldName);
                        fieldElement.setTextContent(value.toString());
                        objectElement.appendChild(fieldElement);
                    }
                }
            }catch (Exception e){
                // Basically, we can't access the field, I know, stupid
                continue;
            }
        }
    }

    /**
     * Get the element name for a class
     *
     * @param clazz The class
     * @return Element name
     */
    private static String getElementNameForClass(Class<?> clazz) {
        // Just the name of the class, not the entire blurt out, like normally
        String className = clazz.getSimpleName();
        // All to lowercase, well, the first letter
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    /**
     * Get the getter method name for a field
     *
     * @param fieldName The field name
     * @param fieldType The field type
     * @return Getter method name
     */
    private static String getGetterMethodName(String fieldName, Class<?> fieldType) {
        String prefix = boolean.class.equals(fieldType) ? "is" : "get";
        return prefix + capitalizeFirstLetter(fieldName);
    }

    /**
     * Capitalize the first letter of a string
     *
     * @param string The string
     * @return String with first letter capitalized
     */
    private static String capitalizeFirstLetter(String string){
        if (string == null || string.isEmpty()){
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Check if a class is one of our model classes
     *
     * @param clazz The class to check
     * @return true if it's a model class
     */
    private static boolean isModelClass(Class<?> clazz){
        return clazz.getPackage() != null && clazz.getPackage().getName().startsWith("org.medirec.medirec.backend.model");
    }
}
