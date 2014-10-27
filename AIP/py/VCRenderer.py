from Renderer import Renderer
from AStar import AStar
import pygame

black = (0, 0, 0)
white = (255, 255, 255)
grey = (125, 125, 125)

green = (0, 255, 0)
red = (255, 0, 0)
blue = (0, 0, 255)
yellow = (255, 255, 0)
orange = (255, 127, 0)
purple = (128, 0, 128)
cyan = (0, 255, 255)
brown = (128, 64, 0)

colors = [red, green, blue, yellow, orange, purple, cyan, brown]

class VCRenderer(Renderer, object):
	def __init__(self, display):
		super(VCRenderer, self).__init__()
		self.display = display
		self.x_offset = 100000.0
		self.y_offset = 100000.0
		self.x_max = 0.0
		self.y_max = 0.0
		self.needs_init = True
	
	def set_offsets(self, node):
		for variable in node.gac.variables.values():
				self.x_offset = min(variable.x, self.x_offset)
				self.y_offset = min(variable.y, self.y_offset)
				self.x_max = max(variable.x, self.x_max)
				self.y_max = max(variable.y, self.y_max)
				
		self.needs_init = False
		print("Xoff: {} \tYoff: {}".format(self.x_offset, self.y_offset))
		print("Xmax: {} \tYmax: {}".format(self.x_max, self.y_max))
	
	def get_point(self, variable, x_scale, y_scale, offset):
		return (int((variable.x - self.x_offset) * x_scale) + offset, int((variable.y - self.y_offset) * y_scale) + offset)
	
	def render(self, astar, node):
		init = False
		if(self.needs_init):
			self.set_offsets(node)
			init = True
			self.display.fill(white)
		radius = 10
		
		#self.display.fill(white)
		
		x_spread = (self.x_max - self.x_offset)
		y_spread = (self.y_max - self.y_offset)
		x_scale = (self.display.get_width() - 2*radius) / x_spread
		y_scale = (self.display.get_height() - 2*radius) / y_spread
		if init:
			for constraint in node.gac.constraints:
				var1 = node.gac.variables[constraint.variables[0]]
				var2 = node.gac.variables[constraint.variables[1]]
				point1 = self.get_point(var1, x_scale, y_scale, radius)
				point2 = self.get_point(var2, x_scale, y_scale, radius)
				pygame.draw.line(self.display, black, point1, point2)

		for variable in node.gac.variables.values():
			color = grey
			if len(variable.domain) == 0:
				color = black
			elif len(variable.domain) == 1:
				color = colors[variable.domain[0]]
			
			point = self.get_point(variable, x_scale, y_scale, radius)
			pygame.draw.circle(self.display, color,  point, radius)
		
		color = black
		pygame.transform.scale
		pygame.display.flip()
		
		
		
		
			
			