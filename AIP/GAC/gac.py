import copy
from gac_util import *

class GAC(object):
	def __init__(self, renderer = None):
		self.queue = []
		self.constraints = []
		self.variables = {}
		self.renderer = renderer
		
	def init(self):
		self.queue = []
		for c in self.constraints:
			for v in c.variables:
				self.queue.append((c, v))
		
	def revise(self, constraint, v_name):
		#Returns true if domain was altered
		domain_changed = False
		print("Checking variable '{}' against '{}'".format(v_name, constraint))
		print("Domain: {}".format(self.variables[v_name]))
		#create a list other variables in the constraint
		o_vars = [i for i in constraint.variables if i != v_name]
		o_vals = [self.variables[i] for i in o_vars]
		
		#check for each value of focal variable
		to_remove = []
		for v_val in self.variables[v_name]:
			d = {v_name : v_val}
			passed = False
			
			for tuple in get_product(*o_vals):
				for i in range(len(o_vals)):
					d[o_vars[i]] = tuple[i]
				if(constraint.f(**d)):
					passed = True
					break
			
			if not passed:
				to_remove.append(v_val)
				domain_changed = True
				
		for val in to_remove:
			print("Removing {}...".format(val))
			self.variables[v_name].remove(val)
		
		return domain_changed
	
	def repopulate(self, constraint, variable):
		for c in self.constraints:
			if c == constraint:
				continue
			
			if c.includes(variable):
				for var in c.variables:
					if (var == variable):
						continue
					
					self.queue.append((c, var))
			
	
	def run(self):
		while len(self.queue) > 0:
			todo = self.queue.pop(0)
			b_altered = self.revise(todo[0], todo[1])
			
			if(b_altered):
				#add constrint passes to queue
				self.repopulate(todo[0], todo[1])

			else:
				#do nothing
				print("Altered nothing")
			print()
			if self.renderer != None:
				self.renderer.render(self)
				
	
class Constraint(object):
	def __init__(self, sl_variables, s_expression):
		self.variables = sl_variables
		self.expression = s_expression
		self.f = makefunc(self.variables, s_expression)
		
	def includes(self, s_variable):
		return s_variable in self.variables
		
	def __str__(self):
		return self.expression
		
class Variable(object):
	def __init__(self, s_name):
		self.domain = []
		self.name = s_name
		
	def domain(self):
		return self.domain
		
	def remove(self, domain_value):
		if(domain_value in self.domain):
			self.domain.remove(domain_value)
			return True
		return False
		
	def copy(self):
		return copy.copy(self)
		
