/**
 * 
 */
package objects;

import java.io.File;
import java.util.ArrayList;

import com.sun.jmx.snmp.tasks.ThreadService;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * @author ���
 *
 */
public class FilesUpdater implements Runnable{
	
	private Server s;
	private boolean switch_on;
	private ArrayList<Long> combLastModified;
	private ArrayList<File> combFileList;
	private ArrayList<String> wigleFolderPath;
	private ArrayList<String> wigleFilePathList;
	
	public FilesUpdater(Server s)
	{
		this.s=s;
		this.switch_on=false;
		this.combFileList=s.getCombFilesList();
		this.wigleFolderPath=s.getWigleFolderPath();
		for(int i=0;i<this.combFileList.size();i++)
		{
			combLastModified.add(this.combFileList.get(i).lastModified());
		}
		for(int i=0;i<this.wigleFolderPath.size();i++)
		{
			File[]tmp=new File(this.wigleFolderPath.get(i)).listFiles();
			for(int j=0;j<tmp.length;j++)
			{
				wigleFilePathList.add(tmp[j].getPath());
			}
		}
	}
	private void treatNewFile()
	{
		for(int i=0;i<this.wigleFolderPath.size();i++)
		{
			File tmp=new File(this.wigleFolderPath.get(i));
			File[] tmpArr=tmp.listFiles();
			DB db;
			for(int j=0;j<tmpArr.length;j++)
			{
				if(!wigleFilePathList.contains(tmpArr[j].getPath()))
				{
					this.wigleFilePathList.add(tmpArr[j].getPath());
					db=new DB(tmpArr[j]);
					s.addDB(db);
				}
			}
		}
	}
	
	private void treatUpdateFile()
	{
		boolean flag=false;
		for(int i=0;i<this.combFileList.size();i++)
		{
			if(this.combFileList.get(i).lastModified()!=this.combLastModified.get(i))
			{
				flag=true;
			}
		}
		if(flag)
			reloadDBS();
	}
	
	private void reloadDBS() 
	{
		this.s.getDbs().clearAll();
		for(int i=0;i<this.wigleFolderPath.size();i++)
		{
			DB tmp=new DB(this.wigleFolderPath.get(i),1);
			s.addDB(tmp);
		}
		for(int i=0;i<this.combFileList.size();i++)
		{
			DB tmp=new DB(this.combFileList.get(i).getPath(),2);
			s.addDB(tmp);
		}
		
	}
	public void run()
	{
		while(switch_on)
		{
			treatNewFile();
			treatUpdateFile();
			try {
				Thread.sleep(5000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	

}