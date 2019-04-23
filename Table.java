import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

class Table {

  private Record newrecord;
  private Number number;
  private ArrayList<Record> table;
  private HashMap<String, Number> numberType;
  private String table_name;
  private int num_records;
  private int num_columns;
  private Control control;
  private Inputoutput io = new Inputoutput();
  private FileHandler file = new FileHandler();


  public Table(String table_name) {
    this.table_name = table_name;
    num_records = 0;
    table = new ArrayList<Record>();
    numberType = new HashMap<String, Number>();
    num_columns = 1;
  }

  // ========== GETTER METHODS ============
  public int getNumColumns() {
    return num_columns;
  }

  public ArrayList<Record> getTable() {
    return table;
  }

  public String getTableName() {
    return table_name;
  }

  public int getNumRecords() {
    return num_records;
  }

  public Number getNumberCol() {
    int index = searchForColumn("Select ");
    Record r = table.get(0);

    String colName = r.getField(index);
    Number num = numberType.get(colName);
    System.out.println(numberType);
    return num;
  }

  // =================== SETTER METHODS ======================
  public void setTableName(String s) {
    table_name = s;
  }

  public void setNumColumns(int n) {
    num_columns = n;
  }

  public void setKeyColumn() {

    newrecord = table.get(0);
    newrecord.addField(0, "key");

    for (int i = 1; i <= num_records; i++)
    {
      newrecord = table.get(i);
      String s = Integer.toString(i);
      newrecord.addField(0, s);
    }
  }

  public void setNumRecords(int r) {
    num_records = r;
  }


   // ============= COLUMN ADDING METHODS =================
   // enables user to give columns names the first time a table
   // is created if they are not importing from a file
   public void giveColumnsNames(Record newrecord, int start) {

     for (int i = start; i < num_columns; i++)
     {
       System.out.print("Column " + i + " name: ");
       String name = io.userinput();
       newrecord.addField(i, name);
       createcolumnTypes(name);
     }
     System.out.println("\nColumns added to table!\n");
   }

   // adds columns to a table
   public int addColumnsToNewTable(ArrayList<Record> table) {

     boolean valid = false;
     int start = 1;

     while (valid == false)
     {
       System.out.print("Number of columns required: ");
       String columns = io.userinput();

       try
       {
         int colsToAdd = Integer.parseInt(columns);
         num_columns += colsToAdd;
         newrecord = new Record(num_columns);
         newrecord.addField(0, "key");
         giveColumnsNames(newrecord, start);
         table.add(newrecord);
         valid = true;
       }
       catch (Exception e)
       {
         System.out.println("\n ==== WARNING! " +
         "Please Enter a valid number ====\n");
       }
     }
    return num_columns;
   }

   // adds columns to existing tables
   public void addColumnstoExistingTable(int size) {
     int start = num_columns;
     num_columns += size;
     resizeTable(num_columns);
     newrecord = table.get(0);
     giveColumnsNames(newrecord, start);
   }

   // resizes all of the records in a table
   // if columns are added
   public void resizeTable(int length) {

     String s;

     for (int l = 0; l < table.size(); l++) {
       Record tempR = table.get(l);
       newrecord = new Record(length);

       for (int i = 0; i < tempR.lengthRecord(); i++) {
         s = tempR.getField(i);
         newrecord.addField(i, s);
       }
       table.set(l, newrecord);
     }
   }


  // ============= COLUMN DELETING METHODS ==================
  //deletes a specific column
  public void deleteColumns() {

    String input = "y";

    while (input.equals("y"))
    {
      int index = searchForColumn("Delete");
      String colName = newrecord.getField(index);
      System.out.print("Are you sure you want to delete this column?\n" +
      "Please note all data in the column will be deleted. (y/n): ");
      input = io.userinput();
      if (input.equals("y"))
      {
        clearColumn(colName, index);
        num_columns--;
        deleteAColumn(index);
        System.out.println("Column deleted!\n");
        System.out.print("Delete another column? (y/n): ");
        input = io.userinput();
      }
    }
  }

