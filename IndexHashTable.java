import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.*;
/**
 * @author Cyril Thomas
 * @since 2014-05-02
 * <h3>CS3810 Assignment 4 External Hashing</h3>
 * <h1>Class: IndexHashTabe</h1>
 * <p>This Class implements an Index Hash Table and performs Insert, Search & Delete function</p>
 */
public class IndexHashTable 
{
	private Integer[] hashArray;
	private int arraySize;
	private int maxRecord; 
	
	/**
	 * 
	 * @param newSize : int newSize which is the arraySize.
	 * @param newMaxRecord : int newMaxRecord which is the maxRecord.
	 * <h5>Description:</h5>
	 * This Constructor creates the hash Array table which is then filled with number in order till n-1.
	 * Then the Array is converted to List and then Collection Shuffle method is called on the hashArrayList.
	 * After that the List is converted back to the array. 
	 */
	public IndexHashTable(int newSize, int newMaxRecord)
	{
		arraySize = newSize;
		maxRecord = newMaxRecord;
		
		hashArray = new Integer[arraySize];
		
		for (int i = 0; i < arraySize; i++)
		{
			hashArray[i] = i;
		}
		
		List<Integer> hashArrayList = Arrays.asList(hashArray); //Converting Array into List.
		Collections.shuffle(hashArrayList); //Shuffling the contents inside the List.
		hashArray = hashArrayList.toArray(new Integer[hashArrayList.size()]); //Converting the List back to Array. 
		
		//for(int i = 0; i<hashArray.length;i++) //Debug
			//System.out.println("\nDebug Table" + i + "\t" + hashArray[i]); //Debug
	}
	
	/**
	 * 
	 * @param key : Accepts any integer value
	 * @return key mod arraySize.
	 * <h5>Description:</h5>
	 * This method is an hash function.
	 */
	public int hashFunc(int key)
	{
		return key % arraySize;
	}
	
	/**
	 * 
	 * @param file : Random Access File which Record.toString is written using the Record.write function. 
	 * @param rec : Record to be inserted
	 * @return 0 if successfully written; -1 if the table is full; 1 if there is Redundancy.  
	 * @throws FileNotFoundException
	 * <h5>Description:</h5>
	 * For a given record the id is parsed into an integer. Then the Hash Function is Applied. 
	 * The value from the hash Function is used as an index of the Array. Then from the value of
	 * the hash Array[index] is used to calculate the start address by multiply it with maxRecord
	 * and 40 which is the total size of an record. Then a Collision test is run for to compare if the
	 * value already exist or another value is occupying the space or if the file block is empty by 
	 * looking for the value "DDDD". If a "DDDD" value is found the Object rec.toString is written to the
	 * file and 0 is returned. If the block already has a different value other than the current Record rec
	 * then the Start Address is increased by 40 each time until an empty space is found. If an empty block 
	 * cannot be found then -1 is returned. If there is duplicate values then 1 is returned for redundancy. 
	 */
	public int insert (RandomAccessFile file, Record rec) throws FileNotFoundException
	{
		int orgKey = Integer.parseInt(rec.getId()); //Parsing string to int
		
		int hashValue = hashFunc(orgKey); //Applying Hash function.
		
		int startOffset = hashArray[hashValue] * maxRecord * 40; //Calculating hash function
		
		for(int i = 0; i < maxRecord; i++)
		{
			try 
			{
				Record tempCompareRec = new Record(); //Creating a temporary record. 
				
				//System.out.println("\nDebug Pointer: "+startOffset); //Debug
				
				file.seek(startOffset); //setting the file pointer. 
				tempCompareRec.read(file); //Having the temp record to read the file 
			
				//System.out.println("Temp ID: "+tempCompareRec.getId()); //Debug
				
				if(tempCompareRec.getId().equals("DDDD")) //checking for empty block
				{
					file.seek(startOffset); //Reseting the file pointer back to current start address
					rec.write(file); //New Record writing to the file at the current pointer (start address)
					return 0;
				}
				
				else if (tempCompareRec.getId().equals(rec.getId())) //checking for redundancy. 
					return 1;
				
				else if (!(tempCompareRec.getId().equals(rec.getId()))) // checking for existing different Records.
					startOffset += 40; //increasing the start address by 40. 
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return -1; //If the for loop runs out without returning any values. Then it is assumed that the block is full. 		
	}
	
	/**
	 * 
	 * @param file : Random Access File to search existing record. 
	 * @param newId : String key value to look for.
	 * @return String "Found" with the existing record.toString or "Not Found." 
	 * <h5>Description:</h5>
	 * Same as the insert method except when the block is found. The id value inside the block is used to compare
	 * to the key if they match then the existing record.toString with the message found is returned. If not matching
	 * the message not found is returned. 
	 */
	public String search(RandomAccessFile file, String newId)
	{
		int key = Integer.parseInt(newId);
		int hashValue = hashFunc(key);
		int startOffset = hashArray[hashValue] * maxRecord * 40;
		
		Record tempCompareRec = new Record();
		
		for(int i = 0; i < maxRecord; i++)
		{
			try 
			{
				//System.out.println("Pointer: "+startOffset); //Debug
				
				file.seek(startOffset);
				
				tempCompareRec.read(file);
				
				if(tempCompareRec.getId().equals(newId)) //Comparing Record temp with the key. 
				{
					return "\nRecord Found!!!" + tempCompareRec.toString();
				}
				else 
					startOffset += 40;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return "Not Found";
	}
	
	/**
	 * 
	 * @param file : Random Access File to which existing values are to be overwritten by "DDD..".
	 * @param newId : String Key value to look for. 
	 * @return the deleted record.toString or "Not Found".
	 * <h5>Description:</h5>
	 * Same as the search method except when the matching values are found. Then the block is over written by
	 * "DDDD..." if not found the "Not found" message is returned to the user.
	 * To note: a block with DDD values indicate that the block is empty. 
	 */
	public String delete(RandomAccessFile file, String newId)
	{
		int key = Integer.parseInt(newId);
		int hashValue = hashFunc(key);
		int startOffset = hashArray[hashValue] * maxRecord * 40;
		
		Record tempCompareRec = new Record();
		Record tempWrite = new Record("DDDDDDDDDDDDDDDD","DDDD"); //Creating a temp Record with "DDD.."
		
		for(int i = 0; i < maxRecord; i++)
		{
			try 
			{
				System.out.println("Pointer: "+startOffset);
				file.seek(startOffset);
				tempCompareRec.read(file);
				
				
				if(tempCompareRec.getId().equals(newId)) //Comparing the temp id with key. 
				{
					file.seek(startOffset);
					tempWrite.write(file); //writing the temp record. 
					
					return "\nRecord Found!!!" + tempCompareRec.toString();
				}
				else 
					startOffset += 40;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return "Not Found";
	}
	
}
