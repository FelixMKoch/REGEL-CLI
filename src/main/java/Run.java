import resnax.Main;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Run {

    public String[] args;

    private String model = "so";        // Changes currently made only to the Run classes of the so model. Only this one works.

    private Integer topRes = 10;

    private Path dir = Paths.get("");

    private Path exampleDir = Paths.get("exp/interactive/customize/example/test/");

    private Path exampleCachePath = Paths.get("exp/interactive/customize/examples_cache/");

    private Path logPath = Paths.get("exp/interactive/customize/logs/test");

    private Path benchmarkPath = Paths.get("exp/customize/benchmark");

    private Path semprePath = Paths.get("sempre");

    private String tmp = "_tmp";

    public Run(String[] args) {
        this.args = args;
    }

    /**
     * Build all the folders if they don't already exist
     */
    public void buildFolders() {

        if(!exampleDir.toFile().exists()) exampleDir.toFile().mkdirs();

        if(!exampleCachePath.toFile().exists()) exampleCachePath.toFile().mkdirs();

        if(!logPath.toFile().exists()) logPath.toFile().mkdirs();

        if(!benchmarkPath.toFile().exists()) benchmarkPath.toFile().mkdirs();

    }


    private void writeBenchmark(String fileName, String nl, List<String> examples) {

        File benchmarkFile = Paths.get(benchmarkPath.toString(), fileName).toFile();

        try (FileWriter writer = new FileWriter(benchmarkFile)) {

            writer.write("// natural language\n " + nl + "\n\n");

            writer.append("//example");

            for(String example : examples)
                writer.append("\n" + example);


            writer.append("\n\n// gt\nna");

        } catch (IOException e) {}

    }



    private void cpBenchmarkToInteractive(String fileName) {

        Path src = Paths.get(benchmarkPath.toString(), fileName);

        Path dest = Paths.get(exampleDir.toString(), fileName);

        try {
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {}

    }


    private void runExec(String command) {

        ProcessBuilder builder = new ProcessBuilder(command.split(" "));

        System.setProperty("user.dir", Paths.get("sempre").toAbsolutePath().toString());

        builder.directory(Paths.get("sempre").toAbsolutePath().toFile());

        try {

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while((line = reader.readLine()) != null)
                System.out.println("Line read: " + line);


        } catch(IOException e) {
            e.printStackTrace();
        }

        System.setProperty("user.dir", dir.toString());

    }



    private List<String> parseDescriptions(String nl, String model, int top) {

        String command = "python3 py_scripts/test.py --dataset " + tmp + " --model_dir pretrained_models/pretrained_" + model + " --topk " + top;

        List<String> desps = new ArrayList<>();

        List<String> sketches = new ArrayList<>();

        desps.add(nl);

        File tmpFile = Paths.get("sempre", "dataset", tmp + ".raw.txt").toFile();

        try {

            tmpFile.createNewFile();

            FileWriter writer = new FileWriter(tmpFile);

            writer.write("#\tNL\tsketch \n");

            for(int i = 0; i < desps.size(); i++) {
                writer.append(i + "\t" + desps.get(i) + "\t null\n");
            }

            System.setProperty("user.dir", semprePath.toAbsolutePath().toString());

            Path sketchDir = Paths.get("outputs", tmp);

            if(!sketchDir.toFile().exists()) sketchDir.toFile().mkdirs();

            runExec(command);

            System.setProperty("user.dir", dir.toString());

            var files = Paths.get("sempre", "outputs", tmp).toFile().listFiles();

            for(int i = 0; i < files.length; i++) {

                File file = files[i];

                if(file.isDirectory()) continue;

                try (BufferedReader reder = new BufferedReader(new FileReader(file))) {

                    String line;

                    while((line = reder.readLine()) != null) {
                        sketches.add(line.split(" ") [1]);
                    }
                }
            }


        } catch(IOException e){
        }

        return sketches;
    }


    private List<String> testBenchmark(String fileName, String naturalLanguage, List<String> sketches, List<String> examples) {

        List<String> ret = new ArrayList<>();

        System.out.println("Natural language is: " + naturalLanguage);

        System.out.println("Your Examples: ");

        examples.forEach(System.out::println);

        System.out.println("Running the syntheziser ...");

        int count = 1;

        for(String sketch : sketches) {

            String[] args = {
                    "0",
                    Paths.get(exampleDir.toString(), fileName).toString(),
                    logPath.toString(),
                    sketch,
                    Integer.toString(count ++),
                    "1",
                    "0"
            };

            String res = Main.runCustom(args);  // Important: This is the Main class of 'resnax' with custom function for this case


            ret.add(res);

        }

        return ret;

    }


    public void runInteractive() {

        List<String> ret = new ArrayList<>();

        buildFolders();

        Scanner scanner = new Scanner(System.in);

        while(true) {

            List<String> examples = new ArrayList<>();

            System.out.println("___________________");
            System.out.println("Enter the name of the file. Press the Enter key to save");

            String fileName = scanner.nextLine();

            if(fileName == null || fileName.isBlank()) break;

            System.out.println("Enter the NL. Press Enter key to save");

            String naturalLanguage = scanner.nextLine();

            if(naturalLanguage == null || naturalLanguage.isBlank()) break;

            naturalLanguage = naturalLanguage.stripTrailing();

            /*
                Collecting examples from the User input
            */
            while(true) {
                while (true) {

                    System.out.println("Please type in an example. If you want to skip, press Enter.");
                    String example = scanner.nextLine();

                    if (example == null || example.isBlank()) break;

                    example = example.stripTrailing();

                    String sign = "";

                    do {

                        System.out.println("Is it a positive (+) or negative (-) example?");

                        sign = scanner.nextLine();

                        examples.add("\n" + example + "," + sign);

                    } while (!("+".equals(sign) || "-".equals(sign)));

                }

                System.out.println("Generating sketches ...");

                List<String> sketches = parseDescriptions(naturalLanguage, model, 25);

                System.out.println("Sketches:");

                sketches.forEach(System.out::println);

                writeBenchmark(fileName, naturalLanguage, examples);

                cpBenchmarkToInteractive(fileName);

                ret.addAll(testBenchmark(fileName, naturalLanguage, sketches, examples));

                for (int i = 0; i < Math.min(topRes, ret.size()); i++) {

                    System.out.println("[" + i + "]: " + ret.get(i));

                }

                System.out.println("Is there a right RegEx (y/n)?");

                String right = scanner.nextLine();

                if (!"y".equals(right)) {

                    System.out.println("Here are all of the outputs: ");

                    ret.forEach(System.out::println);

                }

                else break; // Only break if the right RegEx was found
            }
        }

    }

}
