package smat.jftpd;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

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
	}

	@Test
	public void testHandle_cdup() {
		setUpAuth();
		
		String line;
		StringTokenizer st;
		
		int reply = -1;
		String startStr = "";
		String endStr = "";
		
		try {
			line = "PWD";
			st = new StringTokenizer(line);
			st.nextToken();
			byteArrayOutputStream.reset();
			
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
			
			
		} catch (CommandException e) {
			e.printStackTrace();
			fail("Command failed: " + e.getMessage());
		} catch (Exception e) {
			fail("Uncaught exception: " + e.getMessage());
		}
		
		assertEquals(200, reply);
		assertEquals(startStr, endStr);
	}

	@Test
	public void testHandle_port() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testHandle_stru() {
		fail("Not yet implemented"); // TODO
	}

	
	private void setUpAuth() {
		String line;
		StringTokenizer st;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
