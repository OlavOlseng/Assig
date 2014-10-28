#nono_main
import util
from gac import GAC
from a_gac import *
import sys
import pygame
from pygame.locals import *
from FFNode import FFNode
import time
from copy import deepcopy

def make(path, gac):
	f = open(path, "r")
	width = 0
	height = 0
	map = []
	
	#generate map
	count = 0
	rows = []
	columns = []
	
	for line in f:
		l = util.parse_line(line)
		if count == 0:
			width = int(l[0])
			height = int(l[1])
			print(l)
		
		elif(count <= width):
			row = []
			for segment in l:
				row.append(int(segment))
			rows.append(row)
			print(row)
			
		else:
			col = []
			for segment in l:
				col.append(int(segment))
			columns.append(col)
			print(col)
		count += 1
		
	bitmap = util.get_bitmap_vector(max(width, height) + 1)
	print()
	
	gac = GAC()
	vars = gen_variables(rows, columns, width, height, bitmap)
	
	#add vars to gac
	for var in vars:
		gac.add_variable(var)
	#gen constraints
	#Cnm = (rn & 2**n) & (cm & 2**m)
	#add constraints
	#return gac
	
def gen_variables(rows, columns, width, height, bitmap):
	vars = []
	max_row_value = bitmap[width]
	max_col_value = bitmap[height]
	
	for i in range(len(rows)):
		name = "r{}".format(i)
		domain = gen_domain(rows[i], width, bitmap)
		vars.append(Variable(name, domain))
	
	for i in range(len(columns)):
		name = "c{}".format(i)
		domain = gen_domain(columns[i], height, bitmap)
		vars.append(Variable(name, domain))
	return variables
	
def gen_domain(segments, max_length, bitmap):
	bits = [0] * max_length
	bit_domain = []
	domain = []
	build_recursive(segments, 0, bits, 0, bit_domain)
	
	for number in bit_domain:
		value = 0
		for bit in range(len(number)):
			if(number[bit]):
				value += bitmap[bit]
		domain.append(value)
	return domain
		

def build_recursive(segments, segment_index, initial_bits, bit_caret, results):
	segment_length = segments[segment_index]
	i = bit_caret
	
	while(i + segment_length <= len(initial_bits)):
		if i + segment_length > len(initial_bits):
			return
		
		bits = deepcopy(initial_bits)
		
		for bit in range(i, i + segment_length):
			bits[bit] = 1

		if (segment_index == len(segments) - 1):
			results.append(bits)
		
		else:
			build_recursive(segments, segment_index + 1, bits, i + segment_length + 1, results)
			
		i += 1

	
if __name__ == "__main__":
	
	print("Starting nono_solver...")

	#read args from commandline
	args = sys.argv[1:]
	print(args)

	#make gac
	gac = make(args[0])
	sys.exit()
	print("Initializng pygame")
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("Nonogram solver: " + args[0])
		
	#setup renderer
	#renderer = FfRenderer(display, size)
	
	#setup agac
	#agac = a_gac(FFNode, renderer)
	
	if ("-m" in args):	
		agac.astar.setMode(int(args[args.index("-m") + 1]))
	
	
	print("Starting loop")
	running = True

	while running:
		for event in pygame.event.get():
			if event.type == QUIT:
				running = False
				pygame.quit()
			elif(event.type == KEYDOWN):
				if(event.key == K_ESCAPE):
					pygame.event.post(pygame.event.Event(QUIT))
				elif(event.key == K_f):
					toggle_fullscreen()
					renderer.display = pygame.display.get_surface()
					pygame.display.flip()
				
				elif(event.key == K_r):
					start = time.clock()
					#Run it!
					#run(agac, gac)
					end = time.clock()
					print("Runtime = {} ms".format(1000 * (end-start)))