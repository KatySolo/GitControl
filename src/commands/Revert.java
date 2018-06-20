package commands;
import configurations.UserConfig;
import javafx.util.Pair;
import packets.ErrorPacket;
import packets.RevertPacket;
import connection.ConnectionManager;
import utils.ICommand;
import packets.ICommandPacket;
import utils.LogWriter;
import utils.provider.IDataProvider;

import java.io.File;

public class Revert implements ICommand{
    private RevertPacket packet;

    public Revert(ICommandPacket packet){
        if (packet instanceof RevertPacket) {
            this.packet = (RevertPacket) packet;
        }
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {

        if (packet == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(packet.getUserName());
        if (userConfig.getUserRepository(packet.getUserName()) != null) {
            try {
                LogWriter logger = new LogWriter();
                logger.register(userRep);
//                logger.write(packet.getUserName(),"add");

//                ILogger logger = new Logger(userRep);
                if (packet.getVersion() != null) {
                    File[] files = dataProvider.getNeededVersion(userRep, packet.getVersion());
                    byte[] fileData = ConnectionManager.makeArchive(files);
                    logger.writeNewLog(packet.getUserName() + ": revert " + packet.getVersion(),packet.getUserName());
                    return new Pair<>(new RevertPacket(packet.getUserName(), packet.getVersion(),
                            packet.getFlag(), fileData.length), fileData);
                } else {
                    File[] files = dataProvider.getActualVersion(userRep);
                    byte[] fileData = ConnectionManager.makeArchive(files);
                    logger.writeNewLog(packet.getUserName() + ": revert", packet.getUserName());
                    return new Pair<>(new RevertPacket(packet.getUserName(), packet.getVersion(),
                            "-hard", fileData.length), fileData);
                }
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket
                        (16, "Cannot execute command 'Revert': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
