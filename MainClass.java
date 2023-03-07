import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Hashtable;

class Disk
    // extends Thread
{
    static final int NUM_SECTORS = 2048;
    static final int DISK_DELAY = 800;
    StringBuffer sectors[] = new StringBuffer[NUM_SECTORS];

    Disk()
    {
        //allocate all String Buffers when disk is created
        for (int i = 0; i < NUM_SECTORS; i++) {
            sectors[i] = new StringBuffer();
        }
    }


    void write(int sector, StringBuffer data)  // call sleep
    {   
        try {
            Thread.sleep(DISK_DELAY);
            sectors[sector] = data;
        }
        catch (Exception ex) {
            System.out.println("Exception has been" + " caught" + ex);
        }
        

    }


    void read(int sector, StringBuffer data)   // call sleep
    {
        try {
            Thread.sleep(DISK_DELAY);
            OS141.bufferCopy(data , sectors[sector]);
        }
        catch (Exception ex) {
            System.out.println("Exception has been" + " caught" + ex);
        }
        
    }
}

class Printer
    // extends Thread
{
    static final int PRINT_DELAY = 2750;
    String fileName;
    private int id;

    Printer(int id)
    {
        this.id = id;
        fileName = "PRINTER" + id;

    }

    void print(StringBuffer data)  // call sleep
    {
        try {
            Thread.sleep(PRINT_DELAY);

            FileWriter myWriter = new FileWriter(fileName);

            myWriter.write(data.toString());
            myWriter.write("\n");

            myWriter.close();
        }
        catch (Exception ex) {
            System.out.println("Exception has been" + " caught" + ex);
        }
        
    }
}

class PrintJobThread
    extends Thread
{
    StringBuffer line = new StringBuffer(); // only allowed one line to reuse for read from disk and print to printer

    PrintJobThread(String fileToPrint)
    {
        // FIND A PRINTER AND REQUEST PRINTING RIGHTS // IF ALL BUSY GETS BLOCKED

        // REPEATEDLY READ A SECTOR FROM DISK AND SEND TO PRINTER, ONE LINE AT A TIME
    }

    public void run()
    {
        System.out.println("Print Job running...");
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
    private Hashtable<String, FileInfo> fileDirectory = new Hashtable<String, FileInfo>();

    DirectoryManager()
    {
    }

    void enter(StringBuffer fileName, FileInfo file)
    {
        fileDirectory.put(fileName.toString(), file);
    }

    FileInfo lookup(StringBuffer fileName)
    {
        return fileDirectory.get(fileName.toString());
    }
}

class ResourceManager
{
    boolean isFree[];
    
    ResourceManager(int numberOfItems) {
        isFree = new boolean[numberOfItems];
        for (int i = 0; i < isFree.length; i++) {
            isFree[i] = true;
        }
    }

    synchronized int request() {
        while (true) {
            try {
                for (int i = 0; i < isFree.length; i++){
                    if (isFree[i]) {
                        isFree[i] = false;
                        return i;
                    }
                }
            
                this.wait(); // block until someone releases Resource
            }
            catch (Exception ex) {
                System.out.println("Exception has been" + " caught" + ex);
            }
            
        }
    }

    synchronized void release(int index) {
        isFree[index] = true;
        this.notify(); // let a blocked thread run
    }

}

class DiskManager extends ResourceManager
{

    DiskManager(int numberOfItems) {
        super(numberOfItems);
        System.out.println("Constructed Disk Manager.");
    }
}

class PrinterManager extends ResourceManager
{
    PrinterManager(int numberOfItems) {
        super(numberOfItems);
        System.out.println("Constructed Printer Manager");
    }
}

class UserThread
    extends Thread
{
    private String fileName;
    private String line;
    private int id;

    UserThread(int id) // my commands come from an input file with name USERi where i is my user id
    {
        this.id = id;
        fileName = "USER" + id;
    }

    public void run()
    {
        System.out.println("Thread for USER" + String.valueOf(id) + " running...");
        processUserCommands();
    }

    void processUserCommands() {
        // Create and start new PrintJobThread for each print request
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(inputStream));
            
            for (String line; (line = myReader.readLine()) != null;) {
                this.line = line;
                System.out.println(line);
            } 

            inputStream.close();
        }
        catch (Exception ex) {
            System.out.println("Exception has been" + " caught" + ex);
        }
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
        NUM_USERS = -Integer.parseInt(argv[0]);
        NUM_DISKS = -Integer.parseInt(argv[1]);
        NUM_PRINTERS = -Integer.parseInt(argv[2]);
    }

    OS141(String args[]) {
        configure(args);
        System.out.println("OS Users: " + NUM_USERS);
        System.out.println("OS Disks: " + NUM_DISKS);
        System.out.println("OS Printers: " + NUM_PRINTERS);

        users = new UserThread[NUM_USERS];
        disks = new Disk[NUM_DISKS];
        printers = new Printer[NUM_PRINTERS];

        // CREATE OBJECTS FOR DISKS, USERS, AND PRINTERS

        for (int i = 0; i < NUM_USERS; i++) {
            UserThread newUser = new UserThread(i);
            users[i] = newUser;
        }

        for (int i = 0; i < NUM_DISKS; i++) {
            Disk newDisk = new Disk();
            disks[i] = newDisk;
        }

        for (int i = 0; i < NUM_PRINTERS; i++) {
            Printer newPrinter = new Printer(i);
            printers[i] = newPrinter;
        }

        // CREATE OBJECTS FOR DISK AND PRINTER MANAGER

        diskManager = new DiskManager(NUM_DISKS);
        printerManager = new PrinterManager(NUM_PRINTERS);
        
    }

    void startUserThreads() {
        for (int i = 0; i < NUM_USERS; i++) {
            users[i].start();
        }
    }

    void joinUserThreads() {
        for (int i = 0; i < NUM_USERS; i++) {
            try {
                users[i].join();
            } catch (Exception ex) {
                System.out.println("Exception has been" + " caught" + ex);
            }
        }
    }

    static void bufferCopy(StringBuffer dest, StringBuffer src) {
        System.out.println("copy");
        for (int i = 0; i < src.length(); i++) {
            dest.setCharAt(i, src.charAt(i));
        }
    }

    void main() {
        // DO WORK NECCESARY TO INITIATE OS AND RUN.
        startUserThreads();
        joinUserThreads();
        System.out.println("everything went smooth");
    }

}

public class MainClass
{


    public static void main(String args[])
    {
        System.out.println("*** 141 OS Simulation ***");
        OS141 simulation = new OS141(args);
        simulation.main();
    }

    
}
