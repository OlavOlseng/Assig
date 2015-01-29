#Expectimax tree

NODE_TYPE_PLAYER = 0
NODE_TYPE_ADVERSARY = 1
NODE_TYPE_RANDOM = 2


def run(root, depth, generate_children, evaluate):
	children, probs, moves, child_type = generate_children(root, 0.25, NODE_TYPE_PLAYER)
	move = -1
	score = -4000000000.0
	for i in range(len(children)):
		temp_score = __build(children[i], depth-1, probs[i], generate_children, evaluate, child_type)
		if temp_score > score:
			score = temp_score
			move = moves[i]
	return move
	
def __build(node, depth, prob, generate_children, evaluate, type):
	alpha = 0
	children, probs, moves, child_type = generate_children(node, prob, type)

	if len(children) == 0 or depth == 0:
		return evaluate(node)
	
	if type == NODE_TYPE_ADVERSARY:
		#Return value of minimum-valued child node
		alpha = 2000000.0
		for i in range(len(children)):
			alpha = min(alpha, __build(children[i], depth-1, probs[i], generate_children, evaluate, child_type))
	
	elif type == NODE_TYPE_PLAYER:
		#Return value of maximum-valued child node
		alpha = -2000000.0
		for i in range(len(children)):
			alpha = max(alpha, __build(children[i], depth-1, probs[i], generate_children, evaluate, child_type))
	
	elif type == NODE_TYPE_RANDOM:
		#Return weighted average of all child nodes' values
		alpha = 0
		for i in range(len(children)):
			alpha += probs[i] * __build(children[i], depth-1, probs[i], generate_children, evaluate, child_type)
	children = []	
	return alpha
