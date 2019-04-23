import java.util.ArrayList;
import java.io.*;

class Number {

  private ArrayList<Float> number;

  public Number() {
    number = new ArrayList<Float>();
  }

  // ======== GETTER AND SETTER METHOD ==========
  public void addNumber(float f) {
    number.add(f);
  }

  public float getNumber(int index) {
    float f = number.get(index);
    return f;
  }

  public int getSetSize() {
    int size = number.size();
    return size;
  }

  public void removeNumber(int index) {
    number.remove(index);
  }
}
