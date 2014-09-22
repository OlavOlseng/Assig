#gac_util file
from itertools import product

def makefunc(var_names, expression, envir = globals()):
	args = ""
	for n in var_names: 
		args = "{},{}".format(args, n)
	return eval("(lambda {} : {} )".format(args[1:], expression), envir)
	
def get_product(*args):
	#returns cartesian product of arbitrary number of lists
	return product(*args)