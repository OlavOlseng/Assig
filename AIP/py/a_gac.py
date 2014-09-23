from gac import *
from AStar import *
from VCSPNode import VCSPNode
from copy import *

class a_gac(object):
	def __init__(self, gac_node_class_pointer, astar_renderer = None):
		self.constraints = None
		self.variables = None
		self.initial_gac = None
		self.gac_node_class_pointer = gac_node_class_pointer
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
		
		k = 0
		
		#check success/failure condition
		end = self.astar.run()
		
		return end
		
	def generate_anode(self, gac):
		print("generating S0 from {}".format(self.gac_node_class_pointer))
		return self.gac_node_class_pointer(gac)
		