// const words = [
//     ['actions',     'ideas',        'words'],
//     ['medtitation', 'embarrassment','six'  ],
//     ['perspiration','serenity',     'seven']
// ]
const ALIVE_COLOR     = 'turquoise'
const NOT_ALIVE_COLOR = 'khaki'

const ALIVE = 1;
const NOT_ALIVE = 0;

function renderGrid(grid) {
    function tohtmltable(twoDarray) {
        const width  = 10
        const height = 10
        let html = '<table>'
        for(let i in twoDarray) {
            html += '<tr>'
            for (let j in twoDarray[i]) {
                let color = undefined
                if (NOT_ALIVE == grid[i][j]) {
                    color = NOT_ALIVE_COLOR
                }
                else if (ALIVE == grid[i][j]) {
                    color = ALIVE_COLOR
                }
                html += `
                    <td
                        style   = 'background-color:${color}'
                        width   = '${width}'
                        height  = '${height}'
                        onclick = '
                            matrix[${i}][${j}] = (${NOT_ALIVE} === matrix[${i}][${j}]) 
                                ? ${ALIVE}
                                : ${NOT_ALIVE}
                            renderGrid(matrix)
                        '
                    />
                `
            }
            html += '</tr>'
        }
        html += '</table>'
        return html
    }
    document.getElementById('LifeGrid').innerHTML = tohtmltable(grid)
}

function isAlive(cell) {
    if (cell === NOT_ALIVE) {
        return false
    }
    return true
}
function new2dArray(height,width) {
    return Array.from (
        Array(height),
        (_,i) => new Array(width)
    )
}
function neighbors(grid,i,j) {
    return [
        [i-1,j-1], [i-1,j], [i-1,j+1],
         [i,j-1],            [i,j+1],
        [i+1,j-1], [i+1,j], [i+1,j+1]
    ].filter(
        neighbor => {
            const [i,j] = neighbor
            if(0 <= i) {
                if (i <= grid.length-1) {
                    if (0 <= j) {
                        if (j <= grid[i].length-1) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    )
}
function nextStep(grid) {
    const nextGrid = new2dArray(grid.length, grid[0].length)
    for(let i = 0; i < grid.length; i++) {
        for (let j = 0; j < grid[i].length; j++) {
            const alive_count = neighbors(grid,i,j).reduce(
                (count,c) => {
                    if(isAlive(grid[c[0]][c[1]])) {
                        return count + 1
                    }
                    else {
                        return count
                    }
                }, 0
            )
            if (isAlive(grid[i][j])) {
                if (alive_count < 2 || alive_count > 3) {
                    nextGrid[i][j] = NOT_ALIVE
                }
                else if (alive_count === 2 || alive_count === 3) {
                    nextGrid[i][j] = ALIVE
                }
            } else {
                if (alive_count == 3) {
                    nextGrid[i][j] = ALIVE
                }
                else {
                    nextGrid[i][j] = NOT_ALIVE
                }
            }
        }
    }
    return nextGrid
}

// var matrix = [
//     [0,0,1,0,0,1,  1,0,1,0,0,1], 
//     [1,0,0,1,0,1,  1,0,0,1,0,1], 
//     [0,1,1,1,1,1,  1,1,1,1,1,1], 
//     [0,0,1,1,0,1,  1,0,1,1,0,1], 
//     [0,0,1,0,0,1,  1,0,1,0,0,1], 
//     [0,0,0,1,0,1,  1,0,0,1,0,1], 
                                
//     [1,0,1,0,0,1,  1,0,1,0,0,0], 
//     [1,0,0,1,0,1,  1,0,0,1,0,0], 
//     [1,1,1,1,1,1,  1,1,1,1,1,0], 
//     [1,0,1,1,0,1,  1,0,1,1,0,0], 
//     [1,0,1,0,0,1,  1,0,1,0,0,1], 
//     [1,0,0,1,0,1,  1,0,0,1,0,0], 
// ]

var matrix = new2dArray(45,45)
for (let i = 0; i < matrix.length; i++) {
    for (let j = 0; j < matrix[i].length; j++) {
        matrix[i][j] = NOT_ALIVE
    }
}

renderGrid(matrix)
document.getElementById('NextStepButton').onclick = (
    function() {
        matrix = nextStep(matrix)
        renderGrid(matrix)
    }
)