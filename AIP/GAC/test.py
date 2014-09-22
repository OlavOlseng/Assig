from gac import *

vars = {"x":[1,2,3,5], "y":[2,3,4,5,6]}
c1 = Constraint(vars.keys(), "x + 2 == y")
c2 = Constraint(vars.keys(), "2*x <= y")

csp = GAC()
csp.variables = vars
csp.constraints = [c1, c2]

print("Pre-init:")
print("Queue: {}\n".format(csp.queue))

csp.init()

print("Post-init:")
print("Queue: {}\n".format(csp.queue))

csp.run()

print(csp.variables)
