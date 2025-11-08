
class Solution:
    def containsDuplicate(_self,nums):
        seen = set()
        for n in nums:
            if n in seen:
                return True
            seen.add(n)
    

        return False


sol = Solution()
print(sol.containsDuplicate([1,2,3,4,1]))
