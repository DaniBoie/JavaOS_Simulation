class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 2048;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];
    Disk()
    {
    }
    void write(int sector, StringBuffer data)  // call sleep
    {
    }
    void read(int sector, StringBuffer data)   // call sleep
    {
    }
}

class Printer
    // extends Thread
{
    Printer(int id)
    {
    }

    void print(StringBuffer data)  // call sleep
    {
    }
}

class PrintJobThread
    extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer

    PrintJobThread(String fileToPrint)
    {
    }

    public void run()
    {
    }
}

class FileInfo
{
    int diskNumber;
    int startingSector;
    int fileLength;
}

class DirectoryManager
{
    // private Hashtable<String, FileInfo> T = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
    }

    FileInfo lookup(StringBuffer fileName)
    {
        return null;
    }
}

class ResourceManager
{
}

class DiskManager
{
}

class PrinterManager
{
}

class UserThread
    extends Thread
{
    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
    }

    public void run()
    {
    }
}

class OS141 {
    int NUM_USERS = 0, NUM_DISKS = 0, NUM_PRINTERS = 0;
    String userFileNames[];
    UserThread users[];
    Disk disks[];
    Printer printers[];
    DiskManager diskManager;
    PrinterManager printerManager;

    void configure(String argv[]) {
        NUM_USERS = Integer.parseInt(argv[0]);
        NUM_DISKS = Integer.parseInt(argv[1]);
        NUM_PRINTERS = Integer.parseInt(argv[2]);
    }

    OS141(String args[]) {
        configure(args);
        System.out.println(NUM_USERS);
        System.out.println(NUM_DISKS);
        System.out.println(NUM_PRINTERS);
    }
}


public class MainClass
{
    public static void main(String args[])
    {
        for (int i=0; i<args.length; ++i)
            System.out.println("Args[" + i + "] = " + args[i]);
            
        System.out.println("*** 141 OS Simulation ***");
    }
}
