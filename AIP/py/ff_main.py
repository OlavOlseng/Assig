from gac import GAC
from a_gac import *
import sys
import pygame
from FfRenderer import FfRenderer
from pygame.locals import *
from FFNode import FFNode
import time

WIDTH = 800
HEIGHT = 600

VARIABLE_TYPE_CELL = 0
VARIABLE_TYPE_PIPE = 1

CELL_TYPE_MIDDLE = 0
CELL_TYPE_EDGE = 1
CELL_TYPE_CORNER = 2

class Ff_var(Variable, object):
	def __init__(self, s_name, l_domain, x, y, v_type):
		super(Ff_var, self).__init__(s_name, l_domain)
		self.x = x
		self.y = y
		self.v_type = v_type
		
def make(path):
	f = open(path, "r")
	count = 0
	size = 0
	colors = 0
	map = []
	
	#generate map
	for line in f:
		l = parse_line(line)
		if count == 0:
			size = int(l[0])
			colors = int(l[1])
			for i in range(size):
				map.append([0] * size)
		else:
			map[int(l[2])][int(l[1])] = int(l[0]) + 1
			map[int(l[4])][int(l[3])] = (int(l[0]) + 1)
		
		count += 1
		
	for row in map:
		print(row)
	print()
	
	gac = GAC()
	gen_variables(gac, map, colors)
	gen_constraints(gac, map)
	
	return gac, size
	
def gen_variables(gac, map, i_colors):
	y_bound = len(map)
	x_bound = len(map[0])
	
	for y in range(y_bound):
		
		for x in range(x_bound):
			type = CELL_TYPE_MIDDLE
			
			#Check if on border:
			if (y == y_bound or y == 0):
				type = CELL_TYPE_EDGE
			elif (x == 0 or  x == x_bound):
				if (type == CELL_TYPE_EDGE):
					type = CELL_TYPE_CORNER
				else:
					type = CELL_TYPE_EDGE
	

			#Generate cell
			domain = []
			if map[y][x] == 0:
				domain = [i + 1 for i in range(i_colors)]
			else:
				domain = [map[y][x]]
			
			c_name = get_cell_name(x,y)
			gac.add_variable(Ff_var(c_name, domain, x, y, VARIABLE_TYPE_CELL))
			
			#Generate pipes
			if(x + 1 < x_bound):
				p_name = get_pipe_name(x,y,x+1,y)
				domain = [0,1]
				gac.add_variable(Ff_var(p_name, domain, 0, 0, VARIABLE_TYPE_PIPE))
			
			if (y + 1 < y_bound):
				p_name = get_pipe_name(x,y,x,y+1)
				domain = [0,1]
				gac.add_variable(Ff_var( p_name, domain, 0, 0, VARIABLE_TYPE_PIPE))

#The meat of the code	
def gen_constraints(gac, map):
	y_bound = len(map)
	x_bound = len(map[0])
	constraints = []
	
	for y in range(y_bound):
		up = y > 0
		down = y+1 < y_bound
		
		for x in range(x_bound):
			left = x > 0
			right = x+1 < x_bound
			
			#define it is a source-/sink cell or normal cell
			isEndpoint = map[y][x] != 0
			
			if down:
				constraints.append(gen_nieghbour_cell_constraint(x, y, x, y + 1))
			if right:
				constraints.append(gen_nieghbour_cell_constraint(x, y, x + 1, y))
				
			constraints += gen_cell_integrity_constraint(x, y, up, down, left, right, isEndpoint)
	gac.constraints = constraints
			
def gen_nieghbour_cell_constraint(x1, y1, x2, y2):
	constraints = []
	c1 = get_cell_name(x1,y1)
	c2 = get_cell_name(x2,y2)
	p = get_pipe_name(x1,y1,x2,y2)
	return Constraint([c1, c2, p], "{} == {} and {} == 1 or {} == 0".format(c1, c2, p, p))

def gen_cell_integrity_constraint(x, y, up, down, left, right, isEndpoint):
	
	c1 = get_cell_name(x,y)
	
	base = "+ {} "
	constraint = ""
	base2 = "+ ({} == {})"
	constraint2 = ""
	
	
	vars = []
	vars2 = [c1]
	if up:
		p = get_pipe_name(x,y-1,x,y)
		vars.append(p)
		constraint += base.format(p)
		
		c2 = get_cell_name(x,y-1)
		vars2.append(c2)
		constraint2 += base2.format(c1, c2)
		
	if down:
		p = get_pipe_name(x,y,x,y+1)
		vars.append(p)
		constraint += base.format(p)
		
		c2 = get_cell_name(x,y+1)
		vars2.append(c2)
		constraint2 += base2.format(c1, c2)
		
	if left:
		p = get_pipe_name(x-1,y,x,y)
		constraint += base.format(p)
		vars.append(p)
		
		c2 = get_cell_name(x-1, y)
		vars2.append(c2)
		constraint2 += base2.format(c1, c2)
		
	if right:
		p = get_pipe_name(x,y,x+1,y)
		vars.append(p)
		constraint += base.format(p)
	
		c2 = get_cell_name(x+1,y)
		vars2.append(c2)
		constraint2 += base2.format(c1, c2)
	
	num = 2
	if (isEndpoint):
		num = 1
	constraint += " == {}".format(num)
	constraint2 += " >= {}".format(num)
	
	print(constraint2)
	
	return [Constraint(vars, constraint[1:]), Constraint(vars2, constraint2[1:])]

def get_cell_name(x, y):
	return "c{}_{}".format(x,y)

def get_pipe_name(x1, y1, x2, y2):
	return "p{}_{}_{}_{}".format(x1,y1,x2,y2)
	
def parse_line(line):
	s = line.strip()
	return s.split(" ")

def run(agac, csp):
	agac.init(csp)
	end = agac.run()
	
	#do the stats
	satisfied = 0
	tot_cons = 0
	uncolored_vars = 0
	
	for c in end.gac.constraints:
		tot_cons += 1
		d = {}
		vars = []
		for var in c.variables:
			var = end.gac.variables[var]
			if len(var.domain) != 1:
				continue
			d[var.name] = var.domain[0]
		if c.f(**d):
			satisfied += 1
	
	for var in end.gac.variables.values():
		if len(var.domain) != 1:
				uncolored_vars += 1
	
	length = end.g/end.step_cost
	expanded = agac.astar.expanded_nodes
	size = len(agac.astar.nodes)
	
	
	print("\nStats:")
	print("Unsatisfied constraints:{}/{}".format(tot_cons - satisfied, tot_cons))
	print("Uncolored nodes {}/{}".format(uncolored_vars, len(end.gac.variables)))
	print("Solution length: {}".format(length))
	print("Nodes expanded: {}".format(expanded))
	print("Nodes in tree: {}\n".format(size))
	
if __name__ == "__main__":
	
	print("Starting ff_solver...")

	#read args from commandline
	args = sys.argv[1:]
	print(args)
	gac, size = make(args[0])
		
	print("Initializng pygame")
	pygame.init()
	display = pygame.display.set_mode((WIDTH, HEIGHT), pygame.DOUBLEBUF | pygame.HWSURFACE)
	pygame.display.set_caption("FlowFree Solution")
	
	renderer = FfRenderer(display, size)
	renderer.gac_render(gac)
	
	agac = a_gac(FFNode, renderer)
	
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
					run(agac, gac)
					end = time.clock()
					print("Runtime = {} ms".format(1000 * (end-start)))