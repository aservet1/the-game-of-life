// the game of life dot js

const Matrix = (rows,columns) => {
	return ([0]*columns)*rows
}

const lifeGrid = Matrix(10,10)

console.log(lifeGrid)