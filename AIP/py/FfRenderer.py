from Renderer import Renderer
from AStar import AStar
import pygame
from random import randint

black = (0, 0, 0)
white = (255, 255, 255)
grey = (125, 125, 125)

green = (0, 255, 0)
red = (255, 0, 0)
blue = (0, 0, 255)
yellow = (255, 255, 0)
orange = (255, 127, 0)
purple = (128, 0, 128)

colors = [red, green, blue, yellow, orange, purple]

for i in range(50):
	colors.append((randint(0,255),randint(0,255),randint(0,255)))

class FfRenderer(Renderer, object):
	def __init__(self, display, i_size):
		super(FfRenderer, self).__init__()
		self.display = display
		self.size = i_size
		self.colors = []
		
	def render(self, astar, node):
		self.gac_render(node.gac)
	
	def gac_render(self, gac):
		box_size = min(self.display.get_width(), self.display.get_height()) / self.size
		for var in gac.variables.values():
			if (var.v_type != 0):
				continue
			color = white
			domain_size = len(var.domain)
			if (domain_size == 1):
				color = colors[var.domain[0] - 1]
			elif(domain_size == 0):
				color = black
				
			rect = pygame.Rect(var.x * box_size, var.y * box_size, box_size, box_size)
			pygame.draw.rect(self.display, color, rect)
		
		pygame.display.flip()