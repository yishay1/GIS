package test;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Libraries.read;


public class testRead {

	@Test
	public void readFolderTest() {
		File f=new File("C:\\");
		assertEquals(0, read.readFolder(f.listFiles()).size());
	}
}
