package commands;
import javafx.util.Pair;
import utils.ICommand;
import utils.ICommandPacket;
import utils.provider.IDataProvider;

public class Update implements ICommand{

    public Update(ICommandPacket packet) {

    }

    @Override
    public Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data) {
        return null;
    }
}
