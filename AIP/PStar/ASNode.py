from abc import ABCMeta, abstractmethod

#Baseclass the nodes used by the AStar algorithm 

class ASNode(object):
	__metaclass__ = ABCMeta
	
	__init__(self):
		self.state = None
		self.parent = None
		self.children = []
		self.childCost = {}
		self.g = 0
		self.h = 0
		self.hash = None
		
	@abstractmethod
	def generateChildren(self):
		#This method should generate all children with the hash and g values set.
		pass
		
	@abstractmethod
	def calculateHeuristic(self, goal):
		#this method should set and calculate the heuristic
		pass
	
	@abstractmethod
	def generateHash(self):
		#This method should generate and set a unique hash
		pass
	
	def getF(self):
		return self.g + self.h
		
	def addChild(self, child, cost):
		if(not child in self.children):
			self.children.append(child)
			self.childCost.insert(child.hash, cost)