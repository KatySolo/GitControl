package commands;

import configurations.UserConfig;
import javafx.util.Pair;
import packets.ErrorPacket;
import packets.LogPacket;
import utils.ICommand;
import packets.ICommandPacket;
import utils.LogWriter;
import utils.provider.IDataProvider;

public class Log implements ICommand {
    private LogPacket packet;

    public Log(ICommandPacket packet){
        if (packet instanceof LogPacket) {
            this.packet = (LogPacket) packet;
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
                logger.write(packet.getUserName(),"log");
//                ILogger logger = new Logger(userRep);
//                logger.writeNewLog(logPacket.getUserName() + ": log");
                String log = logger.getLog();
                return new Pair<>(new LogPacket(packet.getUserName(), log), null);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket(
                        11, "Cannot execute command 'Clone': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
