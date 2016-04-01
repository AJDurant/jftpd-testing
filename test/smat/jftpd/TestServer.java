package smat.jftpd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rheise.jftpd.Server;

public class TestServer {

    private static String serverAddr = "127.0.0.1";
    private static int serverPort = 2345;
    private static Process jftpd;

    interface ftpAction {
        boolean action(FTPClient ftp);
    }

    private static ftpAction[] ftpActions;

    // @BeforeClass
    // public static void setUpBeforeClass() throws Exception {
    // setUpCommands();
    // setUpServer();
    // try {
    // TimeUnit.SECONDS.sleep(1);
    // } catch (InterruptedException e) {
    // // Handle exception
    // }
    // }
    //
    // @AfterClass
    // public static void tearDownAfterClass() throws Exception {
    // jftpd.destroy();
    // }

    private FTPClient ftp;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        ftp = new FTPClient();
        try {
            // ftp.setConnectTimeout(5000);
            ftp.connect(serverAddr, serverPort);
            ftp.login("user", "pass");
            ftp.enterLocalActiveMode();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("FTP connection failed.");
        }
    }

    @After
    public void tearDown() {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException ioe) {
                // do nothing
            }
        }
    }

    @Test
    public void testConnection() {
        try {
            boolean testNoop = ftp.sendNoOp();
            assertTrue(testNoop);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("FTP command failed.");
        }
    }

    @Test
    public void testCommandCDUP() {
        try {
            assertEquals("/", ftp.printWorkingDirectory());
            ftp.cwd("test");
            assertEquals("/test", ftp.printWorkingDirectory());
            ftp.cdup();
            assertEquals("/", ftp.printWorkingDirectory());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testFileStore() {
        String pathname = "stor.temp";
        boolean reply = false;

        File f;

        InputStream input;
        InputStream file;

        String inputString = "Test Input data";
        byte[] inputBytes = inputString.getBytes();

        String fileString = "";
        byte[] fileBytes = new byte[32];
        int fileLength = 0;

        input = new ByteArrayInputStream(inputBytes);
        try {
            reply = ftp.storeFile(pathname, input);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertTrue("FTP Store command failed", reply);

        try {
            f = new File(pathname);
            file = new FileInputStream(f);
            fileLength = file.read(fileBytes);
            fileString = new String(fileBytes, 0, fileLength);

            file.close();
            f.delete();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(inputString, fileString);
    }

    public void fuzzTest() {
        while (true) {
            boolean actionResult = runRandomFTPAction(ftp);
            if (!actionResult) {
                fail("Action failed");
                break;
            }
        }
    }

    private boolean runRandomFTPAction(FTPClient ftp2) {
        int action = random.nextInt(ftpActions.length);
        return ftpActions[action].action(ftp2);
    }

    private static void setUpCommands() {
        ftpActions = new ftpAction[] { new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpCWD(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpCDUP(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpPORT(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpTYPE(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpSTRU(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpMODE(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpRETR(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpSTOR(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpDELE(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpRMD(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpMKD(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpPWD(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpLIST(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpNLST(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpSYST(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpNOOP(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpSIZE(ftp2);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp2) {
                return ftpMDTM(ftp2);
            }
        }, };
    }

    protected static boolean ftpCWD(FTPClient ftp2) {

        return false;
    }

    protected static boolean ftpCDUP(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpPORT(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpTYPE(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpSTRU(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpMODE(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpRETR(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpSTOR(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpDELE(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpRMD(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpMKD(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpPWD(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpLIST(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpNLST(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpSYST(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpNOOP(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpSIZE(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    protected static boolean ftpMDTM(FTPClient ftp2) {
        // TODO Auto-generated method stub
        return false;
    }

    private static void setUpServer() {
        ProcessBuilder pb = new ProcessBuilder();

        String fullClassName = Server.class.getName();
        String pathToClassFiles = new File("./classes").getPath();
        pb.command("java", "-cp", pathToClassFiles, fullClassName, Integer.toString(serverPort));

        try {
            jftpd = pb.start();
        } catch (IOException ex) {
            Logger.getLogger(fullClassName).log(Level.SEVERE, null, ex);
        }
    }
}
