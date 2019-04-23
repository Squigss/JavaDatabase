import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;


class Database {

  private HashMap<String, Table> database;
  private FileHandler file = new FileHandler();
  private Inputoutput io = new Inputoutput();
  private Table newtable;
  private Record records;


  public Database () {
    database = new HashMap<String, Table>();
  }

  // ============= GETTER AND SETTER METHODS ================
  public Table getTablefromDatabase(String name) {
    Table t = database.get(name);
    return t;
  }

  public String getTableNamefromUser() {
    boolean tableExists = false;
    String input = new String();

    while (tableExists == false)
    {
      System.out.print("Table name: ");
      input = io.userinput();
      System.out.println();
      tableExists = checkTableExists(input);
    }
    return input;
  }

  // acts as a setter method to insert table from other classes
  public void insertTable(Table t, String name) {
    database.put(name, t);
  }


  // ======= METHODS TO ADD TABLES TO THE DATABASE ==========
  //adds empty tables to the database
  public void addTables() {
    boolean end = false;
    Scanner readinput = new Scanner(System.in);
    String input = "y";

    while (end == false)
    {
      if(input.equals("y"))
      {
        System.out.print("\nTable name: ");
        input = io.userinput();
        newtable = new Table(input);
        database.put(input, newtable);
        int num_columns = newtable.addColumnsToNewTable(newtable.getTable());
        newtable.setNumColumns(num_columns);
        System.out.print("Would you like to create a new table (y/n)? ");
        input = readinput.nextLine();
      }
      else
      {
        end = true;
      }
    }
  }

  //imports from a file a whole database with all the tables
  public void importDatabase() {
    ArrayList<String[]> fileinfo = file.readFile();
    String[] s;
    int num_columns = 1;
    ArrayList<String[]> tableinfo = new ArrayList<String[]>();
    int numTables = numKeys();

    String name = " ";

    numTablestoImport(fileinfo);

    for (int i = 0; i < fileinfo.size(); i++)
    {
      s = fileinfo.get(i);
      if (s[0].equals("TABLE_NAME"))
      {
        name = s[1];
      }

      if (s[0].equals("END_TABLE"))
      {
        newtable = database.get(name);
        newtable.setNumColumns(num_columns);
        newtable.addRecordsfromFile(tableinfo, true);
        System.out.println("TABLE SUCCESSFULLY UPLOADED!\n");
        numTables++;
        tableinfo.clear();
        num_columns = 1;
      }

      else if (!s[0].equals("TABLE_NAME"))
      {
        tableinfo.add(s);
        if (s.length >= num_columns)
        {
          num_columns = s.length + 1;
        }
      }
    }
  }

  public String[] numTablestoImport(ArrayList<String[]> fileinfo) {

    int numTables = numKeys();
    String[] s;

    for (int i = 0; i < fileinfo.size(); i++)
    {
      s = fileinfo.get(i);
      if (s[0].equals("TABLE_NAME"))
      {
        newtable = new Table(s[1]);
        database.put(s[1], newtable);
        numTables++;
      }
    }

  String[] tableNames = new String[numTables];
    int j = 0;
    for (String key : database.keySet())
    {
      tableNames[j] = key;
      j++;
    }
    return tableNames;
  }

  //returns the number of tables in a database
  public int numKeys() {
    int j = 0;
    for (String key : database.keySet())
    {
       j++;
    }
    return j;
  }



  // ============= METHODS TO SAVE TO FILES ================
  public void saveDatabaseToFile() {

      File file = new File("database.txt");
      try
      {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(String key: database.keySet())
        {
           Table tempT = database.get(key);
           tempT.saveTableToFile(bw, key);
        }
        bw.flush();
        bw.close();
        System.out.println("DATABASE SAVED!");
     }
     catch (IOException e)
     {
        System.out.println("\nWARNING! COULD NOT EXPORT DATA TO FILE.\n");
     }
  }


  // ======== METHOD TO SEARCH WHOLE THE DATABASE ===========
  public void searchDatabase() {
    System.out.println("Enter search terms: ");
    String input = io.userinput();
    for (String key : database.keySet())
    {
      Table tempT = database.get(key);
      System.out.println("TABLE: " +key);
      tempT.searchTerm(input);
    }
  }


  // ================ METHODS TO PRINT =====================
  //prints the database
  public void printdatabase() {
    for (String key : database.keySet())
    {
      Table tempT = database.get(key);

      System.out.println("\n\n");
      System.out.println(" ================= TABLE NAME: " + key +
      " =================\n");
      tempT.printtable(tempT);
    }
    System.out.println("\n\n");
  }


  // ================ DELETING METHODS ====================
  public void deleteTable(String key) {
    System.out.print("Are you sure you want to delete this table?" +
    " All associated data will be deleted too. (y/n): ");
    String input = io.userinput();
    if (input.equals("y"))
    {
      database.remove(key);
      System.out.println("TABLE DELETED.\n");
    }
  }

  public void deleteDatabase() {
    System.out.println("Are you sure you want to delete all records?" +
    " All data will be deleted and lost. (y/n): ");
    String input = io.userinput();
    if(input.equals("y"))
    {
      database.clear();
    }
  }


  // =========== ERROR MANAGEMENT METHODS ===============
  public boolean checkTableExists(String tableName) {

    if (tableName.equals("search all"))
    {
      return true;
    }
    for (String key : database.keySet())
    {
      if (key.equals(tableName))
      {
        return true;
      }
    }
    System.out.println("TABLE DOES NOT EXIST!\n");
    return false;
  }
}
