from Renderer import Renderer
from AStar import AStar
import pygame

white = (255, 255, 255)
green = (0, 255, 0)
red = (255, 0, 0)
blue = (0, 0, 255)
black = (0, 0, 0)
grey = (125, 125, 125)

class ManhattenRenderer(Renderer, object):
	def __init__(self, display):
		super(ManhattenRenderer, self).__init__()
		self.display = display
	
	def render(self, astar, node):
		self.display.fill(white)
		box_size = min(self.display.get_width(), self.display.get_height()) / max(node.width, node.height)
		
		for y in range(node.height):
			for x in range(node.width):
				if(node.map[y][x] == 1):
					rect = pygame.Rect(x * box_size, y * box_size, box_size, box_size)
					pygame.draw.rect(self.display, black, rect)
		
		rect = pygame.Rect(node.goal.x * box_size, node.goal.y * box_size, box_size, box_size)
		pygame.draw.rect(self.display, green, rect)
		
		while(node != None):
			rect = pygame.Rect(node.x * box_size, node.y * box_size, box_size, box_size)
			pygame.draw.rect(self.display, red, rect)
			node = node.parent
		
		
		pygame.display.flip()
		
		
		
