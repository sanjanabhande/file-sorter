package com.sanjana.filesorter.operation;

import com.sanjana.filesorter.exception.FileAccessException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileSorterTest {

    private FileSorter fileSorter = new FileSorter();

    @Test(expected = FileAccessException.class)
    public void testFileNotFound() throws IOException {
        fileSorter.run("abc.csv", "output.txt");
    }

    @Test(expected = FileAccessException.class)
    public void testEmptyFile() throws IOException {
        fileSorter.run("", "output.txt");
    }

    @Test
    public void testUnique() throws IOException {
        String outputFile = "output.txt";
        List<String> fileOutput = new ArrayList<>();
        List<String> expectedOutput = Arrays.asList("1","2","3","4","5","6","7","8","9","10");
        fileSorter.run("test-unique.txt", outputFile);
        Files.lines(Paths.get(outputFile))
                .forEach(fileOutput::add);
        Assert.assertEquals(expectedOutput, fileOutput);
    }

    @Test
    public void testDuplicate() throws IOException {
        String outputFile = "output.txt";
        List<String> fileOutput = new ArrayList<>();
        List<String> expectedOutput = Arrays.asList("1","1","2","2","3","4","5","6", "6","7","8","9","10", "10");
        fileSorter.run("test-duplicates.txt", outputFile);
        Files.lines(Paths.get(outputFile))
                .forEach(fileOutput::add);
        Assert.assertEquals(expectedOutput, fileOutput);
    }

}