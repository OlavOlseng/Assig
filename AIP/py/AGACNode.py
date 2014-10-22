from ASNode import ASNode
from gac import *
from random import randint
from copy import deepcopy, copy

class AGACNode(ASNode, object):
	def __init__(self, gac, step_cost):
		super(AGACNode, self).__init__()
		self.gac = gac
		self.step_cost = step_cost
	
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
			child = self.__class__(new_gac)
			child.hash = child.generateHash()
			#child.g = self.g + self.step_cost
			child.g = 0
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
		pass
		
	def mostPressured(self):
		keys = self.gac.variables.keys()
		mins = []
		min = 1000000
		
		for key in keys:
			domain = self.gac.variables[key].domain
			if(self.gac.variables[key].name[0] == "c"):
				continue
			count = len(domain)
			if count < min and count > 1:
				mins = [key]
				min = count
				
			elif count == min:
				mins.append(key)
				
		#return mins[randint(0,len(mins)-1)]
		return mins[0]
		