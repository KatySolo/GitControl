package commands;
import javafx.util.Pair;
import utils.ICommand;
import utils.ICommandPacket;
import utils.provider.IDataProvider;

public class Revert implements ICommand{

    public Revert(ICommandPacket packet){

    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        return null;
    }
}
