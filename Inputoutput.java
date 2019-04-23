import java.util.Scanner;

class Inputoutput {

  void welcomeinstructions() {
    System.out.println("\n > Enter instruction or: \n" +
    " > enter 'info' to view all available instructions\n" +
    " > enter 'exit' to quit the program\n");
  }

 //gathers instruction from user
 String userinput() {

   Scanner readinput = new Scanner(System.in);
   String input = readinput.nextLine();
   return input;
 }

 //prints out information about instructions
 void info() {
   System.out.println("\n ============= INSTRUCTIONS ============ \n" +
   " > 'add records' : enables you to add records.\n" +
   " > 'search records' : enables you to search in a specific table or the whole database.\n" +
   " > 'search all': will allow you to search all records in all tables. Needs to be preceeded by 'search records' to take effect.\n" +
   " > 'view all records' : prints out all the records in a specific table. Same as 'view table'.\n" +
   " > 'modify records' : lets you modify your selected records.\n" +
   " > 'delete record' : lets you delete a specific record\n" +
   " > 'delete all records' : lets you delete all records at once.\n" +
   " > 'create new table': allows you to create a new table.\n" +
   " > 'view all tables': prints out all tables in the database\n" +
   " > 'view table': lets you select a specific table\n" +
   " > 'load database': restors the database made during previous sessions.\n" +
   " > 'clear database': will wipe out everything on the database. We do not recommend using this instruction.\n" +
   " > 'save': this will save everything in the database to a file\n" +
   " > 'end' : ends the activity you are doing.\n" +
   " > 'exit' : exits the program.\n");
 }

 void errormessage() {
   System.out.println("Not valid a valid instruction." +
   "Enter 'info' to view list of all valid instructions.\n");
 }

 //====== check if it is an exit or info command ==========
 //decide if needs to go in a seperate class later
 boolean endinfo(String s) {
   if (s.equals("end")) {
     return true;
   }
   else if (s.equals("info")) {
     info();
     return false;
   }
   return false;
 }
}
