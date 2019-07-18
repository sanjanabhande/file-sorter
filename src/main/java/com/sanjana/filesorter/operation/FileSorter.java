package com.sanjana.filesorter.operation;

import com.sanjana.filesorter.exception.FileAccessException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class FileSorter {

    private static final int LINES_PER_FILE = 1_000_000;

    public void run(String inputFileName, String outputFileName) throws IOException {
        File file = new File(inputFileName);
        if (inputFileName==null || inputFileName.isEmpty() || !file.exists() || !file.canRead()){
            throw new FileAccessException();
        }
        renameFile(merge(split(inputFileName)), outputFileName);
    }

    //Split files
    private Queue<String> split(String inputFile) throws IOException {
        //Queue to add all the split files
        Queue<String> splitFiles = new LinkedList<>();
        //Use linereader to get line number instead of bufferred reader
        LineNumberReader bfr = new LineNumberReader(new FileReader(inputFile));
        String line;
        int filePartCounter = 1;
        List<Integer> items = new ArrayList<>();
        while((line = bfr.readLine())!=null){
            items.add(Integer.parseInt(line));
            //Once we reach lines per file write to file and empty list
            if(bfr.getLineNumber()%LINES_PER_FILE == 0){
                sortAndFlushToFile(items, inputFile+".part"+filePartCounter, splitFiles);
                items = new ArrayList<>();
                filePartCounter++;
            }
        }
        //Sort and flush last batch of data into split file
        if(!items.isEmpty()){
            sortAndFlushToFile(items, inputFile+".part"+filePartCounter, splitFiles);
        }
        return splitFiles;
    }

    //Sort the items, write to file and add in queue
    private void sortAndFlushToFile(List<Integer> items, String splitFileName, Queue<String> splitFiles) throws IOException{
        Collections.sort(items);
        writeItemsToFile(items, splitFileName);
        splitFiles.add(splitFileName);
    }

    //Write items to file
    private void writeItemsToFile(List<Integer> items, String splitFileName) throws IOException {
        System.out.println("Splitting and writing file---->"+splitFileName);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(splitFileName));
        for (int i = 0; i < items.size(); i++) {
            bufferedWriter.write(String.valueOf(items.get(i))+"\n");
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private String merge(Queue<String> files) throws IOException {
        int filePart = 1;
        String file1Name = null;
        String file2Name = null;
        while (!files.isEmpty()){
            //Take 2 files at a time and try to merge
            file1Name = files.poll();
            file2Name = files.poll();
            if(file1Name==null || file2Name == null){
                break;
            }
            BufferedReader bfr1 = new BufferedReader(new FileReader(file1Name));
            BufferedReader bfr2 = new BufferedReader(new FileReader(file2Name));

            String mergedFileName = "merged"+filePart;
            BufferedWriter bfw = new BufferedWriter(new FileWriter(mergedFileName));
            //assume, file 1 and file 2 are both sorted in ascending order
            //Compare contents of first file and second file, add into target file accordingly
            compareContentsAndWrite(bfr1, bfr2, bfw);
            //If one file is complete, flush remaining contents of either of the files into the merged file
            flushSourceFileToTarget(bfr1, bfw);
            flushSourceFileToTarget(bfr2, bfw);
            bfw.flush();
            bfw.close();
            Files.deleteIfExists(Paths.get(file1Name));
            Files.deleteIfExists(Paths.get(file2Name));
            files.add(mergedFileName);
            filePart++;
            System.out.println("Merged file---->"+mergedFileName);
        }
        //The last file in the queue is the final merged file
        return file1Name;
    }

    //Compare contents of first file and second file, add into target file accordingly
    private void compareContentsAndWrite(BufferedReader bfr1, BufferedReader bfr2, BufferedWriter bfw) throws IOException{
        String file1Data = bfr1.readLine();
        String file2Data = bfr2.readLine();
        //Compare data between two files and add
        while((file1Data!=null ) && (file2Data!=null)){
            if(Integer.parseInt(file1Data)<=Integer.parseInt(file2Data)){
                bfw.write(String.valueOf(file1Data)+"\n");
                file1Data = bfr1.readLine();
            }else {
                bfw.write(String.valueOf(file2Data)+"\n");
                file2Data = bfr2.readLine();
            }
        }
        if (file1Data!=null){
            bfw.write(String.valueOf(file1Data)+"\n");
        }
        if (file2Data!=null){
            bfw.write(String.valueOf(file2Data)+"\n");
        }
    }

    //read from source and write to target
    private void flushSourceFileToTarget(BufferedReader source, BufferedWriter target) throws IOException {
        String line;
        while((line=source.readLine())!=null){
            target.write(String.valueOf(line));
            target.write("\n");
        }
    }

    //Rename file
    private void renameFile(String inputFileName, String outputFileName){
        new File(inputFileName).renameTo(new File(outputFileName));
    }

}
