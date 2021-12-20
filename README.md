# the-game-of-life
core C++ game of life tool that im designing to be useful by any program, as the core C++ simply takes a starting file and writes the output of so many steps of the game of life, given some parameters (right now the only special parameter, other than a flip book play by play of the process, is a spontaneous generation rate)

# Absolute simplest usage:
Given a seed file, run `.\gameoflife.exe <filename>` and the program will write the next step in the game of life to the console. You can redirect this output
to a file, and have the iterative process be to keep generating file n+1. The parent context of this (you calling by hand, script, larger program/simulation) will
determine how often is called, what files are written to, how to organize the output (just keep leap frogging between two files or save the whole series of them).
So that's up to the user of this program.

There are other command line options to make this do more specific things but for those, I haven't confirmed that they all work. So to use this as is (updates will
be shown in this readme if more command line options are a guarantee of the tool) you can only guarantee functionality from the simple 'run once for one step' usage
provided by `.\gameoflife.exe <filename>`
