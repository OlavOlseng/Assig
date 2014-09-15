
class AStar(object):
	
	def __init__(self, renderingModule = None):
		self.OPEN = None
		self.nodes = None
		self.start = None
		self.goal = None
		self.success = False
		self.renderingModule = renderingModule
		self.mode = 0

	def initialize(self, start, goal):
		self.success = False

		start.heuristic(goal)
		start.hash = start.generateHash()
		self.start = start
		
		goal.hash = goal.generateHash()
		self.goal = goal
		
		self.OPEN = []
		self.nodes = {}
		
		self.OPEN.append(start)
		self.nodes[start.hash] =  start
		
	def setMode(self, mode):
		self.mode = mode
	
	def insert(self, node):
		if (mode is 0):
			#BEST-FIRST
			self.OPEN.insert(binarySearch(self.OPEN, 0, len(self.OPEN), node)
		
		elif (mode is 1):
			#DEPTH-FIRST
			self.OPEN.insert(0, node)
			
		else:
			#BREADTH-FIRST
			self.OPEN.append(node):
	
	def binarySearch(self, list, start, end, node):
		if (start is end):
			return start
		
		middle = (start + end) / 2
		if (node.f <= list[middle].f):
			return binarySearch(list, start, middle, node)
		else:
			return binarySearch(list, middle + 1, end, node)
		
	
	def run(self):
		expandedNodes = 0
		
		if(self.OPEN is None or self.nodes is None or self.start is None or self.goal is None or self.success is False):
			print "Algorithm not properly initialized"
			return
		
		node = None
		
		while(len(self.OPEN > 0)):
			node = self.OPEN.pop()
			
			#Check if node was goalnode
			if(node.hash is goal.hash):
				self.success = True
				return node
			
			#Expand
			children = node.generateChildren()
			node.children = children
			
			for(i in range(len(children)):
				child = children[i]
				
				#Check if childNode exists
				childNode = self.children.get(child.hash)
				if (childNode is None):
					#node not found yet, make new one.
					childNode = child
				
				
			
				
			
			
			
			
		