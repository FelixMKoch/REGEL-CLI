import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Run run = new Run(args);

        // Ant Builds

        String c1 = "ant -buildfile build.xml clean";
        String c2 = "ant -buildfile build.xml resnax";

        runAnt(c1);

        runAnt(c2);



        run.runInteractive();

    }


    private static void runAnt(String command) {

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(Paths.get("resnax").toFile());

        builder.command(command);

        try {

            Process process = builder.start();

            process.waitFor();

        } catch(IOException |InterruptedException e) {}

    }

}
