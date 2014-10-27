from AGACNode import AGACNode

class FFNode(AGACNode, object):
	def __init__(self, gac):
		super(FFNode, self).__init__(gac, 5)
		
	def calculateHeuristic(self):
		h = 0.0
		vars = len(self.gac.variables)
		
		for var in self.gac.variables:
			if self.gac.variables[var].name[0] == "p":
				continue
			domain = self.gac.variables[var].domain
			count = len(domain)
			if (count == 0):
				h += 2000000000
			else:
				#h += self.step_cost
				h += count
			
		#h -= vars * self.step_cost
		#self.h = float(h*self.step_cost)
		self.h = float(h)
		return self.h