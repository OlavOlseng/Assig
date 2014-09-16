from ASNode import ASNode

stepSize = 10.0
	
class ManhattenNode(ASNode, object):
	def __init__(self, x, y, map):
		super(ManhattenNode, self).__init__()
		self.x = x
		self.y = y
		self.map = map
		self.height = len(map)
		self.width = len(map[0])
		
	def generateChildren(self):
		children = []
		
		newY = self.y - 1
		if (newY >= 0 and self.map[newY][self.x] != 1):
			child = ManhattenNode(self.x, newY, self.map)
			child.generateHash()
			child.g = float(self.g + stepSize)
			children.append(child)
			
		newY = self.y + 1
		if(newY < self.height and self.map[newY][self.x] != 1):
			child = ManhattenNode(self.x, newY, self.map)
			child.generateHash()
			child.g = float(self.g + stepSize)
			children.append(child)
			
		newX = self.x - 1
		if(newX >= 0 and self.map[self.y][newX] != 1):
			child = ManhattenNode(newX, self.y, self.map)
			child.generateHash()
			child.g = float(self.g + stepSize)
			children.append(child)
		
		newX = self.x + 1
		if(newX < self.width and self.map[self.y][newX] != 1):
			child = ManhattenNode(newX, self.y, self.map)
			child.generateHash()
			child.g = float(self.g + stepSize)
			children.append(child)

		return children
		
	def generateHash(self):
		self.hash = "{},{}".format(self.x, self.y)
		return self.hash
		
	def calculateHeuristic(self, goal):
		self.h = float(abs(goal.x - self.x) + abs(goal.y - self.y)) * stepSize
		return self.h
		