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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MetadataDeploy {
    private final String MIN_VERSION_FILE_NAME="PG_RetXCore__RetX_Mobile_App_Settings.MIN_VERSION.md";
    private final String MAX_VERSION_FILE_NAME="PG_RetXCore__RetX_Mobile_App_Settings.MAX_VERSION.md";

    public  void setFirstFoundTagValue(File xmlFile, String tagName, String desiredValue) {
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

    public  String getFirstFoundTagValue(File xmlFile, String tagName) {
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
    public  void retrieveMetadata(String buildToolsPath){
        String retrieve ="ant.bat -Dorg=autosit retrieve";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(buildToolsPath));

        CommandRunner.runCommand(processBuilder,retrieve.split(" "));
    }
    public  String createDeployDir(String dirPath){
        String metadataDirPath = dirPath+"/temp/deploy_delta/customMetadata";
        File customMetadataDir = new File(dirPath+"/temp/deploy_delta/customMetadata");
        customMetadataDir.mkdirs();
        return metadataDirPath;

    }

    public  void commitRepo(String repoLocation,String commitMessage){
        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.directory(new File(repoLocation));
        CommandRunner.runCommand(Arrays.asList("git","commit","-m",commitMessage),processBuilder);

    }

    public  void cloneRepo(String repo, String destination){
        CommandRunner.runCommand("git","clone",repo,destination);
    }

    public  void cloneRepo(String repo, String destination,String username,String password){
        //https://username:password@gitlab.pg.com/SalesforceCOE/Tooling/CICD/salesforce-coe-ci-cd-jenkins-libraries
        repo = String.format(repo,username,password);
        CommandRunner.runCommand("git","clone",repo,destination);
    }

    public static void  copyFile(String srcFile, String destDir){
        String[] splitted = srcFile.split("/");
        String fileName = splitted[splitted.length-1];
        try {
            Files.copy(Paths.get(srcFile),Paths.get(destDir+"/"+fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }



    public void setMinVersion(String desiredVersion, String repoDir,String metadaXMLfilesPath){

        String deployDeltaCustomMetadataPath=repoDir+"/temp/deploy_delta/customMetadata";
        String deployDeltaPath = repoDir+"/temp/deploy_delta/";
        String ant = System.getProperty("os.name").startsWith("Windows") ? "ant.bat" : "ant";
        List<String> commands = new ArrayList<>();
        List<String> command = Arrays.asList("-Dorg=autosit","-Dsf.testLevel=NoTestRun","delta.package.deploy");
        commands.add(ant);
        commands.addAll(command);
        createDeployDir(repoDir);
        copyFile(metadaXMLfilesPath+MIN_VERSION_FILE_NAME,deployDeltaCustomMetadataPath);
        copyFile(metadaXMLfilesPath+"package.xml",deployDeltaPath);

        File minVersionXml = new File(deployDeltaCustomMetadataPath+"/"+MIN_VERSION_FILE_NAME);
        setFirstFoundTagValue(minVersionXml,"value",desiredVersion);

        CommandRunner.runCommand((new ProcessBuilder()).directory(new File(repoDir+"/buildTools")),commands);

        try {
            deleteDirectoryStream(Path.of(deployDeltaCustomMetadataPath));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public void deployDestructiveChanges(String repoDir,String destructiveChangesXMLpath) {
        String deployDeltaPath = repoDir+"/temp/deploy_delta/";
        copyFile(destructiveChangesXMLpath, repoDir + "/temp/deploy_delta");
        CommandRunner.runCommand((new ProcessBuilder()).directory(new File(repoDir + "/buildTools")), "ant.bat", "-Dorg=autosit", "-Dsf.testLevel=NoTestRun", "delta.package.deploy");

        try {
            deleteDirectoryStream(Path.of(deployDeltaPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMinVersion(String desiredVersion){
        //get files from repo
//        String repo = "https://username:password@gitlab.pg.com/SalesforceCOE/Tooling/CICD/salesforce-coe-ci-cd-jenkins-librariesw";
//        String currentMinVersion;
        //zmenić w build credentiale na ładowane skądś
//        File metadataDir = new File("src/resources/metadata");
//        metadataDir.mkdir();
        String repoDir=new File("src/resources/metadata").getAbsolutePath();
        String deployDeltaCustomMetadataPath=repoDir+"/temp/deploy_delta/customMetadata";
        String deployDeltaPath = repoDir+"/temp/deploy_delta/";
        String ant = System.getProperty("os.name").startsWith("Windows") ? "ant.bat" : "ant";

        List<String> commands = new ArrayList<>();
        List<String> command = Arrays.asList("-Dorg=autosit","-Dsf.testLevel=NoTestRun","delta.package.deploy");
        commands.add(ant);
        commands.addAll(command);

        //destructive changes nie może być deploy_delta -> usunąć deploy_delta po deployu
//        cloneRepo(repo,repoDir,"username","password");
        createDeployDir(repoDir);
        copyFile(repoDir+"/metadataXML/"+MIN_VERSION_FILE_NAME,deployDeltaCustomMetadataPath);
        copyFile(repoDir+"/metadataXML/package.xml",deployDeltaPath);
        File minVersionXml = new File(deployDeltaCustomMetadataPath+"/"+MIN_VERSION_FILE_NAME);
        setFirstFoundTagValue(minVersionXml,"value",desiredVersion);

         CommandRunner.runCommand((new ProcessBuilder()).directory(new File(repoDir+"/buildTools")),commands);

        try {
            deleteDirectoryStream(Path.of(deployDeltaCustomMetadataPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        copyFile(repoDir+"/metadataXML/destructiveChangesPost.xml",repoDir+"/temp/deploy_delta");
//        CommandRunner.runCommand((new ProcessBuilder()).directory(new File(repoDir+"/buildTools")),"ant.bat","-Dorg=autosit","-Dsf.testLevel=NoTestRun","delta.package.deploy");
//
//        try {
//            deleteDirectoryStream(Path.of(deployDeltaPath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void setMaxVersion(String desiredVersion){
        //get files from repo
        String repo = "https://username:password@gitlab.pg.com/SalesforceCOE/Tooling/CICD/salesforce-coe-ci-cd-jenkins-librariesw";
        String currentMinVersion;
        File metadataDir = new File("src/resources/metadata");
        metadataDir.mkdir();
        String repoDir=new File("src/resources/metadata").getAbsolutePath();
        String deployDeltaCustomMetadataPath=repoDir+"/temp/deploy_delta/customMetadata";
        File xmlFile = new File(deployDeltaCustomMetadataPath+"/"+MAX_VERSION_FILE_NAME);

        cloneRepo(repo,repoDir,"username","password");
        createDeployDir(repoDir);
        retrieveMetadata(repoDir+"/buildTools");
        copyFile(repoDir+"/temp/retrieve/customMetadata/"+MAX_VERSION_FILE_NAME,deployDeltaCustomMetadataPath);
        currentMinVersion = getFirstFoundTagValue(xmlFile,"value");
        setFirstFoundTagValue(xmlFile,"value",desiredVersion);
        // CommandRunner.runCommand((new ProcessBuilder()).directory(new File(repoDir+"/buildTools")),"ant","-Dorg=autosit","-Dsf.testLevel=NoTestRun","delta.package.deploy");
        //
    }
}
