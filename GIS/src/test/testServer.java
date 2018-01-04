package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import GUI.gisGui;
import objects.DB;
import objects.Filter;
import objects.Server;

public class testServer {
	gisGui window;
	Server s=new Server(window);
	File f=new File("C:\\Users\\���\\Desktop\\test\\test.csv");
	File f2=new File("C:\\Users\\���\\Desktop\\test\\test2.csv");
	DB db=new DB(f);
	DB db2=new DB(f2);
	String[] arr=new String[6];
	String[] arr2=new String[6];
	 
	@Test
	public void testAddDB() {
		s.addDB(db);
		arr[0]="0";
    	arr[1]="1";
    	arr[2]="ys";
    	s.filter(arr);
    	arr2[0]="1";
    	arr2[1]="1";
    	arr2[2]="d";
    	s.filter(arr2);
    	s.addDB(db2);
    	System.out.println( s.getDbs().peek().getScansList());
    	assertEquals(4, s.getDbs().peek().getScansList().size());
	}
	
	@Test
	public void testAddCombFile() //test==true, the exeption is only in the test(it's OK) 
	{
		s.addCombFile(f);
		s.addCombFile(f2);
		assertEquals(2, s.getFu().getFileLastModifiedList().size());
	}
	
	
}
