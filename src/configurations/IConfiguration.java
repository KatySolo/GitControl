package configurations;

import utils.provider.IDataProvider;

public interface IConfiguration {
    void write (String path) throws IDataProvider.ProviderException;
    IConfiguration read(String path) throws  IDataProvider.ProviderException;
}
