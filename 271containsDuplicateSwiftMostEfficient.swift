import Foundation

class Solution{
func containsDuplicate(_ nums:[Int]) -> Bool {
    var seen=Set<Int>()
    for n in nums{
        if seen.contains(n){
            return true
        }
        seen.insert(n)
    }

    return false
}
}

let sol = Solution()
print(sol.containsDuplicate([1,2,3,4,1]))

