#include<iostream>
#include<fstream>
#include<vector>
#include<cassert>
#include<time.h>
#include<stdbool.h>

#ifdef _WIN32
#include<windows.h>
void sleep_msec(int ms) {
	Sleep(ms);
}
#elif __linux__
#include <time.h>
#include <errno.h>
#include <sys/time.h>
int
sleep_msec(long msec) {
	struct timespec ts;
	int res;

	if (msec < 0) {
		errno = EINVAL;
		return -1;
	}

	ts.tv_sec = msec / 1000;
	ts.tv_nsec = (msec % 1000) * 1000000;

	do {
		res = nanosleep(&ts, &ts);
	} while (res && errno == EINTR);

	return res;
} /* https://stackoverflow.com/questions/1157209/is-there-an-alternative-sleep-function-in-c-to-milliseconds */
#endif

// #include <chrono>
// #include <thread>

/*
Rules -- https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules

The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells,
each of which is in one of two possible states, live or dead, (or populated and unpopulated, respectively).
Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or
diagonally adjacent. At each step in time, the following transitions occur:

	1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
	2. Any live cell with two or three live neighbours lives on to the next generation.
	3. Any live cell with more than three live neighbours dies, as if by overpopulation.
	4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

These rules, which compare the behavior of the automaton to real life, can be condensed into the following:

	1. Any live cell with two or three live neighbours survives.
	2. Any dead cell with three live neighbours becomes a live cell.
	3. All other live cells die in the next generation. Similarly, all other dead cells stay dead.

The initial pattern constitutes the seed of the system. The first generation is created by applying the above
rules simultaneously to every cell in the seed, live or dead; births and deaths occur simultaneously, and the
discrete moment at which this happens is sometimes called a tick. Each generation is a pure function of
the preceding one. The rules continue to be applied repeatedly to create further generations.
*/

#define flagging 0
#define flag(msg) if (flagging) { printf(" _>> %s --\n", msg); }

// ALIVE and NOT_ALIVE can be any char literal or char number
#define ALIVE     '@'
#define NOT_ALIVE '-'

// 	fprintf(stderr,"panic file %s line %d:"msg,__FILE__,__LINE__,args); // this string concatenation thing didnt work, see if you can fix it to get the filename and line number in the panic message
#define panic(msg,args...) {	\
	fprintf(stderr,msg,args);	\
	exit(1);					\
}

static double
rand01() {
  return (double)rand() / (RAND_MAX + 1.0);
}

static std::string
strip_whitespace(std::string s) {
	int start = 0, stop = s.size();
	while (s[start] == '\n' || s[start] == ' ' || s[start] == '\t') start++;
	while (s[stop]  == '\n' || s[stop]  == ' ' || s[stop]  == '\t') stop--;
	return s.substr(start,stop);
}

