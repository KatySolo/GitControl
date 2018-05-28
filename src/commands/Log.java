package commands;

import javafx.util.Pair;
import utils.ICommand;
import utils.ICommandPacket;
import utils.provider.IDataProvider;

public class Log implements ICommand {

    public Log(ICommandPacket packet){

    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        return null;
    }
}
