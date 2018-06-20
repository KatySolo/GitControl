package utils;

import java.io.IOException;

public interface ILog {
    void register(String repoName);
    void write(String user, String command);
    String getLog () throws IOException;

}
