# Code for Creating and printing Bingo cards from txt files

[![wakatime](https://wakatime.com/badge/user/ee0b2e88-680b-47cf-ba7c-afd0e1637329/project/8d1fab99-001f-4cb1-a383-164c986c486e.svg)](https://wakatime.com/badge/user/ee0b2e88-680b-47cf-ba7c-afd0e1637329/project/8d1fab99-001f-4cb1-a383-164c986c486e)

## Functionality

### General

This code allows you to create adn Print Bingo cards from txt files. To create a Bingo Card, the code uses the entries form the txt files in the [corresponding folder](src/BingoParts/Sets). In there, you will find an [example txt file](src/BingoParts/Sets/example.txt) for demonstration purposes. It contains the numbers 1 through 30, however the actual entries can be anything you want, including events that might happen.  
I also provided two ways to mark them off:

### ConsoleGame

You can use the main method in [ConsoleGame.java](src/ConsoleGame/ConsoleGame.java) to create a Bingo Card from a set. The Bingo Card will then be printed to the console and you can mark off the fields as prompted.

### Printing

In the class [Printing.java](src/BingoParts/Printing.java) there are three methods for creating PDFs from either a specified Bingo Card or newly created ones. The generated PFDs will be placed in the compiled counterpart to [PrintOutput](src/BingoParts/PrintOutput), in the folder "bin/BingoParts/PrintOutput".  
The PDF generation utilizes the [PDFBox library by apache commons](https://pdfbox.apache.org/).

## Roadmap

- [x] Basic functionality
- [x] Printing to console
- [x] Console bingo game
- [x] Printing to PDF
- [ ] GUI for easily creating sets and printing cards

## Contact

Please do not hesitate to reach out to me if you need help, have any questions about this project or found an error.  
I can be reached via Email: [LucasSchlierf@gmail.com](mailto:LucasSchlierf@gmail.com)

Cheers :)  
Lucas
