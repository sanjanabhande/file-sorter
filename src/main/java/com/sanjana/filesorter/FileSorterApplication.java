package com.sanjana.filesorter;

import com.sanjana.filesorter.operation.FileSorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class FileSorterApplication implements CommandLineRunner {

	@Autowired
	private FileSorter fileSorter;

	public static void main(String[] args) {
		SpringApplication.run(FileSorterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		while(true){
			System.out.println("Welcome to file sorter application! ");
			System.out.println("Please provide your file name to sort. ");
			System.out.println("For simplicity keep the input file in the same directory as this project and give the file name without path.\n Type 'Quit' to exit the program");
			Scanner scanner = new Scanner(System.in);
			String command = scanner.nextLine();
			if(command.equalsIgnoreCase("Quit")){
				System.exit(0);
			}
			fileSorter.run(command, "output.txt");
			System.out.println("The output file is available in 'output.txt'");
		}
	}
}
