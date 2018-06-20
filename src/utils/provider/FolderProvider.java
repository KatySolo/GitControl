package utils.provider;

import configurations.RepositoryConfig;
import configurations.VersionConfig;
import connection.ConnectionManager;
import utils.version.IVersionSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class FolderProvider implements IDataProvider {
    @Override
    public void createRepository(String repositoryName, IVersionSystem versionSystem) throws ProviderException {
        String path = "/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repositoryName;
        File repository = new File(path);
        if (repository.mkdir()) {
            try {
                RepositoryConfig repConfig = new RepositoryConfig(repositoryName, versionSystem);
                repConfig.write(path );
            } catch (ProviderException exception) {
                repository.delete();
                throw new ProviderException(exception.getMessage());
            }
        } else throw new ProviderException("Could not created repository");

    }

    @Override
    public void addNewVersion(String repName, byte[] files, String[] actualFiles) throws ProviderException {
        String pathToRepConfig = "/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName;
        RepositoryConfig repConfig = (RepositoryConfig) new RepositoryConfig(repName).read(pathToRepConfig);
        double nextVersion = repConfig.getVersionSystem().getNextVersion(repConfig.getCurrentVersion());

        String pathToVersion = "/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName + "/" + nextVersion;
        File newVersion = new File(pathToVersion);
        newVersion.delete();

        if (newVersion.mkdir()) {
            try {
                Map<String, byte[]> filesData = ConnectionManager.readArchive(files);
                for (Map.Entry<String, byte[]> file : filesData.entrySet()) {
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(pathToVersion + "/" + file.getKey());
                    fileOutputStream.write(file.getValue());
                    fileOutputStream.close();
                }

                repConfig.setCurrentVersion(nextVersion);
                VersionConfig verConfig = new VersionConfig(actualFiles);

                pathToVersion = pathToVersion + "/version.conf";
                verConfig.write(pathToVersion);
                repConfig.write(pathToRepConfig);
            } catch (IOException | NullPointerException exception) {
                new File(pathToVersion).delete();
                throw new ProviderException("Could not add some file: " + exception.getMessage());
            }
        } else throw new ProviderException("Could not created new version");
    }

    @Override
    public File[] getActualVersion(String repName) throws ProviderException {
        String pathToRep = "/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName;

        RepositoryConfig repConfig = (RepositoryConfig) new RepositoryConfig(repName).read(pathToRep);
        double currentVersion = repConfig.getCurrentVersion();

        if (currentVersion != 0.0) {
            String pathToVerConfig = pathToRep + "/" + currentVersion + "/version.conf";
            VersionConfig verConfig = (VersionConfig) new VersionConfig().read(pathToVerConfig);
            String[] activeFiles = verConfig.getActiveFiles();

            ArrayList<File> result = new ArrayList<>();
            File[] listOfVersion = getListOfVersion(repName);

            for (int i = listOfVersion.length - 1; i >= 0; i--) {
                addNeededFile(result, repName, listOfVersion[i].getName(), activeFiles);
                if (activeFiles.length == result.size()) break;
            }
            return result.toArray(new File[result.size()]);
        } else return new File[0];
    }

    @Override
    public File[] getNeededVersion(String repName, String versionName) throws ProviderException {
        String pathToRep = "/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName;

        String pathToVerConfig = pathToRep + "/" + versionName + "/version.conf";
        VersionConfig verConfig = (VersionConfig) new VersionConfig().read(pathToVerConfig);
        String[] actualFiles = verConfig.getActiveFiles();

        ArrayList<File> result = new ArrayList<>();
        File[] listOfVersion = getListOfVersion(repName);

        for (int i = listOfVersion.length - 1; i >= 0; i--) {
            if (Double.parseDouble(listOfVersion[i].getName()) <= Double.parseDouble(versionName))
                addNeededFile(result, repName, listOfVersion[i].getName(), actualFiles);
            if (actualFiles.length == result.size()) break;
        }

        return result.toArray(new File[result.size()]);
    }

    private File[] getListOfVersion(String repName) throws ProviderException {
        File repository = new File("/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName);
        ArrayList<File> result = new ArrayList<>();
        if (repository.isDirectory()) {
            File[] listOfVersion = repository.listFiles();
            if (listOfVersion != null) {
                for (File file : listOfVersion) {
                    if (file.isDirectory()) result.add(file);
                }
                return result.toArray(new File[result.size()]);
            } else return new File[0];
        } else throw new ProviderException("It's not repository");
    }

    private void addNeededFile(ArrayList<File> result, String repName, String versionName, String[] actualFiles) throws ProviderException {
        File[] files;
        File versionDirectory = new File("/Users/KatySolo/IdeaProjects/Server/src/repositories/" + repName + "/" + versionName);
        if (versionDirectory.isDirectory())
            files = versionDirectory.listFiles();
        else throw new ProviderException(String.format("There is no such repository: %s", repName));
        if (files != null)
            for (File file : files) {
                boolean found = false;
                for (String fileName : actualFiles)
                    if (Objects.equals(fileName, file.getName())) {
                        found = true;
                        break;
                    }
                if (found) {
                    boolean exist = false;
                    for (File resultFiles : result)
                        if (Objects.equals(resultFiles.getName(), file.getName())) {
                            exist = true;
                            break;
                        }
                    if (!exist) result.add(file);
                }
            }
        else throw new ProviderException("Version is empty");
    }
}
