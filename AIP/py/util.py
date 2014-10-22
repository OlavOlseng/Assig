#gac_util file
from itertools import product
import math

def makefunc(var_names, expression, envir = globals()):
	args = ""
	for n in var_names: 
		args = "{},{}".format(args, n)
	'''
	print ("(lambda {} : {} )".format(args[1:], expression))
	input()
	'''
	return eval("(lambda {} : {} )".format(args[1:], expression), envir)
def get_product(*l_args):
	#returns cartesian product of arbitrary number of lists
	return product(*l_args)
	
def get_primes(i_primes):
	primes = []
	number = 2
	
	while len(primes) < i_primes:
		isPrime = True
		for prime in primes:
			if (prime > math.sqrt(number)):
				print("Not prime: {}".format(number))
				isPrime = False
				break
			
			if number % prime == 0:
				print("Not prime: {}".format(number))
				isPrime = False
				break
			
			
			
		if(isPrime):
			primes.append(number)
		number += 1
	
	return primes