package utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class LogWriter implements ILog{

    private String observableFolder;

    @Override
    public void register(String repoName) {
        if (observableFolder == null) {
            observableFolder = repoName;
        }
    }

    @Override
    public void write(String user, String command) {
        String logPath =String.format( "/Users/KatySolo/IdeaProjects/Server/src/repositories/%1$s/%1$s.log",observableFolder);
        File log = new File(logPath);
        try {
            FileWriter fileWriter = new FileWriter(log, true);
            Date dateNow = new Date(System.currentTimeMillis());
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy --- hh:mm:ss ----");
            fileWriter.write(formatForDateNow.format(dateNow) + " " + command +" ");
            fileWriter.write(user + "\n");
            fileWriter.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getLog() throws IOException {
        String logPath =String.format( "/Users/KatySolo/IdeaProjects/Server/src/repositories/%1$s/%1$s.log",observableFolder);
        File log = new File(logPath);
        FileReader fileReader = new FileReader(log);
        StringBuilder result = new StringBuilder();
        int c;
        while ((c = fileReader.read()) != -1) result.append((char) c);
        return result.toString();
    }
    public void writeNewLog(String command, String name) {
        String logPath =String.format( "/Users/KatySolo/IdeaProjects/Server/src/repositories/%1$s/%1$s.log",observableFolder);
        File log = new File(logPath);
        try {
            FileWriter fileWriter = new FileWriter(log, true);
            Date dateNow = new Date(System.currentTimeMillis());
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
            fileWriter.write(formatForDateNow.format(dateNow) + " " + command + "\n");
            fileWriter.write(name);
            fileWriter.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