class life_matrix { // adapted from Jimmy J's answer in https://stackoverflow.com/questions/618511/a-proper-way-to-create-a-matrix-in-c
	std::vector<std::vector<char> >m;
	public:
		life_matrix(int rows, int cols) {
			m.resize(rows, std::vector<char>(cols,NOT_ALIVE));
		}
		life_matrix(int rows, int cols, char default_state) {
			assert(default_state == ALIVE || default_state == NOT_ALIVE);
			m.resize(rows,std::vector<char>(cols,default_state));
		}
		// life_matrix(const life_matrix& to_clone) {
		// 	int n_rows = to_clone.rows(), n_cols = to_clone.cols();
		// 	m.resize(n_rows,std::vector<char>(n_cols));
		// 	for (int i = 0; i < to_clone.rows(); i++) {
		// 		for (int j = 0; j < to_clone.cols(); j++) {
		// 			(*this)[i][j] = to_clone[i][j];
		// 		}
		// 	}
		// }
		life_matrix(std::string filename) {

			std::string file_content;

			if (filename != "") {
				std::ifstream input_source(filename);
				if (!input_source.is_open()) {
					panic("Could not open the file - '%s'\n", filename);
				}
				char c;
				for (std::string line; std::getline(input_source, line); ) {
					file_content += line + "\n";
				}
				input_source.close();
			} else {
				std::string line;
				for ( std::string line; std::getline(std::cin, line); ) {
					file_content += line + "\n";
				}
			}
			
			// file_content = strip_whitespace(file_content);
			
			// std::cout 
			// 	<< "==========================="	<< std::endl
			// 	<< 		file_content				<< std::endl
			// 	<< "==========================="	<< std::endl;
			
			// validating content format
			std::vector<int> row_lengths;
			int row_length = 0;
			#define STATE1 1
			#define STATE2 2
			#define STATE3 3
			#define STATE4 4
			#define STATE5 5
			#define STATEX 6
			
			/* CSV Regex:
				/^[01](,[01])*(\n[01](,[01])*)*\n?/
			
			*/
			
			int STATE = STATE1; // just going through an ad hoc finite automaton that 1) recognized this is in approved format and 2) tracks the rows and row lengths so we can check that the grid is not ragged
			for (char x : file_content) {
				switch(STATE) {
					case STATE1:
						if (x == NOT_ALIVE || x == ALIVE) {
							STATE = STATE2;
						} else {
							STATE = STATEX;
						}
						continue;
					case STATE2:
						if (x == '\n') {
							STATE = STATE4;
							row_lengths.push_back(++row_length);
							row_length = 0;
						} else if (x == ',') {
							STATE = STATE3;
							++row_length;
						} else {
							STATE = STATEX;
						}
						continue;
					case STATE3:
						if (x == NOT_ALIVE || x == ALIVE) {
							STATE = STATE2;
						} else {
							STATE = STATEX;
						}
						continue;
					case STATE4:
						if (x == NOT_ALIVE || x == ALIVE) {
							STATE = STATE2;
						} else {
							STATE = STATEX;
						}
						continue;
					case STATE5:
						panic("%s\n","you reached state 5 and I don't know how to deal with this");
						continue;
					case STATEX:
						STATE = STATEX;
						continue;
					default:
						panic("%s\n","!!");
				}	
			}
			if (STATE == STATE2) { // you ended on an appropriate character, but not a newline so we still need to finish tracking this row length
				row_lengths.push_back(++row_length);
			}

			// std::cout << "STATE: " << STATE << std::endl;
			assert (
				(STATE == STATE4 || STATE == STATE2)
				&& "the grid is not in proper csv format with 0s and 1s as the elements"
			);

			// assert all row lengths are equal, otherwise your csv grid is ragged.
			row_length = row_lengths[0];
			for (int rlen : row_lengths) {
				assert(rlen == row_length && "the grid is ragged. make sure all rows are of the same length");
			}
			
			// now finally fill the matrix with your file read content
			int rows = row_lengths.size(); int cols = row_length;
			m.resize(rows,std::vector<char>(cols));
			int i = 0, j = 0;
			for (char c : file_content) {
				switch(c) {
					case NOT_ALIVE: case ALIVE: 
						m[i][j++] = c;
						continue;
					case ',':
						continue;
					case '\n':
						i++; j = 0;
					default:
						continue;
				}
			}
		}

		const life_matrix
		next_step() {
			double random_genesis_rate = 0;
			return next_step(random_genesis_rate);
		}

		const life_matrix
		next_step(double random_genesis_rate) {
			life_matrix next(rows(),cols());

			for (size_t i = 0; i < rows(); i++) {
				for (size_t j = 0; j < cols(); j++) {
					int c = live_neighbor_count(i,j);
					if ((*this)[i][j] == ALIVE) {
						if (c < 2) {
							next[i][j] = NOT_ALIVE;
						}
						else if (c == 2 || c == 3) {
							next[i][j] = ALIVE;
						}
						else if (c > 3) {
							next[i][j] = NOT_ALIVE;
						} else {
							std::cout << "!!" << std::endl;
							panic("you reached the else case that is supposedly logically impossible to reach. this message is at line %d\n",__LINE__);
						}
					}
					else if ((*this)[i][j] == NOT_ALIVE) {
						if (c == 3) {
							next[i][j] = ALIVE;
						}
						else if (c < 3) {
							next[i][j] = (rand01() < random_genesis_rate) ? ALIVE : NOT_ALIVE;
						}
						else {
							next[i][j] = NOT_ALIVE;
						}
					}
					else {
						std::cout << "!!" << std::endl;
						panic(
							"error: life grid neither ALIVE nor NOT_ALIVE at (i,j)=(%d,%d)=%c\n",
							i, j, (*this)[i][j]
						);
					}
				}
			}
			return next;
		}

