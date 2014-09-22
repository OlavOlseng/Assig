from gac import *

vars = {"x":[1,2,3,4,5], "y":[2,3,4,5,6], "z":[1,2,3,4,5,6,7,8,9]}
c1 = Constraint(["x","y"], "x + 2 == y")
c2 = Constraint(["x","y"], "2*x <= y")
c3 = Constraint(vars.keys(), "2*x + 2*y <= z")

csp = GAC()
csp.variables = vars
csp.constraints = [c1, c2, c3]

print("Pre-init:")
print("Queue: {}\n".format(csp.queue))

csp.init()

print("Post-init:")
print("Queue: {}\n".format(csp.queue))

csp.run()

print(csp.variables)
