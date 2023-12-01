/**
 * CS2370 Programming Assignment 1 Sample Code
 *
 * @author [Insert name(s), email address(es), student number(s),
 *          and group number here for author(s)]
 *
 * Example of how to use the CpuTimer class. You may use this code
 * as starter code for programming assignment 1. Replace this comment with
 * your own description.
 *
 */

public class Assignment1 {

  /**
   * Program main
   *
   * @param args
   */
  public static void main(String[] args) {
    CpuTimer timer = new CpuTimer();

    for (long i = 0; i < 500_000_000; ++i) {
      // do nothing
    }

    System.out.println("CPU time = " + timer.getElapsedCpuTime());
  }

}
