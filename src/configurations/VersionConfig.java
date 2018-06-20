package configurations;

import serializator.Serializator;
import utils.provider.IDataProvider;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class VersionConfig implements IConfiguration {

    String[] activeFiles;
    public VersionConfig(String[] actualFiles) {
        this.activeFiles = actualFiles;
    }
    public VersionConfig(){}

    @Override
    public void write(String path) throws IDataProvider.ProviderException {
        Properties confFile = Serializator.classToProperties(this);
        if (confFile != null)
            try {
                confFile.storeToXML(new FileOutputStream(path), null);
            } catch (IOException exception) {
                throw new IDataProvider.ProviderException("Repository config file was not wrote: " + exception.getMessage());
            }
        else throw new IDataProvider.ProviderException("Could not created config file");

    }

    @Override
    public IConfiguration read(String path) throws IDataProvider.ProviderException {
        Properties confFile = new Properties();
        try {
            confFile.loadFromXML(new FileInputStream(path));
            return (IConfiguration) Serializator.classFromProperties(confFile);
        } catch (IOException exception) {
            throw new IDataProvider.ProviderException("Could not load config file: " + exception.getMessage());
        }
    }

    public String[] getActiveFiles() {
        return activeFiles;
    }
}
