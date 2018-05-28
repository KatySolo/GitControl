package utils;

import javafx.util.Pair;
import utils.provider.IDataProvider;

public interface ICommand {
     Pair<ICommandPacket, byte[]> execute(IDataProvider dataProvider, byte[] data);
}