  // sets column values to null
  public void clearColumn(String colName, int index) {
    for (int l = 0; l < table.size(); l++)
    {
      Record tempR = table.get(l);
      tempR.addField(index, null);
    }
  }

  // removes the column entirely and resifts columns
  public void deleteAColumn(int index) {
    for (int l = 0; l < table.size(); l++)
    {
      Record tempR = table.get(l);
      newrecord = new Record(num_columns);
      int j = 0;

      for (int i = 0; i < tempR.lengthRecord(); i++)
      {
        if (i != index)
        {
          String s = tempR.getField(i);
          newrecord.addField(j, s);
          j++;
        }
      }
      table.set(l, newrecord);
    }
  }


  // ============== SEARCH COLUMN METHOD ==================
  public int searchForColumn(String s) {

    boolean searchforCol = true;
    int index = 0;

    while (searchforCol == true)
    {
      System.out.print("\n" + s + " (enter column name): ");
      String input = io.userinput();
      index = checkEachColumn(input);
      if (index == -1) {
        searchforCol = false;
      }
      if (index > 0) {
        return index;
      }
    }
    return index;
  }

  public int checkEachColumn(String input) {
    Record columnNames = table.get(0);
    int index = 0;

    if (io.endinfo(input) == true) {
      return -1;
    }

    else {
      for (int i = 1; i < num_columns; i++) {
        if (input.equals(columnNames.getField(i))) {
          index = i;
        }
      }
      if (index == 0) {
        System.out.println("COLUMN DOES NOT EXIST.");
        return -2;
      }
    }
    return index;
  }


  // ============== RECORD ADDING METHODS =================
  // adds records from the command line. Allow to insert
  //or add at the end. If adding at the end, pass in 0
  //as argument.
  public void addrecord(int position) {

    boolean end = false;
    String input = "y";


    while (end == false) {

      if (checkRecordInsertPossible(position) == false) {
        System.out.print("\nSelect position to insert record: ");
        input = io.userinput();
        try {
          position = Integer.parseInt(input);
        }
        catch (Exception e) {
          System.out.println(" ==== WARNING ==== Please enter an integer.\n" +
          "Enter '0' if you simply wish to add the record at the end.");
        }
      }
      else {
        newrecord = new Record(num_columns);
        num_records++;
        displayColNames(newrecord);

        if (position == 0) {
          table.add(newrecord);
          System.out.println("Enter new record? (y/n)");
          input = io.userinput();
          if (input.equals("n")) {
            end = true;
          }
        }
        else {
          table.add(position, newrecord);
          end = true;
        }
      }
    }
  }

  //adds records from a file
  public void addRecordsfromFile(ArrayList<String[]> al, boolean newTable) {

    String[] colNames = al.get(0);
    String[] s = al.get(0);
    boolean end = false;

    while (end == false) {
      if (s.length > num_columns) {
        System.out.println("\n====== WARNING! ======\n" +
        "There are not enough columns  " +
        "Please add more columns before continuing.\n");
        addColumnstoExistingTable(s.length - num_columns+1);
      }

      else {
        for(int j = 0; j < al.size(); j++) {
          s = al.get(j);

          newrecord = new Record(s.length + 1);

          if (j == 0 && newTable == true) {
            newrecord.addField(0, "key");
            for (int i = 1; i <= s.length; i++)
            {
              newrecord.addField(i, s[i-1]);
              createcolumnTypes(s[i-1]);
            }
            table.add(newrecord);
          }

          else {
            num_records++;
            newrecord.addField(0, Integer.toString(num_records));
            for (int i = 1; i <= s.length; i++)
            {
              newrecord.addField(i, s[i-1]);
              if (iscolumnNumber(colNames[i-1]) == true) {
                addFloat(s[i-1]);
              }
            }
            table.add(newrecord);
          }
        }
        System.out.println("RECORDS SAVED!\n");
        end = true;
      }
    }
  }

