from ASNode import ASNode
from gac import *
from random import randint
from copy import deepcopy, copy

step_cost = 10.0

class VCSPNode(ASNode, object):
	def __init__(self, gac):
		super(VCSPNode, self).__init__()
		self.gac = gac
	
	def generateChildren(self):
		if self.gac.is_success() == -1:
			return []
		children = []
		key = self.mostPressured()
		
		for val in self.gac.variables[key].domain:
			new_gac = GAC()
			new_gac.variables = deepcopy(self.gac.variables)
			new_gac.constraints = self.gac.constraints
			new_gac.variables[key].domain = [val]
			new_gac.rerun(key)
			child = VCSPNode(new_gac)
			child.hash = child.generateHash()
			child.g = self.g + 1 * step_cost
			children.append(child)
			
		return children
		
	def generateHash(self):
		s = ""
		for key in sorted(self.gac.variables):
			s += "{}:{},".format(key, self.gac.variables[key].domain)
			
		#print("HASH: {}".format(s))
		self.hash = s
		return self.hash
		
	def is_goal_node(self):
		return self.gac.is_success() == 1
		
	def calculateHeuristic(self):
		h = 0.0
		vars = len(self.gac.variables)

		for var in self.gac.variables:
			domain = self.gac.variables[var].domain
			count = len(domain)
			if (count == 0):
				h += 1000 * step_cost
			else:
				#h += step_cost
				h += count * step_cost
			
		h -= vars * step_cost
		self.h = float(h*step_cost)
		#self.h = float(h)
		return self.h
		
	def mostPressured(self):
		keys = self.gac.variables.keys()
		mins = []
		min = 1000000
		
		for key in keys:
			domain = self.gac.variables[key].domain
			count = len(domain)
			if count < min and count > 1:
				mins = [key]
				min = count
				
			elif count == min:
				mins.append(key)
				
		return mins[randint(0,len(mins)-1)]
		