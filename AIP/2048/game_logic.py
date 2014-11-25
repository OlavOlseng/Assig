from random import Random
import copy

DIRECTION_UP = 0
DIRECTION_RIGHT = 1
DIRECTION_DOWN = 2
DIRECTION_LEFT = 3

BOARD_STATE_FAILURE = -1
BOARD_STATE_INVALID_MOVE = 0
BOARD_STATE_OK = 1

rand = Random()

def new_board():
	board = [[0 for i in range(4)] for j in range(4)]
	return board

def new_tile(a_board):
	tuples = []
	for y in range(len(a_board)):
		for x in range(len(a_board[0])):
			if a_board[y][x] == 0:
				tuples.append((x,y))
	number = 1
	if rand.random() < 0.1:
		number += 1
	
	tile = tuples[int(rand.random() * len(tuples))]
	a_board[tile[1]][tile[0]] = number
	

def step(direction, a_board):
	board = copy.deepcopy(a_board)
	if direction == DIRECTION_UP:
		move_up(board)
	elif direction == DIRECTION_RIGHT:
		move_right(board)
	elif direction == DIRECTION_DOWN:
		move_down(board)
	elif direction == DIRECTION_LEFT:
		move_left(board)
	else:
		print("Invalid direction!")
	status = check_board(a_board, board)
	if status == BOARD_STATE_OK:
		a_board = board
	return a_board, status

			
def move_up(a_board):
	merged = [[0 for i in range(4)] for j in range(4)]
	for y in range(1, len(a_board)):
		for x in range(len(a_board[0])):
			move_tile_y(a_board, merged, x, y, -1)
					
def move_down(a_board):
	merged = [[0 for i in range(4)] for j in range(4)]
	
	for y in range(len(a_board) - 2, -1, -1):
		for x in range(len(a_board[0])):
			move_tile_y(a_board, merged, x, y, 1)

def move_left(a_board):
	merged = [[0 for i in range(4)] for j in range(4)]
	
	for x in range(1, len(a_board[0])):
		for y in range(len(a_board)):
			move_tile_x(a_board, merged, x, y, -1)
					
def move_right(a_board):
	merged = [[0 for i in range(4)] for j in range(4)]
	
	for x in range(len(a_board[0]) - 2, -1, -1):
		for y in range(len(a_board)):
			move_tile_x(a_board, merged, x, y, 1)

def move_tile_y(a_board, merge_map, x, y, dir):
	current_num = a_board[y][x]
	current_y = y
	if current_num == 0:
		return
	
	while True:
		if current_y + dir < 0 or current_y + dir >= len(a_board):
			break
		if a_board[current_y + dir][x] == 0:
			a_board[current_y + dir][x] = current_num
			a_board[current_y][x] = 0
			current_y += dir
			continue
			
		elif a_board[current_y + dir][x] != current_num:
			break
		
		elif merge_map[current_y + dir][x] == 0:
			a_board[current_y + dir][x] = current_num + 1
			merge_map[current_y + dir][x] = 1
			a_board[current_y][x] = 0
			break
		return
		
def move_tile_x(a_board, merge_map, x, y, dir):
	current_num = a_board[y][x]
	current_x = x
	if current_num == 0:
		return
	while True:
		if current_x + dir < 0 or current_x + dir >= len(a_board[0]):
			break
		if a_board[y][current_x + dir] == 0:
			a_board[y][current_x + dir] = current_num
			a_board[y][current_x] = 0
			current_x += dir
			continue
			
		elif a_board[y][current_x + dir] != current_num:
			break
		
		elif merge_map[y][current_x + dir] == 0:
			a_board[y][current_x + dir] = current_num + 1
			merge_map[y][current_x + dir] = 1
			a_board[y][current_x] = 0
			break
		return

def check_board(pre_move_board, a_board):
	full = True
	for row in a_board:
		if 0 in row:
			full = False
			break
	if full:
		stuck = check_fail_state(a_board)
		if stuck:
			return BOARD_STATE_FAILURE
	
	equal = compare(pre_move_board, a_board)
	if equal:
		return BOARD_STATE_INVALID_MOVE
	
	return BOARD_STATE_OK

def check_fail_state(a_board):
	t1 = copy.deepcopy(a_board)
	t2 = copy.deepcopy(a_board)
	t3 = copy.deepcopy(a_board)
	t4 = copy.deepcopy(a_board)

	move_up(t1)
	move_right(t2)
	move_down(t3)
	move_left(t4)
	
	up = compare(a_board, t1)
	right = compare(a_board, t2)
	left = compare(a_board, t3)
	down = compare(a_board, t4)
	
	return up and left and down and right
	
def compare(pre_move_board, a_board):
	equal = True
	for y in range(len(a_board)):
		for x in range(len(a_board[0])):
			if pre_move_board[y][x] != a_board[y][x]:
				equal = False
				break
	return equal
	
def print_board(a_board):
	print()
	for row in a_board:
		s_row = ""
		for tile in row:
			s_row += "{}\t".format(tile)
		print(s_row)

		
####################### GAME LOOP######################
if __name__ == "__main__":		
	board = new_board()
	text = ""
	new_tile(board)
	print_board(board)

	while text != "b":
		text = input("next move:\n")
		print()
		dir = -1
		if text == "u":
			dir = DIRECTION_UP
		if text == "r":
			dir = DIRECTION_RIGHT
		if text == "d":
			dir = DIRECTION_DOWN
		if text == "l":
			dir = DIRECTION_LEFT
		
		board, state = step(dir, board)
		if state == BOARD_STATE_FAILURE:
			print_board(board)
			print("YOU FAIL!")
			break
		elif state == BOARD_STATE_INVALID_MOVE:
			print("Invalid move!")
		else:
			new_tile(board)
		print_board(board)