  public void insertRecords() {

    boolean run = true;

    while (run == true)
    {
      System.out.print("Position you want to add the record at: ");
      String input = io.userinput();

      if (io.endinfo(input) == true)
      {
        run = false;
      }
      else
      {
        try
        {
          int position = Integer.parseInt(input);
          addrecord(position);
          setKeyColumn();
        }
        catch (Exception e)
        {
          System.out.println("Could not convert integer");
        }
      }
    }
  }

  //function to stop users inserting records at places that
  //do not exist
  public boolean checkRecordInsertPossible(int position) {
    if (position > num_records)
    {
      System.out.print("\n ==== WARNING ===== Insertion of record impossible.\n" +
      "You currently have " + num_records + " records in the table.\n" +
      "Please select a number within the range 1 to " +
      num_records + " to insert a record.");
      return false;
    }
    return true;
  }

  //saves a number if the type of the column is a number
  public boolean saveNumber(String colName, String s) {
    float f;
    try {
      f = Float.parseFloat(s);
      number = new Number();
      number.addNumber(f);
      numberType.put(colName, number);
      return true;
    }
    catch (Exception e) {
      System.out.println("\n ==== WARNING ==== This column " +
      "requires you to only add numbers");
      return false;
    }
  }

  // ============== SEARCH RECORD METHODS ===================
  // searches records in a given table
  public void searchRecords() {

    boolean end = false;
    int matching_records;

    while (end == false) {
      System.out.print("Enter search terms: ");
      String input = io.userinput();
      System.out.println("\n");

      if (io.endinfo(input) == true)
      {
        end = true;
      }

      else {
        matching_records = searchTerm(input);
        if (matching_records == 0 ) {
            System.out.println("Record does not exist\n");
        }
      }
    }
  }

  //searches for the terms specified by the user in the
  //records of the table and prints them out
  public int searchTerm(String s) {

    int matching_records = 0;
    String search_terms[] = s.split(" ");
    int same_terms = 0;

    for (int i = 0; i < table.size(); i++)
    {

      newrecord = table.get(i);
      int recordLength = newrecord.lengthRecord();

      for (int k = 0; k < search_terms.length; k++)
      {

        for (int j = 0 ; j < recordLength; j++)
        {
          String field = newrecord.getField(j);
          if (field.equals(search_terms[k]))
          {
            same_terms++;
          }
        }
      }

      if (same_terms == search_terms.length)
      {
        for (int j = 0 ; j < recordLength; j++)
        {
          String field = newrecord.getField(j);
          System.out.print(field + " ");
        }
        System.out.println();
        matching_records++;
      }
      same_terms = 0;
    }

    System.out.println("Matching records: " + matching_records + "\n");
    return matching_records;
  }

  //search for a specific record in a table and returns its index
  public int searchForRecord(String s) {

    int key = -1;

    System.out.println("Enter key of record to " + s + " OR " +
    "enter a search term to view matching records and keys.");
    String input = io.userinput();

    if (io.endinfo(input) == true)
    {
      return -2;
    }
    else {
      try {
        key = Integer.parseInt(input);
      }
      catch (Exception e) {
        if (num_records > 0) {
          if (searchTerm(input) == 0) {
            System.out.println("WARNING: There are no records " +
            "with those terms.\n");
          }
          else {
            return 0;
          }
        }
      }
    }
    return key;
  }


