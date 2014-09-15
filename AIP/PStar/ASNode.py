from abc import ABCMeta, abstractmethod

#Baseclass the nodes used by the AStar algorithm 

class ASNode(object):
	__metaclass__ = ABCMeta
	
	__init__(self):
		self.state = None
		self.parent = None
		self.children = []
		self.g = 0
		self.h = 0
		self.f = 0
		self.hash = None
		
	@abstractmethod
	def generateChildren(self):
		pass
		
	@abstractmethod
	def calculateHeuristic(self, goal):
		pass
	
	@abstractmethod
	def generateHash(self):
		pass
	
	def getF(self):
		return self.g + self.h