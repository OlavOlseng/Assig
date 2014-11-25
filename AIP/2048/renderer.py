import pygame
from random import randint

white = (255, 255, 255)
dark_blue = (0, 0, 100)
blue = (0, 0, 255)
light_blue = (0, 150, 255)
cyan = (0, 255, 255)
dark_green = (0, 255, 200)
darker_green = (0, 255, 125)
green = (0, 255, 0)
green_yellow = (180, 255, 0)
yellow = (255, 255, 0)
orange = (255, 125, 0)
red = (255, 0, 0)
grey = (125, 125, 125)
black = (0, 0, 0)

colors = [black, dark_blue, blue, light_blue, cyan, dark_green, darker_green, green, green_yellow, yellow, orange, red]

for i in range(50):
	colors.append((randint(0,255),randint(0,255),randint(0,255)))

FONT_SIZE = 32
BOARD_SIZE = 4
PADDING = 16

class Renderer(object):
	def __init__(self, display):
		super(Renderer, self).__init__()
		self.display = display
		self.size = BOARD_SIZE
		self.font = pygame.font.SysFont("monospace", FONT_SIZE, True)
		
	def render(self, board):
		self.display.fill(black)
		box_size = (min(self.display.get_width(), self.display.get_height())) / self.size
		tile_size = box_size - PADDING
		for y in range(self.size):
			for x in range(self.size):
				number = int(board[y][x])
				if (number == 0):
					continue
				
				i = min(number,11)
				color = colors[i]
				rect = pygame.Rect(x * box_size + PADDING / 2, y * box_size + PADDING / 2, tile_size, tile_size)
				pygame.draw.rect(self.display, color, rect)
				
				s_number = str(2**number)
				box_offset = box_size / 2
				label_offset = self.font.size(s_number)
					
				label = self.font.render(s_number, 1, white)
				self.display.blit(label, (x * box_size + box_offset - label_offset[0] / 2, y * box_size + box_offset - label_offset[1] / 2))
		
		pygame.display.flip()
		
		
		
