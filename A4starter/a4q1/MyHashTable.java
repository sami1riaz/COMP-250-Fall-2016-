package a4q1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class MyHashTable<K,V> implements Iterable<MyHashTable<K,V>.HashEntry>{
  /*
   *   Number of entries in the HashTable. 
   */
  private int entryCount = 0;
  
  /*
   * Number of buckets. The constructor sets this variable to its initial value,
   * which eventually can get changed by invoking the rehash() method.
   */
  private int numBuckets;
  
  /**
   * Threshold load factor for rehashing.
   */
  private final double MAX_LOAD_FACTOR = 0.75;
  
  /**
   *  buckets to store the key-value pairs.   Traditionally an array is used for the buckets
   *  and a linked allEntries is use for the entries within each bucket.   
   *  
   *  We use an ArrayallEntries rather than array, since the former is simpler to use in Java.   
   */
  
  ArrayList<LinkedList<HashEntry>>  buckets;
  
  /* 
   * Constructor.
   * 
   * numBuckets is the initial number of buckets used by this hash table
   */
  
  MyHashTable(int numBuckets) {
    
    //  ADD YOUR CODE HERE
    
    //The contructor specifies the initial capacity of the HashMap
    // (Can also specify the max loadfactor) // from notes
    
    this.numBuckets = numBuckets;
    this.buckets = new ArrayList<LinkedList<HashEntry>>(numBuckets);
    
    for(int i = 0; i < numBuckets; i++){ 
      this.buckets.add(new LinkedList<HashEntry>());                   // Create the table
    }
    
    
  }
  
  /**
   * Given a key, return the bucket position for the key. 
   */
  private int hashFunction(K key) {
    
    return  Math.abs( key.hashCode() ) % numBuckets ;
  }
  
  /**
   * Checking if the hash table is empty.  
   */
  public boolean isEmpty()
  {
    if (entryCount == 0)
      return true;
    else
      return(false);
  }
  
  /**
   *   return the number of entries in the hash table.
   */
  public int size()
  {
    return(entryCount);
  }
  
  /**
   * Adds a key-value pair to the hash table. If the load factor goes above the 
   * MAX_LOAD_FACTOR, then call the rehash() method after inserting. 
   * 
   *  If there was a previous value for the given key in this hashtable, then return it.
   *  Otherwise return null.   
   */
  
  public  V  put(K key, V value) {
    
    //  ADD YOUR CODE HERE
    
    HashEntry tempKey = getEntry(key);                       // entered key
    
    HashEntry entry = new HashEntry(key,value);              //entry at entered key
    
    int bucketPos = hashFunction(key);                       // bucketposition of entered key
    
    if(tempKey != null){                                     // if key exists
      V tempValue = tempKey.getValue();                      // get the value at that key
      int index = buckets.get(bucketPos).indexOf(tempKey);
      buckets.get(bucketPos).set(index,entry);
      return tempValue;
    }
    else{
      buckets.get(bucketPos).add(entry); 
      entryCount++;                                          // increment entryCount
      
      double newloadFactor = (double)(size())/(this.numBuckets);
      if(newloadFactor >= this.MAX_LOAD_FACTOR){             // if newloadfactor exceeds Max loadfactor after 'putting'
        rehash();                                            // rehash
      }
      return null;
    }
  }
  
  
  
  /**
   * Retrieves a value associated with some given key in the hash table.
   Returns null if the key could not be found in the hash table)
   */
  public V get(K key) {
    
    //  ADD YOUR CODE HERE
    
    HashEntry entry = this.getEntry(key); 
    
    if (entry == null) {                        // if key entered does not have a value
      return null;                              // return null
    }
    else {
      return entry.getValue();                  // otherwise return the value
    } 
  }
  
  /**
   * Removes a key-value pair from the hash table.
   * Return value associated with the provided key.   If the key is not found, return null.
   */
  public V remove(K key) {
    //  ADD YOUR CODE HERE
    
    HashEntry entry = this.getEntry(key);
    
    if (entry != null) {
      HashIterator iter = new HashIterator();
      iter.allEntries = buckets.get(hashFunction(key));
      
      HashEntry currentEntry = iter.next();
      while (currentEntry != null) {
        if (currentEntry.getKey() == key) { // if you find key that matches input key
          iter.remove(); // remove 
          return currentEntry.getValue();      
        }
        else {
          currentEntry = iter.next(); 
        }
      }
    }
    return null;
    
  }
  
  
  
  /*
   *  This method is used for testing rehash().  Normally one would not provide such a method. 
   */
  public int getNumBuckets(){
    return numBuckets;
  }
  
  /*
   * Returns an iterator for the hash table. 
   */
  
  //@Override
  public MyHashTable<K, V>.HashIterator iterator(){
    return new HashIterator();
  }
  
  /**
   * Removes all the entries from the hash table, but keeps the number of buckets intact.
   */
  public void clear()
  {
    for (int ct = 0; ct < buckets.size(); ct++){
      buckets.get(ct).clear();
    }
    entryCount=0;  
  }
  
  /**
   *   Create a new hash table that has twice the number of buckets.
   */
  
  public void rehash()
  {
    //   ADD YOUR CODE HERE
    
    int newTableSize = this.numBuckets * 2;                                 // increase size by *2
    
    MyHashTable<K,V> newHashTable = new MyHashTable<K,V>(newTableSize);
    
    V temp;                                                                 // temporary value
    
    for(int i =0; i < this.numBuckets; i++){
      LinkedList<HashEntry> bucketList  = buckets.get(i);                   // store entries in bucketList so that you can re-enter them
      
      for(HashEntry entry : bucketList){                                    // for all the entries in the bucketList
        if(entry.getKey() != null){                                         // if key exists
          temp = get(entry.getKey());                                       // return value at key
          if(temp!= null){                                                  // if value at key is not null
            newHashTable.put(entry.getKey(), temp);                         // put it in the new table at its specific key
          }
        }
      } 
    }
    this.buckets = newHashTable.buckets;
    this.numBuckets = newHashTable.numBuckets;
    this.entryCount = newHashTable.entryCount;
  }      
  
  /*
   * Checks if the hash table contains the given key.
   * Return true if the hash table has the specified key, and false otherwise.
   */
  public boolean containsKey(K key)
  {
    return (get(key) != null);
  }
  
  /*
   * return an ArrayList of the keys in the hashtable
   */
  
  public ArrayList<K>  keys()
  {
    
    ArrayList<K>  listKeys = new ArrayList<K>();
    
    //   ADD YOUR CODE HERE
    
    HashIterator iter = new HashIterator();
    
    while(iter.hasNext()) {
      HashEntry entry = iter.next();
      listKeys.add(entry.getKey());       
    }
    
    
    
    return listKeys;
    
  }
  
  /*
   * return an ArrayList of the values in the hashtable
   */
  public ArrayList <V> values()
  {
    ArrayList<V>  listValues = new ArrayList<V>();
    
    //   ADD YOUR CODE HERE
    
    HashIterator iter = new HashIterator();
    while (iter.hasNext()) {
      HashEntry entry = iter.next();
      listValues.add(entry.getValue());       
    }
    
    return listValues;
  }
  
  @Override
  public String toString() {
    /*
     * Implemented method. You do not need to modify.
     */
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buckets.size(); i++) {
      sb.append("Bucket ");
      sb.append(i);
      sb.append(" has ");
      sb.append(buckets.get(i).size());
      sb.append(" entries.\n");
    }
    sb.append("There are ");
    sb.append(entryCount);
    sb.append(" entries in the hash table altogether.");
    return sb.toString();
  }
  
  /*
   *    Inner class:   Iterator for the Hash Table.
   */
  public class HashIterator implements  Iterator<HashEntry>
  {
    LinkedList<HashEntry>  allEntries;
    
    /**
     * Constructor:   make a linkedlist 'allEntries' of all the entries in the hash table
     */
    public  HashIterator()
    {
      
      //  ADD YOUR CODE HERE
     
      allEntries = new LinkedList<HashEntry>();
      for(int i = 0 ; i < buckets.size(); i++){
        for(int j = 0; j < buckets.get(i).size(); j++){
          if(buckets.get(i).get(j) != null){
            HashEntry entry = buckets.get(i).get(j);
            allEntries.add(entry);
          }
        }
      }
      
      
    }
    
    //  Override
    @Override
    public boolean hasNext()
    {
      return !allEntries.isEmpty();
    }
    
    //  Override
    @Override
    public HashEntry next()
    { 
      return allEntries.removeFirst();
    }
    
    @Override
    public void remove() {
      
      // not implemented,  but must be declared because it is in the Iterator interface
      
    }  
  }
  
  //  helper method
  
  private HashEntry getEntry(K key){
    int index = hashFunction(key);
    LinkedList<HashEntry> bucketList = buckets.get(index);
    for (HashEntry node : bucketList ){
      if (node.getKey() == key)
        return node; 
    }
    return null;   
  }
  
  class HashEntry {
    
    private K key;
    private V value;
    
    /*
     * Constructor.
     */
    HashEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
    
    /*
     * Returns this hash entry's key.   Assume entry is not null.
     * @return This hash entry's key
     */
    K getKey() {
      return(key);
    }
    
    /**
     * Returns this hash entry's value.  Assume entry is not null.
     */
    V getValue() {
      return(value);
    }
    
    /**
     * Sets this hash entry's value.
     */
    void setValue(V value) {
      this.value =  value;  
    }  
  }
  
  
}
