import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CommandRunner {
    public static void runCommand(String... commands) {
        try {
            System.out.println("===============");
            StringBuilder sb = new StringBuilder();
            for (String command : commands)
                sb.append(command + " ");
            System.out.println("Running command: " + sb);
            ProcessBuilder builder = new ProcessBuilder();
            builder.inheritIO().command(commands);
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitCode = process.waitFor();
            System.out.println("Command " + sb + " finished");
            System.out.println("Exit code:" + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCommand( ProcessBuilder processBuilder,String... commands) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String command : commands)
                sb.append(command + " ");
            System.out.println("===============");
            System.out.println("Running command: " + sb);
            ProcessBuilder builder = processBuilder;
            builder.inheritIO().command(commands);
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Command " + sb + " finished");
            System.out.println("Exit code:" + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCommand( ProcessBuilder processBuilder,List<String> commands) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String command : commands)
                sb.append(command + " ");
            System.out.println("===============");
            System.out.println("Running command: " + sb);
            ProcessBuilder builder = processBuilder;
            builder.inheritIO().command(commands);
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Command " + sb + " finished");
            System.out.println("Exit code:" + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCommand(List<String> commands) {
        try {
            System.out.println("===============");
            StringBuilder sb = new StringBuilder();
            for (String command : commands)
                sb.append(command + " ");
            System.out.println("Running command: " + sb);
            ProcessBuilder builder = new ProcessBuilder();
            builder.inheritIO().command(commands);
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitCode = process.waitFor();
            System.out.println("Command " + sb + " finished");
            System.out.println("Exit code:" + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runCommand(List<String> commands, ProcessBuilder processBuilder) {
        try {
            StringBuilder sb = new StringBuilder();
            for (String command : commands)
                sb.append(command + " ");
            System.out.println("===============");
            System.out.println("Running command: " + sb);
            ProcessBuilder builder = processBuilder;
            builder.inheritIO().command(commands);
            Process process = builder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitCode = process.waitFor();
            System.out.println("Command " + sb + " finished");
            System.out.println("Exit code:" + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void changeFirstFoundTagValue(File xmlFile, String tagName, String desiredValue) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(xmlFile);
            Node node = document.getElementsByTagName(tagName).item(0);
            node.setTextContent(desiredValue);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String getFirstFoundTagValue(File xmlFile, String tagName) {
        String value = null;
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(xmlFile);
            Node node = document.getElementsByTagName(tagName).item(0);
            value = node.getTextContent();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return value;
    }

}
