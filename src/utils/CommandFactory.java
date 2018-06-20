package utils;

import commands.*;
import commands.Error;
import packets.ICommandPacket;

public class CommandFactory {

    public static ICommand create(ICommandPacket packet) {
        switch (packet.getClass().getName()) {
            case "packets.AddPacket":
                return new Add(packet);
            case "packets.ClonePacket":
                return new Clone(packet);
            case "packets.UpdatePacket":
                return new Update(packet);
            case "packets.CommitPacket":
                return new Commit(packet);
            case "packets.RevertPacket":
                return new Revert(packet);
            case "packets.LogPacket":
                return new Log(packet);
            default:
                return new Error(packet);
        }
    }

}