		// void
		// import_population(life_matrix& other) {
		// 	int n_cols = std::min(this->cols(), other.cols());
		// 	int n_rows = std::min(this->rows(), other.rows());
		// 	for (int i = 0; i < n_rows; i++) {
		// 		for (int j = 0; j < n_cols; j++) {
		// 			(*this)[i][j] = other[i][j];
		// 		}
		// 	}
		// }

		const int
		number_alive() {
			int count = 0;
			for (int i = 0; i < rows(); i++) {
				for (int j = 0; j < cols(); j++) {
					if (m[i][j] == ALIVE) count++;
				}
			}
			return count;
		}

		class matrix_row {
			std::vector<char>& row;
			public:
				matrix_row(std::vector<char>& r) : row(r) { }
				
				char& operator[](int y) {
					return row.at(y);
				}	
		};
		matrix_row operator[](int x) {
			return matrix_row(m.at(x));
		}
		void print() {
			for (int i = 0; i < m.size(); i++) {
				for (int j = 0; j < m[0].size(); j++) {
					printf("%c ", m[i][j]);
				}
				printf("\n");
			}
		}
		const std::string
		to_string() {
			std::string s("");
			for (int i = 0; i < rows(); i++) {
				for (int j = 0; j < cols(); j++) {
					s += std::string(1,(*this)[i][j])
						+ ((j != cols()-1)
							? ","
							: "");
				}
				s += "\n";
			}
			return strip_whitespace(s);
		}
		void display(){
			// display("Φ"," "); // Ω π
			display("@"," ");
		}
		void // const std::string
		display(std::string alive_marker, std::string not_alive_marker) {

			std::string s(" ");
			std::string line;
			for (int x = 0; x < cols()-1; x++) s += " -"; s += "\n";
			for (int i = 0; i < rows(); i++) {
				line = "|";
				for (int j = 0; j < cols(); j++) {
					line += (
						(m[i][j] == ALIVE) ? alive_marker : not_alive_marker
					) + " ";
				}
				line += "|";
				s += line + "\n";
			}
			s += " "; for (int x = 0; x < cols()-1; x++) s += " -"; s += "\n";
			std::cout << s;
		}
		int const rows() {
			return m.size();
		}
		int const cols() {
			return m[0].size();
		}
		private:
			int const
			live_neighbor_count(int i, int j) { // TODO: group these so you dont have to do 8 3way boolean checks every single time (think about how many checks you can get this to do with different logic grouping setups). maybe have a private method that returns an array of coordinates, which are all of the ones you need to check. then you stop checking when at most you already have at least 3 live neighbors or whatever minimum number you need to check to know whether this cell lives or dies
				int count = 0;

				if( (i-1) >= 0        &&  (j-1) >= 0        && m[i-1][j-1] == ALIVE ) count += 1;
				if( (i-1) >= 0        &&  (j == j)          && m[i-1][ j ] == ALIVE ) count += 1;
				if( (i-1) >= 0        &&  (j+1) < cols()    && m[i-1][j+1] == ALIVE ) count += 1;
				if( (i == i)          &&  (j-1) >= 0        && m[ i ][j-1] == ALIVE ) count += 1;
				if( (i == i)          &&  (j+1) < cols()    && m[ i ][j+1] == ALIVE ) count += 1;
				if( (i+1) < rows()    &&  (j-1) >= 0        && m[i+1][j-1] == ALIVE ) count += 1;
				if( (i+1) < rows()    &&  (j == j)          && m[i+1][ j ] == ALIVE ) count += 1;
				if( (i+1) < rows()    &&  (j+1) < cols()    && m[i+1][j+1] == ALIVE ) count += 1;

				return count;
			}
};

