#Manhatten test
from AStar import AStar
from ManhattenNode import ManhattenNode
from ManhattenRenderer import ManhattenRenderer

map = [[0]*100000]*100000
start = ManhattenNode(0,0,map)
goal = ManhattenNode(99999,99999,map)
rend = ManhattenRenderer()
ast = AStar(rend)
ast.initialize(start, goal)
ast.run()