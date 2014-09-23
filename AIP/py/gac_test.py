from gac import *

vars = [Variable("x",[1,2,3,4,5]), Variable("y",[2,3,4,5,6]), Variable("z",[1,2,3,4,5,6,7,8,9])]

c1 = Constraint(["x","y"], "x + 2 == y")
c2 = Constraint(["x","y"], "2*x <= y")
c3 = Constraint(["x","y","z"], "2*x + 2*y <= z")

csp = GAC()
for v in vars:
	csp.add_variable(v)
	
csp.constraints = [c1, c2, c3]

print("Pre-init:")
print("Queue: {}\n".format(csp.queue))

csp.init()

print("Post-init:")
print("Queue: {}\n".format(csp.queue))

csp.run()

print(csp.variables)
