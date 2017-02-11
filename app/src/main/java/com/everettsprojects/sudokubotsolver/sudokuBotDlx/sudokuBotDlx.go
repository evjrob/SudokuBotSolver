/*
* This wrapper package is used to overcome the type limitations associated with
* the usage of gobind for android. Since 2D [][]int slices and multiple return
* values are not permitted I needed to create conformant functions. I did not want
* to refactor the go code I had already written because I feel that would make it
* less idiomatic as a go  package simply for the sake of android compatibility.
*
*/

package sudokuBotDlx

import (
  "bytes"
  "strconv"
  "strings"
  "github.com/evjrob/sudokuDlx"
)

// Solve takes a sudoku in the form of a comma separated string of integers where
// zeros are used to indicate a blank. It is also necessary to specify the
// dimensions of a single block in the puzzle (3x3 for a normal sudoku puzzle),
// and to specify the delimiter used to separate the cells of the sudoku.s
func Solve(puzzle string, blockXDim, blockYDim int, delimiter string) string {

  puzzleSlice := convertToSlice(puzzle, blockXDim, blockYDim, delimiter)
  solvedPuzzle, success := sudokuDlx.Solve(puzzleSlice, blockXDim, blockYDim)
  if success {
    solution := convertToString(solvedPuzzle, delimiter)
    return solution
  }

  // An empty string is returned on failure
  return ""
}

// convertToSlice translates the string list representation of the puzzle to a
// 2D int slice.
func convertToSlice(providedPuzzle string, blockXDim, blockYDim int, delimiter string) [][]int {

  // Split the puzzle text into its components
  puzzleElements := strings.Split(providedPuzzle,delimiter)
  puzzleDim := blockXDim * blockYDim
  puzzle := make([][]int, puzzleDim)

  for i := 0; i < puzzleDim; i++ {
    puzzle[i] = make([]int, puzzleDim)
    for j := 0; j < puzzleDim; j++ {
      element := puzzleElements[(i*puzzleDim)+j]
      value, err := strconv.Atoi(element)
      if err == nil {
        puzzle[i][j] = value
      }
    }
  }

  return puzzle
}

// convertToSlice translates the 2D int slice back to a string list representation
func convertToString(puzzle [][]int, delimiter string) string {
  puzzleDim := len(puzzle)
  var buffer bytes.Buffer

  for r := range puzzle {
		for c := range puzzle[r] {
      value := strconv.Itoa(puzzle[r][c])
		  buffer.WriteString(value)
      if !(r == (puzzleDim - 1) && c == (puzzleDim - 1)) {
        buffer.WriteString(delimiter)
      }
		}
	}
  return buffer.String()
}
