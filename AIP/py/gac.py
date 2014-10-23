import util
	
class GAC(object):
	def __init__(self, renderer = None):
		self.queue = []
		self.constraints = []
		self.variables = {}
		self.renderer = renderer
		
	def add_variable(self, variable):
		self.variables[variable.name] = variable
			
	def add_constraint(self, constraint):
		self.constraints.append(constraint)
	
	#The initialize function
	def init(self):
		self.queue = []
		for c in self.constraints:
			for v in c.variables:
				self.variables[v].participation += 1
				self.queue.append((c, v))
		
	def revise(self, constraint, v_name):
		#Returns true if domain was altered
		domain_changed = False
		#print("Checking variable '{}' against '{}'".format(v_name, constraint))
		#print("Domain: {}".format(self.variables[v_name].domain))
		#create a list other variables in the constraint
		o_vars = [i for i in constraint.variables if i != v_name]
		o_vals = [self.variables[i] for i in o_vars]
		
		#check for each value of focal variable
		to_remove = []
		for v_val in self.variables[v_name].domain:
			d = {v_name : v_val}
			passed = False
			
			#this loop checks the constraint per domain value
			for tuple in util.get_product(*o_vals):
				for i in range(len(o_vals)):
					d[o_vars[i]] = tuple[i]
				if(constraint.f(**d)):
					passed = True
					break
			
			if not passed:
				to_remove.append(v_val)
				domain_changed = True
				
		for val in to_remove:
			#print("Removing {}...".format(val))
			self.variables[v_name].remove(val)
		
		return domain_changed
	
	#this function repopulates the queue with after a focal variable domain was altered
	def repopulate(self, constraint, variable):
		for c in self.constraints:
			if c == constraint:
				continue
			
			if c.includes(variable):
				for var in c.variables:
					if (var == variable):
						continue
					
					self.queue.append((c, var))
			
	#The gac loop
	def run(self):
		while len(self.queue) > 0:
			todo = self.queue.pop(0)
			b_altered = self.revise(todo[0], todo[1])
			
			if(b_altered):
				#add constraint/variable tuples (TODO) to queue
				self.repopulate(todo[0], todo[1])

			if self.renderer != None:
				self.renderer.render(self)
	
	def rerun(self, variable):
		for constraint in self.constraints:
			if constraint.includes(variable):
				self.repopulate(constraint, variable)

		self.run()
				
	def is_success(self):
		#returns -1 if failstate, 0 if valid state, 1 if success state
			
		for i in self.variables.values():
			count = len(i)
			if count == 0:
				return -1
			elif count > 1:
				return 0
		return 1

#This class represents the Constraint Instances (CIs)
class Constraint(object):
	def __init__(self, sl_variables, s_expression):
		self.variables = sl_variables
		self.expression = s_expression
		self.f = util.makefunc(self.variables, s_expression)
		
	def includes(self, s_variable):
		return s_variable in self.variables
		
	def __str__(self):
		return self.expression

#This class represents the Variable Instances (VIs)
class Variable(object):
	def __init__(self, s_name, domain = []):
		self.domain = domain
		self.name = s_name
		self.participation = 0
		
	def remove(self, domain_value):
		if(domain_value in self.domain):
			self.domain.remove(domain_value)
			return True
		return False
		
	def __str__(self):
		return self.name
		
	def __len__(self):
		return len(self.domain)
	
	def __iter__(self):
		return iter(self.domain)
		
