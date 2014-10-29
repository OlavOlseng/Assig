from AGACNode import AGACNode

size = 0

class FFNode(AGACNode, object):
	def __init__(self, gac):
		super(FFNode, self).__init__(gac, 5)
		
	def calculateHeuristic(self):
		h = 0.0
		vars = len(self.gac.variables)
		map = []
		for i in range(size):
			map.append([0]*size)
		fail_state = False
		for var in self.gac.variables.values():
			#if self.gac.variables[var].name[0] == "p":
			#	continue
			domain = var.domain
			count = len(domain)
			if (count == 0):
				h += 2000000000
				fail_state = True
				break
			elif(count > 1):
				#h += self.step_cost
				h += count
			
			if (var.name[0] == "c" and count == 1):
				map[var.y][var.x] = var.domain[0]
		if(not fail_state):
			#Check for and punish squares of same color
			for y in range(size-1):
				for x in range(size-1):
					color = map[y][x]
					if(color == 0):
						continue
					if(map[y+1][x] == color and map[y+1][x+1] == color and map[y][x+1] == color):
						h += 500
			
		#h -= vars * self.step_cost
		self.h = float(h*self.step_cost)
		#self.h = float(h)
		return self.h
		
	def mostPressured(self):
		#Since only cell variables are chosen, this is esentially min-domain,
		#and then using most constrained among the min-domain variables
		keys = self.gac.variables.keys()
		min = 1000000
		
		most_pressured = 0
		max_participation = 0
		
		
		for key in keys:
			#skip pipe variables
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