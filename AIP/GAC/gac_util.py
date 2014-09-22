#gac_util file

def makefunc(var_names, expression, envir = globals()):
	args = ""
	for n in var_names: 
		args = "{},{}".format(args, n)
	return eval("(lambda {} : {} )".format(args[1:], expression), envir)