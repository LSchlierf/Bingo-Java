# Code for Creating and printing Buzzword Bingo cards from txt files

[![wakatime](https://wakatime.com/badge/user/ee0b2e88-680b-47cf-ba7c-afd0e1637329/project/8d1fab99-001f-4cb1-a383-164c986c486e.svg)](https://wakatime.com/badge/user/ee0b2e88-680b-47cf-ba7c-afd0e1637329/project/8d1fab99-001f-4cb1-a383-164c986c486e)

## Functionality

### General

This code allows you to create and print [Buzzowrd Bingo](https://wikipedia.org/wiki/Buzzword_bingo) cards from txt files. To create a Bingo card, the code uses the entries form the txt files in the [corresponding folder](src/BingoParts/Sets). In there, you will find an [example txt file](src/BingoParts/Sets/example.txt) for demonstration purposes. It contains the numbers 1 through 50, however the actual entries can be anything you want.  
I also provided two ways to mark them off:

### ConsoleGame

You can use the main method in [ConsoleGame.java](src/ConsoleGame/ConsoleGame.java) to create a Bingo card from a set. The Bingo card will then be printed to the console and you can mark off the fields as prompted.

### Printing

In the class [Printing.java](src/BingoParts/Printing.java) there are several methods for creating PDFs from either a specified Bingo card or newly created ones. There is also a main method for generating PDFs. You can run it and enter the values as prompted, or run it from the command line with the arguments "(one|six) (the set name), (the size), (the path where you want to save the PDFs), [--no-free-tile]".  
If you choose to run the main method without arguments, you will be prompted to enter all these values. If you choose not to enter a path, the PDF will be saved in the compiled counterpart to [PrintOutput](src/BingoParts/PrintOutput), in the folder "bin/BingoParts/PrintOutput". I might change this locatioin in the future, since this folder is cleaned upon exit by most IDEs.  
The PDF generation utilizes the [PDFBox library by Apache](https://pdfbox.apache.org/).

## Roadmap

- [x] Basic functionality
- [x] Printing to console
- [x] Console bingo game
- [x] Printing to PDF
- [x] Printing main method with CLI
- [ ] Good formatting for free space
- [ ] Good formatting for single card PDF print
- [ ] GUI for easily creating sets and printing cards

## Contact

Please do not hesitate to reach out to me if you need help, have any questions about this project or found an error.  
I can be reached via Email: [LucasSchlierf@gmail.com](mailto:LucasSchlierf@gmail.com)

Cheers :)  
Lucas