static void
sleep_print(int seconds) {
	for(int s = 0; s < seconds; s++) {
		std::cout << seconds-s << " ";
		sleep_msec(1000);
	}
	std::cout << std::endl;
}

static void
show_usage(char* name) {
	printf("\n  usage: %s [OPTIONS (described below)]\n\n"
				"      [-f | --input-file input_file]                               // file (in proper format) containing the seed image of the game of life. if none provided, seed will be read from stdin\n"
				"      [-n | --steps n]                                             // number of steps of the game of life to apply to the seed\n"
				"      [-o | --output-file output_file]                             // file containing the result of the nth transformation. if none, results will be written to stdout\n"
				"      [-v | --verbose]                                             // show the transformation to the console step by step\n"
				"      [--refresh-rate (msec)]                                      // only relevant for verbose output. time between display of each transformation frame\n"
				"      [--random-genesis-rate (0..1)]                               // the chance between 0 and 1 that a dead cell will spontaneously come to life\n"
				"      [-h | --help]                                                // show this message\n"
				"      [-d | --display-markers '<alive_marker> <not_alive_marker>'] // markers for the animation display. can't use comma as a marker\n"
				"\n",
			name);
	exit(2);
}

int main (int argc, char* argv[]) {
	
	// todo: process enormous images with worker threads
	
	srand(time(nullptr));
	
	int steps = 1;
	bool verbose = false;
	int refresh_rate_msec = 50;
	std::string input_file = "";
	std::string output_file = "";
	double random_genesis_rate = 0;
	std::string alive_marker = "@", not_alive_marker = " ";

	#define cstreq(s1,s2) !strcmp(s1,s2)

	for ( int i = 1; i < argc; ++i ) {
		if (cstreq(argv[i],"-f") || cstreq(argv[i],"--input-file")) {
			if (i+1 == argc) show_usage(argv[0]);
			input_file = argv[++i];
		}
		else if (cstreq(argv[i],"-n") || cstreq(argv[i],"--steps")) {
			if (i+1 == argc) show_usage(argv[0]);
			steps = std::stoi(argv[++i]);
		}
		else if (cstreq(argv[i],"-o") || cstreq(argv[i],"--output-file")) {
			if (i+1 == argc) show_usage(argv[0]);
			output_file = argv[++i];
		}
		else if (cstreq(argv[i],"-v") || cstreq(argv[i],"--verbose")) {
			verbose = true;
		}
		else if (cstreq(argv[i],"--refresh-rate")) {
			if (i+1 == argc) show_usage(argv[0]);
			refresh_rate_msec = std::stoi(argv[++i]);
		}
		else if (cstreq(argv[i],"--random-genesis-rate")) {
			if (i+1 == argc) show_usage(argv[0]);
			random_genesis_rate = std::stod(argv[++i]);
		}
		else if (cstreq(argv[i],"-d") || cstreq(argv[i],"--display-markers")) {
			if (i+1 == argc) show_usage(argv[0]);
			char* items = argv[++i];
			char a[1024], b[1024];

			sscanf(items, "%s %s", a, b);

			alive_marker     = std::string(a);
			not_alive_marker = std::string(b);
		}
		else {
			show_usage(argv[0]);
		}
	}

	life_matrix m(input_file);

	for(int i = 0; i < steps; i++) {
		life_matrix n = m.next_step(random_genesis_rate);
		
		if (verbose) {
			// std::cout << "===============================================" << std::endl;
			/*std::string pretty =*/ n.display(alive_marker, not_alive_marker);
			// std::cout << pretty << std::endl;
			// std::cout << std::endl << std::endl;
			// std::cout << "===============================================" << std::endl;
			sleep_msec(refresh_rate_msec);
		}
	
		m = n;
	}

	if (output_file == "") {
		std::cout << m.to_string();
	}
	else {
		std::ofstream fout(output_file);
		if (!fout.is_open()) {
			panic("%s\n","output stream for your final output file failed to open");
		}
		fout << m.to_string();
		fout.close();
	}

	return 0;
}