  // =========== RECORD MODIFICATION METHODS ==============
  public void modifyRecords() {

    int key = 0;
    boolean end = false;
    String input = "y";

    while (end == false)
    {
      if (key == -2) {
        end = true;
      }
      else if (key == 0 || key == -1) {
        key = searchForRecord("modify");
      }
      else {
        newrecord = table.get(key);
        Record columnNames = table.get(0);
        int recordLength = newrecord.lengthRecord();

        System.out.println("\nRECORD TO MODIFY:");
        System.out.println("------------------");
        for (int j = 0 ; j < recordLength; j++)
        {
          String field = newrecord.getField(j);
          String col_names =  columnNames.getField(j);

          System.out.println(col_names + ": " + field);
        }

        boolean run = true;
        while (run == true)
        {
          int index = searchForColumn("Replace");
          if (index == 0) {
            run = false;
            end = true;
          }
          else {
            System.out.print("with: ");
            input = io.userinput();
            if (io.endinfo(input) == true) {
              System.out.println("End of record modification.\n" +
              "WARNING! Last record not modified.");
              run = false;
              end = true;
            }
            else {
              newrecord.addField(index, input);
              System.out.println("Record modified\n");
            }
          }
        }
      }
    }
  }


  // ================= PRINTING METHODS ===================
  // prints a table in the database
  public void printtable(Table tempT) {

    for (int l = 0; l < tempT.table.size(); l++)
    {
      Record tempR = tempT.table.get(l);

      for (int j = 0; j < tempR.lengthRecord(); j++)
          System.out.printf("%-15.22s", tempR.getField(j));
          System.out.print("\n------------------------------------------" +
                             "------------------------------------------" +
                             "-------------");
          System.out.println("|");
    }
    System.out.println("NUMBER COLUMNS: " + num_columns);
  }

  public void displayColNames(Record newrecord) {
    Record col_names = table.get(0);
    for (int column = 1; column < num_columns; column++)
    {
      String colName = col_names.getField(column);
      System.out.print(" " + colName + ": ");
      newrecord.addField(0, Integer.toString(num_records));
      String input = io.userinput();
      if (iscolumnNumber(colName) == true) {
        addFloat(input);
      }
      newrecord.addField(column, input);
    }
  }

  public void printFloats() {
    for (String str : numberType.keySet())
    {
      Number n = numberType.get(str);
      for (int i = 0; i < n.getSetSize(); i++)
      {
        float f = n.getNumber(i);
        System.out.println("Floats saved: " + f);
      }
    }
  }


  // ================== SAVING METHODS ====================
  // saves table to a file
  public void saveTableToFile(BufferedWriter bw, String tableName) {

    try {
      bw.write("TABLE_NAME, " + tableName + ",");
      bw.newLine();

      for (int l = 0; l < table.size(); l++)
      {
        Record tempR = table.get(l);

        for (int j = 1; j < tempR.lengthRecord(); j++)
            bw.write(tempR.getField(j) + ",");
            bw.newLine();
        }
        bw.write("END_TABLE,");
        bw.newLine();
    }
    catch (IOException e) {
    }
  }

  public void saveSingleTabletoFile() {
    System.out.print("Name of file to save to: ");
    String fileName = io.userinput();
    file.createFile(fileName);
    try
    {
      File file = new File(fileName);
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      saveTableToFile(bw, table_name);
      bw.flush();
      bw.close();
    }
    catch (IOException e)
    {
      System.out.println("\nWARNING! TABLE DATA WAS NOT SAVED.\n");
    }
  }


  /********************************************************
  /    METHODS RELATED TO THE FLOAT TYPE COLUMN           /
  ********************************************************/

  // ======== CREATING AND CHECKING FOR FLOAT COLUMNS ====
  //creates an empty Number object to add to the HashMap
  public void createcolumnTypes(String colName) {
    String[] str = colName.split("\\s+");
    if (str[0].equals("float")) {
      number = new Number();
      numberType.put(colName, number);
    }
  }

  // method that gets called accross the program to check
  //validity of the name of a column entered by users
  public boolean iscolumnNumber(String colName) {
   String[] str = colName.split("\\s+");
   if (str[0].equals("float")) {
     return true;
   }
   return false;
  }

