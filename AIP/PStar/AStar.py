from ASNode import ASNode


MODE_BEST_FIRST = 0
MODE_DEPTH_FIRST = 1
MODE_BREADTH_FIRST = 2

class AStar(object):
	
	def __init__(self, renderer = None):
		self.OPEN = None
		self.nodes = None
		self.start = None
		self.goal = None
		self.success = False
		self.renderer = renderer
		self.mode = 0

	def initialize(self, start, goal):
		self.success = False

		self.start = start
		self.start.hash = start.generateHash()
		self.start.calculateHeuristic(goal)
		
		self.goal = goal
		self.goal.hash = goal.generateHash()
		
		self.OPEN = []
		self.nodes = {}
		
		self.OPEN.append(start)
		self.nodes[start.hash] =  start
		
	def setMode(self, mode):
		self.mode = mode
	
	def insertIntoOpen(self, node):
		if(self.mode == 0):
			#BEST-FIRST
			self.OPEN.insert(self.binarySearch(self.OPEN, 0, len(self.OPEN), node), node)
		elif(self.mode == 1):
			#DEPTH-FIRST
			self.OPEN.insert(0, node)
		else:
		#BREADTH-FIRST
			self.OPEN.append(node)
		
		
	def binarySearch(self, list, start, end, node):
		if (start == end):
			return start
		
		middle = int((start + end) / 2)
		if (node.getF() <= list[middle].getF()):
			return self.binarySearch(list, start, middle, node)
		else:
			return self.binarySearch(list, middle + 1, end, node)
		
	
	def attachAndEval(self, parent, child):
		child.h = child.calculateHeuristic(self.goal)
		child.g = parent.g + parent.childCost.get(child.hash)
		child.parent = parent
		
	def propagate(self, node):
		g = 0.0
		g = node.g
		
		for child in node.children:
			newG = g + node.childCost[child.hash]
			print(newG - child.g)
			if (newG < child.g):
				print("Found Shorter path")
				child.parent = node
				child.g = newG
				
				if(not child.expanded):
					self.OPEN.remove(child)
					self.insertIntoOpen(child)
					
				propagate(child)
			

	def render(self, node):
		if (not self.renderer is None):
			self.renderer.render(self, node)
			return
			
	def run(self):
		expandedNodes = 0
		print("running")
		if(self.OPEN is None or self.nodes is None or self.start is None or self.goal is None or self.success is True):
			print("Algorithm not properly initialized")
			return
		
		node = None
		
		while(len(self.OPEN) > 0):
			node = self.OPEN.pop(0)
			print("Popped node: {}\tHash: {}\tG: {}\tH: {}\tF: {}".format(expandedNodes, node.hash, node.g, node.h, node.getF()))
			#Check if node was goalnode
			if(node.hash == self.goal.hash):
				print("Success!")
				self.success = True
				self.goal = node
				return node
			
			node.expanded = True
			
			#Expand
			children = node.generateChildren()
			
			for i in range(len(children)):
				freshNode = False
				child = children[i]
				
				#Check if childNode exists
				childNode = self.nodes.get(child.hash)
				
				if (childNode is None):
					#node not found yet, make new one.
					childNode = child
					freshNode = True
				
				node.addChild(childNode, child.g - node.g)
				
				if(freshNode):
					self.attachAndEval(node, childNode)
					self.nodes[childNode.hash] = childNode
					self.insertIntoOpen(childNode)

				elif(childNode.g > node.g + node.childCost.get(childNode.hash)):
					self.attachAndEval(node, childNode)
					
					if(childNode.expanded):
						self.propagate(childNode)
					else:
						self.OPEN.remove(childNode)
						self.insertIntoOpen(childNode)	

			child.expanded = True
			expandedNodes += 1		
			self.render(node)
	