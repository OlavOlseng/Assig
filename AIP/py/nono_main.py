#nono_main
import util
from gac import GAC
from a_gac import *
import sys
import pygame
from pygame.locals import *
import time
from copy import deepcopy
from VCNode import VCNode
from NNRenderer import NNRenderer

WIDTH = 800
HEIGHT = 600

def make(path):
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
			width = int(l[0]) #columns
			height = int(l[1])	#rows
			print(l)
		
		elif(count <= height):
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
		
	bitmap = util.get_bitmap_vector(max(width, height))
	print()
	
	gac = GAC()
	#gen variables
	vars = gen_variables(rows, columns, width, height, bitmap)
	for var in vars:
		gac.add_variable(var)

	#gen constraints
	constraints = gen_constraints(width, height)
	gac.constraints = constraints
	
	return gac, width, height
	
def gen_variables(rows, columns, width, height, bitmap):
	vars = []
	print(len(bitmap))
	
	for i in range(len(rows)):
		name = "r{}".format(height - i - 1)
		domain = gen_domain(rows[i], width, bitmap)
		print("{}: {}".format(name, domain))
		vars.append(Variable(name, domain))
	
	for i in range(len(columns)):
		name = "c{}".format(i)
		columns[i].reverse()
		domain = gen_domain(columns[i], height, bitmap)
		print("{}: {}".format(name, domain))
		vars.append(Variable(name, domain))
	return vars
	
def gen_domain(segments, max_length, bitmap):
	bits = [0] * max_length
	bit_domain = []
	domain = []
	build_recursive(segments, 0, bits, 0, bit_domain)
	for l in bit_domain:
		print(l)
	for number in bit_domain:
		value = 0
		for bit in range(len(number)):
			if(number[bit]):
				value += bitmap[max_length - bit - 1]
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

def gen_constraints(width, height):
	constraints = []
	print("WIDTH: {}\tHEIGHT: {}".format(width, height))
	for x in range(width):
		col = "c{}".format(x)
		row_bit = (width - x - 1)
		for y in range(height):
			col_bit = (y)
			row = "r{}".format(y)
			constraint = "({0} & 2**{2} > 0) == ({1} & 2**{3} > 0)".format(row, col, row_bit, col_bit)
			constraints.append(Constraint([row, col], constraint))
			print(constraint)
	return constraints

def run(agac, csp):
	agac.init(csp)
	end = agac.run()
	renderer.gac_render(end.gac)

	length = end.g/end.step_cost
	expanded = agac.astar.expanded_nodes
	size = len(agac.astar.nodes)
	
	
	print("\nStats:")
	print("Solution length: {}".format(length))
	print("Nodes expanded: {}".format(expanded))
	print("Nodes in tree: {}\n".format(size))	

if __name__ == "__main__":
	
	print("Starting nono_solver...")
	
	
	#sys.exit()
	#read args from commandline
	args = sys.argv[1:]
	print(args)

	#make gac
	gac, width, height = make(args[0])
	
	print("Initializng pygame")
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("Nonogram solver: " + args[0])
		
	#setup renderer
	renderer = NNRenderer(display, width, height)
	renderer.gac_render(gac)
	#setup agac
	agac = a_gac(VCNode, None)
	
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
					run(agac, gac)
					end = time.clock()
					
					print("Runtime = {} ms".format(1000 * (end-start)))