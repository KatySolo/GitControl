package commands;
import configurations.UserConfig;
import javafx.util.Pair;
import packets.ClonePacket;
import packets.CommitPacket;
import packets.ErrorPacket;
import utils.ICommand;
import packets.ICommandPacket;
import utils.LogWriter;
import utils.provider.IDataProvider;

public class Commit implements ICommand{

    private CommitPacket packet;

    public Commit(ICommandPacket packet){
        if (packet instanceof CommitPacket) {
            this.packet = (CommitPacket) packet;
        }
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {

        if (packet == null) return new Pair<>(new ErrorPacket(1, "BED. Received a bad package"), null);
        UserConfig userConfig = UserConfig.getInstance();
        String userRep = userConfig.getUserRepository(packet.getUserName());
        if (userConfig.getUserRepository(packet.getUserName()) != null) {
            try {
                if (packet.getActualFiles() != null) {
                    dataProvider.addNewVersion(userRep, data, packet.getActualFiles());
                    LogWriter logger = new LogWriter();
                    logger.register(userRep);
                    logger.write(packet.getUserName(),"commit");
                    return new Pair<>(new CommitPacket(packet.getUserName(), 0, packet.getActualFiles()), null);
                } else return new Pair<>(new ErrorPacket(12, "No actual files in commit packet"), null);
            } catch (Exception exception) {
                return new Pair<>(new ErrorPacket(
                        13, "Cannot execute command 'Commit': " + exception.getLocalizedMessage()), null);
            }
        } else return new Pair<>(new ErrorPacket(5, "Make command clone at first"), null);
    }
}
