from AGACNode import AGACNode

class VCNode(AGACNode, object):
	def __init__(self, gac):
		super(VCNode, self).__init__(gac, 5)
		
	def calculateHeuristic(self):
		h = 0.0
		vars = len(self.gac.variables)

		for var in self.gac.variables:
			domain = self.gac.variables[var].domain
			count = len(domain)
			if (count == 0):
				h += 1000 * self.step_cost
			else:
				#h += step_cost
				h += count * self.step_cost
			
		h -= vars * self.step_cost
		self.h = float(h*self.step_cost)
		#self.h = float(h)
		return self.h