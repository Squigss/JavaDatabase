import java.util.Scanner;
import java.io.*;

class Record {

  private String[] record;

  public Record(int record_size) {
    record = new String[record_size];
  }

  public boolean addField(int index, String value) {
   if (index >= 0 && index <= record.length)
   {
     record[index] = value;
     return true;
   }
   else
   {
     return false;
   }
  }

  public String getField(int index) {
    return record[index];
  }

  public int lengthRecord() {
    return record.length;
  }

  public String[] getRecord() {
    return record;
  }

  // ============== Testing ===============

  /*public static void main(String[] args) {
      Record record = new Record(20);
      record.run(args);
  }

  public void run(String[] args) {
     boolean testing = false;
     assert(testing = true);
     if (args.length == 0 && testing) test();
  }*/

  void test() {
    test_addField_getRecord_getField();
    test_lengthRecord();
  }

  void test_addField_getRecord_getField() {
    addField(0, "name");
    assert(record[0] == "name");
    assert(getField(0) == "name");

    addField(1, "age");
    assert(record[1] == "age");
    assert(getField(1) == "age");

    addField(2, "location");
    assert(record[2] == "location");
    assert(getField(2) == "location");

    addField(3, "Paul Harris");
    assert(record[3] == "Paul Harris");
    assert(getField(3) == "Paul Harris");

    addField(4, "2");
    assert(record[4] == "2");
    assert(getField(4) == "2");

    addField(0, "Welcome to this database system. Hope you enjoy 333");
    assert(record[0] == "Welcome to this database system. Hope you enjoy 333");
    assert(getField(0) == "Welcome to this database system. Hope you enjoy 333");

    assert(addField(-1, "key") == false);
    assert(addField(0, " ") == true);
    assert(addField(12034, "test") == false);
    assert(addField(-354, "key") == false);
  }

  void test_lengthRecord() {
    assert(record.length == 20);
  }

}
