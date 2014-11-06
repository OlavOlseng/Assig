import game_logic
import pygame
from pygame.locals import *
from renderer import Renderer
import sys
import random
import copy

WIDTH = 800
HEIGHT = 800
AI_MODE = False
CHEAT_MODE = True
renderer = None
move_stack = []

def initialize():
	board = game_logic.new_board()
	game_logic.new_tile(board)
	
	'''
	board[0][0] = 2
	board[0][1] = 4
	board[0][2] = 8
	board[0][3] = 16
	board[1][0] = 32
	board[1][1] = 64
	board[1][2] = 128
	board[1][3] = 256
	board[2][0] = 512
	board[2][1] = 1024
	board[2][2] = 2048
	'''
	
	#render(board)
	AI_MODE = False
	move_stack = []
	return board

def get_move(a_board):
	return random.randint(0,3)
	
def render(board):
	if renderer == None:
		print("no render module!!!")
		return
	renderer.render(board)
	
if (__name__ == "__main__"):
	running = True
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("AStar testing")
	#read args from commandline
	args = sys.argv
	renderer = Renderer(display)
	
	board = initialize()
	
	while running:
		
		direction = None
		for event in pygame.event.get():
			if event.type == QUIT:
				pygame.quit()
			elif(event.type == KEYDOWN):
				if(event.key == K_ESCAPE):
					running = False
					pygame.event.post(pygame.event.Event(QUIT))
				elif(event.key == K_UP):
					direction = game_logic.DIRECTION_UP
				elif(event.key == K_RIGHT):
					direction = game_logic.DIRECTION_RIGHT
				elif(event.key == K_DOWN):
					direction = game_logic.DIRECTION_DOWN
				elif(event.key == K_LEFT):
					direction = game_logic.DIRECTION_LEFT
				elif(event.key == K_RETURN):
					board = initialize()
					move_stack = []
					AI_MODE = False
				elif(event.key == K_a):
					AI_MODE = not AI_MODE
					print("AI_MODE set to: {}".format(AI_MODE))
				elif(event.key == K_z):
					print("CHEATING!")
					if(CHEAT_MODE):
						if len(move_stack) > 0:
							board = move_stack.pop()
		
		if AI_MODE:
			direction = get_move(board)
		if direction != None:
			new_board, status = game_logic.step(direction, board)
			if status == game_logic.BOARD_STATE_OK:
				if CHEAT_MODE:
					move_stack.append(copy.deepcopy(board))
				board = new_board
				game_logic.new_tile(board)
			if status == game_logic.BOARD_STATE_FAILURE:
				AI_MODE = False
		render(board)
