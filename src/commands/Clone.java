package commands;
import connection.ConnectionManager;
import configurations.UserConfig;
import javafx.util.Pair;
import packets.ClonePacket;
import packets.ErrorPacket;
import packets.ICommandPacket;
import utils.ICommand;
import utils.LogWriter;
import utils.provider.IDataProvider;

import java.io.File;

public class Clone implements ICommand{

    private ClonePacket packet;

    public Clone(ICommandPacket packet){
        if (packet instanceof ClonePacket) {
            this.packet = (ClonePacket) packet;
        }
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {

        if (packet == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        try {
            File[] files = dataProvider.getActualVersion(packet.getRepositoryName());
            byte[] archive = ConnectionManager.makeArchive(files);

            UserConfig userConfig = UserConfig.getInstance();
            userConfig.addNewUsers(packet.getUserName(), packet.getRepositoryName());

            LogWriter logger = new LogWriter();
            logger.register(packet.getRepositoryName());
            logger.write(packet.getUserName(),"add");
            return new Pair<>(new ClonePacket(packet.getUserName(), packet.getPath(),
                    packet.getRepositoryName(), packet.getFlag(), archive.length), archive);
        } catch (Exception exception) {
            return new Pair<>(new ErrorPacket(
                    3, "Cannot execute command clone" + exception.getLocalizedMessage()), null);
        }
    }
}