  public void addFloat(String input) {
   try {
     float f = Float.parseFloat(input);
     number.addNumber(f);
   }
   catch (Exception e) {
     System.out.println("ERROR ADDING NUMBER.");
   }
 }
  // ================= OPERATION METHODS ==================
  // Enables you to select the column you want to perform the
  //sum or average operation on
  public float calculateColSum() {
    float f = 0;
    System.out.print("Name of column: ");
    String input = io.userinput();
    checkEachColumn(input);
    if (iscolumnNumber(input)) {
      f = sumColumn(input);
    }
    else {
      System.out.println("\n ===== WARNING ====== " +
      " The column name you have selected is not a float type column.");
    }
    return f;
  }

  // Calculates the sum of the numbers in the selected column
  public float sumColumn(String colName) {
    float sum = 0;
    Number n = numberType.get(colName);
    for (int i = 0; i < n.getSetSize(); i++)
    {
      float f = n.getNumber(i);
      sum += f;
    }
    System.out.println("SUM: " + sum);
    return sum;
  }

  // calculates the average of the numbers in a given column
  public float averageColumn() {
     float average = calculateColSum()/(num_records - 1);
     System.out.println("AVERAGE: " + average);
     return average;
   }

   public float calculateOccurence() {
     float f;
     float occ = 0;

     System.out.print("Name of column: ");
     String input = io.userinput();
     checkEachColumn(input);

     if (iscolumnNumber(input)) {
       System.out.print("Number you would like the occurence of: ");
       input = io.userinput();
       try {
         f = Float.parseFloat(input);
         occ = occurence(f)*100/num_records;
         System.out.println("OCCURENCE OF " + f + " : " + occ + " %");
       }
       catch (Exception e) {
         System.out.println("OUPS! LOOKS LIKE THERE IS A GLITCH.\n");
       }
     }

     else {
       System.out.println("\n ===== WARNING ====== " +
       " The column name you have selected is not a float type column.");
     }
     return occ;
   }

    public float occurence(float f) {
      int occurence = 0;
      float percentage = 0;

      for (String str : numberType.keySet())
      {
        Number n = numberType.get(str);
        for (int i = 0; i < num_records; i++)
        {
          if (f == n.getNumber(i)) {
            occurence++;
          }
        }
      }
      return occurence;
    }

  // ================ DELETING METHODS ====================
  public void deleteRecord() {

    int key = searchForRecord("delete");

    if (key == 0)
    {
      key = searchForRecord("delete");
    }
    if (key == -1)
    {
      System.out.println("RECORD DOES NOT EXIST");
    }
    else
    {
      table.remove(key);
      num_records--;
      setKeyColumn();

      newrecord = table.get(0);
      String[] colNames = newrecord.getRecord();
      for (int i = 0; i < colNames.length; i++) {
        if (iscolumnNumber(colNames[i]) == true) {
          number = numberType.get(colNames[i]);
          number.removeNumber(key-1);
        }
      }

      System.out.println("RECORD DELETED.\n");
    }
  }


  // ============== Testing =================================
  /*public static void main(String[] args) {
      Table table = new Table("TestTable");
      table.run(args);
  }

  public void run(String[] args) {
     boolean testing = false;
     assert(testing = true);
     if (args.length == 0 && testing) test();
  }*/

  public void test() {
    test_addAndDeleteColstoTable();
    addrecord(0);
    insertRecords();
  }

  void test_addAndDeleteColstoTable() {
    int n = addColumnsToNewTable(table);
    assert(num_columns == n);
    Record r = table.get(0);
    assert(r.getField(0) == "key");

    addColumnstoExistingTable(2);
    assert(num_columns == n + 2);
    n += 2;
    addColumnstoExistingTable(10);
    assert(num_columns == n + 10);
    n += 10;
    addColumnstoExistingTable(5);
    assert(num_columns == n + 5);
    n += 5;
    int m = searchForColumn("delete");
    deleteColumns();
    assert(num_columns == n - 1);
  }

  void test_addAndInsertofRecord() {
    addrecord(0);
    insertRecords();
  }
}
