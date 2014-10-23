from gac import GAC
from VCNode import *
from a_gac import *
from VCRenderer import VCRenderer
import sys
import pygame
from pygame.locals import *
from threading import *


WIDTH = 1024
HEIGHT = 720

class Vc_var(Variable, object):
	def __init__(self, s_name, l_domain, x, y):
		super(Vc_var, self).__init__(s_name, l_domain)
		self.x = x
		self.y = y
		

def make_csp(path, colors):
	gac = GAC()

	f = open(path, "r")
	
	NV = 0
	NE = 0
	
	count = 1
	for line in f:
		l = parse_line(line)
		print(l)
		if count == 1:
			NV = int(l[0])
			NE = int(l[1])
			
		elif count > 1 and count <= NV + 1:
			print("Making var...")
			name = "v{}".format(l[0])
			print(name)
			print("")
			domain = [i for i in range(colors)]
			gac.add_variable(Vc_var(name, domain, float(l[1]), float(l[2])))
			
		else:
			print("Making constraint...\n")
			gac.add_constraint(gen_constraint(l[0], l[1]))

		count += 1
	
	return gac
		
def parse_line(line):
	s = line.strip()
	return s.split(" ")

def gen_constraint(v1_index, v2_index):
	n1 = "v{}".format(v1_index)
	n2 = "v{}".format(v2_index)
	return Constraint([n1, n2], "{} != {}".format(n1, n2))
	
def toggle_fullscreen():
    screen = pygame.display.get_surface()
    tmp = screen.convert()
    caption = pygame.display.get_caption()
    
    w,h = screen.get_width(),screen.get_height()
    flags = screen.get_flags()
    bits = screen.get_bitsize()
    
    pygame.display.quit()
    pygame.display.init()
    
    screen = pygame.display.set_mode((w,h),flags^FULLSCREEN,bits)
    screen.blit(tmp,(0,0))
    pygame.display.set_caption(*caption)
 
    pygame.key.set_mods(0) #HACK: work-a-round for a SDL bug??
 
    return screen
	
def run(agac):
	agac.init(csp)
	end = agac.run()
	
	#do the stats
	satisfied = 0
	tot_cons = 0
	uncolored_vars = 0
	
	for c in end.gac.constraints:
		tot_cons += 1
		d = {}
		for var in c.variables:
			var = end.gac.variables[var]
			if len(var.domain) != 1:
				continue
			d[var.name] = var.domain[0]
		if len(d) != 2:
			continue
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
	args = sys.argv[1:]
	renderer = None
	
	csp = make_csp(args[0], int(args[1]))
	pygame.init()
	
	display = pygame.display.set_mode((WIDTH, HEIGHT), HWSURFACE|DOUBLEBUF)
	pygame.display.set_caption("VC A*-GAC testing")	
	if("-nr" not in args):
		renderer = VCRenderer(display)
	
	agac = a_gac(VCNode, renderer)
	
	if ("-m" in args):	
		agac.astar.setMode(int(args[args.index("-m") + 1]))
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
					run(agac)
					
					
