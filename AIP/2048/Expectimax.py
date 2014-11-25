#Expectimax tree

NODE_TYPE_PLAYER = 0
NODE_TYPE_ADVERSARY = 1
NODE_TYPE_RANDOM = 2

class TreeNode(object):
	def __init__(self, type, probability):
		super(object, self).__init__()
		self.children = []
		self.type = type
		self.prob = probability
		
	def evaluate(self):
		pass
		
	def generate_children(self):
		pass
	
	def isTerminal(self):
		pass
	
	
class Tree(object):
	def __init__(self, root, max_depth):
		self.root = root
		self.max_depth = max_depth
		
	def run(self):
		self.root.generate_children()
		best_child = None
		score = -4000000000.0
		for child in self.root.children:
			temp_score = self.__build(child, self.max_depth-1)
			if temp_score > score:
				score = temp_score
				best_child = child
		return best_child
		
	def __build(self, node, depth):
		alpha = 0
		node.generate_children()

		if node.isTerminal() or depth == 0:
			return node.evaluate()
		
		if node.type == NODE_TYPE_ADVERSARY:
			#Return value of minimum-valued child node
			alpha = 2000000.0
			for child in node.children:
				alpha = min(alpha, self.__build(child, depth-1))
		
		elif node.type == NODE_TYPE_PLAYER:
			#Return value of maximum-valued child node
			alpha = -2000000.0
			for child in node.children:
				alpha = max(alpha, self.__build(child, depth-1))
		
		elif node.type == NODE_TYPE_RANDOM:
			#Return weighted average of all child nodes' values
			alpha = 0
			for child in node.children:
				alpha -= child.prob * self.__build(child, depth-1)

		return alpha