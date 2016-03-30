package smat.jftpd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rheise.jftpd.Server;

public class TestServer {

    private static String serverAddr = "127.0.0.1";
    private static int serverPort = 23450;
    private static Process jftpd;

    interface ftpAction {
        boolean action(FTPClient ftp);
    }

    private static ftpAction[] ftpActions;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        setUpCommands();
        setUpServer();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // Handle exception
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jftpd.destroy();
    }

    private FTPClient ftp;
    private Random random;

    @Before
    public void setUp() {
        random = new Random();
        ftp = new FTPClient();
        try {
            ftp.setConnectTimeout(5000);
            ftp.connect(serverAddr, serverPort);
            ftp.login("user", "pass");
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
            public boolean action(FTPClient ftp) {
                return ftpCWD(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpCDUP(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpPORT(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpTYPE(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpSTRU(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpMODE(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpRETR(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpSTOR(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpDELE(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpRMD(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpMKD(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpPWD(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpLIST(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpNLST(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpSYST(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpNOOP(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpSIZE(ftp);
            }
        }, new ftpAction() {
            public boolean action(FTPClient ftp) {
                return ftpMDTM(ftp);
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
