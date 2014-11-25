import game_logic
import pygame
from pygame.locals import *
from renderer import Renderer
import sys
import random
from Node2048 import *
import Expectimax
import time

WIDTH = 800
HEIGHT = 800
AI_MODE = False
CHEAT_MODE = False
renderer = None
move_stack = []

search_depth = 4

def initialize():
	board = game_logic.new_board()
	game_logic.new_tile(board)
	global search_depth
	search_depth = 3
	
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
	threshhold = 0.300
	elapsed = 0
	move = None
	global search_depth
	start = time.time()
	while(1):
		move = Expectimax.run(a_board, search_depth, generate_children, evaluate)
		elapsed = (time.time() - start)
		#if move < 0:
		#	break
		#print(elapsed)
		#if(elapsed < threshhold/20.0):
		#	search_depth += 1
		#elif(elapsed > threshhold):
		#	search_depth -= 1
		#	break
		#else:
		break
	#print(search_depth)
	if move != None:
		return move
	else:
		print("Failed to find move!")
		return random.randint(0,3)
	return random.randint(0,3)
	
def render(board):
	if renderer == None:
		print("no render module!!!")
		return
	renderer.render(board)
	
def loop():
	board = initialize()
	search_depth = 3
	AI_MODE = False
	running = True
	direction = None
	attempts = 0
	wins = 0
	while running:
	
		
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
					move_stack.append([i[:] for i in board])
				board = new_board
				game_logic.new_tile(board)
			if status == game_logic.BOARD_STATE_FAILURE:
				print("Game Over")
				attempts += 1
				board = initialize()
				#AI_MODE = False
		direction = None
		for i in board:
			if i.count(11) > 0:
				attempts += 1
				wins += 1
				board = initialize()
		
		render(board)
	print("Winrate: {} / {}".format(wins, attempts))
		
if (__name__ == "__main__"):
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("AStar testing")
	#read args from commandline
	args = sys.argv
	renderer = Renderer(display)
	
	loop()
	
