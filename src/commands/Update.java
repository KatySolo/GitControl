package commands;
import configurations.UserConfig;
import javafx.util.Pair;
import packets.ErrorPacket;
import packets.RevertPacket;
import packets.UpdatePacket;
import connection.ConnectionManager;
import utils.ICommand;
import packets.ICommandPacket;
import utils.LogWriter;
import utils.provider.IDataProvider;

import java.io.File;

public class Update implements ICommand {

    private UpdatePacket packet;

    public Update(ICommandPacket packet) {
        if (packet instanceof UpdatePacket) {
            this.packet = (UpdatePacket) packet;
        }
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        if (packet == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(packet.getUserName());
        if (userConfig.getUserRepository(packet.getUserName()) != null) {
            try {
                File[] files = dataProvider.getActualVersion(userRep);
                byte[] fileData = ConnectionManager.makeArchive(files);
                LogWriter logger = new LogWriter();
                logger.register(userRep);
                logger.write(packet.getUserName(), "add");
//                ILogger logger = new Logger(userRep);
//                logger.writeNewLog(updatePacket.getUserName() + ": add");
                return new Pair<>(new UpdatePacket(packet.getUserName(), fileData.length), fileData);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket
                        (6, "Cannot execute command 'Update': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
