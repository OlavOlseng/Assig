#gamenode
from Expectimax import *
import game_logic
import copy
from math import sqrt

class Node2048(TreeNode):
	def __init__(self, type, probability, board, dir):
		super(Node2048, self).__init__(type, probability)
		self.board = board
		self.dir = dir
		
	def generate_children(self):
		if self.prob <= 0.1:
			return
		if self.type == NODE_TYPE_PLAYER:
			child_type = NODE_TYPE_ADVERSARY
			
			t1 = copy.deepcopy(self.board)
			game_logic.move_up(t1)
			if game_logic.check_board(self.board, t1) == 1:
				self.children.append(Node2048(child_type, 0.25, t1, 0))
			
			t2 = copy.deepcopy(self.board)
			game_logic.move_right(t2)
			if game_logic.check_board(self.board, t2) == 1:
				self.children.append(Node2048(child_type, 0.25, t2, 1))
			
			t3 = copy.deepcopy(self.board)
			game_logic.move_down(t3)
			if game_logic.check_board(self.board, t3) == 1:
				self.children.append(Node2048(child_type, 0.25, t3, 2))
			
			t4 = copy.deepcopy(self.board)
			game_logic.move_left(t4)
			if game_logic.check_board(self.board, t4) == 1:
				self.children.append(Node2048(child_type, 0.25, t4, 3))
			
		else:
			child_type = NODE_TYPE_PLAYER
			
			for y in range(len(self.board)):
				for x in range(len(self.board[0])):
					
					if(self.board[y][x] == 0):
						t1 = copy.deepcopy(self.board)
						t2 = copy.deepcopy(self.board)
						
						t1[y][x] = 1
						t2[y][x] = 2
						
						self.children.append(Node2048(child_type, 0.9, t1, -1))
						self.children.append(Node2048(child_type, 0.1, t2, -1))
			
	def isTerminal(self):
		return len(self.children) == 0
		
	def evaluate(self):
		#heuristic function
		mono = [0]*8
		free = 0
		smooth = 0
		flat_scale = 1
		
		
		r = 0.66
		x = 0
		y = 0
		y_dir = 1
		x_dir = 1
		while y < len(self.board):
			while x < len(self.board[0]) and x >= 0:
				if self.board[y][x] == 0:
					free += 1
				mono[0] += function(self.board[y][x], r, flat_scale)
				mono[1] += function(self.board[x][y], r, flat_scale)
				mono[2] += function(self.board[y][3-x], r, flat_scale)
				mono[3] += function(self.board[x][3-y], r, flat_scale)
				mono[4] += function(self.board[3-y][x], r, flat_scale)
				mono[5] += function(self.board[3-x][y], r, flat_scale)
				mono[6] += function(self.board[3-y][3-x], r, flat_scale)
				mono[7] += function(self.board[3-x][3-y], r, flat_scale)
				r = r**2
				x += x_dir
				
			x -= x_dir
			x_dir = x_dir * -1
			y += y_dir
		
		num = 0
		cx = 0
		cy = 0
		for y in range(len(self.board)):
			for x in range(len(self.board[0])):
				number = self.board[y][x]
				if number > num:
					num = number
					cx = x
					cy = y
		monotonity = 0
		for y in range(len(self.board)):
			for x in range(len(self.board[0])):
				monotonity += (sqrt((cx - x)**2 + (cy - y)**2)) / 4.0 * self.board[y][x]
		
		smooth = 0
		for y in range(1,len(self.board)):
			for x in range(len(self.board[0])):
				if self.board[y][x] != 0:
					smooth += (self.board[y][x] == self.board[y-1][x]) * self.board[y][x]
		
		for y in range(len(self.board)):
			for x in range(1,len(self.board[0])):
				if self.board[y][x] != 0:
					smooth += (self.board[y][x] == self.board[y][x-1]) * 2**self.board[y][x]
			
		#print("Mono: {}, Free: {}, Smooth: {}".format(max(mono), free, smooth))
		#return 5*max(mono) + free + smooth
		
		print("Mono: {}, Free: {}, Smooth: {}".format(monotonity, free, smooth))
		return monotonity + free + smooth + 2*max(mono)
		#return 0.5*monotonity + 10*free + 5*smooth
		
def function(val, lf, scale):
	return 2**val * lf * scale