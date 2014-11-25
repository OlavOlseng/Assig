#gamenode
from Expectimax import *
import game_logic
from math import sqrt

def generate_children(board, prob, type):
	if prob < 0.01:
		return [], [], [], -1
	children = []
	moves = []
	probs = []
	child_type = -1
	
	if type == NODE_TYPE_PLAYER:
		child_type = NODE_TYPE_RANDOM
		t1 = [i[:] for i in board]
		game_logic.move_up(t1)
		if game_logic.check_board(board, t1) == 1:
			children.append(t1)
			moves.append(0)
			probs.append(0.25)
		
		t2 = [i[:] for i in board]
		game_logic.move_right(t2)
		if game_logic.check_board(board, t2) == 1:
			children.append(t2)
			moves.append(1)
			probs.append(0.25)
		
		t3 = [i[:] for i in board]
		game_logic.move_down(t3)
		if game_logic.check_board(board, t3) == 1:
			children.append(t3)
			moves.append(2)
			probs.append(0.25)
			
		t4 = [i[:] for i in board]
		game_logic.move_left(t4)
		if game_logic.check_board(board, t4) == 1:
			children.append(t4)
			moves.append(3)
			probs.append(0.25)
			
	else:
		child_type = NODE_TYPE_PLAYER
		zeroes = 0
		for i in board:
			zeroes += i.count(0)
		for y in range(len(board)):
			for x in range(len(board[0])):
				
				if(board[y][x] == 0):
					t1 = [i[:] for i in board]
					t2 = [i[:] for i in board]
					
					t1[y][x] = 1
					t2[y][x] = 2
					
					children.append(t1)
					probs.append(0.9/float(zeroes))
					moves.append(-1)
					children.append(t2)
					probs.append(0.1/float(zeroes))
					moves.append(-1)
	return children, probs, moves, child_type
	
def evaluate(board):
	#heuristic function
	mono = [0]*8
	free = 0
	smooth = 0
	'''
	flat_scale = 1
	
	
	r = 0.25
	x = 0
	y = 0
	y_dir = 1
	x_dir = 1
	while y < len(board):
		while x < len(board[0]) and x >= 0:
			if board[y][x] == 0:
				free += 1
			mono[0] += function(board[y][x], r, flat_scale)
			mono[1] += function(board[x][y], r, flat_scale)
			mono[2] += function(board[y][3-x], r, flat_scale)
			mono[3] += function(board[x][3-y], r, flat_scale)
			mono[4] += function(board[3-y][x], r, flat_scale)
			mono[5] += function(board[3-x][y], r, flat_scale)
			mono[6] += function(board[3-y][3-x], r, flat_scale)
			mono[7] += function(board[3-x][3-y], r, flat_scale)
			r = r**2
			x += x_dir
			
		x -= x_dir
		x_dir = x_dir * -1
		y += y_dir
	'''
	monotonity = [0]*4
	r = 0.25
	for y in range(len(board)):
		for x in range(len(board[0])):
			if board[y][x] == 0:
				free += 1
			monotonity[0] += r**((x) + (y)) * 2**board[y][x]
			monotonity[1] += r**((3-x) + (y)) * 2**board[y][x]
			monotonity[2] += r**((3-x) + (3-y)) * 2**board[y][x]
			monotonity[3] += r**((x) + (3-y)) * 2**board[y][x]
	
	smooth = 0
	for y in range(1,len(board)):
		for x in range(len(board[0])):
			if board[y][x] != 0:
				smooth += (board[y][x] == board[y-1][x]) * 2**board[y][x]
	
	for y in range(len(board)):
		for x in range(1,len(board[0])):
			if board[y][x] != 0:
				smooth += (board[y][x] == board[y][x-1]) * 2**board[y][x]
		
	#print("Mono: {}, Free: {}, Smooth: {}".format(max(mono), free, smooth))
	#return 5*max(mono) + free + smooth
	
	#print("Mono: {}, Free: {}, Smooth: {}".format(monotonity, free, smooth))
	#return 0.3*monotonity + 10*free + 5*smooth + 100*max(mono)
	return 4*max(monotonity) + 2*free + 0.5*smooth
	
def function(val, lf, scale):
	return 2**val * lf * scale