from gac import *
from AStar import *
from AGACNode import AGACNode
from copy import *

class a_gac(object):
	def __init__(self, agac_node_class_pointer, astar_renderer = None):
		self.constraints = None
		self.variables = None
		self.initial_gac = None
		self.agac_node_class_pointer = agac_node_class_pointer
		self.astar = AStar(astar_renderer)
	
	def init(self, gac):
		self.initial_gac = gac
		self.constraints = copy(gac.constraints)
		self.variables = deepcopy(gac.variables)
		gac.init()
		gac.run()
		
	def run(self):
		if self.constraints == None or self.variables == None:
			print("Not properly initialized. Run init() before run()")
			return
		
		current_csp = self.initial_gac
		node = self.generate_anode(current_csp) 
		
		self.astar.init(node)
		
		end = self.astar.run()
		
		return end
		
	def generate_anode(self, gac):
		print("generating S0 from {}".format(self.agac_node_class_pointer))
		return self.agac_node_class_pointer(gac)
		