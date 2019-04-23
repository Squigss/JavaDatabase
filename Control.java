import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Control {

  private Table newtable;
  private Record records;
  private Database database = new Database();
  private Inputoutput io = new Inputoutput();
  private FileHandler file = new FileHandler();

  public static void main(String[] args) {
      Control control = new Control();
      control.run(args);
  }

  public void run(String[] args) {
    manageDatabase();
  }


  public void manageDatabase() {

    boolean manipulate_database = true;

    io.welcomeinstructions();

    while (manipulate_database == true)
    {
      System.out.print("\n --> Enter instruction: ");
      String input = io.userinput();
      System.out.println();

      if (input.equals("exit"))
      {
        manipulate_database = false;
      }

      else
      {
        if (input.equals("info"))
        {
          io.info();
        }
        else if (input.equals("create table"))
        {
          createTables();
        }
        else if (input.equals("add records"))
        {
          addRecords();
        }
        else if (input.equals("view all tables"))
        {
          database.printdatabase();
        }
        else if (input.equals("view table") ||
                 input.equals("view all records"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.printtable(temp);
        }
        else if (input.equals("search records"))
        {
          searchForRecords();
        }
        else if (input.equals("modify records"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.modifyRecords();
        }
        else if (input.equals("insert records"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.insertRecords();
        }
        else if (input.equals("add columns"))
        {
          addColumns();
        }
        else if (input.equals("delete columns"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.deleteColumns();
        }
        else if (input.equals("delete records"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.deleteRecord();

        }
        else if (input.equals("delete table"))
        {
           String tableName = database.getTableNamefromUser();
           database.deleteTable(tableName);
        }
        else if (input.equals("delete database"))
        {
          database.deleteDatabase();
        }
        else if (input.equals("save table"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.saveSingleTabletoFile();
        }
        else if(input.equals("save"))
        {
          database.saveDatabaseToFile();
        }
        else if(input.equals("load database"))
        {
          database.importDatabase();
        }
        else if(input.equals("get occurence"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.calculateOccurence();
        }
        else if(input.equals("get average"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.averageColumn();
        }
        else if(input.equals("get sum"))
        {
          String tableName = database.getTableNamefromUser();
          Table temp = database.getTablefromDatabase(tableName);
          temp.calculateColSum();
        }
        else
        {
          io.errormessage();
        }
      }
    }
  }


  // =========== CONTROL METHODS ============
  public void createTables() {
    System.out.print("Import from file (y/n)?  ");
    String input = io.userinput();
    if (input.equals("y")) {
      database.importDatabase();
    }
    else {
      database.addTables();
    }
  }

  public void addRecords() {

      String tableName = database.getTableNamefromUser();
        Table temp = database.getTablefromDatabase(tableName);
        System.out.print("\nImport from file? (y/n): ");
        String input = io.userinput();

        if (input.equals("y"))
        {
          ArrayList<String[]> fileinfo = file.readFile();
          temp.addRecordsfromFile(fileinfo, false);
        }

        else
        {
          temp.addrecord(0);
        }
      }

  public void searchForRecords() {
    String tableName = database.getTableNamefromUser();

    if (tableName.equals("search all"))
    {
      database.searchDatabase();
    }
    else
    {
      Table temp = database.getTablefromDatabase(tableName);
      temp.searchRecords();
    }
  }

  public void addColumns() {
    boolean valid = false;

    String tableName = database.getTableNamefromUser();
    Table temp = database.getTablefromDatabase(tableName);

    while (valid == false)
    {
      System.out.print("Additional columns to add: ");
      String input = io.userinput();
      try
      {
        int size = Integer.parseInt(input);
        temp.addColumnstoExistingTable(size);
        valid = true;
      }
      catch (Exception e)
      {
        System.out.println("Please enter a valid number.");
      }
    }
  }

}
