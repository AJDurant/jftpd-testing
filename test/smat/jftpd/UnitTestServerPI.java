package smat.jftpd;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rheise.jftpd.CommandException;
import rheise.jftpd.ServerPI;

public class UnitTestServerPI {

    private static Socket clientMock;
    private static ByteArrayOutputStream byteArrayOutputStream;
    private static ByteArrayInputStream byteArrayInputStream;

    private ServerPI serverPI;
    private String line;
    private StringTokenizer st;
    private int reply;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        clientMock = mock(Socket.class);
        byteArrayOutputStream = new ByteArrayOutputStream();
        when(clientMock.getOutputStream()).thenReturn(byteArrayOutputStream);

        byteArrayInputStream = new ByteArrayInputStream(new byte[32]);
        when(clientMock.getInputStream()).thenReturn(byteArrayInputStream);
    }

    @Before
    public void setUp() throws Exception {
        serverPI = new ServerPI(clientMock);
        setUpAuth();
        byteArrayOutputStream.reset();

        reply = -1;
    }

    @Test
    public void testHandle_cdup() {

        String startStr = "";
        String endStr = "";

        try {
            line = "PWD";
            st = new StringTokenizer(line);
            st.nextToken();

            serverPI.handle_pwd(line, st);
            startStr = byteArrayOutputStream.toString();

            line = "CWD test";
            st = new StringTokenizer(line);
            st.nextToken();

            serverPI.handle_cwd(line, st);

            line = "CDUP";
            st = new StringTokenizer(line);
            st.nextToken();

            reply = serverPI.handle_cdup(line, st);

            line = "PWD";
            st = new StringTokenizer(line);
            st.nextToken();
            byteArrayOutputStream.reset();

            serverPI.handle_pwd(line, st);
            endStr = byteArrayOutputStream.toString();

        } catch (CommandException ce) {
            reply = ce.getCode();
            endStr = ce.getText();
        } catch (Exception e) {
            fail("Uncaught exception: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }

        // Expect 200 Command okay. OR 250 Requested file action okay,
        // completed.
        assertThat(reply, anyOf(is(200), is(250)));
        // Expect PWD to match the start.
        assertEquals(startStr, endStr);
    }

    @Test
    public void testHandle_port() {

        try {
            line = "PORT a,b,c,d,e,f";
            st = new StringTokenizer(line);
            st.nextToken();

            reply = serverPI.handle_port(line, st);
        } catch (CommandException ce) {
            reply = ce.getCode();
        } catch (Exception e) {
            fail("Uncaught exception: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }

        // Expect 501 Syntax error in parameters or arguments.
        assertEquals(501, reply);
    }

    @Test
    public void testHandle_stru() {

        try {
            line = "STRU R";
            st = new StringTokenizer(line);
            st.nextToken();

            reply = serverPI.handle_stru(line, st);
        } catch (CommandException ce) {
            reply = ce.getCode();
        } catch (Exception e) {
            fail("Uncaught exception: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }

        // Expect 504 Command not implemented for that parameter.
        assertEquals(504, reply);
    }

    @Test
    public void testHandle_mdtm() {
        String pathname = "mdtm.temp";
        long time = 1459347459000L;
        String replyStr = "";

        createTempFile(pathname, time);

        try {
            line = "MDTM " + pathname;
            st = new StringTokenizer(line);
            st.nextToken();

            reply = serverPI.handle_mdtm(line, st);
            replyStr = byteArrayOutputStream.toString();
        } catch (CommandException ce) {
            reply = ce.getCode();
            replyStr = byteArrayOutputStream.toString();
        } catch (Exception e) {
            fail("Uncaught exception: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
        removeTempFile(pathname);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = dateFormat.format(new Date(time));

        assertEquals(213, reply);
        assertEquals("213 " + dateStr + "\n", replyStr);
    }

    private void setUpAuth() {
        try {
            line = "USER test";
            st = new StringTokenizer(line);
            st.nextToken();
            serverPI.handle_user(line, st);

            line = "PASS test";
            st = new StringTokenizer(line);
            st.nextToken();
            serverPI.handle_pass(line, st);
        } catch (CommandException e) {
            e.printStackTrace();
        }
        // Clear any output received
        byteArrayOutputStream.reset();
    }

    private void createTempFile(String pathname, long time) {
        File f = new File(pathname);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        f.setLastModified(time);
    }

    private void removeTempFile(String pathname) {
        File f = new File(pathname);
        f.delete();
    }
}
