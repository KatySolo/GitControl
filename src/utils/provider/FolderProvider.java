package utils.provider;

import utils.version.IVersionSystem;

import java.io.File;

public class FolderProvider implements IDataProvider {
    @Override
    public void createRepository(String repositoryName, IVersionSystem versionSystem) throws ProviderException {

    }

    @Override
    public void addNewVersion(String repName, byte[] files, String[] actualFiles) throws ProviderException {

    }

    @Override
    public File[] getActualVersion(String repName) throws ProviderException {
        return new File[0];
    }

    @Override
    public File[] getNeededVersion(String repName, String versionName) throws ProviderException {
        return new File[0];
    }
}
