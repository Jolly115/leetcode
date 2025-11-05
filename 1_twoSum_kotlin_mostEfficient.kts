

class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val map = mutableMapOf<Int, Int>()

        for (i in nums.indices) {
            val complement = target - nums[i]
	    println(complement)
            if (map.containsKey(complement)) {
                return intArrayOf(map[complement]!!, i)
            }
            map[nums[i]] = i
	    println(map)
        }

        return intArrayOf() // Return empty if no solution found
    }
}

fun main() {
    val sol = Solution()
    val result = sol.twoSum(intArrayOf(1, 2, 3, 4, 5), 9)
    print(result)
    println(result.joinToString(", "))  // Output: 3, 4
}
