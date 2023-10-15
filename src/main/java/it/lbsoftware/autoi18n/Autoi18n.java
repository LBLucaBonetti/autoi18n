package it.lbsoftware.autoi18n;

import picocli.CommandLine;

@CommandLine.Command
public class Autoi18n implements Runnable {

  @Override
  public void run() {
    System.out.println("Hello, world!");
  }
}
