package utils;

public class Packet implements ICommandPacket {

    String command = "";
    String arg1 = "";
    String arg2="";
    String arg3 = "";


//    private Serializator serializator = new Serializator();

    public Packet(byte[] data){
        // raw bytes array
       decode(data);
        // and so what
    }

    public ICommand decode(byte[] input) {
        return null;
    }


    public byte[] encode(ICommand command) {
        return new byte[0];
    }
}
// add repoName
//clone path repoName flags
//update
//commit
//revert version flag
//log
//TODO make Packet init working

/*
For ADD command
command: add
arg1: name_fpr_new_repo

for CLONE command
command: clone
arg1: path
arg2: repoNmae
arg3: flags

for UPDATE command
command: update

for COMMIT command:
command: commit

for REVERT command:
command: revert
arg1: version
arg2: flags

for LOG command:
command: log
 */