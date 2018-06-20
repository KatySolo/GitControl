package commands;

import javafx.util.Pair;
import packets.ErrorPacket;
import utils.ICommand;
import packets.ICommandPacket;
import utils.provider.IDataProvider;

public class Error implements ICommand {
    private ErrorPacket packet;

    public Error(ICommandPacket packet){
        if (packet instanceof ErrorPacket) {
            this.packet = (ErrorPacket)packet;
        }
    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        return new Pair<>(new ErrorPacket(10, packet.getErrorInfo()), null);
    }
}
