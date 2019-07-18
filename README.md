# File sorter Application
The application is designed to sort huge files, with memory constraints in an optimized way.

### Description 
Given with a line separated text file of integers ranging anywhere from Integer.MIN to Integer.MAX of size 1024MB, the program should be able to produce line separated text file which has the sorted content of the input file. 

### Preconditions 
* The program can run with a memory constraint of 100MB i.e. the -Xmx100m for sorting a 1 GB file.
* Duplicates can be present in the input file. This case is also handled. 

### Assumptions
* The text in the file has only integers which are line separated and no other characters. 

### Run the application
* Run the application using `./mvnw spring-boot:run`
* Keep the input file in the same directory as the project.
* Enter the file name to sort, eg. `BigFile.txt`
* The sorted file is created as `output.txt` in ascending order.

### Technologies used
* Spring boot
* Junit

### Design

**Split and Sort**
* Initially the input file is split based on lines per file (eg: `1_000_000`).
* Each split is sorted and a temp file is created.
* The split files are added into a queue to keep track of the split files.

**Merge**
* Each item in the queue is polled and then merged with the subsequent file.
* The split files are deleted.
* The merged file is added back to the end of the queue for the next merge
* The above steps is repeated until we reach the end of the queue.
* Last file of the queue will contain the sorted contents of the input file.
* The last file is renamed as `output.txt`


### Ideas for improvement
* Output file name can be configurable.
* Lines per file for split can be determined at runtime possibly.