import java.util.*;
import java.io.*;
/**
 * <h3>Title:</h3> CS3810 Assignment 4 External Hashing
 * @author [Prepared by the instructor]
 * <h1>Record class</h1>
 *	<p2>This class defines a record which is specifically for storing in
 *   a Java random access file. See API of RandomAccessFile class in Java.</p2>

 * <h2>Attributes:</h2>
 *	<p3>name - String, the max length is 16 chars
 *	id - String, the number of digits in id is 4.</p3>
 */
public class Record 
{
	private String name;
	private String id;
	private int NAMELENGTH=16;
	private int IDLENGTH=4;
	
	/**
	 * default constructor
	 */
	public Record()
	{
		name=" ";
		id=" ";
	}
	
	/**
	 * 
	 * @param newName : String input for name.
	 * @param newId	  : String input for id.
	 * <h5>Description:</h5>
	 * Read a 'name' and the related 'id' from the current position of
	 * the given file to the instance variables 'name' and 'id'
	 */
	public Record(String newName, String newId)
	{
		name= newName;
		id= newId;
	}
	
	/**
	 * 
	 * @param file : RandomAccessFile pass through
	 * @throws IOException : If not able to read the pass through Random Access File
	 * <h5>Description:</h5>
	 * Write the value of 'this' object to the given file
	 */
	public void read(RandomAccessFile file) throws IOException
	{
		name= readString(file, NAMELENGTH);
		id = readString(file, IDLENGTH);
	}
	
	/**
	 * 
	 * @param file : RandomAccessFile pass through
	 * @throws IOException : If not able to read the pass through Random Access File
	 * <h5>Description:</h5>
	 * Write the value of 'this' object to the given file
	 */
	public void write(RandomAccessFile file) throws IOException
	{
		try {
			writeStr(file, name, NAMELENGTH);
			writeStr(file, id, IDLENGTH);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @return String id
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * 
	 * @return String name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 * @param file : Pass Through Random Access File
	 * @param strLength : length of the String as Integer
	 * @return String theStr
	 * @throws IOException : If failed to read the file
	 * <h5>Description:</h5>
	 * Help method: read a string with length of strLength from the file
	 * Return the string to the caller
	 */
	private String readString(RandomAccessFile file, int strLength) throws IOException
	{
		char[] chs = new char[strLength];
		
		for (int i = 0; i < chs.length; i++)
		{
			try 
			{
				chs[i] = file.readChar();
			} 
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
		}
		
		String theStr = new String(chs);
		return theStr;
	}
	
	/**
	 * 
	 * @param file : Pass Through Random Access File
	 * @param str : String 
	 * @param strLength : String Length
	 * @throws IOException : If unable to write. 
	 * <h5>Description:</h5>
	 * Help method: write a string with length of strLength to the file
	 */
	private void writeStr(RandomAccessFile file, String str, int strLength) throws IOException
	{
		StringBuffer buffer = null;
		
		if(str != null)
			buffer = new StringBuffer(str);
		else
			buffer = new StringBuffer(strLength);
		
		buffer.setLength (strLength);
		file.writeChars(buffer.toString());
	}
	
	/**
	 * @return String name & id
	 */
	public String toString()
	{
		return "\nName: " + name + "\nID: " + id;
	}
	
	
}
