#Manhatten test
import pygame
from pygame.locals import *

import sys

from AStar import AStar
from ManhattenNode import ManhattenNode
from ManhattenRenderer import ManhattenRenderer

WIDTH = 1024
HEIGHT = 720

def parse_file(path):
	f = open("{}.txt".format(path), "r")
	dims = parse_line(f.readline())
	map = create_map(dims[0],dims[1])
	
	dims = parse_line(f.readline())
	start = ManhattenNode(dims[0], dims[1], map)
	
	dims = parse_line(f.readline())
	goal = ManhattenNode(dims[0], dims[1], map)
	
	for line in f:
		insert_box(map, parse_line(line))
		
	return start, goal, map
		
def create_map(width, height):
	map = []
	for i in range(height):
		map.append([0]*width)
		
	return map

def parse_line(line):
	line = line.strip()
	line = line.strip("(")
	line = line.strip(")")
	line = line.split(",")
	dims = [int(x) for x in line]
	return dims
	
def insert_box(map, dims):
	x = dims[0]
	y = dims[1]
	width = dims[2]
	height = dims[3]
	for i in range(height):
		for j in range(width):
			map[y + i][x + j] = 1
			
def run_test(astar, start):
	astar.init(start)
	astar.setMode(0)
	end = astar.run()
	print("Nodes expanded: {}\nFinal path lenghth: {}\n".format(astar.expanded_nodes, end.g))
	astar.init(start)
	astar.setMode(1)
	end = astar.run()
	print("Nodes expanded: {}\nFinal path lenghth: {}\n".format(astar.expanded_nodes, end.g))
	astar.init(start)
	astar.setMode(2)
	end = astar.run()
	print("Nodes expanded: {}\nFinal path lenghth: {}\n".format(astar.expanded_nodes, end.g))
	
	
if (__name__ == "__main__"):
	running = True
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("AStar testing")
	renderer = ManhattenRenderer(display)
	astar = AStar(renderer)
	
	#read args from commandline
	args = sys.argv
	start, goal, map = parse_file(sys.argv[1])
	
	goal.hash = goal.generateHash()
	start.goal = goal
	
	astar.init(start)
	astar.render(start)
	
	fpsClock = pygame.time.Clock()
	
	while running:
		for event in pygame.event.get():
			if event.type == QUIT:
				pygame.quit()
			elif(event.type == KEYDOWN):
				if(event.key == K_ESCAPE):
					pygame.event.post(pygame.event.Event(QUIT))
				elif(event.key == K_r):
					astar.init(start)
					astar.run()
				elif(event.key == K_1):
					astar.setMode(0)
				elif(event.key == K_2):
					astar.setMode(1)
				elif(event.key == K_3):
					astar.setMode(2)
				elif(event.key == K_t):
					run_test(astar, start)
					