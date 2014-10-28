from AGACNode import AGACNode

size = 0

class FFNode(AGACNode, object):
	def __init__(self, gac):
		super(FFNode, self).__init__(gac, 5)
		
	def calculateHeuristic(self):
		h = 0.0
		vars = len(self.gac.variables)
		map = []
		
		for var in self.gac.variables:
			#if self.gac.variables[var].name[0] == "p":
			#	continue
			domain = self.gac.variables[var].domain
			count = len(domain)
			if (count == 0):
				h += 2000000000
			else:
				#h += self.step_cost
				h += count
			
			
		#h -= vars * self.step_cost
		self.h = float(h*self.step_cost)
		#self.h = float(h)
		return self.h
		
def mostPressured(self):
		keys = self.gac.variables.keys()
		min = 1000000
		
		most_pressured = 0
		max_participation = 0
		
		
		for key in keys:
			if key[0] == "p":
				continue
			domain = self.gac.variables[key].domain
			count = float(len(domain))
			pressure = count / self.gac.variables[key].init_domain_size
			
			if(count > 1):
				if pressure < min:
					most_pressured = key
					min = pressure
					max_participation = self.gac.variables[key].participation
					
				elif pressure == min:
					participation = self.gac.variables[key].participation
					if  participation > max_participation:
						most_pressured = key
						max_participation = participation
						
		#return mins[randint(0,len(mins)-1)]
		return most_pressured