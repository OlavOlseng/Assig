from gac import *
import sys

class Vc_var(Variable, object):
	def __init__(self, s_name, l_domain, x, y):
		super(Vc_var, self).__init__(s_name, l_domain)
		self.x = x
		self.y = y
		

def make_csp(path, colors):
	gac = GAC()

	f = open(path, "r")
	
	NV = 0
	NE = 0
	
	count = 1
	for line in f:
		l = parse_line(line)
		print(l)
		if count == 1:
			NV = int(l[0])
			NE = int(l[1])
			
		elif count > 1 and count <= NV + 1:
			print("Making var...")
			name = "v{}".format(l[0])
			print(name)
			print()
			domain = [i for i in range(colors)]
			gac.add_variable(Vc_var(name, domain, float(l[1]), float(l[2])))
			
		else:
			print("Making constraint...\n")
			gac.add_constraint(gen_constraint(l[0], l[1]))

		count += 1
	
	return gac
		
def parse_line(line):
	s = line.strip()
	return s.split(" ")

def gen_constraint(v1_index, v2_index):
	n1 = "v{}".format(v1_index)
	n2 = "v{}".format(v2_index)
	return Constraint([n1, n2], "{} != {}".format(n1, n2))

if __name__ == "__main__":
	args = sys.argv[1:]
	csp = make_csp(args[0], int(args[1]))
	csp.init()
	csp.run()
	print(csp.variables)