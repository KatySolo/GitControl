package commands;
import javafx.util.Pair;
import packets.AddPacket;
import packets.ErrorPacket;
import utils.ICommand;
import packets.ICommandPacket;
import utils.LogWriter;
import utils.version.CommonVersion;
import utils.provider.IDataProvider;


public class Add implements ICommand{

    private AddPacket packet;

    public Add(ICommandPacket packet){
        if (packet instanceof AddPacket)
            this.packet = (AddPacket) packet;
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (packet == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        try {
            dataProvider.createRepository(packet.getRepositoryName(),new CommonVersion());
            LogWriter logger = new LogWriter();
            logger.register(packet.getRepositoryName());
            logger.write(packet.getUserName(),"add");
            return new Pair<>(new AddPacket(packet.getUserName(), packet.getRepositoryName()), null);
        } catch (IDataProvider.ProviderException e) {
            return new Pair<>(new ErrorPacket(2, "BED. Cannot create a repository" + e.getLocalizedMessage()),null);
        }
    }
}
