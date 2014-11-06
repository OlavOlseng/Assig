from Renderer import Renderer
from AStar import AStar
import pygame
from random import randint

black = (0, 0, 0)
white = (255, 255, 255)
grey = (125, 125, 125)

green = (0, 255, 0)
red = (128, 0, 0)
blue = (0, 0, 128)
yellow = (255, 255, 0)
orange = (255, 127, 0)
purple = (128, 0, 128)


class NNRenderer(Renderer, object):
	def __init__(self, display, i_width, i_height):
		super(NNRenderer, self).__init__()
		self.display = display
		self.width = i_width
		self.height = i_height
		
	def render(self, astar, node):
		self.gac_render(node.gac)
	
	def gac_render(self, gac):
		map = []
		for y in range(self.height):
			map.append([0]*self.width)
		box_size = min(self.display.get_width(), self.display.get_height()) / max(self.width, self.height)
		
		for var in gac.variables.values():
			if var.name[0] == "c" and len(var.domain) == 1:
				x = int(var.name[1:])
				for y in range(self.height):
					if (var.domain[0] & 2**y) > 0:
						map[y][x] += 1
			
			elif var.name[0] == "r" and len(var.domain) == 1:
				y = int(var.name[1:])
				for x in range(self.width):
					if (var.domain[0] & 2**(self.width - x - 1)) > 0:
						map[y][x] += 2
						
		for y in range(len(map)):
			for x in range(len(map[0])):
				color = white
				val = map[y][x]
				if(val == 2):
					color = blue
				elif(val == 1):
					color = red
				elif(val == 3):
					color = purple
				rect = pygame.Rect(x * box_size, y * box_size, box_size, box_size)
				pygame.draw.rect(self.display, color, rect)
		
		pygame.display.flip()