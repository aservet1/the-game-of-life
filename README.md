# the-game-of-life
my implementation of [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life)

core C++ game of life tool that im designing to be useful by any program, as the core C++ simply takes a starting file and writes the output of so many steps of the game of life, given some parameters (right now the only special parameter, other than a flip book play by play of the process, is a spontaneous generation rate).

## help
Get help on what the command line arguments entail with `.\gameoflife.exe --help`

## build
`g++ gameoflife.cpp -o gameoflife`. It's all contained in one file, so I haven't made a makefile for this project or do any rigorous project structuring or anything yet.
