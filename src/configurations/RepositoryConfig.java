package configurations;

import serializator.Serializator;
import utils.provider.IDataProvider;
import utils.version.IVersionSystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class RepositoryConfig implements IConfiguration{

    private String configuredFolder;
    private IVersionSystem versionSystem;
    private double currentVersion = 0.0;

    public RepositoryConfig(String repositoryName, IVersionSystem versionSystem) {
        configuredFolder = repositoryName;
        this.versionSystem = versionSystem;
    }
    public RepositoryConfig(){}
    public RepositoryConfig(String name) {
        configuredFolder = name;
    }
    @Override
    public void write(String pathBegin) throws IDataProvider.ProviderException {
        String path = pathBegin + String.format("/%s.conf", configuredFolder);
        Properties confFile = Serializator.classToProperties(this);
        if (confFile != null)
            try {
                confFile.storeToXML(new FileOutputStream(path), null);
            } catch (IOException exception) {
                throw new IDataProvider.ProviderException("RepConfig file was not wrote: " + exception.getMessage());
            }
        else throw new IDataProvider.ProviderException("Could not created config file");
    }

    @Override
    public IConfiguration read(String pathBegin) throws IDataProvider.ProviderException {
        String path = pathBegin + String.format("/%s.conf", configuredFolder);
        Properties confFile = new Properties();
        try {
            confFile.loadFromXML(new FileInputStream(path));
            return (IConfiguration) Serializator.classFromProperties(confFile);
        } catch (IOException exception) {
            throw new IDataProvider.ProviderException("Could not load config file: " + exception.getMessage());
        }
    }

    public double getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(double newVersion) {
        currentVersion = newVersion;
    }
    public IVersionSystem getVersionSystem(){
        return versionSystem;
    }
}
