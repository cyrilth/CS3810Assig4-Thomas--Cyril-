import java.io.IOException;
import java.util.*;
import java.io.*;
/**
 * @author Cyril Thomas
 * @since 2014-05-02
 * <h3>CS3810 Assignment 4 External Hashing</h3> 
 * <h1>Class: ExternalHashing </h1>
 * <p>Main App Class to Create RandomAccessFile and test IndexHashTable Class</p>
 */
public class ExternalHashing {

	public static void main(String[] args) throws IOException 
	{
		Scanner scan = new Scanner(System.in);
		int inputBlock = 0, inputRecord = 0;
		RandomAccessFile f = null;
		
		/**
		 * Create Random Access File known as "f"
		 */
		try 
		{
			f = new RandomAccessFile("newfile.txt", "rw");
		
		} 
		catch (Exception e) 
		{
			System.out.println("Cannot write\n " + e);
		}
		
		
		/**
		 * Ask User input for Block Size and Record 
		 * both are Integer Input. 
		 */
		try
		{	
			System.out.print("Enter the Block Size: ");
			inputBlock = scan.nextInt();
			System.out.print("\nEnter the # of Records: ");
			inputRecord = scan.nextInt();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			scan.nextLine();
		}
		
		/**
		 * Initiating the empty file with "DDD.." using for loop
		 * with control as inputBlock*inputRecord. 
		 */
		try 
		{
			Record tempFill = new Record("DDDDDDDDDDDDDDDD","DDDD");
			
			for (int i =0 ; i<=(inputBlock*inputRecord); i++)
				tempFill.write(f);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		/**
		 * Initiating IndexHashTable data using inputBlock and inputRecord
		 */
		IndexHashTable data = new IndexHashTable(inputBlock, inputRecord);
		
		/**
		 * Using switch Case
		 * (0)Quit
		 * (1)Insert
		 * (2)Search
		 * (3)Delete
		 */
		try 
		{
			do 
			{
				System.out.println("\nEnter: 1 to Insert ");
				System.out.println("Enter: 2 to Search ");
				System.out.println("Enter: 3 to Delete ");
				System.out.println("Enter: 0 to Quit ");
				System.out.print("Enter: ");
				int input = scan.nextInt();
				
				switch (input) 
				{
					case 1:
					{	
						scan.nextLine();
						System.out.print("\nEnter Student's Name: ");
						String name = scan.nextLine();
						System.out.print("\nEnter Student's ID: ");
						String id = scan.nextLine();
						
						Record student = new Record(name, id);
						
						int success = data.insert(f, student);
						
						if(success == 0)
							System.out.println("\nWrite Successful");
						else if (success == -1)
							System.out.println("The Block is Full");
						else if (success == 1)
							System.out.println("The Student Record already Exists!!!");
						
						break;
					}
					
					case 2:
					{
						scan.nextLine();
						
						System.out.print("\nEnter Student ID to search: ");
						String key = scan.nextLine();
						
						String message = data.search(f, key);
						
						System.out.println(message);
						
						break;
					}
					
					case 3:
					{
						scan.nextLine();
						System.out.print("\nEnter Student ID to delete: ");
						String key = scan.nextLine();
						String message = data.delete(f, key);
						System.out.println(message);
						break;
					}
					
					default:
					{
						System.exit(0);
						break;
					}
				}
				
			} while (true);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			f.close();
		}

	}

}
