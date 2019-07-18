package com.sanjana.filesorter.operation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BigFileWriter {
    public static void main(String[] args) throws IOException{
        BufferedWriter bfw = new BufferedWriter(new FileWriter("test-unique.txt"));
        bfw.flush();
        bfw.close();
    }

}
