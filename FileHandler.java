import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class FileHandler {

  private Database database;
  private Table table;
  private Inputoutput io = new Inputoutput();

  // ========= Reads data for tables from files ===========
  public ArrayList<String[]> readFile() {

    ArrayList<String[]> records = new ArrayList<>();
    boolean run = true;


    while (run == true) {

      System.out.print("Enter file name to import from: ");
      String input = io.userinput();

      if (io.endinfo(input) == true)
      {
        run = false;
      }

      try (BufferedReader br = new BufferedReader(new FileReader(input)))
      {
        String line;

        while ((line = br.readLine()) != null)
        {
          String[] values = line.split("\\s*,\\s*");
          records.add(values);
        }
        run = false;
      }
      catch (IOException e)
      {
        System.out.println("\n ===== WARNING! COULD NOT READ DATA FROM FILE " +
                           "OR FILE DOES NOT EXIST. =====\n");
        run = true;
      }
    }
    return records;
  }


  // ===== Functions to save the database to a file =======
  public void createFile(String fileName) {
    try
    {
      File file = new File(fileName);
      file.createNewFile();
    }
    catch (IOException e)
    {
      System.out.println("\nWARNING! FILE TO SAVE DATABASE NOT CREATED.\n");
    }
  }
}
