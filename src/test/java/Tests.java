import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Tests {


    @Test(timeOut = 3600000, testName = "copyFileTest", alwaysRun = true)
    @Parameters({"srcFile","destDir"})
    public void copyFileTest(String srcFile,String destDir){
        MetadataDeploy.copyFile(srcFile,destDir);
        String[] srcFilePath = srcFile.split("/");
        File file = new File(destDir+"/"+srcFilePath[srcFilePath.length-1]);
        Assert.assertTrue(file.exists());
    }

    @Test(timeOut = 3600000, testName = "commandRunnerTest", alwaysRun = true)
    @Parameters({"command"})
    public void commandRunnerTest(String command){
        CommandRunner.runCommand(command.split(" "));

    }

    @AfterSuite
    public void cleanUp(){
        try {
            MetadataDeploy.deleteDirectoryStream(Path.of("src/main/resources/b/testFile.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
