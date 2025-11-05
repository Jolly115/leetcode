
from typing import List

class Solution:
	def two_sum(self, arr:List[int] , target:int) -> List[int]:
	
		hashmap = {}
		for i in range(len(arr)):
			complement = target-arr[i]
			if complement in hashmap:
				return [hashmap[complement],i]
			hashmap[arr[i]]=i

sol = Solution()
print(sol.two_sum([1,2,3,4,5],9))
